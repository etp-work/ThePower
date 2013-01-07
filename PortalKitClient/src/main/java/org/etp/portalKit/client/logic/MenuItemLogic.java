package org.etp.portalKit.client.logic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * The purpose of this class is to provide a simple logic API
 * interface of MenuItem
 */
public class MenuItemLogic {
    private List<MenuItemElement> list;
    private Menu menuBar;

    /**
     * Creates a new instance of <code>MenuItemLogic</code>.
     */
    public MenuItemLogic() {
        list = new ArrayList<MenuItemElement>();
    }

    /**
     * @param parent
     * @param display
     * @return created MenuBar
     */
    public Menu createMenuBar(Shell parent, Display display) {
        menuBar = display.getMenuBar();
        if (menuBar == null) {
            menuBar = new Menu(parent, SWT.BAR);
            parent.setMenuBar(menuBar);
        }
        return menuBar;

    }

    /**
     * @return true if menuBar created. Otherwise false
     */
    public boolean isMenuBarCreated() {
        return menuBar != null;
    }

    /**
     * @return Menu bar if created, otherwise null
     */
    public Menu getMenuBar() {
        return menuBar;
    }

    /**
     * @param text
     * @return MenuItemElement of a top MenuItem with specified text
     */
    public MenuItemElement getMenuItemByText(String text) {
        return this.getMenuItemByText(text, null);
    }

    /**
     * @param text
     * @param parentText
     * @return MenuItemElement of a MenuItem with specified text and
     *         parent
     */
    public MenuItemElement getMenuItemByText(String text, String parentText) {
        for (MenuItemElement element : list) {
            if (parentText == null) {
                if (element.getParentText() == null && element.getMenuItemText().equals(text))
                    return element;
            } else if (parentText.equals(element.getParentText()) && element.getMenuItemText().equals(text))
                return element;
        }
        return null;
    }

    /**
     * @param parentText
     * @return an array of MenuItemElement, with a specified parent
     */
    public List<MenuItemElement> getMenuItemsByParentText(String parentText) {
        List<MenuItemElement> result = new ArrayList<MenuItemElement>();
        if (parentText == null) {
            for (MenuItemElement element : list) {
                if (element.getParentText() == null)
                    result.add(element);
            }
            return result;
        }
        for (MenuItemElement element : list) {
            if (element.getParentText().equals(parentText))
                result.add(element);
        }
        return result;
    }

    /**
     * add a top level menuItem
     * 
     * @param text
     * @return added MenuItem
     */
    public MenuItem createMenuItem(String text) {
        MenuItem menuItem = null;
        if (this.getMenuItemByText(text) == null) {
            menuItem = new MenuItem(menuBar, SWT.CASCADE);
            menuItem.setText(text);
            Menu dropdown = new Menu(menuBar);
            menuItem.setMenu(dropdown);
            this.addMenuItem(text, menuItem, dropdown);
        } else
            throw new RuntimeException("It's an exists menuitem there on menuBar");
        return menuItem;
    }

    /**
     * @param text
     * @param parentText
     * @param listener
     * @return added MenuItem
     */
    public MenuItem createMenuItem(String text, String parentText, SelectionListener listener) {
        MenuItem menuItem = null;
        if (this.getMenuItemByText(parentText) == null)
            throw new RuntimeException("Top level MenuItem " + parentText + " doesn't exist");
        if (this.getMenuItemByText(text, parentText) == null) {
            menuItem = new MenuItem(this.getMenuItemByText(parentText).getDropDown(), SWT.PUSH);
            menuItem.setText(text);
            menuItem.addSelectionListener(listener);
            this.addMenuItem(text, parentText, menuItem);
        } else
            throw new RuntimeException("It's an exists menuitem there under menuItem " + parentText);
        return menuItem;
    }

    /**
     * Add an MenuItem into logic controller
     * 
     * @param text
     * @param parentText
     * @param menuItem
     * @param dropDwon
     */
    private void addMenuItem(String text, String parentText, MenuItem menuItem, Menu dropDwon) {
        MenuItemElement element = new MenuItemElement();
        element.setMenuItem(menuItem);
        element.setMenuItemText(text);
        element.setParentText(parentText);
        element.setDropDown(dropDwon);
        list.add(element);
    }

    /**
     * Add a MenuItem into logic controller to specified parent
     * 
     * @param text
     * @param parentText
     * @param menuItem
     */
    private void addMenuItem(String text, String parentText, MenuItem menuItem) {
        this.addMenuItem(text, parentText, menuItem, null);
    }

    /**
     * Add a top level MenuItem into logic controller
     * 
     * @param text
     * @param menuItem
     * @param dropDwon
     */
    private void addMenuItem(String text, MenuItem menuItem, Menu dropDwon) {
        this.addMenuItem(text, null, menuItem, dropDwon);
    }
}
