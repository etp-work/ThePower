package org.etp.portalKit.init.logic;

import java.util.List;

import javax.annotation.Resource;

import org.etp.portalKit.init.service.IndexViewSettings;
import org.etp.portalKit.powerbuild.bean.response.ViewInfo;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is
 */
@Component(value = "initLogic")
public class InitLogic {

    @Resource(name = "indexViewSettings")
    private IndexViewSettings viewSettings;

    /**
     * @return list of viewInfo.
     */
    public List<ViewInfo> retrieveViewInfo() {
        return viewSettings.retrieveViewInfo();
    }
}
