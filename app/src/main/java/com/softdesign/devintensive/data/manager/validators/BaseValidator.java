package com.softdesign.devintensive.data.manager.validators;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.softdesign.devintensive.R;

/**
 * Base validator class allow to check some conditions and notify about checking results.
 * To use:
 * 1. Create one not abstract validator.
 * 2. Set View to validate.
 * 3. Add as many checkers as you want (note: first added checker has highest priority).
 * All checker are sublasses of {@link BaseValidatorConditionChecker}.
 */
public abstract class BaseValidator {

    protected TextInputLayout externalErrorView = null;
    private ArrayList<BaseValidatorConditionChecker> checkers = new ArrayList<BaseValidatorConditionChecker>();
    private ValidationChangesListener changesListener = null;
    private Activity mActivity;

    public enum ValidationMode {
        /** Check conditions automatically. On data changes, focus loose etc. */
        Auto,
        /** Check manually. */
        Manual
    }
    /**
     * Set view to validate their data.
     * @param view - view to validate.
     * @param mode - validation mode.
     * @throws IllegalArgumentException when view could not be validated.
     */
    public abstract void setViewToValidate(View view, ValidationMode mode) throws IllegalArgumentException;

    /**
     * Set view to validate their data.
     * Overloaded method, launches {@link #setViewToValidate(View, ValidationMode)} with mode {@link ValidationMode#Auto}
     */
    public void setViewToValidate(View view) throws IllegalArgumentException {
        setViewToValidate(view, ValidationMode.Auto);
    }

    /**
     * Get text from validated view.
     */
    protected abstract String getTextToValidate();

    /**
     * Display an error message in external error view if is was set.
     * When external error view not set does nothing.
     * @param error - message, if no error pass null.
     */
    protected void displayError(String error,int intNormal) {
        if (externalErrorView != null) {
            if (error != null) {
//                externalErrorView.setHint(externalErrorView.getHint()+error);
//                externalErrorView.text;
                externalErrorView.setErrorEnabled(true);
                externalErrorView.setError(error);
            } else {
//                externalErrorView.setHint(mActivity.getResources().getString(intNormal));
                externalErrorView.setErrorEnabled(false);
            }
        }
    }

    /**
     * Perform checks manually.
     */
    public void performCheck(int stringNormal) {
        displayError(checkConditions(),stringNormal);
    }

    //External error view ------------------------------------------------------

    /**
     * Set text view as a place to display error.
     * Set errorView visibility to GONE at call.
     */
    public void setExternalErrorView(TextInputLayout errorView, Activity activity) {
        resetExternalErrorView();
        externalErrorView = errorView;
        mActivity = activity;
    }
    /**
     * Remove external error view.
     */
    public void resetExternalErrorView() {
        if (externalErrorView == null)
            return;
        externalErrorView = null;
    }

    /**
     * Add new checker to validator.
     */
    public void addConditionChecker(BaseValidatorConditionChecker checker) {
        checkers.add(checker);
    }

    /**
     * Remove checker from validator.
     */
    public void removeConditionChecker(BaseValidatorConditionChecker checker) {
        checkers.remove(checker);
    }

    /**
     * Remove all checkers from validator.
     */
    public void clearCheckers() {
        checkers.clear();
    }

    /**
     * Check all conditions.
     * @return null - if all conditions are satisfied, Error-text - if one of conditions fails (in that case other conditions won't be checked).
     */
    public String checkConditions() {
        for (Iterator<BaseValidatorConditionChecker> checkerIterator = checkers.iterator(); checkerIterator.hasNext();) {
            BaseValidatorConditionChecker checker = (BaseValidatorConditionChecker) checkerIterator.next();
            if (!checker.checkCondition(this.getTextToValidate())) {
                notifyCurrentCheckState(false);
                return checker.getErrorMessage();
            }
        }
        notifyCurrentCheckState(true);
        return null;
    }

    /**
     * Set changes listener.
     * Pass null to remove listener.
     */
    void setChangesListener(ValidationChangesListener listener) {
        changesListener = listener;
    }
    /** Notify listener about next check. Work only if listener is present. */
    private void notifyCurrentCheckState(boolean isValid) {
        if (changesListener != null)
            changesListener.onValidationStateUpdated(this, isValid);
    }
}