package com.softdesign.devintensive.data.manager.validators;

import java.util.HashMap;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Check state of multiple validators to give one response.
 * @author Dmitry Sitnikov
 */
public class ValidationSummary implements ValidationChangesListener {

    /** Map of all validators and their state */
    HashMap<BaseValidator, Boolean> validatorsState = new HashMap<BaseValidator, Boolean>();

    //Listened validators state - true when all validator has no errors.
    boolean isCurrentStateCorrect = false;
    private int mIntNormalText;
    String errorMessage;

    /**
     * Create validator summary and set error message and view to display it.
     * Set errorView visibility to GONE at call.
     * @param errorMessage error message.
     */
    public ValidationSummary(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void onValidationStateUpdated(BaseValidator sender, boolean isValid) {
        validatorsState.put(sender, isValid);
        for (BaseValidator validator: validatorsState.keySet()) {
            if ( !validatorsState.get(validator)) { //One mismatch - set false to state.
                isCurrentStateCorrect = false;
                return;
            }
        }
        isCurrentStateCorrect = true;
    }

    /**
     * Add validator to control their state.
     * Do not modify current state of ValidationSummary.
     */
    public void addValidator(BaseValidator validator) {
        if (!validatorsState.containsKey(validator)) {
            validatorsState.put(validator, true);
            validator.setChangesListener(this);
        }
    }

    /**
     * Is all validators do not contains errors.
     */
    public boolean isCorrect() {
        return isCurrentStateCorrect;
    }

    /**
     * Force check for all nested validators.
     */
    public void performCheck(CoordinatorLayout coordinatorLayout) {
        for (BaseValidator validator: validatorsState.keySet()) {
            validator.displayError(validator.checkConditions(),((EditTextValidator)validator).getInt()) ;
        }
        if (!isCorrect()) Snackbar.make(coordinatorLayout,errorMessage,Snackbar.LENGTH_LONG).show();
    }
}