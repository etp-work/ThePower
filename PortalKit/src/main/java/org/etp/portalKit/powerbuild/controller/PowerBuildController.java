package org.etp.portalKit.powerbuild.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.etp.portalKit.common.shell.CommandResult;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.powerbuild.bean.Selection;
import org.etp.portalKit.powerbuild.logic.PowerBuildLogic;
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

    @Resource(name = "buildLogic")
    private PowerBuildLogic logic;

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
        return logic.getSpecBuildTrees();
    }

    /**
     * @param selection
     * @return next page
     */
    @RequestMapping(value = "/powerbuild/setDefault.ajax", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> setDefault(@RequestBody Selection selection) {
        logic.setSelectionsToSettings(selection);
        return Collections.emptyMap();
    }

    /**
     * Build one package.
     * 
     * @param selection
     * @return build result
     */
    @RequestMapping(value = "/powerbuild/build.ajax", method = RequestMethod.POST)
    public @ResponseBody
    CommandResult build(@RequestBody String selection) {
        CommandResult build = logic.build(selection);
        return build;
    }
}
