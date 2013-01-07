package org.etp.portalKit.common.util;

/**
 * The purpose of this class is to provide a bean storage to hold the
 * execution infomration.
 */
public class CommandResult {
    private int stateCode;

    private String message;
    private boolean isSuccess;

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

    /**
     * @return indicate that the command is executed successful or
     *         not.
     */
    public boolean isSuccess() {
        return isSuccess;
    }

    /**
     * @param isSuccess
     */
    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
}
