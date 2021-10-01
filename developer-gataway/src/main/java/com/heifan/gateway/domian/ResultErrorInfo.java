package com.heifan.gateway.domian;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author z201.coding@gmail.com
 * @date 2020-09-18
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultErrorInfo {
    /**
     * 请求id
     */
    private String traceId;
    /**
     * 请求路径
     */
    private String path;
    /**
     * 具体描述
     */
    private String message;
    /**
     * 异常时间戳
     */
    private Long timestamp;
    /**
     * 异常扩展信息
     */
    private Object data;

}
