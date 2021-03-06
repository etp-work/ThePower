package org.etp.portalKit.common.util;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.StringUtils;

/**
 * The purpose of this class is to provide some APIs to help to get File related
 * stuff from specified folder.
 */
public class FileUtils {

	/**
	 * Tests whether the file denoted by this abstract pathname is a directory.
	 * 
	 * @param path
	 *            path of directory
	 * @return true if given path is directory, otherwise false.
	 */
	public static boolean isDirectory(String path) {
		if (StringUtils.isBlank(path)) {
			return false;
		}
		return isDirectory(new File(path));
	}

	/**
	 * Tests whether the file denoted by this abstract pathname is a directory.
	 * 
	 * @param path
	 *            path of directory
	 * @return true if given path is directory, otherwise false.
	 */
	public static boolean isDirectory(File path) {
		if (path == null) {
			return false;
		}
		return path.isDirectory();
	}

	/**
	 * Given a baseFoler path, find all the child folder with specified
	 * folderPrefix.
	 * 
	 * @param baseFolder directory as base for searching.
	 * @param folderPrefix prefix of the directory's name for filtering.
	 * @return find folder
	 */
	public static File[] FolderFinder(String baseFolder, String folderPrefix) {
		if (StringUtils.isBlank(baseFolder)) {
			throw new NullPointerException(
					"baseFolder could not be empty or null.");
		}
		if (StringUtils.isBlank(folderPrefix)) {
			throw new NullPointerException(
					"folderPrefix could not be empty or null.");
		}
		File baseFile = new File(baseFolder);
		if (!baseFile.isDirectory()) {
			return new File[0];
		}
		CustomizedFolderFilter folderFilter = new CustomizedFolderFilter();
		folderFilter.folderPrefix = folderPrefix;
		File[] finds = baseFile.listFiles(folderFilter);
		return finds;
	}

	/**
	 * Given a baseFolder path, find the child file with specified file prefix.
	 * 
	 * @param baseFolder directory as base for searching.
	 * @param filePrefix prefix of the directory's name for filtering.
	 * @param fileSuffix suffix of the directory's name for filtering.
	 * @return find file
	 */
	public static File[] FileFinder(String baseFolder, String filePrefix,
			String fileSuffix) {
		if (StringUtils.isBlank(baseFolder)) {
			throw new NullPointerException(
					"baseFolder could not be empty or null.");
		}
		if (StringUtils.isBlank(filePrefix)) {
			throw new NullPointerException(
					"filePrefix could not be empty or null.");
		}
		File baseFile = new File(baseFolder);
		if (!baseFile.isDirectory()) {
			return new File[0];
		}
		CustomizedFileFilder fileFilter = new CustomizedFileFilder();
		fileFilter.filePrefix = filePrefix;
		fileFilter.fileSuffix = fileSuffix;
		File[] finds = baseFile.listFiles(fileFilter);
		return finds;
	}

	/**
	 * The purpose of this class is filter to find file with specified prefix.
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
			if (!pathname.isFile()) {
				return false;
			}
			if (StringUtils.isBlank(filePrefix)) {
				throw new RuntimeException(
						"filePrefix could not be empty or null.");
			}

			if (StringUtils.isBlank(fileSuffix)) {
				return pathname.getName().indexOf(filePrefix) > -1;
			}
			return (pathname.getName().indexOf(filePrefix) > -1)
					&& pathname.getName().endsWith(fileSuffix);

		}

	}

	/**
	 * The purpose of this class is filter to find folder with specified prefix.
	 */
	static class CustomizedFolderFilter implements FileFilter {
		/**
		 * <code>folderPrefix</code>
		 */
		String folderPrefix;

		@Override
		public boolean accept(File pathname) {
			if (!pathname.isDirectory()) {
				return false;
			}
			if (!StringUtils.isBlank(folderPrefix)) {
				return pathname.getName().indexOf(folderPrefix) > -1;
			}
			throw new RuntimeException(
					"folderPrefix could not be empty or null.");
		}
	}

}
