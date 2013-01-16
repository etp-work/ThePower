package org.etp.portalKit.powerbuild.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.JSONUtils;
import org.etp.portalKit.powerbuild.bean.request.Selection;
import org.etp.portalKit.powerbuild.bean.response.DirTree;
import org.etp.portalKit.setting.bean.Settings;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * The purpose of this class is to provide special directories
 * information with DirTree.
 */
@Component(value = "specProvider")
public class SpecDirProvider implements DirProvider {

    @javax.annotation.Resource(name = "pathMatchingResourcePatternResolver")
    private PathMatchingResourcePatternResolver resolver;
    @javax.annotation.Resource(name = "propertiesManager")
    private PropertiesManager handler;
    private List<String> selected;

    private String SPECIAL_DIR = "specialDirs.json";

    /**
     * Creates a new instance of <code>SpecDirProvider</code>.
     */
    public SpecDirProvider() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DirTree> retrieveDirInfo() {
        List<DirTree> list = new ArrayList<DirTree>();
        String path = handler.get(Settings.PORTAL_TEAM_PATH);
        if (path == null)
            return list;
        String defs = handler.get(Selection.SPEC_DEFAULT);
        if (!StringUtils.isBlank(defs))
            selected = (List<String>) org.etp.portalKit.common.util.StringUtils.fromString(defs);
        else
            selected = new ArrayList<String>();
        Resource resource = resolver.getResource(SPECIAL_DIR);
        String json = null;
        try {
            json = FileUtils.readFileToString(resource.getFile(), Charsets.UTF_8);
        } catch (IOException e) {
            return list;
        }
        list = JSONUtils.fromJSON(json, new TypeReference<List<DirTree>>() {
            //            
        });
        Iterator<DirTree> itr = list.iterator();
        while (itr.hasNext()) {
            DirTree tree = itr.next();
            iterateTrees(tree, path, itr);
        }
        return list;
    }

    private void iterateTrees(DirTree tree, String base, Iterator<DirTree> itr) {
        tree.setChecked(selected.contains(tree.getName()));
        if (StringUtils.isBlank(tree.getRelativePath())) {
            List<DirTree> subs = tree.getSubDirs();
            if (!CollectionUtils.isEmpty(subs)) {
                Iterator<DirTree> subItr = subs.iterator();
                while (subItr.hasNext()) {
                    DirTree subTree = subItr.next();
                    iterateTrees(subTree, base, subItr);
                }
            }
        } else {
            String relativePath = tree.getRelativePath();
            File relative = new File(base, relativePath);
            if (relative.isDirectory())
                tree.setAbsolutePath(relative.getAbsolutePath());
            else
                itr.remove();
        }
    }
}
