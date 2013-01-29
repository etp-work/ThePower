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

    private static CustomizedFolderFilter folderFilter = new CustomizedFolderFilter();
    private static CustomizedFileFilder fileFilter = new CustomizedFileFilder();

    /**
     * Given a baseFoler path, find all the child folder with
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
        folderFilter.folderPrefix = folderPrefix;
        File[] finds = baseFile.listFiles(folderFilter);
        if (ArrayUtils.isEmpty(finds))
            throw new RuntimeException("Could not find any folder with specified folder prefix.");
        return finds[0];
    }

    /**
     * Given a baseFolder path, find the child file with specified
     * file prefix.
     * 
     * @param baseFolder
     * @param filePrefix
     * @param fileSuffix
     * @return find file
     */
    public static File FileFinder(String baseFolder, String filePrefix, String fileSuffix) {
        if (StringUtils.isBlank(baseFolder))
            throw new NullPointerException("baseFolder could not be empty or null.");
        if (StringUtils.isBlank(filePrefix))
            throw new NullPointerException("filePrefix could not be empty or null.");
        File baseFile = new File(baseFolder);
        if (!baseFile.isDirectory())
            throw new RuntimeException("baseFolder is invalid folder path.");

        fileFilter.filePrefix = filePrefix;
        fileFilter.fileSuffix = fileSuffix;
        File[] finds = baseFile.listFiles(fileFilter);
        if (ArrayUtils.isEmpty(finds))
            throw new RuntimeException("Could not find any folder with specified file prefix.");

        return finds[0];
    }

    /**
     * The purpose of this class is filter to find file with specified
     * prefix.
     */
    static class CustomizedFileFilder implements FileFilter {
        /**
         * <code>filePrefix</code>
         */
        String filePrefix;
        /**
         * <code>fileSuffix</code>
         */
        String fileSuffix;

        @Override
        public boolean accept(File pathname) {
            if (!pathname.isFile())
                return false;
            if (StringUtils.isBlank(filePrefix))
                throw new RuntimeException("filePrefix could not be empty or null.");

            if (StringUtils.isBlank(fileSuffix))
                return pathname.getName().indexOf(filePrefix) > -1;
            else
                return pathname.getName().indexOf(filePrefix) > -1 && pathname.getName().endsWith(fileSuffix);

        }

    }

    /**
     * The purpose of this class is filter to find folder with
     * specified prefix.
     */
    static class CustomizedFolderFilter implements FileFilter {
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
