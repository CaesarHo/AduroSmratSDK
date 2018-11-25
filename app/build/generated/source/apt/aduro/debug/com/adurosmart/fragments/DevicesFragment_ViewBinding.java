// Generated code from Butter Knife. Do not modify!
package com.adurosmart.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DevicesFragment_ViewBinding implements Unbinder {
  private DevicesFragment target;

  private View view2131362035;

  private View view2131362010;

  private View view2131362032;

  private View view2131361858;

  private View view2131362163;

  private View view2131362162;

  @UiThread
  public DevicesFragment_ViewBinding(final DevicesFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.remote_switch, "field 'remoteSwitch' and method 'onClick'");
    target.remoteSwitch = Utils.castView(view, R.id.remote_switch, "field 'remoteSwitch'", SwitchCompat.class);
    view2131362035 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.net_btn, "field 'netBtn' and method 'onClick'");
    target.netBtn = Utils.castView(view, R.id.net_btn, "field 'netBtn'", Button.class);
    view2131362010 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.read_attribute, "field 'readAttribute' and method 'onClick'");
    target.readAttribute = Utils.castView(view, R.id.read_attribute, "field 'readAttribute'", Button.class);
    view2131362032 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.layoutLl = Utils.findRequiredViewAsType(source, R.id.layout_ll, "field 'layoutLl'", LinearLayout.class);
    target.list = Utils.findRequiredViewAsType(source, R.id.list, "field 'list'", ListView.class);
    target.srl = Utils.findRequiredViewAsType(source, R.id.srl, "field 'srl'", SwipeRefreshLayout.class);
    target.ip_text = Utils.findRequiredViewAsType(source, R.id.ip_text, "field 'ip_text'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_reset, "field 'btn_reset' and method 'onClick'");
    target.btn_reset = Utils.castView(view, R.id.btn_reset, "field 'btn_reset'", Button.class);
    view2131361858 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.update_info, "field 'update_info' and method 'onClick'");
    target.update_info = Utils.castView(view, R.id.update_info, "field 'update_info'", Button.class);
    view2131362163 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.update_btn, "field 'update_btn' and method 'onClick'");
    target.update_btn = Utils.castView(view, R.id.update_btn, "field 'update_btn'", Button.class);
    view2131362162 = view;
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
    DevicesFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.remoteSwitch = null;
    target.netBtn = null;
    target.readAttribute = null;
    target.layoutLl = null;
    target.list = null;
    target.srl = null;
    target.ip_text = null;
    target.btn_reset = null;
    target.update_info = null;
    target.update_btn = null;

    view2131362035.setOnClickListener(null);
    view2131362035 = null;
    view2131362010.setOnClickListener(null);
    view2131362010 = null;
    view2131362032.setOnClickListener(null);
    view2131362032 = null;
    view2131361858.setOnClickListener(null);
    view2131361858 = null;
    view2131362163.setOnClickListener(null);
    view2131362163 = null;
    view2131362162.setOnClickListener(null);
    view2131362162 = null;
  }
}
