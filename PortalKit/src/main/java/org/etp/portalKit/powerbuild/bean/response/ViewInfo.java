package org.etp.portalKit.powerbuild.bean.response;

/**
 * The purpose of this class is a model that contains information of
 * view settings, which will be used in index.jsp.
 */
public class ViewInfo {
    private String viewId;
    private String viewName;
    private String js;
    private String templateUrl;
    private boolean defaultView;

    /**
     * @return Returns the defaultView.
     */
    public boolean isDefaultView() {
        return defaultView;
    }

    /**
     * @param defaultView The defaultView to set.
     */
    public void setDefaultView(boolean defaultView) {
        this.defaultView = defaultView;
    }

    /**
     * @return Returns the templateUrl.
     */
    public String getTemplateUrl() {
        return templateUrl;
    }

    /**
     * @param templateUrl The templateUrl to set.
     */
    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

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
