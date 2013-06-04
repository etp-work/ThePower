package org.etp.portalKit.tomcatmonitor;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import org.etp.portalKit.ssp.logic.SspNotificationManager;
import org.etp.portalKit.tomcatmonitor.logic.TomcatLogic;
import org.etp.portalKit.tomcatmonitor.service.TomcatStatus;
import org.etp.portalKit.tomcatmonitor.service.TomcatStatusMonitor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * test <code>TomcatStatusMonitor</code>
 */
@RunWith(MockitoJUnitRunner.class)
public class TomcatStatusMonitorTest {

	@InjectMocks
	private TomcatStatusMonitor monitor;
	@Mock
	private TomcatLogic logic;

	@Mock
	private SspNotificationManager ssp;

	/**
	 * test monitor method, with notification
	 */
	@Test
	public void monitorWithNotify_test() {
		when(logic.retrieveStatus()).thenReturn(TomcatStatus.RUNNING);
		doNothing().when(ssp).notifyClient(anyString());
		monitor.monitor();
		verify(logic).retrieveStatus();
		verify(ssp).notifyClient(anyString());
	}

	/**
	 * test monitor method, with notification
	 */
	@Test
	public void monitorWithoutNotify_test() {
		when(logic.retrieveStatus()).thenReturn(TomcatStatus.RUNNING);
		doNothing().when(ssp).notifyClient(anyString());
		monitor.monitor();
		monitor.monitor();
		verify(logic, times(2)).retrieveStatus();
		verify(ssp, times(1)).notifyClient(anyString());
	}
}
