// Generated code from Butter Knife. Do not modify!
package com.adurosmart.activitys;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SensorsActivity_ViewBinding implements Unbinder {
  private SensorsActivity target;

  private View view2131361957;

  @UiThread
  public SensorsActivity_ViewBinding(SensorsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SensorsActivity_ViewBinding(final SensorsActivity target, View source) {
    this.target = target;

    View view;
    target.scenesList = Utils.findRequiredViewAsType(source, R.id.scenes_list, "field 'scenesList'", ListView.class);
    target.txt_value = Utils.findRequiredViewAsType(source, R.id.txt_value, "field 'txt_value'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ibtn_left, "field 'ibtn_laft' and method 'onClick'");
    target.ibtn_laft = Utils.castView(view, R.id.ibtn_left, "field 'ibtn_laft'", ImageButton.class);
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
    SensorsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.scenesList = null;
    target.txt_value = null;
    target.ibtn_laft = null;

    view2131361957.setOnClickListener(null);
    view2131361957 = null;
  }
}
