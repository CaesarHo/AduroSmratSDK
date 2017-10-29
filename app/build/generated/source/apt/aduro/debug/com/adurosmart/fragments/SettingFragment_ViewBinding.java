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

public class SettingFragment_ViewBinding<T extends SettingFragment> implements Unbinder {
  protected T target;

  private View view2131951893;

  private View view2131951894;

  private View view2131951895;

  private View view2131951896;

  private View view2131951897;

  private View view2131951898;

  @UiThread
  public SettingFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_3d_desktop, "field 'btn3dDesktop' and method 'onClick'");
    target.btn3dDesktop = Utils.castView(view, R.id.btn_3d_desktop, "field 'btn3dDesktop'", Button.class);
    view2131951893 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_facer, "field 'btn_facer' and method 'onClick'");
    target.btn_facer = Utils.castView(view, R.id.btn_facer, "field 'btn_facer'", Button.class);
    view2131951894 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_ftp, "field 'btn_ftp' and method 'onClick'");
    target.btn_ftp = Utils.castView(view, R.id.btn_ftp, "field 'btn_ftp'", Button.class);
    view2131951895 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_okhttp, "field 'btn_scrol' and method 'onClick'");
    target.btn_scrol = Utils.castView(view, R.id.btn_okhttp, "field 'btn_scrol'", Button.class);
    view2131951896 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.dec_btn, "field 'dec_btn' and method 'onClick'");
    target.dec_btn = Utils.castView(view, R.id.dec_btn, "field 'dec_btn'", Button.class);
    view2131951897 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_codec, "field 'btn_codec' and method 'onClick'");
    target.btn_codec = Utils.castView(view, R.id.btn_codec, "field 'btn_codec'", Button.class);
    view2131951898 = view;
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

    target.btn3dDesktop = null;
    target.btn_facer = null;
    target.btn_ftp = null;
    target.btn_scrol = null;
    target.dec_btn = null;
    target.btn_codec = null;

    view2131951893.setOnClickListener(null);
    view2131951893 = null;
    view2131951894.setOnClickListener(null);
    view2131951894 = null;
    view2131951895.setOnClickListener(null);
    view2131951895 = null;
    view2131951896.setOnClickListener(null);
    view2131951896 = null;
    view2131951897.setOnClickListener(null);
    view2131951897 = null;
    view2131951898.setOnClickListener(null);
    view2131951898 = null;

    this.target = null;
  }
}
