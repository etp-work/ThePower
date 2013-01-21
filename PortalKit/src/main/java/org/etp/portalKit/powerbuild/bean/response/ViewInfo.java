package org.etp.portalKit.powerbuild.bean.response;

/**
 * The purpose of this class is a model that contains information of
 * view settings, which will be used in index.jsp.
 */
public class ViewInfo {
    private String viewId;
    private String viewName;
    private String js;

    /**
     * @return Returns the viewId.
     */
    public String getViewId() {
        return viewId;
    }

    /**
     * @param viewId The viewId to set.
     */
    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    /**
     * @return Returns the viewName.
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * @param viewName The viewName to set.
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * @return Returns the js.
     */
    public String getJs() {
        return js;
    }

    /**
     * @param js The js to set.
     */
    public void setJs(String js) {
        this.js = js;
    }

}
