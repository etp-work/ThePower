package org.etp.portalKit.powerbuild.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.type.TypeReference;
import org.etp.portalKit.common.util.JSONUtils;
import org.etp.portalKit.powerbuild.bean.response.ViewInfo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to render a settings indicate what the
 * index view will look like.
 */
@Component(value = "indexViewSettings")
public class IndexViewSettings {

    @javax.annotation.Resource(name = "pathMatchingResourcePatternResolver")
    private PathMatchingResourcePatternResolver resolver;

    private String CONFIG_FILE = "view-settings.json";

    /**
     * @return list of ViewInform.
     */
    public List<ViewInfo> retrieveViewInfo() {
        List<ViewInfo> list = new ArrayList<ViewInfo>();
        Resource resource = resolver.getResource(CONFIG_FILE);
        String json = null;
        try {
            json = FileUtils.readFileToString(resource.getFile(), Charsets.UTF_8);
        } catch (IOException e) {
            return list;
        }
        list = JSONUtils.fromJSON(json, new TypeReference<List<ViewInfo>>() {
            //            
        });

        return list;
    }
}
