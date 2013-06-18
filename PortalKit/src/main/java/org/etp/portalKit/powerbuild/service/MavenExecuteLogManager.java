/**
 * 
 */
package org.etp.portalKit.powerbuild.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

/**
 * A singleton message container that caches all the failure messages from a
 * maven execution.
 * 
 * @author ehaozuo
 * 
 */
@Component(value = "mavenExecuteLogManager")
public class MavenExecuteLogManager {
	private Map<String, FailureMessage> messages = new HashMap<String, FailureMessage>();
	private Timer timer;

	/**
	 * default constructor
	 */
	public MavenExecuteLogManager() {
		timer = new Timer();
	}

	/**
	 * 
	 * push an error message into message container.
	 * 
	 * @param errorMsg
	 *            failure message that comes from a maven execution.
	 * @return messageId it can be used for fetching this error message by
	 *         client.
	 */
	public String push(String errorMsg) {
		final String messageId = System.currentTimeMillis() + "";
		FailureMessage fm = new FailureMessage();
		fm.errorMsg = errorMsg;
		fm.task = new TimerTask() {
			@Override
			public void run() {
				messages.remove(messageId);
			}
		};
		messages.put(messageId, fm);
		timer.schedule(fm.task, 1800000);
		return messageId;
	}

	/**
	 * @param messageId
	 *            id of an error message.
	 * @return errorMsg if exists, otherwise empty string returned.
	 */
	public String get(String messageId) {
		if (!messages.containsKey(messageId)) {
			return "";
		}
		FailureMessage fm = messages.remove(messageId);
		fm.task.cancel();
		timer.purge();
		return fm.errorMsg;
	}

	/**
	 * cancel timer when destroying this component.
	 */
	@PreDestroy
	public void onDestroy() {
		timer.cancel();
	}

	/**
	 * inner class for errorMsg and timerTask
	 */
	class FailureMessage {
		/**
		 * <code>errorMsg</code> from a maven execution.
		 */
		String errorMsg;
		/**
		 * TimerTask for this errorMsg handling
		 */
		TimerTask task;
	}
}
