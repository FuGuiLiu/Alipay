package com.sky.boot.entity;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Bill {
    /**
     * 商户订单号,商户网站订单系统中唯一订单号,必填
     */
    @NotBlank(message = "订单号不能为空")
    private String orderId;
    /**
     * 订单名称,必填
     */
    @NotBlank(message = "订单名称不能为空")
    private String orderName;
    /**
     * 付款金额,必填
     */
    @NotBlank(message = "付款金额不能为空")
    private String payMoney;
    /**
     * 商品描述可以为空
     */
    private String orderDescription;
}
