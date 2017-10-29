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

public class DevicesFragment_ViewBinding<T extends DevicesFragment> implements Unbinder {
  protected T target;

  private View view2131951867;

  private View view2131951864;

  private View view2131951866;

  private View view2131951865;

  private View view2131951868;

  private View view2131951869;

  @UiThread
  public DevicesFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.remote_switch, "field 'remoteSwitch' and method 'onClick'");
    target.remoteSwitch = Utils.castView(view, R.id.remote_switch, "field 'remoteSwitch'", SwitchCompat.class);
    view2131951867 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.net_btn, "field 'netBtn' and method 'onClick'");
    target.netBtn = Utils.castView(view, R.id.net_btn, "field 'netBtn'", Button.class);
    view2131951864 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.read_attribute, "field 'readAttribute' and method 'onClick'");
    target.readAttribute = Utils.castView(view, R.id.read_attribute, "field 'readAttribute'", Button.class);
    view2131951866 = view;
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
    view2131951865 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.update_info, "field 'update_info' and method 'onClick'");
    target.update_info = Utils.castView(view, R.id.update_info, "field 'update_info'", Button.class);
    view2131951868 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.update_btn, "field 'update_btn' and method 'onClick'");
    target.update_btn = Utils.castView(view, R.id.update_btn, "field 'update_btn'", Button.class);
    view2131951869 = view;
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
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

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

    view2131951867.setOnClickListener(null);
    view2131951867 = null;
    view2131951864.setOnClickListener(null);
    view2131951864 = null;
    view2131951866.setOnClickListener(null);
    view2131951866 = null;
    view2131951865.setOnClickListener(null);
    view2131951865 = null;
    view2131951868.setOnClickListener(null);
    view2131951868 = null;
    view2131951869.setOnClickListener(null);
    view2131951869 = null;

    this.target = null;
  }
}
