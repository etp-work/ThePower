package org.etp.portalKit.clean.controller;

import javax.annotation.Resource;

import org.etp.portalKit.clean.bean.response.CleanItems;
import org.etp.portalKit.clean.logic.CleanLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The purpose of this class is to provide a controller to handle all
 * the request that related to Clean functionalities.
 */
@Controller
public class CleanController {

    @Resource(name = "cleanLogic")
    private CleanLogic logic;

    /**
     * @return CleanItems (widgetCaches: [], warFiles: [])
     */
    @RequestMapping(value = "/clean/getCleanItems.ajax", method = RequestMethod.GET)
    public @ResponseBody
    CleanItems geCleanItems() {
        return logic.retrieveCleanItems();
    }
}
