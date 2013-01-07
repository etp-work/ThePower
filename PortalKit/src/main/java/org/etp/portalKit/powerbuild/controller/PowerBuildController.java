package org.etp.portalKit.powerbuild.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The purpose of this class is
 */
@Controller
public class PowerBuildController {
    /**
     * @return next page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }
}
