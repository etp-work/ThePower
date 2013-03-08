package org.etp.portalKit.powerbuild.bean;

import java.util.List;

import org.etp.portalKit.fw.annotation.MarkinFile;

/**
 * The purpose of this class is to provide a Model that hold default
 * selection in power build page.
 */
public class SelectionCommand {

    /**
     * <code>DEFAULT_BUILD_LIST</code> default options within
     * defaultBuildList.
     */
    public static final String DEFAULT_BUILD_LIST = "defaultBuildList";

    @MarkinFile(name = DEFAULT_BUILD_LIST)
    private List<String> selection;

    /**
     * @return Returns the selection.
     */
    public List<String> getSelection() {
        return selection;
    }

    /**
     * @param selection The selection to set.
     */
    public void setSelection(List<String> selection) {
        this.selection = selection;
    }
}
