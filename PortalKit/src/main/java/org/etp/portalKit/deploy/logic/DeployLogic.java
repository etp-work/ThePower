package org.etp.portalKit.deploy.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.deploy.bean.DeployInfo4CI;
import org.etp.portalKit.deploy.bean.PackageInfo4CI;
import org.etp.portalKit.deploy.bean.request.CheckPackageCommand;
import org.etp.portalKit.deploy.bean.response.DownloadedPath;
import org.etp.portalKit.deploy.bean.response.PackageCheckedResult;
import org.etp.portalKit.deploy.service.PackagesCheckService;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide a bussiness logic adapter
 * for deploy related function.
 */
@Component(value = "deployLogic")
public class DeployLogic {

    @Resource(name = "packagesCheckService")
    private PackagesCheckService packageCheck;

    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    /**
     * Firstly, set the cmd.downloadPath to store file. And scan the
     * downloadedPath for specified typeToDeploy.If cmd.typeToDeploy
     * is null, all the .gz file's name will be returned. Otherwise,
     * the specified type of the .gz file's name will be returned.
     * 
     * @param cmd cmd.downloadPath path of the downloaded packages
     *            from CI. cmd.typeToDeploy typeToDeploy
     *            referencePortal/multiscreenPortal, those two types
     *            are supported.
     * @return array of PackageChoose that can be used to deploy.
     */
    public PackageCheckedResult setPathAndRetrieveincludedPkgs(CheckPackageCommand cmd) {
        if (StringUtils.isBlank(cmd.getDownloadPath()))
            throw new RuntimeException("downloadPath could not be null.");
        DownloadedPath path = new DownloadedPath();
        path.setDownloadedPath(cmd.getDownloadPath());
        prop.fromBean(path);
        PackageCheckedResult pcr = new PackageCheckedResult();
        File[] allDeployedPkgs = packageCheck.retrieveDeployedPkgs(cmd.getDownloadPath());
        List<String> list = new ArrayList<String>();
        for (File file : allDeployedPkgs) {
            list.add(file.getName());
        }
        pcr.setAllGzFiles(list);
        DeployInfo4CI deployInfo4Ci = packageCheck.retrieveRelationShip();
        List<String> refs = new ArrayList<String>();
        for (PackageInfo4CI packageInfo : deployInfo4Ci.getReferencePortal()) {
            refs.add(packageInfo.getPackageName());
        }
        List<String> multis = new ArrayList<String>();
        for (PackageInfo4CI packageInfo : deployInfo4Ci.getMultiscreenPortal()) {
            multis.add(packageInfo.getPackageName());
        }
        pcr.setMultiscreenPortal(multis);
        pcr.setReferencePortal(refs);
        return pcr;
    }

    /**
     * @return downloadPath from store file or cache.
     */
    public DownloadedPath getDownloadPath() {
        DownloadedPath path = new DownloadedPath();
        prop.toBean(path);
        return path;
    }

}
