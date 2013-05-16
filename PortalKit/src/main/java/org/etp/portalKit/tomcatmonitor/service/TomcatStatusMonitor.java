package org.etp.portalKit.tomcatmonitor.service;

import javax.annotation.Resource;

import org.etp.portalKit.common.service.ProcessMonitor;
import org.etp.portalKit.tomcatmonitor.logic.TomcatLogic;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * TomcatStatusMonitor is a monitor to tomcat.
 */
@Component(value = "tomcatStatusMonitor")
@EnableScheduling
public class TomcatStatusMonitor {

    @Resource(name = "processMonitor")
    private ProcessMonitor monitor;

    @Resource(name = "tomcatLogic")
    private TomcatLogic logic;

    /**
     * check if tomcat is alive.
     */
    @Scheduled(fixedDelay = 10000)
    public void monitor() {
        boolean isExist = logic.tomcatStarted();
        logic.updateTomcatStatus(isExist ? TomcatStatus.RUNNING : TomcatStatus.STOPPED);
        System.out.println("Tomcat status = " + System.currentTimeMillis() + " || "
                + (isExist ? TomcatStatus.RUNNING : TomcatStatus.STOPPED));
    }
}
