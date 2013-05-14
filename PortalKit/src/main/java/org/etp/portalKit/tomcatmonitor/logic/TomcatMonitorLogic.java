package org.etp.portalKit.tomcatmonitor.logic;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.ProcessMonitor;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.ShellRunner;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.springframework.stereotype.Component;

/**
 * Logic layer of TomcatMonitor
 */
@Component(value = "tomcatMonitorLogic")
public class TomcatMonitorLogic {

    @Resource(name = "processMonitor")
    private ProcessMonitor monitor;

    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    private ShellRunner runner;

    /**
     * @return true if tomcat is started. Otherwise false.
     */
    public boolean tomcatStarted() {
        return monitor.isExist("Tomcat", new String[] { "java", "Tomcat" });
    }

    /**
     * @return true if the tomcat you have installed on your laptop is
     *         controllable. Otherwise, false.
     */
    public boolean canBeControlled() {
        String webapps = prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
        if (StringUtils.isBlank(webapps)) {
            throw new RuntimeException("You have set tomcat's webapps path first.");
        }
        File webappsF = new File(webapps);
        if (!webappsF.isDirectory()) {
            throw new RuntimeException("You have set a incorrect tomcat's webapps path.");
        }
        File bin = new File(webappsF.getParent(), "bin");
        if (!bin.isDirectory()) {
            throw new RuntimeException("You have set a incorrect tomcat's webapps path.");
        }
        File startUp = new File(bin, "startup.bat");
        return startUp.exists();
    }

    private String getCatalinaHome() {
        String webapps = prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
        if (StringUtils.isBlank(webapps)) {
            throw new RuntimeException("You have set tomcat's webapps path first.");
        }
        File webappsF = new File(webapps);
        if (!webappsF.isDirectory()) {
            throw new RuntimeException("You have set a incorrect tomcat's webapps path.");
        }

        File catalineHome = new File(webappsF.getParentFile().getAbsolutePath());
        return catalineHome.getAbsolutePath();
        //        return "E:\\Study\\designenv\\apache-tomcat-7.0.33";
    }

    /**
     * @return true if tomcat started.
     */
    public boolean startTomcat() {
        if ((runner != null) && runner.processIsRunning()) {
            throw new RuntimeException("Tomcat has been started up.");
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                if (runner == null) {
                    runner = new ShellRunner();
                }
                runner.setCmdline(getCatalinaHome() + "\\bin\\startup.bat");
                Map<String, String> getenv = System.getenv();
                Properties p = new Properties();
                p.putAll(System.getProperties());
                p.putAll(getenv);
                p.setProperty("CATALINA_HOME", getCatalinaHome());
                runner.setRuningEnv(p);
                runner.run();
            }
        });
        t.start();
        return true;
    }

    /**
     * @return true if tomcat stopped
     */
    public boolean stopTomcat() {
        if ((runner != null) && runner.processIsRunning()) {
            runner.stopProcess();
        }
        return true;
    }
}
