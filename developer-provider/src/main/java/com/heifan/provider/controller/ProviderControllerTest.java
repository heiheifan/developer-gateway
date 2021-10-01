package com.heifan.provider.controller;

import com.heifan.provider.service.ProviderServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HiF
 */
@RestController
@RequestMapping("/provider")
public class ProviderControllerTest {

    @Autowired
    ProviderServiceI providerService;

    @RequestMapping("/test")
    public Object gatewayProviderTest(){
        return providerService.gatewayProviderTest();
    }
}
