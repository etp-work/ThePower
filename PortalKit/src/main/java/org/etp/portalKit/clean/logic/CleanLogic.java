package org.etp.portalKit.clean.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.clean.bean.response.CleanItems;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.FileUtils;
import org.etp.portalKit.setting.bean.Settings;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide a logic layer to handle all
 * the operations of clean functionality.
 */
@Component(value = "cleanLogic")
public class CleanLogic {

    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    private String WIDGET_CACHE_RELATIVE_PATH = "AppData\\Local\\Opera";
    private String WIDGET_HISTORY_RELATIVE_PATH = "AppData\\Roaming\\Opera";
    private String WIDGET_PREFIX = "Widget MSDK Widget";

    private String WAR_FILE_PREFIX1 = "portal-";
    private String WAR_FILE_PREFIX2 = "public";
    private String WAR_FILE_PREFIX3 = "private";

    /**
     * Retrieve all the clean needed stuff. According to user.home and
     * set tomcat path.
     * 
     * @return CleanItems
     */
    public CleanItems retrieveCleanItems() {
        String userHome = System.getProperty("user.home");
        String webappsHome = prop.get(Settings.TOMCAT_WEBAPPS_PATH);
        if (StringUtils.isBlank(userHome))
            throw new NullPointerException("user.home could not be null or empty.");
        if (StringUtils.isBlank(webappsHome))
            throw new NullPointerException("You haven't set tomcat's webapps path yet.");
        CleanItems items = new CleanItems();
        File cacheBase = new File(userHome, WIDGET_CACHE_RELATIVE_PATH);
        File[] caches = FileUtils.FolderFinder(cacheBase.getAbsolutePath(), WIDGET_PREFIX);
        List<String> widgetCaches = new ArrayList<String>();
        for (File file : caches) {
            widgetCaches.add(file.getName());
        }
        items.setWidgetCaches(widgetCaches);
        File[] portalWars = FileUtils.FileFinder(webappsHome, WAR_FILE_PREFIX1, ".war");
        File[] publicWars = FileUtils.FileFinder(webappsHome, WAR_FILE_PREFIX2, ".war");
        File[] privateWars = FileUtils.FileFinder(webappsHome, WAR_FILE_PREFIX3, ".war");

        List<String> warFiles = new ArrayList<String>();
        for (File portal : portalWars) {
            warFiles.add(portal.getName());
        }
        for (File pub : publicWars) {
            warFiles.add(pub.getName());
        }
        for (File pri : privateWars) {
            warFiles.add(pri.getName());
        }
        items.setWarFiles(warFiles);
        return items;
    }
}
