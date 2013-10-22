package org.etp.portalKit.powerbuild.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The purpose of this class is to provide a json format allowed data
 * collection.
 */
public class DirTree implements Serializable {
    /**
     * <code>serialVersionUID</code>, For cloning purpose.
     */
    private static final long serialVersionUID = -5814542843030755068L;
    private String name;
    private String absolutePath;
    private String relativePath;
    private List<DirTree> subDirs;
    private boolean checked;

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
     * @return Returns the relativePath.
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * @param relativePath The relativePath to set.
     */
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
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
     * @return checked
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * @param checked true if this dir is selected.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}