package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends AppCompatActivity {
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
        setListViewHeightBasedOnChildren(mRepositoriesView);
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
                Uri uri = Uri.parse(ConstantManager.SCHEME_HTTPS + repositories.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) { view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, android.app.ActionBar.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED); totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)); listView.setLayoutParams(params);
    }
}
