package org.etp.portalKit.common.shell;

/**
 * The purpose of this class is to provide a interface of command
 * output handler. This should be used with
 */
public interface OutputHandler {
    /**
     * @param message
     * @param isErrorOut
     */
    public void onOutRead(String message, boolean isErrorOut);
}
