package org.etp.portalKit.common.service;

import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.type.TypeReference;
import org.etp.portalKit.common.bean.DeployInfo;
import org.etp.portalKit.common.util.JSONUtils;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide a reader that can be used
 * to retrieve Deploy Information.
 */
@Component(value = "deployInfoDesignReader")
public class DeployInfoReader {
    @javax.annotation.Resource(name = "pathMatchingResourcePatternResolver")
    private PathMatchingResourcePatternResolver resolver;

    /**
     * retrieve deploy information from json file.
     * 
     * @return deploy information
     */
    public DeployInfo retrieve() {
        org.springframework.core.io.Resource resource = resolver.getResource("powerbuild/deployInfo4Design.json");
        String json = null;
        DeployInfo info = null;
        try {
            json = FileUtils.readFileToString(resource.getFile(), Charsets.UTF_8);
        } catch (IOException e) {
            return info;
        }
        info = JSONUtils.fromJSON(json, new TypeReference<DeployInfo>() {
            //            
        });
        return info;
    }
}
