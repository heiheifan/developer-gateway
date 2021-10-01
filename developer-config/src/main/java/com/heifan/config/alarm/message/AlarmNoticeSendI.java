package com.heifan.config.alarm.message;


import com.heifan.config.alarm.dto.ServiceNotice;

/**
 * @author z201.coding@gmail.com
 **/
public interface AlarmNoticeSendI {

    void send(String content);

    void send(String content, String blamedFor);

    void send(ServiceNotice serviceNotice, String... blamedFor);

    void send(ServiceNotice serviceNotice, String blamedFor);

    void sendNotice(Throwable throwable, String extMessage, String blamedFor);

    void sendNotice(Throwable throwable, String extMessage, String... blamedFor);

    void sendNotice(Throwable throwable, String appTraceId, String extMessage, String... blamedFor);


}
