// Generated code from Butter Knife. Do not modify!
package com.adurosmart.activitys;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.WheelView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddTaskActivity_ViewBinding implements Unbinder {
  private AddTaskActivity target;

  private View view2131361843;

  private View view2131362136;

  private View view2131361875;

  private View view2131361876;

  private View view2131362115;

  private View view2131362149;

  private View view2131362150;

  private View view2131361856;

  private View view2131361864;

  private View view2131361863;

  private View view2131361852;

  private View view2131361851;

  private View view2131361861;

  private View view2131361860;

  private View view2131361845;

  private View view2131362090;

  private View view2131361958;

  private View view2131361957;

  @UiThread
  public AddTaskActivity_ViewBinding(AddTaskActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddTaskActivity_ViewBinding(final AddTaskActivity target, View source) {
    this.target = target;

    View view;
    target.date_year = Utils.findRequiredViewAsType(source, R.id.date_year, "field 'date_year'", WheelView.class);
    target.date_month = Utils.findRequiredViewAsType(source, R.id.date_month, "field 'date_month'", WheelView.class);
    target.date_day = Utils.findRequiredViewAsType(source, R.id.date_day, "field 'date_day'", WheelView.class);
    target.date_hour = Utils.findRequiredViewAsType(source, R.id.date_hour, "field 'date_hour'", WheelView.class);
    target.date_minute = Utils.findRequiredViewAsType(source, R.id.date_minute, "field 'date_minute'", WheelView.class);
    target.date_second = Utils.findRequiredViewAsType(source, R.id.date_second, "field 'date_second'", WheelView.class);
    target.tvSettingUrban = Utils.findRequiredViewAsType(source, R.id.tv_setting_urban, "field 'tvSettingUrban'", TextView.class);
    target.tvUtc = Utils.findRequiredViewAsType(source, R.id.tv_utc, "field 'tvUtc'", TextView.class);
    target.w_urban = Utils.findRequiredViewAsType(source, R.id.w_urban, "field 'w_urban'", WheelView.class);
    view = Utils.findRequiredView(source, R.id.bt_set_timezone, "field 'bt_set_timezone' and method 'onClick'");
    target.bt_set_timezone = Utils.castView(view, R.id.bt_set_timezone, "field 'bt_set_timezone'", Button.class);
    view2131361843 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.time_text, "field 'time_text' and method 'onClick'");
    target.time_text = Utils.castView(view, R.id.time_text, "field 'time_text'", TextView.class);
    view2131362136 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.choose_device, "field 'choose_device' and method 'onClick'");
    target.choose_device = Utils.castView(view, R.id.choose_device, "field 'choose_device'", Button.class);
    view2131361875 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.choose_scene, "field 'choose_scene' and method 'onClick'");
    target.choose_scene = Utils.castView(view, R.id.choose_scene, "field 'choose_scene'", Button.class);
    view2131361876 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.swc, "field 'switch_btn' and method 'onClick'");
    target.switch_btn = Utils.castView(view, R.id.swc, "field 'switch_btn'", Switch.class);
    view2131362115 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.trigger_device, "field 'trigger_device' and method 'onClick'");
    target.trigger_device = Utils.castView(view, R.id.trigger_device, "field 'trigger_device'", Button.class);
    view2131362149 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.trigger_scene, "field 'trigger_scene' and method 'onClick'");
    target.trigger_scene = Utils.castView(view, R.id.trigger_scene, "field 'trigger_scene'", Button.class);
    view2131362150 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_one, "field 'btn_one' and method 'onClick'");
    target.btn_one = Utils.castView(view, R.id.btn_one, "field 'btn_one'", Button.class);
    view2131361856 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_two, "field 'btn_two' and method 'onClick'");
    target.btn_two = Utils.castView(view, R.id.btn_two, "field 'btn_two'", Button.class);
    view2131361864 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_three, "field 'btn_three' and method 'onClick'");
    target.btn_three = Utils.castView(view, R.id.btn_three, "field 'btn_three'", Button.class);
    view2131361863 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_four, "field 'btn_four' and method 'onClick'");
    target.btn_four = Utils.castView(view, R.id.btn_four, "field 'btn_four'", Button.class);
    view2131361852 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_five, "field 'btn_five' and method 'onClick'");
    target.btn_five = Utils.castView(view, R.id.btn_five, "field 'btn_five'", Button.class);
    view2131361851 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_six, "field 'btn_six' and method 'onClick'");
    target.btn_six = Utils.castView(view, R.id.btn_six, "field 'btn_six'", Button.class);
    view2131361861 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_seven, "field 'btn_seven' and method 'onClick'");
    target.btn_seven = Utils.castView(view, R.id.btn_seven, "field 'btn_seven'", Button.class);
    view2131361860 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvTask = Utils.findRequiredViewAsType(source, R.id.tv_task, "field 'tvTask'", TextView.class);
    target.edit_task = Utils.findRequiredViewAsType(source, R.id.edit_task, "field 'edit_task'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btn_add_task, "field 'btn_add_task' and method 'onClick'");
    target.btn_add_task = Utils.castView(view, R.id.btn_add_task, "field 'btn_add_task'", Button.class);
    view2131361845 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.setting_time, "field 'setting_time' and method 'onClick'");
    target.setting_time = Utils.castView(view, R.id.setting_time, "field 'setting_time'", RelativeLayout.class);
    view2131362090 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ibtn_right, "field 'ibtn_right' and method 'onClick'");
    target.ibtn_right = Utils.castView(view, R.id.ibtn_right, "field 'ibtn_right'", ImageButton.class);
    view2131361958 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ibtn_left, "method 'onClick'");
    view2131361957 = view;
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
    AddTaskActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.date_year = null;
    target.date_month = null;
    target.date_day = null;
    target.date_hour = null;
    target.date_minute = null;
    target.date_second = null;
    target.tvSettingUrban = null;
    target.tvUtc = null;
    target.w_urban = null;
    target.bt_set_timezone = null;
    target.time_text = null;
    target.choose_device = null;
    target.choose_scene = null;
    target.switch_btn = null;
    target.trigger_device = null;
    target.trigger_scene = null;
    target.btn_one = null;
    target.btn_two = null;
    target.btn_three = null;
    target.btn_four = null;
    target.btn_five = null;
    target.btn_six = null;
    target.btn_seven = null;
    target.tvTask = null;
    target.edit_task = null;
    target.btn_add_task = null;
    target.setting_time = null;
    target.ibtn_right = null;

    view2131361843.setOnClickListener(null);
    view2131361843 = null;
    view2131362136.setOnClickListener(null);
    view2131362136 = null;
    view2131361875.setOnClickListener(null);
    view2131361875 = null;
    view2131361876.setOnClickListener(null);
    view2131361876 = null;
    view2131362115.setOnClickListener(null);
    view2131362115 = null;
    view2131362149.setOnClickListener(null);
    view2131362149 = null;
    view2131362150.setOnClickListener(null);
    view2131362150 = null;
    view2131361856.setOnClickListener(null);
    view2131361856 = null;
    view2131361864.setOnClickListener(null);
    view2131361864 = null;
    view2131361863.setOnClickListener(null);
    view2131361863 = null;
    view2131361852.setOnClickListener(null);
    view2131361852 = null;
    view2131361851.setOnClickListener(null);
    view2131361851 = null;
    view2131361861.setOnClickListener(null);
    view2131361861 = null;
    view2131361860.setOnClickListener(null);
    view2131361860 = null;
    view2131361845.setOnClickListener(null);
    view2131361845 = null;
    view2131362090.setOnClickListener(null);
    view2131362090 = null;
    view2131361958.setOnClickListener(null);
    view2131361958 = null;
    view2131361957.setOnClickListener(null);
    view2131361957 = null;
  }
}
