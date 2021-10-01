package com.heifan.config.alarm.message.impl;


import com.google.gson.Gson;
import com.heifan.config.alarm.AlarmNoticeEnum;
import com.heifan.config.alarm.client.SimpleHttpClient;
import com.heifan.config.alarm.dto.AlarmResult;
import com.heifan.config.alarm.dto.ExceptionNotice;
import com.heifan.config.alarm.dto.ServiceNotice;
import com.heifan.config.alarm.dto.WorkWxText;
import com.heifan.config.alarm.manager.AlarmWebHookTokenManager;
import com.heifan.config.alarm.message.AlarmNoticeSendI;
import com.heifan.config.alarm.property.AlarmNoticeProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class AlarmNoticeSendImpl implements AlarmNoticeSendI {

    private static final String DD_ULR = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    private static final String WX_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    @Autowired(required = false)
    private AlarmWebHookTokenManager alarmWebHookTokenManager;

    @Autowired(required = false)
    private AlarmNoticeProperty alarmNoticeProperty;

    @Override
    public void send(String content) {
        send(content,alarmNoticeProperty.getDefaultNotice());
    }

    @Override
    public void send(String content, String blamedFor) {
        if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
            Gson gson = new Gson();
            WorkWxText exceptionTxtNotice = new WorkWxText(content,
                    new String[]{blamedFor});
            log.info("{} ",gson.toJson(exceptionTxtNotice));
            try {
                String url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
                AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                        AlarmResult.class);
                log.info("result {} ", result);
            } catch (Exception e) {
                log.info("通知失败 {}", content);
            }
        }
    }


    @Override
    public void send(ServiceNotice serviceNotice, String... blamedFor) {
        if (blamedFor != null) {
            if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
                Gson gson = new Gson();
                WorkWxText exceptionTxtNotice = new WorkWxText(serviceNotice.createText(),
                        blamedFor);
                log.info("{} ",gson.toJson(exceptionTxtNotice));
                try {
                    String url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
                    AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                            AlarmResult.class);
                    log.info("result {} ", result);
                } catch (Exception e) {
                    log.info("异常预警失败 {}", serviceNotice);
                }
            }
        } else {
            log.info("无法进行通知，不存在背锅侠");
        }
    }

    @Override
    public void send(ServiceNotice serviceNotice, String blamedFor) {
        send(serviceNotice, new String[]{blamedFor});
    }

    @Override
    public void sendNotice(Throwable throwable, String extMessage, String blamedFor) {
        sendNotice(throwable, extMessage, new String[]{blamedFor});
    }

    @Override
    public void sendNotice(Throwable throwable, String extMessage, String... blamedFor) {
        if (blamedFor != null) {
            ExceptionNotice exceptionNotice = new ExceptionNotice(throwable,
                    new String[]{extMessage});
            exceptionNotice.setProject(alarmNoticeProperty.getProjectName());
            if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
                Gson gson = new Gson();
                WorkWxText exceptionTxtNotice = new WorkWxText(exceptionNotice.createWorkWxText(),
                        blamedFor);
                log.info("{} ",gson.toJson(exceptionTxtNotice));
                try {
                    String url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
                    AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                            AlarmResult.class);
                    log.info("result {} ", result);
                } catch (Exception e) {
                    log.info("异常预警失败 {}", exceptionNotice);
                }
            }
        } else {
            log.info("无法进行通知，不存在背锅侠");
        }
    }

    @Override
    public void sendNotice(Throwable throwable, String appTraceId, String extMessage, String... blamedFor) {
        if (blamedFor != null) {
            ExceptionNotice exceptionNotice = new ExceptionNotice(throwable, appTraceId,
                    new String[]{extMessage});
            if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
                exceptionNotice.setProject(alarmNoticeProperty.getProjectName());
                Gson gson = new Gson();
                WorkWxText exceptionTxtNotice = new WorkWxText(exceptionNotice.createWorkWxText(),
                        blamedFor);
                if (log.isDebugEnabled()) {
                    log.debug(gson.toJson(exceptionTxtNotice));
                }
                try {
                    String url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
                    AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                            AlarmResult.class);
                    log.info("result {} ", result);
                } catch (Exception e) {
                    log.info("异常预警失败 {} {}", exceptionNotice, e);
                }
            }
        } else {
            log.info("无法进行通知，不存在背锅侠");
        }
    }

}
