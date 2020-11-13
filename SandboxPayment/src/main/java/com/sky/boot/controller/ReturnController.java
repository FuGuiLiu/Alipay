package com.sky.boot.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.sky.boot.util.AlipayTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Administrator
 */
@Controller
public class ReturnController {
    @Autowired
    private AlipayTool alipayTool;

    @GetMapping(value = "/return")
    @ResponseBody
    public String returnInfo(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {
        /**
         * 该页面仅做页面展示，业务逻辑处理请勿在该页面执行
         */
        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
//调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTool.alipayPublicKey, alipayTool.charset, alipayTool.signType);
        PrintWriter pw = response.getWriter();
        String info = null;
        //——请在这里编写您的程序（以下代码仅作参考）——
        if (signVerified) {
            //商户订单号
            String orderId = new String(request.getParameter("orderId").getBytes("ISO-8859-1"), "UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //付款金额
            String payMoney = new String(request.getParameter("payMoney").getBytes("ISO-8859-1"), "UTF-8");


            info = "trade_no:" + trade_no + "<br/>out_trade_no:" + orderId + "<br/>total_amount:" + payMoney;
        } else {
            info = "<h3 style=\"color:red;\">验签失败</h3>";
        }
        pw.print(info);
        pw.flush();
        pw.close();
        //——请在这里编写您的程序（以上代码仅作参考）——
        return null;
    }
}
