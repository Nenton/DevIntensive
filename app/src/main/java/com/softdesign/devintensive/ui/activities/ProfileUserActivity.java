package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.QueryInBdCustomUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends AppCompatActivity {
    private static final String TAG = ConstantManager.TAG_PREFIX + "Profile Activity";
    @BindView(R.id.toolbar_profile_user)
    Toolbar mToolbar;
    @BindView(R.id.profile_userPhoto)
    ImageView mUserProfileImage;
    @BindView(R.id.about_profile_text)
    EditText mUserBio;
    @BindView(R.id.rating_user)
    TextView mUserRating;
    @BindView(R.id.code_value_user)
    TextView mUserCodeLines;
    @BindView(R.id.project_value_user)
    TextView mUserProjects;
    @BindView(R.id.repositories_list)
    ListView mRepositoriesView;

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @BindView(R.id.collapsing_toolbar_user_profile)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);
        setupToolbar();
        initProfileData();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileData() {
        final UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLER_KEY);
        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);

        mRepositoriesView.setAdapter(repositoriesAdapter);
        RepositoriesAdapter.setListViewHeightBasedOnChildren(mRepositoriesView);
        mUserBio.setText(userDTO.getBio());
        mUserRating.setText(userDTO.getRating());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserProjects.setText(userDTO.getProject());
        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());

        Picasso.with(this)
                .load(userDTO.getPhoto())
                .error(R.drawable.profile_photo)
                .placeholder(R.drawable.profile_photo)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mUserProfileImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, ConstantManager.LOAD_FROM_CACHE);
                    }

                    @Override
                    public void onError() {
                        Picasso.with(ProfileUserActivity.this)
                                .load(userDTO.getPhoto())
                                .error(R.drawable.profile_photo)
                                .placeholder(R.drawable.profile_photo)
                                .into(mUserProfileImage, new Callback() {
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

        mRepositoriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(ConstantManager.SCHEME_HTTPS + repositories.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

}
