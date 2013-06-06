package org.etp.portalKit.common.util;

/**
 * The purpose of this class is to provide a interface of command
 * output handler. This should be used with
 */
public interface OutputHandler {
    /**
     * @param message output
     * @param isErrorOut true if it is an error output. Otherwise false.
     */
    public void onOutRead(String message, boolean isErrorOut);
}
