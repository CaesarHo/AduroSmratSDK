// Generated code from Butter Knife. Do not modify!
package com.adurosmart.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ScenesFragment_ViewBinding<T extends ScenesFragment> implements Unbinder {
  protected T target;

  private View view2131951889;

  @UiThread
  public ScenesFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.editTxt = Utils.findRequiredViewAsType(source, R.id.edit_txt, "field 'editTxt'", EditText.class);
    view = Utils.findRequiredView(source, R.id.add_scenes, "field 'addScenes' and method 'onClick'");
    target.addScenes = Utils.castView(view, R.id.add_scenes, "field 'addScenes'", Button.class);
    view2131951889 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.scenesList = Utils.findRequiredViewAsType(source, R.id.scenes_list, "field 'scenesList'", ListView.class);
    target.srl = Utils.findRequiredViewAsType(source, R.id.srl, "field 'srl'", SwipeRefreshLayout.class);
    target.groupList = Utils.findRequiredViewAsType(source, R.id.group_list, "field 'groupList'", ListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.editTxt = null;
    target.addScenes = null;
    target.scenesList = null;
    target.srl = null;
    target.groupList = null;

    view2131951889.setOnClickListener(null);
    view2131951889 = null;

    this.target = null;
  }
}
