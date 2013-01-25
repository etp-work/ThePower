package org.etp.portalKit.deploy.controller;

import javax.annotation.Resource;

import org.etp.portalKit.deploy.bean.request.CheckPackageCommand;
import org.etp.portalKit.deploy.bean.response.DownloadedPath;
import org.etp.portalKit.deploy.bean.response.PackageCheckedResult;
import org.etp.portalKit.deploy.logic.DeployLogic;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The purpose of this class is to provide a http request controller
 * for deploy related functions.
 */
@Controller
public class DeployController {
    @Resource(name = "deployLogic")
    private DeployLogic logic;

    /**
     * @return next page
     */
    @RequestMapping(value = "/deploy/getDownLoadedPath.ajax", method = RequestMethod.GET)
    public @ResponseBody
    DownloadedPath getDownloadPath() {
        return logic.getDownloadPath();
    }

    /**
     * @param cmd { downloadPath: xxx, typeToDeploy: xxx }
     * @return next page
     */
    @RequestMapping(value = "/deploy/setCheckPackages.ajax", method = RequestMethod.POST)
    public @ResponseBody
    PackageCheckedResult setDownloadPathAndCheck(@RequestBody CheckPackageCommand cmd) {
        return logic.setPathAndRetrieveincludedPkgs(cmd);
    }
}
