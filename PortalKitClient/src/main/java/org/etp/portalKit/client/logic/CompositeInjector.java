package org.etp.portalKit.client.logic;

import org.eclipse.swt.widgets.Composite;

/**
 * The purpose of this class is to provide a interface to create a
 * certral panel into main shell.
 */
public interface CompositeInjector {
    /**
     * @param composite
     * @return created composite
     */
    public Composite ceatePanel(Composite composite);

}
