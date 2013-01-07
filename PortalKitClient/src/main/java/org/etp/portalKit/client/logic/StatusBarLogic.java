package org.etp.portalKit.client.logic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * The purpose of this class is to provide a simple API interface to
 * StatusBar
 */
public class StatusBarLogic {
    private Composite statusbar;
    private Label statusbarLabel;

    /**
     * create a status bar within a top level shell
     * 
     * @param parent
     */
    public void createStatusBar(Shell parent) {
        if (statusbar != null)
            return;
        statusbar = new Composite(parent, SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 19;
        statusbar.setLayoutData(gridData);
        RowLayout layout = new RowLayout();
        layout.marginLeft = layout.marginTop = 0;
        statusbar.setLayout(layout);
        statusbarLabel = new Label(statusbar, SWT.NONE);
        statusbarLabel.setLayoutData(new RowData(SWT.DEFAULT, SWT.DEFAULT));
    }

    /**
     * set text to status bar
     * 
     * @param text
     */
    public void setText(String text) {
        statusbarLabel.setText(text);
        statusbarLabel.pack();
    }

    /**
     * @return true if status bar created. Otherwise false
     */
    public boolean isStatusBarCreated() {

        return statusbar != null;
    }
}
