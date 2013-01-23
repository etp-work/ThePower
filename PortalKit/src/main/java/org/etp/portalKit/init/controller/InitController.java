package org.etp.portalKit.init.controller;

import java.util.List;

import javax.annotation.Resource;

import org.etp.portalKit.init.logic.InitLogic;
import org.etp.portalKit.powerbuild.bean.response.ViewInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The purpose of this class is
 */
@Controller
public class InitController {

    @Resource(name = "initLogic")
    private InitLogic logic;

    /**
     * @param model
     * @return next page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        List<ViewInfo> retrieveViewInfo = logic.retrieveViewInfo();
        model.addAttribute("viewInfo", retrieveViewInfo);
        return "index";
    }

    /**
     * @return viewsInfomation
     */
    @RequestMapping(value = "/init/getViewsInfo.ajax", method = RequestMethod.GET)
    public @ResponseBody
    List<ViewInfo> getViewsInfo() {
        List<ViewInfo> retrieveViewInfo = logic.retrieveViewInfo();
        return retrieveViewInfo;
    }

}
