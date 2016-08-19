package com.jpardogo.android.listbuddies.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jpardogo.android.listbuddies.R;
import com.jpardogo.android.listbuddies.Utils.ScaleToFitWidhtHeigthTransform;
import com.jpardogo.android.listbuddies.models.ImageBean;
import com.jpardogo.listbuddies.lib.adapters.CircularLoopAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CircularAdapter extends CircularLoopAdapter {
    private static final String TAG = CircularAdapter.class.getSimpleName();

    //private List<String> mItems = new ArrayList<String>();
    private List<ImageBean> mItems = new ArrayList<ImageBean>();
    private Context mContext;
    private int mRowHeight;

//    public CircularAdapter(Context context, int rowHeight, List<String> imagesUrl) {
//        mContext = context;
//        mRowHeight = rowHeight;
//        mItems = imagesUrl;
//    }

    public CircularAdapter(Context context, int rowHeight, List<ImageBean> imagesUrl) {
        mContext = context;
        mRowHeight = rowHeight;
        mItems = imagesUrl;
    }

    @Override
    public ImageBean getItem(int position) {
        return mItems.get(getCircularPosition(position));
    }

    @Override
    protected int getCircularCount() {
        return mItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setMinimumHeight(mRowHeight);

        Picasso.with(mContext)
                .load(getItem(position).getUrl())
                .transform(new ScaleToFitWidhtHeigthTransform(mRowHeight, true))
                .config(Bitmap.Config.RGB_565)
                .into(holder.image);
        Log.e("adapter","url:"+getItem(position));
        holder.tvComment.setText(getItem(position).getComment());
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView tvComment;

        public ViewHolder(View convertView) {
            image = (ImageView) convertView.findViewById(R.id.image);
            tvComment = (TextView)convertView.findViewById(R.id.tv_comment);
        }
    }
}
