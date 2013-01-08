package org.etp.portalKit.powerbuild.controller;

import java.util.List;

import javax.annotation.Resource;

import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.powerbuild.service.DirProvider;
import org.springframework.stereotype.Controller;
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
    @RequestMapping(value = "/power/getAllTrees.ajax", method = RequestMethod.GET)
    public @ResponseBody
    List<DirTree> getBuildTrees() {
        List<DirTree> retrieveDirInfo = specProvider.retrieveDirInfo();
        return retrieveDirInfo;
    }
}
