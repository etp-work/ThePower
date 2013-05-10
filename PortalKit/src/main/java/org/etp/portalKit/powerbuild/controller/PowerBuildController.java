package org.etp.portalKit.powerbuild.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.powerbuild.bean.BuildInformation;
import org.etp.portalKit.powerbuild.bean.BuildResult;
import org.etp.portalKit.powerbuild.bean.ExecuteMultiCommand;
import org.etp.portalKit.powerbuild.bean.ExecuteSingleCommand;
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
    @RequestMapping(value = "/powerbuild/getBuildInformation.ajax", method = RequestMethod.GET)
    public @ResponseBody
    BuildInformation getBuildTrees() {
        return logic.getBuildInformation();
    }

    /**
     * Build one package.
     * 
     * @param cmd
     * @return build result
     */
    @RequestMapping(value = "/powerbuild/execute.ajax", method = RequestMethod.POST)
    public @ResponseBody
    BuildResult build(@RequestBody ExecuteSingleCommand cmd) {
        if (StringUtils.isBlank(cmd.getAbsolutePath())) {
            throw new RuntimeException("Error occurs when executing, please inform ZuoHao about this issue.");
        }
        return logic.executeCommand(cmd.getAbsolutePath(), cmd);
    }

    /**
     * Build a set of packages. Such as reference portal, multiscreen.
     * 
     * @param cmd
     * @return build result
     */
    @RequestMapping(value = "/powerbuild/executeWithType.ajax", method = RequestMethod.POST)
    public @ResponseBody
    BuildResult buildSet(@RequestBody ExecuteMultiCommand cmd) {
        if (cmd.getType() == null) {
            throw new RuntimeException(
                    "Error occurs when executing a set of portal, please inform ZuoHao about this issue.");
        }
        return logic.executeWithType(cmd.getType(), cmd);
    }
}
