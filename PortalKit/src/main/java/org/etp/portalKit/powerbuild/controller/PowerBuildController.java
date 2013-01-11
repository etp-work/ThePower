package org.etp.portalKit.powerbuild.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.powerbuild.bean.DefaultSelection;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.powerbuild.service.DirProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The purpose of this class is
 */
@Controller
public class PowerBuildController {

    @Resource(name = "specProvider")
    private DirProvider specProvider;

    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    /**
     * @return next page
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    /**
     * @return next page
     */
    @RequestMapping(value = "/powerbuild/getAllTrees.ajax", method = RequestMethod.GET)
    public @ResponseBody
    List<DirTree> getBuildTrees() {
        List<DirTree> retrieveDirInfo = specProvider.retrieveDirInfo();
        return retrieveDirInfo;
    }

    /**
     * @param selection 
     * @return next page
     */
    @RequestMapping(value = "/powerbuild/setDefault.ajax", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> setDefault(@RequestBody DefaultSelection selection) {
        prop.fromBean(selection);
        return Collections.emptyMap();
    }
}
