package com.jpardogo.android.listbuddies.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.jpardogo.android.listbuddies.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DetailActivity extends BaseActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_COMMENT = "comment";
    @InjectView(R.id.image)
    ImageView mImageView;

    @InjectView(R.id.tv_comment)
    TextView mTvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        ButterKnife.inject(this);
        mBackground = mImageView;
        final String imageUrl = getIntent().getExtras().getString(EXTRA_URL);
        mTvComment.setText(getIntent().getExtras().getString(EXTRA_COMMENT));
        Picasso.with(this)
                .load(imageUrl)
                .config(Bitmap.Config.RGB_565)
                .into((ImageView) findViewById(R.id.image), new Callback() {
            @Override
            public void onSuccess() {
                moveBackground();
            }

            @Override
            public void onError() {
                Log.e("DetailActivity","loadError:"+imageUrl);
            }
        });

//        Glide.with(this)         //上下文
//                .load(imageUrl)          //图片的url
//                .placeholder(R.drawable.ic_launcher) //占位符,加载过程中显示图片
//                .error(R.drawable.empty_photo)       //网络错误时显示图片
//                .into(mImageView); //图片显示的view
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}


