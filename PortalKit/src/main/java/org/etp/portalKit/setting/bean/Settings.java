package org.etp.portalKit.setting.bean;

import org.etp.portalKit.fw.annotation.MarkinFile;

/**
 * The purpose of this class is a settings content interface that used
 * to convert post parameters to a instance of java.
 */
public class Settings {

    /**
     * <code>PORTAL_TEAM_PATH</code> absolute path of portal-team
     * repository.
     */
    public static final String PORTAL_TEAM_PATH = "portal-team-path";
    /**
     * <code>TOMCAT_WEBAPPS_PATH</code> absolute path of tomcat's
     * webapps wich you have installed in your laptop.
     */
    public static final String TOMCAT_WEBAPPS_PATH = "tomcat-webapps-path";

    @MarkinFile(name = PORTAL_TEAM_PATH)
    private String portalTeamPath;

    @MarkinFile(name = TOMCAT_WEBAPPS_PATH)
    private String tomcatWebappsPath;

    /**
     * @return Returns the portalTeamPath.
     */
    public String getPortalTeamPath() {
        return portalTeamPath;
    }

    /**
     * @param portalTeamPath The portalTeamPath to set.
     */
    public void setPortalTeamPath(String portalTeamPath) {
        this.portalTeamPath = portalTeamPath;
    }

    /**
     * @return Returns the tomcatWebappsPath.
     */
    public String getTomcatWebappsPath() {
        return tomcatWebappsPath;
    }

    /**
     * @param tomcatWebappsPath The tomcatWebappsPath to set.
     */
    public void setTomcatWebappsPath(String tomcatWebappsPath) {
        this.tomcatWebappsPath = tomcatWebappsPath;
    }
}
