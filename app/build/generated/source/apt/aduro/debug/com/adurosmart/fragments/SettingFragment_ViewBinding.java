// Generated code from Butter Knife. Do not modify!
package com.adurosmart.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingFragment_ViewBinding implements Unbinder {
  private SettingFragment target;

  private View view2131361844;

  private View view2131361850;

  private View view2131361853;

  private View view2131361855;

  private View view2131361900;

  private View view2131361848;

  @UiThread
  public SettingFragment_ViewBinding(final SettingFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_3d_desktop, "field 'btn3dDesktop' and method 'onClick'");
    target.btn3dDesktop = Utils.castView(view, R.id.btn_3d_desktop, "field 'btn3dDesktop'", Button.class);
    view2131361844 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_facer, "field 'btn_facer' and method 'onClick'");
    target.btn_facer = Utils.castView(view, R.id.btn_facer, "field 'btn_facer'", Button.class);
    view2131361850 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_ftp, "field 'btn_ftp' and method 'onClick'");
    target.btn_ftp = Utils.castView(view, R.id.btn_ftp, "field 'btn_ftp'", Button.class);
    view2131361853 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_okhttp, "field 'btn_scrol' and method 'onClick'");
    target.btn_scrol = Utils.castView(view, R.id.btn_okhttp, "field 'btn_scrol'", Button.class);
    view2131361855 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.dec_btn, "field 'dec_btn' and method 'onClick'");
    target.dec_btn = Utils.castView(view, R.id.dec_btn, "field 'dec_btn'", Button.class);
    view2131361900 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_codec, "field 'btn_codec' and method 'onClick'");
    target.btn_codec = Utils.castView(view, R.id.btn_codec, "field 'btn_codec'", Button.class);
    view2131361848 = view;
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
    SettingFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btn3dDesktop = null;
    target.btn_facer = null;
    target.btn_ftp = null;
    target.btn_scrol = null;
    target.dec_btn = null;
    target.btn_codec = null;

    view2131361844.setOnClickListener(null);
    view2131361844 = null;
    view2131361850.setOnClickListener(null);
    view2131361850 = null;
    view2131361853.setOnClickListener(null);
    view2131361853 = null;
    view2131361855.setOnClickListener(null);
    view2131361855 = null;
    view2131361900.setOnClickListener(null);
    view2131361900 = null;
    view2131361848.setOnClickListener(null);
    view2131361848 = null;
  }
}
