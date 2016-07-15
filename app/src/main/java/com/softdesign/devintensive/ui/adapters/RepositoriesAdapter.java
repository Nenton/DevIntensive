package com.softdesign.devintensive.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import java.util.List;

public class RepositoriesAdapter extends BaseAdapter {
    private List<String> mRepositories;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public RepositoriesAdapter(Context context, List<String> repositories) {
        mRepositories = repositories;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return mRepositories.size();
    }

    @Override
    public Object getItem(int position) {
        return mRepositories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = mLayoutInflater.inflate(R.layout.item_repositories_list,parent,false);
        }

        TextView textView = (TextView)view.findViewById(R.id.github);
        textView.setText(mRepositories.get(position));
        return view;
    }
}
