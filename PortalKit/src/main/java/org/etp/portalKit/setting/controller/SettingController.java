package org.etp.portalKit.setting.controller;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.setting.bean.SettingsCommand;
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
     * @param settings
     * @return do nothing
     */
    @RequestMapping(value = "/settings/set.ajax", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> set(@RequestBody SettingsCommand settings) {
        prop.fromBean(settings);
        return Collections.emptyMap();
    }

    /**
     * @return do nothing
     */
    @RequestMapping(value = "/settings/getAll.ajax", method = RequestMethod.GET)
    public @ResponseBody
    SettingsCommand get() {
        SettingsCommand set = new SettingsCommand();
        prop.toBean(set);
        return set;
    }
}
