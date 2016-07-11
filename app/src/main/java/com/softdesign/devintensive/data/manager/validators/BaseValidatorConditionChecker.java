package com.softdesign.devintensive.data.manager.validators;

import com.softdesign.devintensive.utils.ConstantManager;

public abstract class BaseValidatorConditionChecker {

    private String errorMessage_;

    /**
     * Create checker with default error message.
     */
    public BaseValidatorConditionChecker() {
        setErrorMessage(ConstantManager.STANDART_ERROR_MESSAGE);
    }

    /**
     * Create checker and set error message.
     */
    public BaseValidatorConditionChecker(String errorMessage) {
        setErrorMessage(errorMessage);
    }

    /**
     * Check whether text fulfill the condition.
     */
    public abstract boolean checkCondition(String textToCheck);

    /**
     * Set error message.
     */
    public void setErrorMessage(String message) {
        errorMessage_ = message;
    }

    /**
     * Get error message.
     */
    public String getErrorMessage() {
        return errorMessage_;
    }
}