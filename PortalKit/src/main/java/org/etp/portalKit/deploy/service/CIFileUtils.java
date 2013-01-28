package org.etp.portalKit.deploy.service;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The purpose of this class is to provide some APIs to help to get
 * File related stuff from downloaded CI packages folder.
 */
public class CIFileUtils {

    private static CustomizedFilter filter = new CustomizedFilter();

    /**
     * Given a baseFoler path, find all the children folder with
     * specified folderPrefix.
     * 
     * @param baseFolder
     * @param folderPrefix
     * @return find folder
     */
    public static File FolderFinder(String baseFolder, String folderPrefix) {
        if (StringUtils.isBlank(baseFolder))
            throw new NullPointerException("baseFolder could not be empty or null.");
        if (StringUtils.isBlank(folderPrefix))
            throw new NullPointerException("folderPrefix could not be empty or null.");
        File baseFile = new File(baseFolder);
        if (!baseFile.isDirectory())
            throw new RuntimeException("baseFolder is invalid folder path.");
        filter.folderPrefix = folderPrefix;
        File[] finds = baseFile.listFiles(filter);
        if (ArrayUtils.isEmpty(finds))
            throw new RuntimeException("Could not find any folder with specified prefix.");
        return finds[0];
    }

    /**
     * The purpose of this class is filter to find folder with
     * specified prefix.
     */
    static class CustomizedFilter implements FileFilter {
        /**
         * <code>folderPrefix</code>
         */
        String folderPrefix;

        @Override
        public boolean accept(File pathname) {
            if (!pathname.isDirectory())
                return false;
            if (!StringUtils.isBlank(folderPrefix))
                return pathname.getName().indexOf(folderPrefix) > -1;
            throw new RuntimeException("folderPrefix could not be empty or null.");
        }
    }

}
