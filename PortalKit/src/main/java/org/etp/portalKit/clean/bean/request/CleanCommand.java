package org.etp.portalKit.clean.bean.request;

/**
 * The class hold data of clean functionality that which item will be
 * removed.
 */
public class CleanCommand {
    private String cleanItem;
    private String cleanType;

    /**
     * @return Returns the cleanItem.
     */
    public String getCleanItem() {
        return cleanItem;
    }

    /**
     * @param cleanItem The cleanItem to set.
     */
    public void setCleanItem(String cleanItem) {
        this.cleanItem = cleanItem;
    }

    /**
     * @return Returns the cleanType.
     */
    public String getCleanType() {
        return cleanType;
    }

    /**
     * @param cleanType The cleanType to set.
     */
    public void setCleanType(String cleanType) {
        this.cleanType = cleanType;
    }
}
