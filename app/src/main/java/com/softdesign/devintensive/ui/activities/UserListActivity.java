package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosConnector;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.AvatarRounded;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevIntensiveApplication;
import com.softdesign.devintensive.utils.QueryInBd;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends BaseActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = ConstantManager.TAG_PREFIX + "User List Activity";
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
    private static String sQueryString;
    private static int sPositionItemUser = 0;
    private Handler mHandler;
    private static int mWidthWindows;
    private ChronosConnector mChronosConnector;

    public static int getmWidthWindows() {
        return mWidthWindows;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChronosConnector.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mChronosConnector.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        mChronosConnector.onPause();
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mDataManager = DataManager.getInstanse();
        ButterKnife.bind(this);
        mChronosConnector = new ChronosConnector();
        mChronosConnector.onCreate(this,savedInstanceState);
        mHandler = new Handler();

        mRecyclerView = (RecyclerView) findViewById(R.id.user_list_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        setupSwipe();

        if (sQueryString == null || sQueryString.isEmpty()) {
            mChronosConnector.runOperation(new QueryInBd(), true);
        } else {
            showUserByQuery(sQueryString, 0);
        }
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);
        mWidthWindows = metricsB.widthPixels;
    }

    private void setupSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                showSnackbar("3");
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                showSnackbar("2");
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                showSnackbar("1");
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }


    public void onOperationFinished(final QueryInBd.Result users) {
        UsersAdapter usersAdapter = new UsersAdapter(users.getOutput(), new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                sPositionItemUser = position;
                UserDTO userProfile = new UserDTO(users.getOutput().get(position));
                Intent userIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                userIntent.putExtra(ConstantManager.PARCELABLER_KEY, userProfile);
                startActivity(userIntent);
            }
        });

        if (sQueryString == null || sQueryString.isEmpty()){
            mRecyclerView.setAdapter(usersAdapter);
        } else {
            mRecyclerView.swapAdapter(usersAdapter, false);
        }
        if (sPositionItemUser != 0) {
            mRecyclerView.scrollToPosition(sPositionItemUser);
            sPositionItemUser = 0;
        }
    }

//    private void loadUsersInfoFromDb() {
//        if (mDataManager.getUserListFromDb().size() == 0) {
//            showSnackbar("Не удалось получить ");
//        } else {
//            mChronosConnector.runOperation(new QueryInBd(sQueryString),true);
//
//        }
//    }

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
        mUserEmail.setText(mDataManager.getPreferencesManager().getEmail());
        mUserName.setText(String.format("%s", mDataManager.getPreferencesManager().getFullName()));
        Picasso.with(this)
                .load(mDataManager.getPreferencesManager().loadAvatarImage())
                .placeholder(R.drawable.avatar)
                .transform(new AvatarRounded())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mAvatarImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, " load avatar from cache");
                    }

                    @Override
                    public void onError() {
                        Picasso.with(UserListActivity.this)
                                .load(mDataManager.getPreferencesManager().loadAvatarImage())
                                .placeholder(R.drawable.avatar)
                                .transform(new AvatarRounded())
                                .into(mAvatarImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, " error load avatar from cache");
                                    }
                                });
                    }
                });
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
        if (sQueryString != null && !sQueryString.isEmpty()) {
            searchItem.expandActionView();
            searchView.setQuery(sQueryString, false);
        }

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mChronosConnector.runOperation(new QueryInBd(),true);
                sQueryString = null;
                return true;
            }
        });
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        showUserByQuery(newText, AppConfig.POST_DELAY_SEARCH_QUERY);
        if (newText.isEmpty()) {
            showUserByQuery(newText, 0);
        } else {
            showUserByQuery(newText, AppConfig.POST_DELAY_SEARCH_QUERY);
        }
        return true;
    }

    private void showUserByQuery(final String query, int delay) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sQueryString = query;
                mChronosConnector.runOperation(new QueryInBd(query),true);
            }
        };
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, delay);

    }

}

