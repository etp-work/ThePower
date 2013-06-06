package org.etp.portalKit.common.util;

/**
 * The purpose of this class is to provide a bean storage to hold the
 * execution infomration.
 */
public class CommandResult {
    private int stateCode;

    private String messageId;
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
     * @param stateCode code of this execution.
     */
    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * @return messageId from execution.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId id of this errorMessage
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
