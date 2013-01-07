package org.etp.portalKit.common.service;

import java.io.File;

import org.springframework.stereotype.Component;

/**
 * Implementation of Source, that provide a File instance of
 * configuration file.
 */
@Component(value = "configSource")
public class SourceOfConfig implements Source {

    @Override
    public File getSource() {
        File configFile = null;
        String home = System.getenv("HOME");
        File root = new File(home);
        if (!root.isDirectory()) {
            root = new File("c:");
        }
        configFile = new File(root, ".portalkit");
        return configFile;
    }

}
