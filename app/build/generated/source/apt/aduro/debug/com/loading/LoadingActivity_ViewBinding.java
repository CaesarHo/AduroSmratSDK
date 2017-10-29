// Generated code from Butter Knife. Do not modify!
package com.loading;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import com.loading.widget.book.BookLoading;
import com.loading.widget.newton.NewtonCradleLoading;
import com.loading.widget.rotate.RotateLoading;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoadingActivity_ViewBinding<T extends LoadingActivity> implements Unbinder {
  protected T target;

  private View view2131951933;

  private View view2131951935;

  private View view2131951937;

  @UiThread
  public LoadingActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.bookloading = Utils.findRequiredViewAsType(source, R.id.bookloading, "field 'bookloading'", BookLoading.class);
    view = Utils.findRequiredView(source, R.id.btn_bookloading, "field 'btnBookloading' and method 'onClick'");
    target.btnBookloading = Utils.castView(view, R.id.btn_bookloading, "field 'btnBookloading'", Button.class);
    view2131951933 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.rotateloading = Utils.findRequiredViewAsType(source, R.id.rotateloading, "field 'rotateloading'", RotateLoading.class);
    view = Utils.findRequiredView(source, R.id.btn_rotateloading, "field 'btnRotateloading' and method 'onClick'");
    target.btnRotateloading = Utils.castView(view, R.id.btn_rotateloading, "field 'btnRotateloading'", Button.class);
    view2131951935 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.newtonCradleLoading = Utils.findRequiredViewAsType(source, R.id.newton_cradle_loading, "field 'newtonCradleLoading'", NewtonCradleLoading.class);
    view = Utils.findRequiredView(source, R.id.btn_newton, "field 'btnNewton' and method 'onClick'");
    target.btnNewton = Utils.castView(view, R.id.btn_newton, "field 'btnNewton'", Button.class);
    view2131951937 = view;
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

    target.bookloading = null;
    target.btnBookloading = null;
    target.rotateloading = null;
    target.btnRotateloading = null;
    target.newtonCradleLoading = null;
    target.btnNewton = null;

    view2131951933.setOnClickListener(null);
    view2131951933 = null;
    view2131951935.setOnClickListener(null);
    view2131951935 = null;
    view2131951937.setOnClickListener(null);
    view2131951937 = null;

    this.target = null;
  }
}
