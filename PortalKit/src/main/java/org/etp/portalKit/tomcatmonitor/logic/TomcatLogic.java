package org.etp.portalKit.tomcatmonitor.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.ProcessMonitor;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.ShellRunner;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.etp.portalKit.tomcatmonitor.service.TomcatStatus;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Logic layer of Tomcat.
 */
@Component(value = "tomcatLogic")
@Scope("singleton")
public class TomcatLogic {

    @Resource(name = "processMonitor")
    private ProcessMonitor monitor;

    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    private ShellRunner runner;

    private List<DeferredResult<TomcatStatus>> queue = new ArrayList<DeferredResult<TomcatStatus>>();

    private TomcatStatus lastStatus;

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
     * Update <code>TomcatStatus</code> by another thread.
     * 
     * @param status
     */
    public void updateTomcatStatus(TomcatStatus status) {
        if (queue.size() <= 0) {
            return;
        }
        if (lastStatus == status) {
            return;
        }
        queue.get(0).setResult(status);
        lastStatus = status;
    }

    /**
     * @param firstRequest true if it is first time to request this
     *            status.
     * @return <code>TomcatStatus.RUNNING</code>,
     *         <code>TomcatStatus.STOPPED</code>
     */
    public DeferredResult<TomcatStatus> retrieveStatus(boolean firstRequest) {
        final DeferredResult<TomcatStatus> deferredResult = new DeferredResult<TomcatStatus>();
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                queue.remove(deferredResult);
            }
        });
        queue.add(deferredResult);

        if (firstRequest) {
            TomcatStatus status = tomcatStarted() ? TomcatStatus.RUNNING : TomcatStatus.STOPPED;
            queue.get(0).setResult(status);
            lastStatus = status;
        }
        return deferredResult;
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
