package org.etp.portalKit.deploy.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.etp.portalKit.common.util.JSONUtils;
import org.etp.portalKit.deploy.bean.DeployInfo4CI;
import org.etp.portalKit.deploy.bean.PackageInfo4CI;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide a simple API to check if
 * packages want to be deployed are downloaded.
 */
@Component(value = "packagesCheckService")
public class PackagesCheckService {
    @javax.annotation.Resource(name = "pathMatchingResourcePatternResolver")
    private PathMatchingResourcePatternResolver resolver;
    private String DEPLOY_INFO_FOR_CI_JSON = "deploy/deployInfo4CI.json";
    private DeployInfo4CI deployInfo;

    /**
     * initial read deployinfo4Ci
     */
    @PostConstruct
    public void init() {
        Resource resource = resolver.getResource(DEPLOY_INFO_FOR_CI_JSON);
        String json = null;
        try {
            json = FileUtils.readFileToString(resource.getFile(), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("failed to retrieve deployed package information.");
        }
        deployInfo = JSONUtils.fromJSON(json, new TypeReference<DeployInfo4CI>() {
            //            
        });
    }

    /**
     * Scan the specified downloaded path for all the .gz packages.
     * 
     * @param downloadedPath a path in which the .gz packages placed.
     * @return included .gz packages that can be scaned.
     */
    public File[] retrieveDeployedPkgs(String downloadedPath) {
        return retrieveDeployedPkgs(downloadedPath, null);
    }

    /**
     * Scan the specified downloaded path for specified type of the
     * .gz packages.
     * 
     * @param downloadedPath path of packages that you have downloaded
     *            from CI.
     * @param typeWantoDeploy indicate which type of set you want to
     *            deploy. such as referencePortal/multiscreenPortal.
     * @return included .gz packages that can be scaned.
     */
    public File[] retrieveDeployedPkgs(String downloadedPath, String typeWantoDeploy) {
        if (StringUtils.isBlank(downloadedPath))
            throw new RuntimeException("downloadedPath should not be empty.");
        File file = new File(downloadedPath);
        if (!file.isDirectory())
            throw new RuntimeException("downloadedPath is invalid.");

        final Set<String> pkgs = new LinkedHashSet<String>();
        if (typeWantoDeploy == null || typeWantoDeploy.equals("referencePortal"))
            for (PackageInfo4CI pkg : deployInfo.getReferencePortal()) {
                pkgs.add(pkg.getPackageName());
            }
        if (typeWantoDeploy == null || typeWantoDeploy.equals("multiscreenPortal"))
            for (PackageInfo4CI pkg : deployInfo.getMultiscreenPortal()) {
                pkgs.add(pkg.getPackageName());
            }
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (!pathname.isFile())
                    return false;
                if (!pathname.getName().endsWith(".gz"))
                    return false;
                for (String pkg : pkgs) {
                    if (pathname.getName().indexOf(pkg) > -1)
                        return true;
                }
                return false;
            }
        });
        return files;
    }

    /**
     * @return DeployInfo4CI
     */
    public DeployInfo4CI retrieveRelationShip() {
        return deployInfo;
    }

}
