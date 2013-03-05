package org.etp.portalKit.powerbuild.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.util.MavenUtils;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * The purpose of this class is to provide special directories
 * information with DirTree.
 */
@Component(value = "commonBuildListProvider")
public class CommonBuildListProvider {

    private String basePath;
    private List<String> defaultSelection;
    private List<DirTree> basedListTree;

    private boolean hasReset;

    /**
     * Iterate basedListTree for set the absolute path for each item
     * within this basedListTree. Note: each converted absolute path
     * should a regular maven project folder(include a pom.xml and a
     * target folder), if not, remvoe this item from the DirTree which
     * this item belongs to.
     */
    private void resetDirInfo() {
        if (StringUtils.isBlank(basePath))
            throw new RuntimeException("basePath could not be empty or null.");
        if (CollectionUtils.isEmpty(basedListTree))
            throw new RuntimeException("basedListTree could not be empty or null.");
        List<String> selection = null;
        if (CollectionUtils.isEmpty(defaultSelection))
            selection = new ArrayList<String>();
        else
            selection = new ArrayList<String>(defaultSelection);
        Iterator<DirTree> itr = basedListTree.iterator();
        while (itr.hasNext()) {
            DirTree tree = itr.next();
            iterateTreesForConvertPath(tree, itr, selection);
        }
        itr = basedListTree.iterator();
        while (itr.hasNext()) {
            DirTree tree = itr.next();
            iterateTreesForRemoveNoChildItem(tree, itr);
        }
        hasReset = true;
    }

    /**
     * @return get the converted result.
     */
    public List<DirTree> retrieveDirTrees() {
        if (!hasReset)
            resetDirInfo();
        return new ArrayList<DirTree>(basedListTree);
    }

    /**
     * iterate dirTree to check whether the current DirTree contains
     * any child. if not, remove this Dir from current DirTree.
     * Otherwise, continue iterate the sub DirTree.
     * 
     * @param tree The DirTree which will be iterated for removing
     *            no-child item.
     * @param itr used to remove this dir from current DirTree
     */
    private void iterateTreesForRemoveNoChildItem(DirTree tree, Iterator<DirTree> itr) {
        if (CollectionUtils.isEmpty(tree.getSubDirs()) && StringUtils.isBlank(tree.getRelativePath()))
            itr.remove();
        else {
            List<DirTree> subs = tree.getSubDirs();
            Iterator<DirTree> subItr = subs.iterator();
            while (subItr.hasNext()) {
                DirTree subTree = subItr.next();
                iterateTreesForRemoveNoChildItem(subTree, subItr);
            }
        }
    }

    /**
     * iterate dirTree to check whether the relativePath is empty or
     * not, if not, combine it with base and set to absolutePath.
     * Note: if converted absolute path is not a regular maven project
     * path, remove this dir from DirTree.
     * 
     * @param tree The DirTree which will be iterated for converting
     *            absolute path.
     * @param itr used to remove this dir from current DirTree.
     * @param selection defaultSelection that used to set the checked
     *            state.
     */
    private void iterateTreesForConvertPath(DirTree tree, Iterator<DirTree> itr, List<String> selection) {
        tree.setChecked(selection.contains(tree.getName()));
        if (StringUtils.isBlank(tree.getRelativePath())) {
            List<DirTree> subs = tree.getSubDirs();
            if (!CollectionUtils.isEmpty(subs)) {
                Iterator<DirTree> subItr = subs.iterator();
                while (subItr.hasNext()) {
                    DirTree subTree = subItr.next();
                    iterateTreesForConvertPath(subTree, subItr, selection);
                }
            }
        } else {
            String relativePath = tree.getRelativePath();
            File relative = new File(basePath, relativePath);
            if (MavenUtils.isMavenProject(relative))
                tree.setAbsolutePath(relative.getAbsolutePath());
            else
                itr.remove();
        }
    }

    /**
     * @return Returns the basePath.
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * @param basePath a folder path used to combined with relative
     *            path for each item itself to generate absolute path
     *            and set it to each item.
     */
    public void setBasePath(String basePath) {
        if (StringUtils.isBlank(basePath))
            throw new RuntimeException("basePath could not be empty or null.");
        this.basePath = basePath;
        hasReset = false;
    }

    /**
     * @return Returns the defaultSelection.
     */
    public List<String> getDefaultSelection() {
        return defaultSelection;
    }

    /**
     * @param defaultSelection list of selection, if a item included
     *            in defaultSelection, set the checked state to true,
     *            otherwise false.
     */
    public void setDefaultSelection(List<String> defaultSelection) {
        this.defaultSelection = defaultSelection;
        hasReset = false;
    }

    /**
     * @return Returns the basedListTree.
     */
    public List<DirTree> getBasedListTree() {
        return basedListTree;
    }

    /**
     * @param basedListTree a based list of DirTree, which will be
     *            full filled the absolute path of each item.
     */
    public void setBasedListTree(List<DirTree> basedListTree) {
        if (CollectionUtils.isEmpty(basedListTree))
            throw new RuntimeException("basedListTree could not be empty or null.");
        this.basedListTree = basedListTree;
        hasReset = false;
    }
}
