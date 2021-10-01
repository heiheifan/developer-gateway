package com.heifan.gateway.exception;

import com.heifan.config.alarm.SpringContentTools;
import com.heifan.config.alarm.manager.AlarmNoticeManage;
import com.heifan.gateway.domian.GatewayResult;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.net.ConnectException;

@Component
@Slf4j
public class GateWayExceptionHandlerAdvice {

    @Autowired(required = false)
    AlarmNoticeManage alarmNoticeManage;

    @Autowired(required = false)
    SpringContentTools springContentTools;

    private void sendAlarmNotice(Throwable e, String msg, String ext, String blamedFor) {
        if (null != alarmNoticeManage) {
            alarmNoticeManage.createNotice(e, msg + " " + ext, blamedFor);
        } else {
            log.error("预警失败 未开启alarm模块");
        }
    }

    @ExceptionHandler(value = {ResponseStatusException.class})
    @ResponseStatus(HttpStatus.OK)
    public GatewayResult handle(ResponseStatusException ex) {
        log.error("response status exception:{}", ex.getMessage());
        if (ex.getStatus().equals(HttpStatus.NOT_FOUND)) {
            return GatewayResult.fail("404！");
        }
        if (ex.getStatus().equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
            return GatewayResult.fail("服务故障,管理人员处理中！");
        }
        sendAlarmNotice(ex, "", ex.getMessage(), "");
        return GatewayResult.fail("服务开小差,请稍后尝试！");
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    @ResponseStatus(HttpStatus.OK)
    public GatewayResult handle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        sendAlarmNotice(ex, "connect timeout  ", ex.getMessage(), "");
        return GatewayResult.fail("服务开小差,请稍后尝试！");
    }

    @ExceptionHandler(value = {ConnectException.class})
    @ResponseStatus(HttpStatus.OK)
    public GatewayResult handle(ConnectException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        sendAlarmNotice(ex, "connect 服务下线  ", ex.getMessage(), "");
        return GatewayResult.fail("服务下线,请稍后尝试！");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GatewayResult handle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return GatewayResult.fail("404！");
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    public GatewayResult handle(RuntimeException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        sendAlarmNotice(ex, "runtime exception  ", ex.getMessage(), "");
        return GatewayResult.fail();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public GatewayResult handle(Exception ex) {
        log.error("exception:{}", ex.getMessage());
        sendAlarmNotice(ex, "exception  ", ex.getMessage(), "");
        return GatewayResult.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.OK)
    public GatewayResult handle(Throwable throwable) {
        GatewayResult result = GatewayResult.fail();
        if (throwable instanceof ResponseStatusException) {
            result = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            result = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof RuntimeException) {
            result = handle((RuntimeException) throwable);
        } else if (throwable instanceof ConnectException) {
            result = handle((ConnectException) throwable);
        } else if (throwable instanceof Exception) {
            result = handle((Exception) throwable);
        }
        return result;
    }


}
