package org.rone.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Rone
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() {
        return "rone";
    }
}
