package org.etp.portalKit.setting.controller;

import javax.annotation.Resource;

import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.setting.bean.Settings;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Hold all the request that related to settings.
 */
@Controller
public class SettingController {
    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    /**
     * @return next page
     */
    @RequestMapping(value = "/settings/set.ajax", method = RequestMethod.POST)
    public @ResponseBody
    String set(@RequestBody Settings settings) {
        return "index";
    }
}
