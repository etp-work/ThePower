package org.etp.portalKit.init.logic;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.JSONUtils;
import org.etp.portalKit.init.bean.PortalKitInfo;
import org.etp.portalKit.init.bean.ViewInfo;
import org.etp.portalKit.init.service.IndexViewSettings;
import org.etp.portalKit.setting.bean.SettingsCommand;
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
    
    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    private String PORTALKIT_INFO_JSON = "PortalKitInfo.json";

    /**
     * @return list of viewInfo.
     */
    public List<ViewInfo> retrieveViewInfo() {
        List<ViewInfo> viewInfos = viewSettings.retrieveViewInfo();
        if(StringUtils.isBlank(prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH))||StringUtils.isBlank(prop.get(SettingsCommand.PORTAL_TEAM_PATH))){
            for (ViewInfo viewInfo : viewInfos) {
                viewInfo.setDefaultView(false);
                if(!"Set".equals(viewInfo.getViewName())){
                    continue;
                }
                viewInfo.setDefaultView(true);
            }
        }
        return viewInfos;
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
