package org.etp.portalKit.powerbuild.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.powerbuild.bean.BuildResult;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.powerbuild.bean.ExecuteCommand;
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
     * Build one package.
     * 
     * @param cmd
     * @return build result
     */
    @RequestMapping(value = "/powerbuild/execute.ajax", method = RequestMethod.POST)
    public @ResponseBody
    BuildResult build(@RequestBody ExecuteCommand cmd) {
        BuildResult br = null;
        if (StringUtils.isBlank(cmd.getSelection())) {
            throw new RuntimeException("Error occurs when executing, please inform ZuoHao about this issue.");
        }
        if (cmd.isNeedBuild() && cmd.isNeedDeploy()) {
            br = logic.buildDeploy(cmd.getSelection());
        } else if (cmd.isNeedBuild()) {
            br = logic.build(cmd.getSelection());
        } else if (cmd.isNeedDeploy()) {
            br = logic.deploy(cmd.getSelection());
        } else {
            throw new RuntimeException("You should choose at least one option(such as: deploy, build).");
        }
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
    BuildResult buildSet(@RequestBody ExecuteCommand cmd) {
        BuildResult br = logic.buildDeploySet(cmd.getSelection());
        return br;
    }
}
