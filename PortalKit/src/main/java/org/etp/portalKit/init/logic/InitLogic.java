package org.etp.portalKit.init.logic;

import java.util.List;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.etp.portalKit.common.util.JSONUtils;
import org.etp.portalKit.init.bean.PortalKitInfo;
import org.etp.portalKit.init.bean.ViewInfo;
import org.etp.portalKit.init.service.IndexViewSettings;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is
 */
@Component(value = "initLogic")
public class InitLogic {

    @Resource(name = "indexViewSettings")
    private IndexViewSettings viewSettings;

    @Resource(name = "pathMatchingResourcePatternResolver")
    private PathMatchingResourcePatternResolver pathResolver;

    private String PORTALKIT_INFO_JSON = "PortalKitInfo.json";

    /**
     * @return list of viewInfo.
     */
    public List<ViewInfo> retrieveViewInfo() {
        return viewSettings.retrieveViewInfo();
    }

    /**
     * @return PortalKitInfo
     */
    public PortalKitInfo retrievePortalKitInfo() {
        PortalKitInfo portalKitInfo = JSONUtils.fromJSONResource(pathResolver.getResource(PORTALKIT_INFO_JSON),
                new TypeReference<PortalKitInfo>() {
                    //            
                });
        return portalKitInfo;
    }

}
