package com.zhz.pickers.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;


/**
 * Created by Zack_zhuo on 2017/7/4.
 */

public abstract class CommonRecylerAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder.ViewHolder>{

    private Context mContext;
    private int layout_id;
    private int[] layout_ids;
    private List<Map<String,T>> mDates;
    private ItemTouchListener mItemTouchListener;

    private boolean isDraging = false;


    public CommonRecylerAdapter(Context context, int layout_id, List<Map<String,T>> mDatas, ItemTouchListener mItemTouchListener){
        this.mContext = context;
        this.layout_id = layout_id;
        this.mDates = mDatas;
        this.mItemTouchListener = mItemTouchListener;
    }

    public CommonRecylerAdapter(Context context, int[] layout_ids, List<Map<String,T>> mDates, ItemTouchListener mItemTouchListener){
        this.mContext = context;
        this.layout_ids = layout_ids;
        this.mDates = mDates;
        this.mItemTouchListener = mItemTouchListener;
    }

    @Override
    public RecyclerViewHolder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int l_id;
        switch (viewType){
            case -1:
                l_id = layout_id;
                break;
            default:
                l_id = layout_ids[viewType];
                break;
        }
        RecyclerViewHolder.ViewHolder holder = new RecyclerViewHolder(mContext,l_id,parent).getHolder();
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if(layout_ids != null && mDates != null && mDates.size()>0){
            return  Integer.valueOf(mDates.get(position).get("type").toString());
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder.ViewHolder holder, final int position) {
        setDate(holder,mDates.get(position),position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemTouchListener.onItemClick(mDates.get(position),position);
            }
        });


    }

//    public abstract void setDate(RecyclerViewHolder.ViewHolder holder, Map<String,T> item,ItemTouchListener mItemTouchListener);
    public abstract void setDate(RecyclerViewHolder.ViewHolder holder, Map<String,T> item,int position);

    @Override
    public int getItemCount() {
        return null == mDates? 0:mDates.size();
    }




    public boolean isDraging(){
        return isDraging;
    }

    public void stopDraging(){
        isDraging = false;
    }
}
