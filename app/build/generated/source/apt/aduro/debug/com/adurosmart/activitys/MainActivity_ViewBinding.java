// Generated code from Butter Knife. Do not modify!
package com.adurosmart.activitys;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.CompatViewpager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131361912;

  private View view2131361946;

  private View view2131362061;

  private View view2131362120;

  private View view2131362088;

  private View view2131362170;

  private View view2131361957;

  private View view2131361958;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target.deviceImage = Utils.findRequiredViewAsType(source, R.id.device_image, "field 'deviceImage'", ImageView.class);
    target.deviceText = Utils.findRequiredViewAsType(source, R.id.device_text, "field 'deviceText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.device_layout, "field 'deviceLayout' and method 'onClick'");
    target.deviceLayout = Utils.castView(view, R.id.device_layout, "field 'deviceLayout'", RelativeLayout.class);
    view2131361912 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.groupImage = Utils.findRequiredViewAsType(source, R.id.group_image, "field 'groupImage'", ImageView.class);
    target.groupText = Utils.findRequiredViewAsType(source, R.id.group_text, "field 'groupText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.group_layout, "field 'groupLayout' and method 'onClick'");
    target.groupLayout = Utils.castView(view, R.id.group_layout, "field 'groupLayout'", RelativeLayout.class);
    view2131361946 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.sceneImage = Utils.findRequiredViewAsType(source, R.id.scene_image, "field 'sceneImage'", ImageView.class);
    target.sceneText = Utils.findRequiredViewAsType(source, R.id.scene_text, "field 'sceneText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.scene_layout, "field 'sceneLayout' and method 'onClick'");
    target.sceneLayout = Utils.castView(view, R.id.scene_layout, "field 'sceneLayout'", RelativeLayout.class);
    view2131362061 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.taskImage = Utils.findRequiredViewAsType(source, R.id.task_image, "field 'taskImage'", ImageView.class);
    target.taskText = Utils.findRequiredViewAsType(source, R.id.task_text, "field 'taskText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.task_layout, "field 'taskLayout' and method 'onClick'");
    target.taskLayout = Utils.castView(view, R.id.task_layout, "field 'taskLayout'", RelativeLayout.class);
    view2131362120 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.settingImage = Utils.findRequiredViewAsType(source, R.id.setting_image, "field 'settingImage'", ImageView.class);
    target.settingText = Utils.findRequiredViewAsType(source, R.id.setting_text, "field 'settingText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.setting_layout, "field 'settingLayout' and method 'onClick'");
    target.settingLayout = Utils.castView(view, R.id.setting_layout, "field 'settingLayout'", RelativeLayout.class);
    view2131362088 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.vp, "field 'vp' and method 'onClick'");
    target.vp = Utils.castView(view, R.id.vp, "field 'vp'", CompatViewpager.class);
    view2131362170 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ibtn_left, "field 'ibtnLeft' and method 'onClick'");
    target.ibtnLeft = Utils.castView(view, R.id.ibtn_left, "field 'ibtnLeft'", ImageButton.class);
    view2131361957 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tv_title, "field 'tvTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ibtn_right, "field 'ibtnRight' and method 'onClick'");
    target.ibtnRight = Utils.castView(view, R.id.ibtn_right, "field 'ibtnRight'", ImageButton.class);
    view2131361958 = view;
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
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.deviceImage = null;
    target.deviceText = null;
    target.deviceLayout = null;
    target.groupImage = null;
    target.groupText = null;
    target.groupLayout = null;
    target.sceneImage = null;
    target.sceneText = null;
    target.sceneLayout = null;
    target.taskImage = null;
    target.taskText = null;
    target.taskLayout = null;
    target.settingImage = null;
    target.settingText = null;
    target.settingLayout = null;
    target.vp = null;
    target.ibtnLeft = null;
    target.tvTitle = null;
    target.ibtnRight = null;

    view2131361912.setOnClickListener(null);
    view2131361912 = null;
    view2131361946.setOnClickListener(null);
    view2131361946 = null;
    view2131362061.setOnClickListener(null);
    view2131362061 = null;
    view2131362120.setOnClickListener(null);
    view2131362120 = null;
    view2131362088.setOnClickListener(null);
    view2131362088 = null;
    view2131362170.setOnClickListener(null);
    view2131362170 = null;
    view2131361957.setOnClickListener(null);
    view2131361957 = null;
    view2131361958.setOnClickListener(null);
    view2131361958 = null;
  }
}
