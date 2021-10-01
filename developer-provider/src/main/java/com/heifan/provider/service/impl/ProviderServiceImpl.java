package com.heifan.provider.service.impl;

import com.heifan.provider.service.ProviderServiceI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author HiF
 */
@Service
@Slf4j
public class ProviderServiceImpl implements ProviderServiceI {
    @Override
    public Object gatewayProviderTest() {
        return "{\n" +
                "\t\"success\": true,\n" +
                "\t\"code\": 200,\n" +
                "\t\"msg\": null,\n" +
                "\t\"data\": null,\n" +
                "}";
    }
}
