package org.etp.portalKit.common.service;

import java.text.MessageFormat;

import javax.annotation.Resource;

import org.etp.portalKit.common.util.OutputHandler;
import org.etp.portalKit.common.util.ShellRunner;
import org.springframework.stereotype.Component;

/**
 * A <code>ProcessMonitor</code> to observe the status of a specified process,
 * to see if it is exist or not.
 */
@Component(value = "processMonitor")
public class ProcessMonitor {

	@Resource(name = "shellRunner")
	private ShellRunner runner;

	private static final String TASKLIST = "tasklist /V /FI \"WINDOWTITLE eq {0}\"";
	private boolean isStarted;

	/**
	 * @param windowTitle
	 *            title on the window of the program you want to observe.
	 * @param checkPoints string which included in process.
	 * @return true if the specified process is exist.
	 */
	public boolean isExist(String windowTitle, final String[] checkPoints) {
		String taskCmd = MessageFormat.format(TASKLIST, windowTitle);
		isStarted = false;
		runner.setCmdline(taskCmd);
		runner.setOutputHandler(new OutputHandler() {
			@Override
			public void onOutRead(String message, boolean isErrorOut) {
				if (checkPoints == null) {
					return;
				}
				for (String checkPoint : checkPoints) {
					if (message.indexOf(checkPoint) < 0) {
						return;
					}
				}
				isStarted = true;
			}
		});
		runner.run();
		return isStarted;
	}
}
