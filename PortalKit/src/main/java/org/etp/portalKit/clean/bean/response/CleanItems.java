package org.etp.portalKit.clean.bean.response;

import java.util.List;

/**
 * The class hold the data of clean functionality that will be rendor
 * to client.
 */
public class CleanItems {
    private List<String> widgetCaches;
    private List<String> warFiles;

    /**
     * @return Returns the widgetCaches.
     */
    public List<String> getWidgetCaches() {
        return widgetCaches;
    }

    /**
     * @param widgetCaches The widgetCaches to set.
     */
    public void setWidgetCaches(List<String> widgetCaches) {
        this.widgetCaches = widgetCaches;
    }

    /**
     * @return Returns the warFiles.
     */
    public List<String> getWarFiles() {
        return warFiles;
    }

    /**
     * @param warFiles The warFiles to set.
     */
    public void setWarFiles(List<String> warFiles) {
        this.warFiles = warFiles;
    }
}
