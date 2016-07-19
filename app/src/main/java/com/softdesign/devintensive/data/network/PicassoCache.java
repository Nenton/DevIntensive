package com.softdesign.devintensive.data.network;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class PicassoCache {
    private Context mContext;
    private Picasso mPicassoInstance;

    public PicassoCache(Context context) {
        this.mContext = context;
        OkHttp3Downloader okHttp3Downloader = new OkHttp3Downloader(context, Integer.MAX_VALUE);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(okHttp3Downloader);

        mPicassoInstance = builder.build();
        Picasso.setSingletonInstance(mPicassoInstance);
    }

    public Picasso getPicassoInstance() {
        if (mPicassoInstance == null) {
            new PicassoCache(mContext);
            return mPicassoInstance;
        }
        return mPicassoInstance;
    }
}

//
//{super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallbackItemTouchHelper);
//        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
//        layoutManager=new LinearLayoutManager(MainActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
// posts=returnListItems();
//        adapter=new RecyclerViewAdapter(MainActivity.this,posts);
//        recyclerView.setAdapter(adapter);
//        itemTouchHelper.attachToRecyclerView(recyclerView);
//        }
//private List<ItemObject>returnListItems()
//        {List<ItemObject>items=new ArrayList<ItemObject>();
//        items.add(new ItemObject("Blank Space","Taylor Swift","2016"));
//        items.add(new ItemObject("Uptown Funk","Mark Ronson","2016"));
//        items.add(new ItemObject("Can't Feel My Face","The Weeknd","2016"));
//        items.add(new ItemObject("Cheerleader","OMI","2016"));
//        items.add(new ItemObject("What Do You Mean?","Justin Bieber","2016"));
//        items.add(new ItemObject("Hello","Adele","2016"));
//        return items;
//        }
//        ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT)
//        {@Override
//public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,RecyclerView.ViewHolder target)
//        {final int fromPosition=viewHolder.getAdapterPosition();
//final int toPosition=target.getAdapterPosition();
//        ItemObject prev=posts.remove(fromPosition);
//        posts.add(toPosition>fromPosition?toPosition-1:toPosition,prev);
//        adapter.notifyItemMoved(fromPosition,toPosition);
//        adapter.notifyItemMoved(fromPosition,toPosition);
//        return true;}
//@Override
//public void onSwiped(RecyclerView.ViewHolder viewHolder,int direction)
//        {int position=viewHolder.getAdapterPosition();
//        posts.remove(position);
//        adapter.notifyDataSetChanged();
//        }};}