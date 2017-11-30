package com.zhz.pickers.util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Zack_zhuo on 2017/7/4.
 */

public class RecyclerViewHolder {

    private ViewHolder viewHolder;
    private final SparseArray<View> mViews;
    View convertView;

    public RecyclerViewHolder(Context context, int layout_id, ViewGroup parent){
        convertView = LayoutInflater.from(context).inflate(layout_id,parent,false);
        viewHolder = new ViewHolder(convertView);
        this.mViews = new SparseArray<View>();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public <T extends View> T getView(int vId){
            View view = mViews.get(vId);
            if(null == view){
                view = convertView.findViewById(vId);
                mViews.put(vId,view);
            }
            return (T) view;
        }

        public void setText(int vId,String str){
            TextView textView = viewHolder.getView(vId);
            if(textView != null)
            textView.setText(str);
        }

        public void setEditText(int vId, String str){
            EditText editText = viewHolder.getView(vId);
            if(editText != null){
                editText.setText(str);
            }
        }

        public void setTextColor(int vId,int color){
            TextView textView = viewHolder.getView(vId);
            if(textView != null)
                textView.setTextColor(color);
        }

        public void setImage(int vId,int rId){
            ImageView imageView = viewHolder.getView(vId);
            if(imageView != null){
                imageView.setImageResource(rId);
            }
        }

        public void setVisibility(int vId, int visibility){
            View view = viewHolder.getView(vId);
            if(view != null)
            view.setVisibility(visibility);
        }

        public void setBackground(int vId, int color){
            View view = viewHolder.getView(vId);
            if(view != null)
                view.setBackgroundColor(color);
        }

        public void setBackgroundRes(int vId, int rId){
            View view = viewHolder.getView(vId);
            if(view != null) {
                view.setBackgroundResource(rId);
            }
        }

        public void setHint(int vId,String str){
            EditText editText = viewHolder.getView(vId);
            if(editText != null){
                editText.setHint(str);
            }
        }

        public void setFocusEnable(int vId, boolean canFocus){
            EditText editText = viewHolder.getView(vId);
            if(editText != null){
                editText.setFocusable(canFocus);
            }
        }

    }

    public ViewHolder getHolder(){
        return viewHolder;
    }
}
