package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.redmadrobot.chronos.ChronosConnector;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.Repositories;
import com.softdesign.devintensive.data.storage.models.RepositoriesDao;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.data.storage.models.UserDao;
import com.softdesign.devintensive.ui.adapters.UsersAdapter;
import com.softdesign.devintensive.utils.AppConfig;
import com.softdesign.devintensive.utils.AvatarRounded;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.QueryInBdCustomUser;
import com.softdesign.devintensive.utils.UpdateUserInBd;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class CustomSortListActivity extends BaseActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = ConstantManager.TAG_PREFIX + "CustomSort Activity";

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
    private ChronosConnector mChronosConnector;
    private List<User> mUsers;
    private UsersAdapter usersAdapter;
    private RepositoriesDao mRepositoriesDao;
    private UserDao mUserDao;

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
        mChronosConnector.onCreate(this, savedInstanceState);
        mHandler = new Handler();

        mRecyclerView = (RecyclerView) findViewById(R.id.user_list_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        setupSwipe();

        if (sQueryString == null || sQueryString.isEmpty()) {
            mChronosConnector.runOperation(new QueryInBdCustomUser(), true);
        } else {
            showUserByQuery(sQueryString, 0);
        }
    }

    private void setupSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                final int fromPosition = viewHolder.getAdapterPosition();
                final int toPosition = target.getAdapterPosition();
                User prev = mUsers.remove(fromPosition);
                mUsers.add(toPosition, prev);
                if (toPosition > fromPosition) {
                    mChronosConnector.runOperation(new UpdateUserInBd(prev.getRemoteId(), true, toPosition), true);
//                    mDataManager.updateUser(prev.getRemoteId(), true, toPosition);
                    for (int i = fromPosition; i < toPosition; i++) {
                        mChronosConnector.runOperation(new UpdateUserInBd(mUsers.get(i).getRemoteId(), true, i), true);
//                        mDataManager.updateUser(mUsers.get(i).getRemoteId(), true, i);
                    }
                } else {
                    mChronosConnector.runOperation(new UpdateUserInBd(prev.getRemoteId(), true, toPosition), true);
//                    mDataManager.updateUser(prev.getRemoteId(), true, toPosition);
                    for (int i = toPosition + 1; i < fromPosition + 1; i++) {
                        mChronosConnector.runOperation(new UpdateUserInBd(mUsers.get(i).getRemoteId(), true, i), true);
//                        mDataManager.updateUser(mUsers.get(i).getRemoteId(), true, i);
                    }
                }
                usersAdapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                User user = mUsers.get(viewHolder.getAdapterPosition());
                mDataManager.updateUser(user.getRemoteId(), false, user.getSortPosition());
                int position = viewHolder.getAdapterPosition();
                mUsers.remove(position);
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    public void onOperationFinished(final QueryInBdCustomUser.Result users) {
        mUsers = users.getOutput();
        usersAdapter = new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
            @Override
            public void onUserItemClickListener(int position) {
                sPositionItemUser = position;
                UserDTO userProfile = new UserDTO(mUsers.get(position));
                Intent userIntent = new Intent(CustomSortListActivity.this, ProfileUserActivity.class);
                userIntent.putExtra(ConstantManager.PARCELABLER_KEY, userProfile);
                startActivity(userIntent);
            }
        });

        if (sQueryString == null || sQueryString.isEmpty()) {
            mRecyclerView.setAdapter(usersAdapter);
        } else {
            mRecyclerView.swapAdapter(usersAdapter, false);
        }
        if (sPositionItemUser != 0) {
            mRecyclerView.scrollToPosition(sPositionItemUser);
            sPositionItemUser = 0;
        }
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

    private void setupDrawer() {
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case ConstantManager.MENU_ITEM_MY_PROFILE:
                        Intent intent = new Intent(CustomSortListActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case ConstantManager.MENU_ITEM_TEAM:
                        break;
                    case ConstantManager.MENU_ITEM_RATING:
                        Intent intentRating = new Intent(CustomSortListActivity.this, UserListActivity.class);
                        startActivity(intentRating);
                        break;
                    case ConstantManager.MENU_ITEM_REFRESH:
                        refreshBd();
                        break;
                    case ConstantManager.MENU_ITEM_EXIT:
                        Intent startActivity = new Intent(CustomSortListActivity.this, AuthActivity.class);
                        mDataManager.getPreferencesManager().saveAuthToken(null);
                        mDataManager.getPreferencesManager().saveUserId(null);
                        startActivity(startActivity);
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
        mDataManager.getPicasso()
                .load(mDataManager.getPreferencesManager().loadAvatarImage())
                .placeholder(R.drawable.avatar)
                .transform(new AvatarRounded())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mAvatarImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, ConstantManager.LOAD_FROM_CACHE);
                    }

                    @Override
                    public void onError() {
                        mDataManager.getPicasso()
                                .load(mDataManager.getPreferencesManager().loadAvatarImage())
                                .placeholder(R.drawable.avatar)
                                .transform(new AvatarRounded())
                                .into(mAvatarImage, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.d(TAG, ConstantManager.ERROR_LOAD_FROM_CACHE);
                                    }
                                });
                    }
                });
    }

    private void refreshBd() {
        Call<UserListRes> call = mDataManager.getListUserFromNetwork();
        call.enqueue(new retrofit2.Callback<UserListRes>() {
            @Override
            public void onResponse(Call<UserListRes> call, Response<UserListRes> response) {
                try {
                    if (response.code() == ConstantManager.RESPONSE_CODE_ACCESS) {
                        List<Repositories> allRepositories = new ArrayList<Repositories>();
                        List<User> allUsers = new ArrayList<User>();
                        mUserDao = mDataManager.getDaoSession().getUserDao();
                        mRepositoriesDao = mDataManager.getDaoSession().getRepositoriesDao();
                        for (UserListRes.Datum userRes : response.body().getData()) {
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }
                        mRepositoriesDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);
                    } else {
                        Snackbar.make(mDrawerLayout, ConstantManager.LIST_USER_NOT_CAN_GET, Snackbar.LENGTH_LONG).show();
                    }

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Snackbar.make(mDrawerLayout, ConstantManager.STANDART_ERROR_MESSAGE, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserListRes> call, Throwable t) {
                Snackbar.make(mDrawerLayout, ConstantManager.STANDART_ERROR_MESSAGE, Snackbar.LENGTH_LONG).show();
            }

        });
    }

    private List<Repositories> getRepoListFromUserRes(UserListRes.Datum userData) {
        final String userId = userData.getId();
        List<Repositories> repositories = new ArrayList<>();
        for (UserListRes.Repo repositoryRes : userData.getRepositories().getRepo()) {
            repositories.add(new Repositories(repositoryRes, userId));
        }
        return repositories;
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_list_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Exit from navbar and from app
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(ConstantManager.SEARCH_ON_FULL_NAME);
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
                mChronosConnector.runOperation(new QueryInBdCustomUser(), true);
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
                mChronosConnector.runOperation(new QueryInBdCustomUser(query), true);
            }
        };
        mHandler.removeCallbacks(runnable);
        mHandler.postDelayed(runnable, delay);

    }

}

