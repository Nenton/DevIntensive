package com.softdesign.devintensive.ui.activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends BaseActivity {
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
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLER_KEY);

        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);

        mRepositoriesView.setAdapter(repositoriesAdapter);
        mUserBio.setText(userDTO.getBio());
        mUserRating.setText(userDTO.getRating());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserProjects.setText(userDTO.getProject());

        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());
        Picasso.with(this)
                .load(userDTO.getPhoto())
                .error(R.drawable.profile_photo)
                .placeholder(R.drawable.profile_photo)
                .into(mUserProfileImage);

        mRepositoriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 14.07.2016 сделать реализацию интент
            }
        });
    }
}
