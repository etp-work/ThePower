package org.etp.portalKit.powerbuild.service;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.setting.bean.Settings;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide directories information
 * within JSON format.
 */
@Component(value = "designProvider")
public class DesignDirProvider implements DirProvider {

    private FileFilter filter;
    private List<String> selected;

    @Resource(name = "propertiesManager")
    private PropertiesManager handler;

    /**
     * Creates a new instance of <code>DirectoryProvider</code>.
     */
    public DesignDirProvider() {
        filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isFile())
                    return false;
                File[] subFiles = pathname.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File subname) {
                        return subname.isFile() && "pom.xml".equals(subname.getName());
                    }
                });
                return subFiles.length == 1;
            }
        };

    }

    private boolean hasSubElement(File file) {
        if (file.isFile())
            return false;
        if (file.listFiles(filter).length > 0)
            return true;
        return false;
    }

    @Override
    public List<DirTree> retrieveDirInfo() {
        List<DirTree> list = new ArrayList<DirTree>();
        String path = handler.get(Settings.PORTAL_TEAM_PATH);
        if (StringUtils.isBlank(path))
            return list;
        String defs = handler.get("pt-default");
        if (!StringUtils.isBlank(defs))
            selected = Arrays.asList(StringUtils.split(defs, ','));
        else
            selected = new ArrayList<String>();
        File root = new File(path);
        if (!root.isDirectory())
            return list;
        DirTree rootTree = new DirTree(root.getName(), root.getAbsolutePath());
        rootTree.setChecked(selected.contains(root.getName()) ? "checked" : "");
        iterateTrees(root, rootTree);
        list.add(rootTree);
        return list;
    }

    private void iterateTrees(File parent, DirTree dt) {
        File[] files = parent.listFiles(filter);
        for (File file : files) {
            DirTree subDt = new DirTree(file.getName(), file.getAbsolutePath());
            subDt.setChecked(selected.contains(file.getName()) ? "checked" : "");
            dt.addSub(subDt);
            if (hasSubElement(file)) {
                iterateTrees(file, subDt);
            }
        }
    }
}
