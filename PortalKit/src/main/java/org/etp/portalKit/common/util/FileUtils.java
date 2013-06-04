package org.etp.portalKit.common.util;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide some APIs to help to get File related
 * stuff from specified folder.
 */
@Component(value = "fileUtil")
public class FileUtils {

	/**
	 * Creates a new <code>File</code> instance by converting the given pathname
	 * string into an abstract pathname. If the given string is the empty
	 * string, then the result is the empty abstract pathname.
	 * 
	 * @param pathname
	 *            A pathname string
	 * @return file instance if created.
	 * @throws NullPointerException
	 *             If the <code>pathname</code> argument is <code>null</code>
	 */
	public File createFile(String pathname) {
		return new File(pathname);
	}

	/**
	 * Creates a new <code>File</code> instance from a parent pathname string
	 * and a child pathname string.
	 * 
	 * <p>
	 * If <code>parent</code> is <code>null</code> then the new
	 * <code>File</code> instance is created as if by invoking the
	 * single-argument <code>File</code> constructor on the given
	 * <code>child</code> pathname string.
	 * 
	 * <p>
	 * Otherwise the <code>parent</code> pathname string is taken to denote a
	 * directory, and the <code>child</code> pathname string is taken to denote
	 * either a directory or a file. If the <code>child</code> pathname string
	 * is absolute then it is converted into a relative pathname in a
	 * system-dependent way. If <code>parent</code> is the empty string then the
	 * new <code>File</code> instance is created by converting
	 * <code>child</code> into an abstract pathname and resolving the result
	 * against a system-dependent default directory. Otherwise each pathname
	 * string is converted into an abstract pathname and the child abstract
	 * pathname is resolved against the parent.
	 * 
	 * @param parent
	 *            The parent pathname string
	 * @param child
	 *            The child pathname string
	 * @return file if created.
	 * @throws NullPointerException
	 *             If <code>child</code> is <code>null</code>
	 */
	public File createFile(String parent, String child) {
		return new File(parent, child);
	}

	/**
	 * Creates a new <code>File</code> instance from a parent abstract pathname
	 * and a child pathname string.
	 * 
	 * <p>
	 * If <code>parent</code> is <code>null</code> then the new
	 * <code>File</code> instance is created as if by invoking the
	 * single-argument <code>File</code> constructor on the given
	 * <code>child</code> pathname string.
	 * 
	 * <p>
	 * Otherwise the <code>parent</code> abstract pathname is taken to denote a
	 * directory, and the <code>child</code> pathname string is taken to denote
	 * either a directory or a file. If the <code>child</code> pathname string
	 * is absolute then it is converted into a relative pathname in a
	 * system-dependent way. If <code>parent</code> is the empty abstract
	 * pathname then the new <code>File</code> instance is created by converting
	 * <code>child</code> into an abstract pathname and resolving the result
	 * against a system-dependent default directory. Otherwise each pathname
	 * string is converted into an abstract pathname and the child abstract
	 * pathname is resolved against the parent.
	 * 
	 * @param parent
	 *            The parent abstract pathname
	 * @param child
	 *            The child pathname string
	 * @return file if created.
	 * @throws NullPointerException
	 *             If <code>child</code> is <code>null</code>
	 */
	public File createFile(File parent, String child) {
		return new File(parent, child);
	}

	/**
	 * Tests whether the file denoted by this abstract pathname is a directory.
	 * 
	 * @param path
	 *            path of directory
	 * @return true if given path is directory, otherwise false.
	 */
	public boolean isDirectory(String path) {
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
	public boolean isDirectory(File path) {
		if (path == null) {
			return false;
		}
		return path.isDirectory();
	}

	/**
	 * Given a baseFoler path, find all the child folder with specified
	 * folderPrefix.
	 * 
	 * @param baseFolder
	 * @param folderPrefix
	 * @return find folder
	 */
	public File[] FolderFinder(String baseFolder, String folderPrefix) {
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
	 * @param baseFolder
	 * @param filePrefix
	 * @param fileSuffix
	 * @return find file
	 */
	public File[] FileFinder(String baseFolder, String filePrefix,
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
	class CustomizedFileFilder implements FileFilter {
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
			} else {
				return (pathname.getName().indexOf(filePrefix) > -1)
						&& pathname.getName().endsWith(fileSuffix);
			}

		}

	}

	/**
	 * The purpose of this class is filter to find folder with specified prefix.
	 */
	class CustomizedFolderFilter implements FileFilter {
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
