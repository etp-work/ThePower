package org.etp.portalKit.client.logic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

/**
 * The purpose of this class is ...
 */
public class AutoHideComposite {
    private boolean isHidden;
    private Display display;
    private Shell shell;
    private Composite centralPanel;
    private MenuItemLogic menuItemLogic;
    private StatusBarLogic statusBarLogic;
    private Tray tray;
    private TrayItem trayItem;

    private MouseMoveListener mmlistener;
    private MouseTrackListener mtlistener;
    private ControlListener clistener;

    private int EXTRA_HEIGHT = 10;

    /**
     * Creates a new instance of <code>AutoHideComposite</code>.
     */
    public AutoHideComposite() {
        init();
    }

    private void init() {
        menuItemLogic = new MenuItemLogic();
        statusBarLogic = new StatusBarLogic();
        display = new Display();
        tray = display.getSystemTray();
        trayItem = new TrayItem(tray, SWT.NONE);
        trayItem.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
        trayItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (isHidden) {
                    Rectangle bounds = shell.getBounds();
                    shell.setLocation(bounds.x, 0);
                    isHidden = false;
                    shell.addMouseTrackListener(mtlistener);
                } else {
                    if (!shell.isVisible()) {
                        shell.setVisible(true);
                        shell.setMinimized(false);
                    }
                }
            }
        });

        int style = SWT.ON_TOP | SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.MIN;
        shell = new Shell(display, style);
        shell.addShellListener(new ShellAdapter() {
            @Override
            public void shellIconified(ShellEvent e) {
                shell.setVisible(false);
            }

        });
        mmlistener = new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                Rectangle bounds = shell.getBounds();
                Point p = display.getCursorLocation();
                if (isHidden && p.y == 0) {
                    shell.setLocation(bounds.x, 0);
                    isHidden = false;
                    shell.addMouseTrackListener(mtlistener);
                    centralPanel.removeMouseMoveListener(mmlistener);
                }
            }
        };

        mtlistener = new MouseTrackAdapter() {
            @Override
            public void mouseExit(MouseEvent e) {
                Rectangle bounds = shell.getBounds();
                System.out.println("y = " + bounds.y + "||isHidden = " + isHidden);
                if (!isHidden && bounds.y < 1) {
                    shell.setLocation(bounds.x, EXTRA_HEIGHT - bounds.height);
                    shell.removeMouseTrackListener(mtlistener);
                    isHidden = true;
                }
            }
        };

        clistener = new ControlAdapter() {
            @Override
            public void controlMoved(ControlEvent e) {
                Rectangle bounds = shell.getBounds();
                if (bounds.y < 1 && !isHidden) {
                    shell.setLocation(bounds.x, EXTRA_HEIGHT - bounds.height);
                    shell.removeMouseTrackListener(mtlistener);
                    isHidden = true;
                    centralPanel.addMouseMoveListener(mmlistener);
                }
            }
        };
        shell.addControlListener(clistener);
    }

    /**
     * Add a new component into stack
     * 
     * @param injector
     * @return added composite
     */
    public Composite setPanel(CompositeInjector injector) {
        centralPanel = injector.ceatePanel(shell);
        return centralPanel;
    }

    /**
     * @return created MenuBar
     */
    public Menu createMenuBar() {
        return menuItemLogic.createMenuBar(shell, display);
    }

    /**
     * Add a menuItem to the top of MenuBar
     * 
     * @param text
     * @return added top MenuItem
     */
    public MenuItem addMenuItem(String text) {
        if (!menuItemLogic.isMenuBarCreated())
            this.createMenuBar();
        return this.addMenuItem(text, null, null);
    }

    /**
     * Add a MenuItem to specified parent
     * 
     * @param text text of MenuItem
     * @param parentText text of the MenuItem's parent
     * @param listener listener to the MenuItem
     * @return added MenuItem
     */
    public MenuItem addMenuItem(String text, String parentText, SelectionListener listener) {
        if (!menuItemLogic.isMenuBarCreated())
            throw new RuntimeException("Menu bar has not created, you cannot add Menuitem.");
        if (parentText == null || parentText.trim().equals(""))
            return menuItemLogic.createMenuItem(text);
        return menuItemLogic.createMenuItem(text, parentText, listener);
    }

    /**
     * create StatusBar
     */
    public void createStatusbar() {
        createStatusbar(null);
    }

    /**
     * create StatusBar with specified text
     * 
     * @param text
     */
    public void createStatusbar(String text) {
        statusBarLogic.createStatusBar(shell);
        if (text != null)
            statusBarLogic.setText(text);
    }

    /**
     * set text to status bar
     * 
     * @param text
     */
    public void setTextToStatusbar(String text) {
        if (!statusBarLogic.isStatusBarCreated())
            createStatusbar(text);
        statusBarLogic.setText(text);
    }

    /**
     * Sets the receiver's size to the point specified by the
     * arguments. Note: Attempting to set the width or height of the
     * receiver to a negative number will cause that value to be set
     * to zero instead.
     * 
     * @param width the new width for the receiver
     * @param height the new height for the receiver
     */
    public void setSize(int width, int height) {
        shell.setSize(width, height);
    }

    /**
     * Sets the receiver's text, which is the string that the window
     * manager will typically display as the receiver's title, to the
     * argument, which must not be null.
     * 
     * @param title the new text of title
     */
    public void setTitle(String title) {
        shell.setText(title);
        trayItem.setToolTipText(title);
    }

    /**
     * Moves the receiver to the top of the drawing order for the
     * display on which it was created (so that all other shells on
     * that display, which are not the receiver's children will be
     * drawn behind it), marks it visible, sets the focus and asks the
     * window manager to make the shell active.
     */
    public void display() {
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        AutoHideComposite auto = new AutoHideComposite();
        auto.setSize(200, 500);
        auto.display();
    }
}
