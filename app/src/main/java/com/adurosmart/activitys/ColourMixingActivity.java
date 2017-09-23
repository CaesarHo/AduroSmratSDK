package com.adurosmart.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adurosmart.sdk.R;
import com.adurosmart.widget.ColorPickerView;
import com.core.entity.AppDevice;
import com.core.gatewayinterface.SerialHandler;
import com.core.utils.TransformUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.adurosmart.global.Constants.hue;
import static com.adurosmart.global.Constants.level;
import static com.adurosmart.global.Constants.sat;
import static com.adurosmart.global.Constants.temp;

/**
 * Created by best on 2016/10/25.
 */
public class ColourMixingActivity extends BaseActivity implements View.OnClickListener {
    private String Tag = "ColourMixingActivity";
    @BindView(R.id.ibtn_left)
    ImageButton imageButton;
    @BindView(R.id.tv_title)
    TextView textView;
    @BindView(R.id.color_seebar)
    SeekBar seebar_temp;
    @BindView(R.id.color_view)
    ColorPickerView color_view;

    @BindView(R.id.red)
    Button red_btn;
    @BindView(R.id.green)
    Button green_btn;
    @BindView(R.id.blue)
    Button blue_btn;
    @BindView(R.id.white)
    Button white_btn;
    @BindView(R.id.grey)
    Button grey_btn;
    @BindView(R.id.brown)
    Button brown_btn;

    public Context context;
    private AppDevice appDevice;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_colour_mixing);
        ButterKnife.bind(this);
        context = ColourMixingActivity.this;
        appDevice = (AppDevice) getIntent().getSerializableExtra("AppDeviceInfo");
        initView();
    }

    @Override
    public void initView() {
        textView.setTextSize(20);
        textView.setText("调色,调色温");
        color_view.setOnColorChangedListenner(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                System.out.println("color = " + color);
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);

                float ft_hue = hsv[0];
                float ft_sat = hsv[1];
                float ft_value = hsv[2];
                float hue_ft = ft_hue / 360.0f;
                System.out.println("ft_hue = " + ft_hue);
                System.out.println("ft_sat = " + ft_sat);
                System.out.println("ft_value = " + ft_value);

                int int_sat = (int) (ft_sat * 255.0f);
                int int_hue = (int) (hue_ft * 255.0f);
                hue = int_hue;
                sat = int_sat;
                System.out.println("int_sat = " + int_sat);
                System.out.println("int_hue = " + int_hue);

                //HSV还原
                float[] call_ft = new float[3];
                call_ft[0] = int_hue / 255.0f * 360.0f;
                call_ft[1] = int_sat / 255.0f;
                call_ft[2] = 1.0f;

                int call_color = Color.HSVToColor(call_ft);
                textView.setTextColor(call_color);
                appDevice.setColorHue(int_hue);
                appDevice.setColorSat(int_sat);

                textView.setText("H = " + int_hue + "," + "S = " + int_sat);
                System.out.println("调色板 = " + "H = " + int_hue + "," + "S = " + int_sat);
                //调颜色
//                SerialHandler.getInstance().setDeviceHue(appDevice,int_hue);
                //调颜色饱和度
                SerialHandler.getInstance().setDeviceHueSat(appDevice, int_hue, int_sat);
            }
        });
        seebar_temp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int level_value = 255 * progress / 100;
                level = level_value;
                Intent intent = new Intent();
                intent.setAction("level_value_action");
                intent.putExtra("level_value", level_value);
                context.sendBroadcast(intent);

                System.out.println("Level = " + 500 * progress / 153 + 153);

                int startColorTemp = 500;
                int colorTempRange = startColorTemp - 153;
                int iiii = colorTempRange * progress / 100;
                int temper = 500 - iiii;
                temp = temper;
                System.out.println("temper = " + temper);
                appDevice.setTemp(temp);
                SerialHandler.getInstance().setColorTemperature(appDevice, temper);
            }
        });
    }

    int RGB(int color) {
        int r = 0xFF & color;
        int g = 0xFF00 & color;
        g >>= 8;
        int b = 0xFF0000 & color;
        b >>= 16;
        return Color.rgb(r, g, b);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(Tag, "onRestart called.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(Tag, "onResume called.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(Tag, "onPause called.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag, "onDestory called.");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @OnClick({R.id.ibtn_left, R.id.red, R.id.green, R.id.blue, R.id.white, R.id.grey, R.id.brown})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_left:
                this.finish();
                break;
            case R.id.red://标准红色色坐标为(0.67，0.33)
                float[] hsv_red = new float[3];
                Color.RGBToHSV(255, 0, 0, hsv_red);
//                int color_red = Color.RED;
//                Color.colorToHSV(color_red, hsv_red);

                float ft_hue_red = hsv_red[0];
                float ft_sat_red = hsv_red[1];
                float ft_value_red = hsv_red[2];
                float hue_ft_red = ft_hue_red / 360.0f;
                System.out.println("ft_hue_white" + ft_hue_red + ", " + ft_sat_red + " ," + ft_value_red);

                int int_sat_red = (int) (ft_sat_red * 255.0f);
                int int_hue_red = (int) (hue_ft_red * 255.0f);
                textView.setText("H = " + int_hue_red + "," + "S = " + int_sat_red);
                textView.setTextColor(TransformUtils.hsvToColor(int_hue_red, int_sat_red));
                SerialHandler.getInstance().setDeviceHue(appDevice, int_hue_red);
                break;
            case R.id.green://标准绿色色坐标为(0.21，0.71)
                float[] hsv_green = new float[3];
                Color.RGBToHSV(0, 255, 0, hsv_green);
//                int color_green = Color.GREEN;
//                Color.colorToHSV(color_green, hsv_green);

                float ft_hue_green = hsv_green[0];
                float ft_sat_green = hsv_green[1];
                float ft_value_green = hsv_green[2];
                float hue_ft_green = ft_hue_green / 360.0f;
                System.out.println("ft_hue_white" + ft_hue_green + ", " + ft_sat_green + " ," + ft_value_green);

                int int_sat_green = (int) (ft_sat_green * 255.0f);
                int int_hue_green = (int) (hue_ft_green * 255.0f);
                textView.setText("H = " + int_hue_green + "," + "S = " + int_sat_green);
                textView.setTextColor(TransformUtils.hsvToColor(int_hue_green, int_sat_green));
                SerialHandler.getInstance().setDeviceHue(appDevice, int_hue_green);
                break;
            case R.id.blue://标准蓝色色坐标为(0.14，0.08)
                float[] hsv_blue = new float[3];
                Color.RGBToHSV(0, 0, 255, hsv_blue);
//                int color_blue = Color.BLUE;
//                Color.colorToHSV(color_blue, hsv_blue);

                float ft_hue_blue = hsv_blue[0];
                float ft_sat_blue = hsv_blue[1];
                float ft_value_blue = hsv_blue[2];
                float hue_ft_blue = ft_hue_blue / 360.0f;
                System.out.println("ft_hue_white = " + ft_hue_blue + " , " + ft_sat_blue + " , " + ft_value_blue);

                int int_sat_blue = (int) (ft_sat_blue * 255.0f);
                int int_hue_blue = (int) (hue_ft_blue * 255.0f);
                textView.setText("H = " + int_hue_blue + "," + "S = " + int_sat_blue);
                textView.setTextColor(TransformUtils.hsvToColor(int_hue_blue, int_sat_blue));
                SerialHandler.getInstance().setDeviceHue(appDevice, int_hue_blue);
                break;
            case R.id.white://纯正的白光色坐标为(0.33，0.33)
                float[] hsv_white = new float[3];
                Color.RGBToHSV(255, 255, 255, hsv_white);
//                int color_white = Color.WHITE;
//                Color.colorToHSV(color_white, hsv_white);

                float ft_hue_white = hsv_white[0];
                float ft_sat_white = hsv_white[1];
                float ft_value_white = hsv_white[2];
                float hue_ft_white = ft_hue_white / 360.0f;
                System.out.println("ft_hue_white" + ft_hue_white + ", " + ft_sat_white + " ," + hue_ft_white);

                int int_hue_white = (int) (hue_ft_white * 255.0f);
                int int_sat_white = (int) (ft_sat_white * 255.0f);

                int hue_xy_w = (int) (0.33 * 255.0f);
                int sat_xy_w = (int) (0.33 * 255.0f);
                textView.setText("H = " + int_hue_white + "," + "S = " + int_sat_white);
                textView.setTextColor(TransformUtils.hsvToColor(int_hue_white, int_sat_white));
                SerialHandler.getInstance().setDeviceHueSat(appDevice, int_hue_white, int_sat_white);
                break;


            case R.id.grey:
//                targetImage.setOnTouchListener(new ImageView.OnTouchListener(){
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//                        int x=0;
//                        int y=0;
//                        textView.setText("Touch coordinates : " +String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
//                        ImageView imageView = ((ImageView)v);
//                        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//                        int pixel = bitmap.getPixel(x,y);
//                        int redValue = Color.red(pixel);
//                        int blueValue = Color.blue(pixel);
//                        int greenValue = Color.green(pixel);
//                        if(pixel == Color.RED){
//                            textViewCol.setText("It is RED");
//                        }
//
//        /*if(redValue == 255){
//            if(blueValue == 0)
//                if(greenValue==0)
//               textViewCol.setText("It is Red");
//            }*/
//                        return true;    }
//                });
//                ImageView imageView = ((ImageView)v);
//                Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//                int pixel = bitmap.getPixel(x,y);
//
//                int redValue = Color.red(pixel);
//                int blueValue = Color.blue(pixel);
//                int greenValue = Color.green(pixel);
//                SerialHandler.getInstance().setDeviceHueSat(appDevice, int_hue, int_sat);
                break;
            case R.id.brown:
//                SerialHandler.getInstance().setDeviceHueSat(appDevice, int_hue, int_sat);
                break;
        }
    }
}
