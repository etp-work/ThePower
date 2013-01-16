package org.etp.portalKit.powerbuild.bean.request;

import java.util.List;

import org.etp.portalKit.fw.annotation.MarkinFile;

/**
 * The purpose of this class is to provide a Model that hold default
 * selection in power build page.
 */
public class Selection {

    /**
     * <code>SPEC_DEFAULT</code> default options within spec-default.
     */
    public static final String SPEC_DEFAULT = "spec-default";

    @MarkinFile(name = SPEC_DEFAULT)
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
