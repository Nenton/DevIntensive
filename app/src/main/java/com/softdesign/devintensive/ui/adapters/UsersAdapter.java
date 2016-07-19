package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.data.storage.models.User;
import com.softdesign.devintensive.ui.activities.UserListActivity;
import com.softdesign.devintensive.ui.views.AspectRatioImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private static final String TAG = "UsersAdapter";
    private Context mContext;
    private List<User> mUsers;
    private UserViewHolder.CustomClickListener mCustomClickListener;
    private Display display;

    public UsersAdapter(List<User> users, UserViewHolder.CustomClickListener customClickListener) {
        mUsers = users;
        this.mCustomClickListener = customClickListener;
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(convertView, mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.UserViewHolder holder, int position) {
        final User user = mUsers.get(position);

        final String userPhoto;
        if (user.getPhoto().isEmpty()) {
            userPhoto = "null";
            Log.e(TAG, "onBindViewHolder: user with name " + user.getFullName() + "has empty name");
        } else {
            userPhoto = user.getPhoto();
        }
        if (!user.getPhoto().isEmpty()) {
            DataManager.getInstanse().getPicasso()
                    .load(userPhoto)
                    .placeholder(holder.mUserCachePhoto)
                    .error(holder.mUserCachePhoto)
                    .resize(UserListActivity.getmWidthWindows(), (int) (UserListActivity.getmWidthWindows() / 1.73))
                    .centerCrop()
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.userPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, " load from cache");
                        }

                        @Override
                        public void onError() {
                            DataManager.getInstanse().getPicasso()
                                    .load(userPhoto)
                                    .placeholder(holder.mUserCachePhoto)
                                    .error(holder.mUserCachePhoto)
                                    .resize(UserListActivity.getmWidthWindows(), (int) (UserListActivity.getmWidthWindows() / 1.73))
                                    .centerCrop()
                                    .into(holder.userPhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.d(TAG, " load from cache");
                                        }
                                    });
                        }
                    });
        }
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodeLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProjects.setText(String.valueOf(user.getProjects()));
        if (user.getBio() == null || user.getBio().isEmpty()) {
            holder.mBio.setVisibility(View.GONE);
        } else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getBio());
        }
    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected AspectRatioImageView userPhoto;
        protected TextView mFullName, mRating, mCodeLines, mProjects, mBio;
        protected Button mShowMore;
        protected Drawable mUserCachePhoto;

        private CustomClickListener mClickListener;

        public UserViewHolder(View itemView, CustomClickListener customClickListener) {

            super(itemView);
            this.mClickListener = customClickListener;

            userPhoto = (AspectRatioImageView) itemView.findViewById(R.id.user_photo);
            mFullName = (TextView) itemView.findViewById(R.id.user_full_name_txt);
            mRating = (TextView) itemView.findViewById(R.id.user_rating);
            mCodeLines = (TextView) itemView.findViewById(R.id.user_code_lines);
            mProjects = (TextView) itemView.findViewById(R.id.user_projects);
            mBio = (TextView) itemView.findViewById(R.id.user_bio_txt);
            mShowMore = (Button) itemView.findViewById(R.id.more_info_btn);

            mUserCachePhoto = userPhoto.getContext().getResources().getDrawable(R.drawable.profile_photo);
            mShowMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }


}