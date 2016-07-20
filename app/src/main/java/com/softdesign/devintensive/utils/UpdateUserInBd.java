package com.softdesign.devintensive.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

public class UpdateUserInBd extends ChronosOperation<Boolean>{
    String remoteId;
    Boolean teamIn;
    int position;

    public UpdateUserInBd(String remoteId, Boolean teamIn, int position){
        this.remoteId = remoteId;
        this.teamIn = teamIn;
        this.position = position;
    }

    @Nullable
    @Override
    public Boolean run() {
        DataManager.getInstanse().updateUser(remoteId,teamIn,position);
        return true;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<Boolean>> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<Boolean> {
    }
}
