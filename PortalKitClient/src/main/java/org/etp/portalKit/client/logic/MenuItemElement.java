package org.etp.portalKit.client.logic;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * The purpose of this class is to provide source controller to
 * MenuItem
 */
public class MenuItemElement {
    private String MenuItemText;
    private MenuItem menuItem;
    private Menu dropDown;
    private String parentText;

    /**
     * @return dropDown menu of current MenuItem if it is a top
     *         MenuItem. Otherwise null
     */
    public Menu getDropDown() {
        return dropDown;
    }

    /**
     * @param dropDown
     */
    public void setDropDown(Menu dropDown) {
        this.dropDown = dropDown;
    }

    /**
     * @return text of current MenuItem
     */
    public String getMenuItemText() {
        return MenuItemText;
    }

    /**
     * @param menuItemText
     */
    public void setMenuItemText(String menuItemText) {
        MenuItemText = menuItemText;
    }

    /**
     * @return instance of current MenuItem
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * @param menuItem
     */
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    /**
     * @return text of current MenuItem's parent
     */
    public String getParentText() {
        return parentText;
    }

    /**
     * @param parentText
     */
    public void setParentText(String parentText) {
        this.parentText = parentText;
    }
}
