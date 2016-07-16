package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.AvatarRounded;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "User List Activity";
    static private Response<UserListRes> mUserListResCall;
    @BindView(R.id.users_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.users_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.users_drawer_layout)
    DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private ImageView mAvatarImage;
    SearchView searchView;
    private DataManager mDataManager;
    private static List<UserListRes.Datum> mUsers;
    private static List<UserListRes.Datum> listSearchUsers;
    private static String sQueryString;
    private static int sPositionItemUser = 0;

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
                sPositionItemUser = position;
                UserDTO userProfile = new UserDTO(users.get(position));
                Intent userIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                userIntent.putExtra(ConstantManager.PARCELABLER_KEY, userProfile);
                startActivity(userIntent);
            }
        });
        mRecyclerView.setAdapter(usersAdapter);
        if (sPositionItemUser != 0){
            mRecyclerView.scrollToPosition(sPositionItemUser);
            sPositionItemUser = 0;
        }
    }

    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case "Мой профиль":
                        Intent intent = new Intent(UserListActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case "Команда":
                        break;
                    case "Рейтинг":
                        break;
                }

                item.setCheckable(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        mAvatarImage = (ImageView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.avatar_image);
        TextView mUserName = (TextView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.user_name_nav);
        TextView mUserEmail = (TextView) navigationView.getHeaderView(ConstantManager.NULL).findViewById(R.id.user_mail_nav);
        mUserEmail.setText(mDataManager.getPreferencesManager().loadUserProfileData().get(ConstantManager.NUMBER_VIEW_IN_ARRAY_EMAIL));
        mUserName.setText(String.format("%s %s", mDataManager.getPreferencesManager().loadFirstSecondNameUser().get(0), mDataManager.getPreferencesManager().loadFirstSecondNameUser().get(1)));
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadAvatarImage())
                .placeholder(R.drawable.avatar)
                .transform(new AvatarRounded())
                .into(mAvatarImage);
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

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Поиск по имени-фамилии");
        if (sQueryString != null && !sQueryString.isEmpty()){
            searchItem.expandActionView();
            searchView.setQuery(sQueryString,false);
        }

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
                    sQueryString = null;
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
            if (user.getFullName().toLowerCase().contains(query.toLowerCase())) {
                listSearchUsers.add(user);
            }
        }
        loadUsers(listSearchUsers);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        sQueryString = newText;
        return true;
    }
}
