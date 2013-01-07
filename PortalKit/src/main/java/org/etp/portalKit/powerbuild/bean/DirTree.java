package org.etp.portalKit.powerbuild.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to provide a json format allowed data
 * collection.
 */
public class DirTree {
    private String name;
    private String absolutePath;
    private List<DirTree> subDirs;
    private boolean selected;

    /**
     * Creates a new instance of <code>DirTree</code>.
     */
    public DirTree() {
        this("", "");
    }

    /**
     * Creates a new instance of <code>DirTree</code>.
     * 
     * @param n directory name of current node
     * @param ab absolute path of current node
     */
    public DirTree(String n, String ab) {
        this.name = n;
        this.absolutePath = ab;
        this.subDirs = new ArrayList<DirTree>();
    }

    /**
     * @param dt instance of DirTree
     */
    public void addSub(DirTree dt) {
        this.subDirs.add(dt);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the absolutePath.
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * @param absolutePath The absolutePath to set.
     */
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    /**
     * @return Returns the subDirs.
     */
    public List<DirTree> getSubDirs() {
        return subDirs;
    }

    /**
     * @param subDirs The subDirs to set.
     */
    public void setSubDirs(List<DirTree> subDirs) {
        this.subDirs = subDirs;
    }

    /**
     * @return Returns the selected.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected The selected to set.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}