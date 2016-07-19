package com.softdesign.devintensive.data.manager.validators;

import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.utils.ConstantManager;

/**
 * Validator for editText.
 * Could display error using standard {@link EditText#setError(CharSequence)}
 * when there is no external error view.
 */
public class EditTextValidator extends BaseValidator implements TextWatcher, OnFocusChangeListener {
    //Validated view.
    protected EditText viewToValidate = null;

    public int getInt() {
        return mInt;
    }

    private int mInt;

    /* (non-Javadoc)
     * @see com.ruswizards.validators.BaseValidator#setViewToValidate(android.view.View)
     */
    @Override
    public void setViewToValidate(View view, ValidationMode mode) {
        if (!(view instanceof EditText))
            throw new IllegalArgumentException(ConstantManager.ILLEGAL_ARGUMENT_EXCEPTION);

        //If we have previous view, remove listener
        if (viewToValidate != null) {
            viewToValidate.removeTextChangedListener(this);
            viewToValidate.setOnFocusChangeListener(null);
        }

        //Store new view
        viewToValidate = (EditText)view;

        switch (viewToValidate.getId()){
            case R.id.phone_text:
                mInt = R.string.mobile_phone;
                break;
            case R.id.mail_text:
                mInt = R.string.mail;
                break;
            case R.id.vk_text:
                mInt = R.string.vk;
                break;
//            case R.id.github_text:
//                mInt = R.string.git;
//                break;
        }

        switch (mode) {
            case Auto:
                //and set listener if necessary
                viewToValidate.addTextChangedListener(this);
                viewToValidate.setOnFocusChangeListener(this);
                break;
            case Manual:
                //Nothing to do for manual
                break;
        }
    }

    /* (non-Javadoc)
     * @see com.ruswizards.validators.BaseValidator#getTextToValidate()
     */
    @Override
    protected String getTextToValidate() {
        if (viewToValidate == null)
            return null;
        return viewToValidate.getText().toString();
    }

    /* (non-Javadoc)
     * @see com.ruswizards.validators.BaseValidator#displayError(java.lang.String)
     */

//    @Override
//    protected void displayError(String error, int idRString) {
//        if (externalErrorView != null)
//            super.displayError(error,mString);
//        else
//            viewToValidate.setError(error);
//    }

    /* (non-Javadoc)
     * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
     */
    @Override
    public void afterTextChanged(Editable s) {
        performCheck(mInt);
    }

    /* (non-Javadoc)
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /* (non-Javadoc)
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /* (non-Javadoc)
     * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View, boolean)
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //Check conditions on loosing focus.
        if (!hasFocus)
            performCheck(mInt);
    }
}