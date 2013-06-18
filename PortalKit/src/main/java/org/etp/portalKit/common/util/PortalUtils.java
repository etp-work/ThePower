package org.etp.portalKit.common.util;

import java.io.File;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.springframework.stereotype.Component;

/**
 * This class provide functionalities that help developers easily to access war
 * files related stuff under tomcat.
 * 
 * @author ehaozuo
 * 
 */
@Component(value = "portalUtils")
public class PortalUtils {
	@Resource(name = "propertiesManager")
	private PropertiesManager prop;

	/**
	 * Fetch the deployed war folder under tomcat's webapps, and the name of
	 * this folder should contain <code>folderPrefix</code>
	 * 
	 * @param folderPrefix
	 *            to narrow the range of war folders.
	 * @return File fetched folder instance
	 */
	public File fetchDeployedWar(String folderPrefix) {
		String webapps = prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
		if (StringUtils.isBlank(webapps))
			return null;
		File[] warFolders = FileUtils.FolderFinder(webapps, folderPrefix);
		if (ArrayUtils.isEmpty(warFolders))
			return null;
		return warFolders[0];
	}

	/**
	 * Fetch the name of deployed war folder under tomcat's webapps, and the
	 * name of this folder should contain <code>folderPrefix</code>
	 * 
	 * @param folderPrefix
	 *            to narrow the range of war folders.
	 * @param defaultName
	 *            if the deployed war folder doesn't exist, defaultName will be
	 *            returned.
	 * @return String fetched name
	 */
	public String fetchDeployedWarName(String folderPrefix, String defaultName) {
		File war = fetchDeployedWar(folderPrefix);
		return war == null ? defaultName : war.getName();
	}
}
