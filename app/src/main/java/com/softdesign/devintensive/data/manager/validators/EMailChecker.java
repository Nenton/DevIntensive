package com.softdesign.devintensive.data.manager.validators;

import com.softdesign.devintensive.utils.ConstantManager;

public class EMailChecker extends BaseValidatorConditionChecker{


    /**
     * Create checker for Email
     * @param error Message error
     */
    public EMailChecker(String error) {
        super(error);
    }

    /**
     * Check Email
     * @param textToCheck Email to check
     * @return True if Email is correct
     */
    @Override
    public boolean checkCondition(String textToCheck) {
        char[] chars = textToCheck.trim().toCharArray();
        int posDog = ConstantManager.NULL;
        int posPoint= ConstantManager.NULL;
        for (int i = chars.length - 1; i >= 0 ; i--) {
            if (chars[i] == ConstantManager.EMAIL) posDog = i;
            if (chars[i] == ConstantManager.POINT) posPoint = i;
        }
        return posDog >= ConstantManager.NUMBER_SYMBOL_BEFORE_EMAIL
                && posPoint - posDog - 1 >= ConstantManager.NUMBER_SYMBOL_BEFORE_POINT
                && chars.length - posPoint - 1 >= ConstantManager.NUMBER_SYMBOL_AFTER_POINT;
    }
}
