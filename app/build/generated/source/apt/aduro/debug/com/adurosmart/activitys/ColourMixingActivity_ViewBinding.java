// Generated code from Butter Knife. Do not modify!
package com.adurosmart.activitys;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.ColorPickerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ColourMixingActivity_ViewBinding implements Unbinder {
  private ColourMixingActivity target;

  private View view2131361957;

  private View view2131362034;

  private View view2131361942;

  private View view2131361839;

  private View view2131362195;

  private View view2131361943;

  private View view2131361842;

  @UiThread
  public ColourMixingActivity_ViewBinding(ColourMixingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ColourMixingActivity_ViewBinding(final ColourMixingActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.ibtn_left, "field 'imageButton' and method 'onClick'");
    target.imageButton = Utils.castView(view, R.id.ibtn_left, "field 'imageButton'", ImageButton.class);
    view2131361957 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.textView = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'textView'", TextView.class);
    target.seebar_temp = Utils.findRequiredViewAsType(source, R.id.color_seebar, "field 'seebar_temp'", SeekBar.class);
    target.color_view = Utils.findRequiredViewAsType(source, R.id.color_view, "field 'color_view'", ColorPickerView.class);
    view = Utils.findRequiredView(source, R.id.red, "field 'red_btn' and method 'onClick'");
    target.red_btn = Utils.castView(view, R.id.red, "field 'red_btn'", Button.class);
    view2131362034 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.green, "field 'green_btn' and method 'onClick'");
    target.green_btn = Utils.castView(view, R.id.green, "field 'green_btn'", Button.class);
    view2131361942 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.blue, "field 'blue_btn' and method 'onClick'");
    target.blue_btn = Utils.castView(view, R.id.blue, "field 'blue_btn'", Button.class);
    view2131361839 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.white, "field 'white_btn' and method 'onClick'");
    target.white_btn = Utils.castView(view, R.id.white, "field 'white_btn'", Button.class);
    view2131362195 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.grey, "field 'grey_btn' and method 'onClick'");
    target.grey_btn = Utils.castView(view, R.id.grey, "field 'grey_btn'", Button.class);
    view2131361943 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.brown, "field 'brown_btn' and method 'onClick'");
    target.brown_btn = Utils.castView(view, R.id.brown, "field 'brown_btn'", Button.class);
    view2131361842 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ColourMixingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageButton = null;
    target.textView = null;
    target.seebar_temp = null;
    target.color_view = null;
    target.red_btn = null;
    target.green_btn = null;
    target.blue_btn = null;
    target.white_btn = null;
    target.grey_btn = null;
    target.brown_btn = null;

    view2131361957.setOnClickListener(null);
    view2131361957 = null;
    view2131362034.setOnClickListener(null);
    view2131362034 = null;
    view2131361942.setOnClickListener(null);
    view2131361942 = null;
    view2131361839.setOnClickListener(null);
    view2131361839 = null;
    view2131362195.setOnClickListener(null);
    view2131362195 = null;
    view2131361943.setOnClickListener(null);
    view2131361943 = null;
    view2131361842.setOnClickListener(null);
    view2131361842 = null;
  }
}
