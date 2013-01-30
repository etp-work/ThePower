package org.etp.portalKit.clean.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.etp.portalKit.clean.bean.request.CleanCommand;
import org.etp.portalKit.clean.bean.response.CleanItems;
import org.etp.portalKit.clean.logic.CleanLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * @param cmd (cleanItem: "", cleanType: "")
     * @return result: true/false
     */
    @RequestMapping(value = "/clean/cleanItem.ajax", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Boolean> geCleanItems(@RequestBody CleanCommand cmd) {
        boolean isTrue = logic.cleanItem(cmd);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", isTrue);
        return result;
    }

}
