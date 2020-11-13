package com.sky.boot.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.sky.boot.entity.Bill;
import com.sky.boot.util.AlipayTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author Administrator
 */
@Controller
public class PayController {
    @Autowired
    @Qualifier(value = "AlipayTool")
    private AlipayTool alipayTool;


    @PostMapping(value = "/pay")
    @ResponseBody
    public String pay(@Valid Bill bill, BindingResult bindingResult, HttpServletRequest request,
                      HttpServletResponse httpResponse, Model model) throws Exception {

// TODO: 2020/11/12 判断传入的信息是否正确
        if (bindingResult.hasErrors()) {
            // 如果错误数量大于1,将全部错误信息放入model中
            if (bindingResult.getFieldErrors().size() > 1) {
                model.addAttribute("errors", bindingResult.getFieldErrors());
                return "error";
                // 如果错误数量等于1,将这个错误信息放入model中
            } else if (bindingResult.getFieldErrors().size() == 1) {
                model.addAttribute("error", bindingResult.getFieldError());
                return "error";
            }
        }
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(alipayTool.gatewayUrl, alipayTool.appId, alipayTool.merchantPrivateKey, "json", alipayTool.charset, alipayTool.alipayPublicKey, alipayTool.signType);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayTool.returnUrl);
        alipayRequest.setNotifyUrl(alipayTool.notifyUrl);

        alipayRequest.setBizContent("{\"out_trade_no\":\"" + bill.getOrderId() + "\","
                + "\"total_amount\":\"" + bill.getPayMoney() + "\","
                + "\"subject\":\"" + bill.getOrderName() + "\","
                + "\"body\":\"" + bill.getOrderDescription() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            //调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + alipayTool.charset);
        System.out.println(form);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();

        return null;
    }
}

