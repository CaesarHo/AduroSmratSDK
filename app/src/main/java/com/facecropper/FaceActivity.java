package com.facecropper;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.adurosmart.sdk.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import butterknife.ButterKnife;

public class FaceActivity extends AppCompatActivity {

    private Picasso mPicasso;
    private FaceCropper mFaceCropper;
    private ViewPager mViewPager;

    private Transformation mCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            return mFaceCropper.getCroppedImage(source);
        }

        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();

            builder.append("faceCrop(");
            builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
            builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

            FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
            if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
            } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
            }

            return builder.append(")").toString();
        }
    };

    private Transformation mDebugCropTransformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            return mFaceCropper.getFullDebugImage(source);
        }

        @Override
        public String key() {
            StringBuilder builder = new StringBuilder();

            builder.append("faceDebugCrop(");
            builder.append("minSize=").append(mFaceCropper.getFaceMinSize());
            builder.append(",maxFaces=").append(mFaceCropper.getMaxFaces());

            FaceCropper.SizeMode mode = mFaceCropper.getSizeMode();
            if (FaceCropper.SizeMode.EyeDistanceFactorMargin.equals(mode)) {
                builder.append(",distFactor=").append(mFaceCropper.getEyeDistanceFactorMargin());
            } else if (FaceCropper.SizeMode.FaceMarginPx.equals(mode)) {
                builder.append(",margin=").append(mFaceCropper.getFaceMarginPx());
            }

            return builder.append(")").toString();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        ButterKnife.bind(this);
        mFaceCropper = new FaceCropper(1f);
        mFaceCropper.setFaceMinSize(0);
        mFaceCropper.setDebug(true);
        mPicasso = Picasso.with(this);

        final ImageAdapter adapter = new ImageAdapter();
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                adapter.updateView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mFaceCropper.setEyeDistanceFactorMargin((float) i / 10);
                adapter.updateView(mViewPager.getCurrentItem());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setProgress(10);
    }

    class ImageAdapter extends PagerAdapter {

        private int[] urls = new int[]{
                R.mipmap.lluis1,
                R.mipmap.vueling,
                R.mipmap.arol1,
        };

        @Override
        public int getCount() {
            return (urls == null) ? 0 : urls.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = getLayoutInflater().inflate(R.layout.pager_item, null, false);

            setupView(v, position);

            v.setTag(position);
            container.addView(v);
            return v;
        }

        public void setupView(View v, int position) {
            if (v == null) return;
            ImageView image = (ImageView) v.findViewById(R.id.imageView);
            ImageView imageCropped = (ImageView) v.findViewById(R.id.imageViewCropped);

            mPicasso.load(urls[position]).transform(mDebugCropTransformation).into(image);

            mPicasso.load(urls[position])
                    .config(Bitmap.Config.RGB_565)
                    .transform(mCropTransformation)
                    .into(imageCropped);
        }

        public void updateView(int position) {
            setupView(mViewPager.findViewWithTag(position), position);
        }
    }
}
