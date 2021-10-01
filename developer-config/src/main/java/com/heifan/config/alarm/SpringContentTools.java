package com.heifan.config.alarm;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashSet;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@Component
@Slf4j
public class SpringContentTools {

    @Autowired
    Environment environment;

    @Autowired
    ApplicationContext applicationContext;

    public String instanceInfo() {
        String port = environment.getProperty("local.server.port");
        LinkedHashSet<String> address = NetUtil.localIpv4s();
        String ip = Strings.EMPTY;
        if (!address.isEmpty()) {
            ip = address.iterator().next();
        }
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return " ip:  " + ip + ":" + port + " pid " + pid;
    }

}
