package org.etp.portalKit.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Operations on {@link java.lang.String} that are <code>null</code>
 * safe.
 * </p>
 */
public class StringUtils {

    /**
     * @param arrayString
     * @return List<String>
     */
    public static List<String> fromString(String arrayString) {
        List<String> list = new ArrayList<String>();
        if (org.apache.commons.lang.StringUtils.isBlank(arrayString))
            return list;
        String arrayStringClone = null;
        if (arrayString.startsWith("[") && arrayString.endsWith("]")) {
            arrayStringClone = org.apache.commons.lang.StringUtils.removeEnd(arrayString, "]");
            arrayStringClone = org.apache.commons.lang.StringUtils.removeStart(arrayStringClone, "[");
        } else {
            arrayStringClone = arrayString;
        }

        list = Arrays.asList(org.apache.commons.lang.StringUtils.split(arrayStringClone, ','));
        return list;
    }
}
