// Generated code from Butter Knife. Do not modify!
package com.ftp;

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

public class FtpActivity_ViewBinding<T extends FtpActivity> implements Unbinder {
  protected T target;

  private View view2131951809;

  private View view2131951810;

  @UiThread
  public FtpActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_dow, "field 'btnDow' and method 'onClick'");
    target.btnDow = Utils.castView(view, R.id.btn_dow, "field 'btnDow'", Button.class);
    view2131951809 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_upd, "field 'btnUpd' and method 'onClick'");
    target.btnUpd = Utils.castView(view, R.id.btn_upd, "field 'btnUpd'", Button.class);
    view2131951810 = view;
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

    target.btnDow = null;
    target.btnUpd = null;

    view2131951809.setOnClickListener(null);
    view2131951809 = null;
    view2131951810.setOnClickListener(null);
    view2131951810 = null;

    this.target = null;
  }
}
