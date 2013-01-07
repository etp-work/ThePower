package org.etp.portalKit.common.util;

/**
 * The purpose of this class is to provide a interface of command
 * output handle
 */
public interface OutputHandler {
    /**
     * @param message
     * @param isErrorOut
     */
    public void onOutRead(String message, boolean isErrorOut);
}
