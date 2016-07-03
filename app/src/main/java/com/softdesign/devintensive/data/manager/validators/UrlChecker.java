package com.softdesign.devintensive.data.manager.validators;

import com.softdesign.devintensive.utils.ConstantManager;

public class UrlChecker extends BaseValidatorConditionChecker{
    private int mMinNumber, mMaxNumber;
    private String mPrefix;

    /**
     * Create checker for URL
     * @param error Message error
     * @param prefix Ideal prefix
     * @param minNumber Min position get prefix from string to check
     * @param maxNumber Max position get prefix from string to check
     */
    public UrlChecker(String error, String prefix, int minNumber, int maxNumber) {
        super(error);
        mMinNumber = minNumber;
        mMaxNumber = maxNumber;
        mPrefix = prefix;
    }

    /**
     * Check URL
     * @param textToCheck String for check
     * @return True if is correct
     */
    @Override
    public boolean checkCondition(String textToCheck) {
        return mMaxNumber <= textToCheck.length() && mPrefix.equals(textToCheck.substring(mMinNumber, mMaxNumber));
    }
}
