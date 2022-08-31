package org.rone.web.controller;

import org.rone.web.service.RoneService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rone
 */
@RestController
public class TestController {

    private final RoneService roneService;

    public TestController(RoneService roneService) {
        this.roneService = roneService;
    }

    @RequestMapping("/test")
    public String test() {
        return "rone";
    }

    @RequestMapping("/rone")
    public String rone() {
        roneService.rone();
        return "success";
    }
}
