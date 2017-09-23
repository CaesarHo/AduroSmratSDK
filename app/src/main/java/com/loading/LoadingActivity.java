package com.loading;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.adurosmart.sdk.R;
import com.loading.widget.book.BookLoading;
import com.loading.widget.newton.NewtonCradleLoading;
import com.loading.widget.rotate.RotateLoading;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by best on 2016/12/16.
 */

public class LoadingActivity extends AppCompatActivity {
    @BindView(R.id.bookloading)
    BookLoading bookloading;
    @BindView(R.id.btn_bookloading)
    Button btnBookloading;
    @BindView(R.id.rotateloading)
    RotateLoading rotateloading;
    @BindView(R.id.btn_rotateloading)
    Button btnRotateloading;
    @BindView(R.id.newton_cradle_loading)
    NewtonCradleLoading newtonCradleLoading;
    @BindView(R.id.btn_newton)
    Button btnNewton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_activity);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_bookloading, R.id.btn_rotateloading, R.id.btn_newton})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bookloading:
                if (bookloading.isStart()) {
                    btnBookloading.setText("start");
                    bookloading.stop();
                } else {
                    btnBookloading.setText("stop");
                    bookloading.start();
                }
                break;
            case R.id.btn_rotateloading:
                if (rotateloading.isStart()) {
                    rotateloading.stop();
                    btnRotateloading.setText("start");
                } else {
                    rotateloading.start();
                    btnRotateloading.setText("stop");
                }
                break;
            case R.id.btn_newton:
                if (newtonCradleLoading.isStart()) {
                    btnNewton.setText("start");
                    newtonCradleLoading.stop();
                } else {
                    btnNewton.setText("stop");
                    newtonCradleLoading.start();
                }
                break;
        }
    }
}
