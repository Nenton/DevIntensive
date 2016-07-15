package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.database.MatrixCursor;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.network.res.UserModelRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "User List Activity";
    static private Response<UserListRes> mUserListResCall;
    @BindView(R.id.users_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.users_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.users_drawer_layout)
    DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private static List<UserListRes.Datum> mUsers;
    private static List<UserListRes.Datum> listSearchUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mDataManager = DataManager.getInstanse();
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.user_list_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if (mUserListResCall == null && mUsers == null) {
            loadUsersInfo();
        } else if (listSearchUsers == null) {
            loadUsers(mUsers);
        } else {
            loadUsers(listSearchUsers);
        }
        setupToolbar();
        setupDrawer();

    }

    private void loadUsersInfo() {

        Call<UserListRes> call = mDataManager.getListUser();

        call.enqueue(new Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    mUserListResCall = response;
                    mUsers = response.body().getData();
                    loadUsers(mUsers);
                } catch (NullPointerException e) {
                    Log.e(TAG, e.toString());
                    showSnackbar("Чтото пошло не так");
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                // TODO: 14.07.2016 Обработка ошибок
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG);

    }

    private void loadUsers(final List<UserListRes.Datum> users) {
        UsersAdapter usersAdapter = new UsersAdapter(users, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                UserDTO userProfile = new UserDTO(users.get(position));
                Intent userIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                userIntent.putExtra(ConstantManager.PARCELABLER_KEY, userProfile);
                startActivity(userIntent);
            }
        });
        mRecyclerView.setAdapter(usersAdapter);
    }

    private void setupDrawer() {
        // TODO: 13.07.2016 реализовать переход в другую активити при клике по элементу меню в NavigationDrawer
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_list_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                loadUsers(mUsers);
                if (listSearchUsers != null && !listSearchUsers.isEmpty()) {
                    listSearchUsers.clear();
                }
                return true;
            }
        });
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        List<UserListRes.Datum> mSearchUsers = mUserListResCall.body().getData();
        listSearchUsers = new ArrayList<>();
        for (UserListRes.Datum user : mSearchUsers) {
            if (user.getFullName().contains(query)) {
                listSearchUsers.add(user);
            }
        }
        loadUsers(listSearchUsers);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
