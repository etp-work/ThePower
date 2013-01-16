package org.etp.portalKit.common.shell;

/**
 * The purpose of this class is to provide a bean storage to hold the
 * execution infomration.
 */
public class CommandResult {
    private int stateCode;

    private String message;
    private boolean success;

    /**
     * @return Returns the success.
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success The success to set.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return stateCode from execution.
     */
    public int getStateCode() {
        return stateCode;
    }

    /**
     * @param stateCode
     */
    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * @return message from execution.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
