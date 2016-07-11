package com.softdesign.devintensive.data.manager.validators;

public interface ValidationChangesListener {
    /**
     * Fired when validator updates state.
     * @param sender validator that change state.
     * @param isValid is current state valid.
     */
    public void onValidationStateUpdated(BaseValidator sender, boolean isValid);
}