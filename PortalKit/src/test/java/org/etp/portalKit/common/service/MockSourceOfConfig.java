package org.etp.portalKit.common.service;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * Implementation of Source, that provide a File instance of
 * configuration file.
 */
@Component(value = "configSource")
public class MockSourceOfConfig implements Source {

    @javax.annotation.Resource(name = "resolver")
    private PathMatchingResourcePatternResolver resolver;

    @Override
    public File getSource() {
        File configFile = null;
        try {
            configFile = resolver.getResource("portalkit").getFile();
            if (!configFile.exists())
                configFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configFile;
    }

}
