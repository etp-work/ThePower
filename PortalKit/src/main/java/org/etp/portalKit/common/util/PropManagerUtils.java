package org.etp.portalKit.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * This class provide APIs to handler propertiesManager related stuff.
 */
public class PropManagerUtils {

    /**
     * @param arrayString
     * @return object
     */
    public static Object fromString(String arrayString) {
        if (StringUtils.isBlank(arrayString))
            return "";
        if (arrayString.startsWith("[") && arrayString.endsWith("]")) {
            List<String> list = new ArrayList<String>();
            String arrayStringClone = null;
            arrayStringClone = StringUtils.removeEnd(arrayString, "]");
            arrayStringClone = StringUtils.removeStart(arrayStringClone, "[");
            list = Arrays.asList(StringUtils.stripAll(StringUtils.split(arrayStringClone, ',')));
            return list;
        }

        return arrayString;
    }
}
