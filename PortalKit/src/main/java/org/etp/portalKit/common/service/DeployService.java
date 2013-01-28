package org.etp.portalKit.common.service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide some APIs to deploy
 * specified war file to destination.
 */
@Component(value = "deployService")
public class DeployService {

    private FileFilter filter;

    /**
     * Creates a new instance of <code>DeployService</code>.
     */
    public DeployService() {
        filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && StringUtils.endsWith(pathname.getName(), "war");
            }
        };
    }

    /**
     * Deploy the .war file under specified src/target folder to
     * destination.
     * 
     * @param src a folder path indicate which project will be
     *            deployed.
     * @param dest a folder path indicate what place the war file
     *            should be moved into.
     * @return deploy
     */
    public boolean deployFromFolder(String src, String dest) {
        if (StringUtils.isBlank(src))
            throw new NullPointerException("Specified src path could not be empty or null.");
        if (StringUtils.isBlank(dest))
            throw new NullPointerException("Specified dest path could not be empty or null.");
        File warFile = getWarFile(src);
        return deployFromFile(warFile.getAbsolutePath(), dest);
    }

    /**
     * Deploy the specified .war file to destination.
     * 
     * @param warFile a url indicate which .war file will be deployed.
     * @param dest a folder path indicate what place the war file
     *            should be moved into.
     * @return deploy
     */
    public boolean deployFromFile(String warFile, String dest) {
        if (StringUtils.isBlank(warFile))
            throw new NullPointerException("Specified warFile could not be empty or null.");
        if (StringUtils.isBlank(dest))
            throw new NullPointerException("Specified dest path could not be empty or null.");
        File war = new File(warFile);
        if (!war.isFile() || !war.exists())
            throw new RuntimeException("Specified warFile is invalid.");
        File destDir = new File(dest);
        try {
            FileUtils.copyFileToDirectory(war, destDir);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Deploy the .war files from specified set of folders to
     * destination.
     * 
     * @param srcs a set of folder paths indicate which project will
     *            be deployed.
     * @param dest a folder path indicate what place the war file
     *            should be moved into.
     * @return deploy
     */
    public boolean deployListFromFolder(List<String> srcs, String dest) {
        boolean isAllTrue = true;
        for (String src : srcs) {
            if (!deployFromFolder(src, dest))
                isAllTrue = false;
        }
        return isAllTrue;
    }

    /**
     * Deploy the .war files to destination.
     * 
     * @param wars a set of urls indicate which war file will be
     *            deployed.
     * @param dest a folder path indicate what place the war file
     *            should be moved into.
     * @return deploy
     */
    public boolean deployListFromFile(List<String> wars, String dest) {
        boolean isAllTrue = true;
        for (String war : wars) {
            if (!deployFromFile(war, dest))
                isAllTrue = false;
        }
        return isAllTrue;
    }

    /**
     * Given a compile folder, automatically scan the child target
     * folder, find the .war file.
     * 
     * @param src compile folder, should contains a pom.xml and a
     *            target child folder.
     * @return the compiled .war file.
     */
    private File getWarFile(String src) {
        File parent = new File(src);
        if (!parent.isDirectory())
            throw new RuntimeException("The src is not a regular folder path.");
        File target = new File(parent, "target\\");
        File[] warfiles = target.listFiles(filter);
        if (warfiles.length > 0)
            return warfiles[0];
        throw new RuntimeException("There is no any .war file under specified src folder path.");
    }

}
