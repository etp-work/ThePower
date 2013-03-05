package org.etp.portalKit.common.util;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.StringUtils;

/**
 * The purpose of this class is to provide some useful APIs to help
 * for maven related stuff.
 */
public class MavenUtils {
    /**
     * check whether the given file is a regular maven project foler
     * or not.
     * 
     * @param file file used to check.
     * @return true if the given file is a regualr maven project
     *         folder. otherwise false
     */
    public static boolean isMavenProject(File file) {
        if (file == null)
            return false;
        if (!file.isDirectory())
            return false;
        File[] children = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile() && "pom.xml".equals(pathname.getName());
            }
        });
        if (children.length == 1)
            return true;
        return false;
    }

    /**
     * check whether the given file path is a regular maven project
     * foler or not.
     * 
     * @param filePath used to check.
     * @return true if the given file is a regualr maven project
     *         folder. otherwise false
     */
    public static boolean isMavenProject(String filePath) {
        if (StringUtils.isBlank(filePath))
            throw new NullPointerException("maven project folder path could not be null.");
        File file = new File(filePath);
        return isMavenProject(file);
    }
}
