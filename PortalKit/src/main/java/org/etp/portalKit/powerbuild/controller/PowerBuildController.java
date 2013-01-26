package org.etp.portalKit.powerbuild.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.etp.portalKit.powerbuild.bean.request.BuildCommand;
import org.etp.portalKit.powerbuild.bean.request.Selection;
import org.etp.portalKit.powerbuild.bean.response.BuildResult;
import org.etp.portalKit.powerbuild.bean.response.DirTree;
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
    @RequestMapping(value = "/powerbuild/getAllTrees.ajax", method = RequestMethod.GET)
    public @ResponseBody
    List<DirTree> getBuildTrees() {
        return logic.getCommonBuildListDirTrees();
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
     * @param cmd
     * @return build result
     */
    @RequestMapping(value = "/powerbuild/build.ajax", method = RequestMethod.POST)
    public @ResponseBody
    BuildResult build(@RequestBody BuildCommand cmd) {
        BuildResult br = null;
        if (cmd.isNeedDeploy())
            br = logic.buildDeploy(cmd.getSelection(), null);
        else
            br = logic.build(cmd.getSelection(), null);
        return br;
    }

    /**
     * Build a set of packages. Such as reference portal, multiscreen.
     * 
     * @param cmd
     * @return build result
     */
    @RequestMapping(value = "/powerbuild/buildset.ajax", method = RequestMethod.POST)
    public @ResponseBody
    BuildResult buildSet(@RequestBody BuildCommand cmd) {
        BuildResult br = logic.buildDeploySet(cmd.getSelection());
        return br;
    }
}
