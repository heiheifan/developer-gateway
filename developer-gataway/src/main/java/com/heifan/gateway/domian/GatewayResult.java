package com.heifan.gateway.domian;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author z201.coding@gmail.com
 **/
@Data
@NoArgsConstructor
public class GatewayResult {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private Object msg;

    public GatewayResult(int code, String message) {
        this.code = code;
        this.msg = buildResultErrorInfo(message);

    }

    public static GatewayResult fail() {
        return new GatewayResult(Code.ERROR.getValue(), Code.ERROR.getMsg());
    }

    public static GatewayResult fail(String msg) {
        return new GatewayResult(Code.ERROR.getValue(), msg);
    }

    public static GatewayResult success() {
        return new GatewayResult(Code.SUCCESS.getValue(), Code.SUCCESS.getMsg());
    }

    /**
     * 拓展状态码
     */
    @Getter
    public enum Code {
        // 正确的
        SUCCESS(200, "成功"),
        ERROR(501, "网络开小差，请稍后尝试");
        /**
         * 状态码值
         */
        private int value;
        private String msg;

        Code(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }
    }

    private ResultErrorInfo buildResultErrorInfo(String msg) {
        ResultErrorInfo resultErrorInfo = new ResultErrorInfo();
        resultErrorInfo.setTraceId("");
        resultErrorInfo.setPath("");
        resultErrorInfo.setTimestamp(System.currentTimeMillis());
        resultErrorInfo.setMessage(msg);
        return resultErrorInfo;
    }
}
