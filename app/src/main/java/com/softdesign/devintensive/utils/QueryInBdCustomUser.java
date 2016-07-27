package com.softdesign.devintensive.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.storage.models.User;

import java.util.List;

public class QueryInBdCustomUser extends ChronosOperation<List<User>> {

    String query = null;

    public QueryInBdCustomUser(String query) {
        this.query = query;
    }


    public QueryInBdCustomUser() {
    }

    @Nullable
    @Override
    public List<User> run() {
        if (query == null || query.isEmpty()){
            return DataManager.getInstanse().getUserCustom();
        } else {
            return DataManager.getInstanse().getUserCustomByName(query);
        }

    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<User>>> getResultClass() {
        return Result.class;
    }
    public static final class Result extends ChronosOperationResult<List<User>> {
    }
}
