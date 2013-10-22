package org.etp.portalKit.powerbuild.controller;

import javax.annotation.Resource;

import org.etp.portalKit.powerbuild.bean.BuildCommand;
import org.etp.portalKit.powerbuild.bean.BuildInformation;
import org.etp.portalKit.powerbuild.bean.BuildResult;
import org.etp.portalKit.powerbuild.logic.PowerBuildLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
     * Build a war file.
     * 
     * @param cmd BuildCommand
     * @return Build result
     */
    @RequestMapping(value = "/powerbuild/build.ajax", method = RequestMethod.POST)
    public @ResponseBody
    BuildResult buildWar(@RequestBody BuildCommand cmd) {
        return logic.build(cmd.getWarFile(), cmd.getParam());
    }

    /**
     * @param messageId id of error message
     * @return error message
     */
    @RequestMapping(value = "/powerbuild/getLog.ajax", method = RequestMethod.GET)
    public @ResponseBody
    String getLog(@RequestParam("messageId") String messageId) {
        return logic.getErrorMsgById(messageId);
    }
}
