// Generated code from Butter Knife. Do not modify!
package com.adurosmart.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TasksFragment_ViewBinding implements Unbinder {
  private TasksFragment target;

  private View view2131361931;

  @UiThread
  public TasksFragment_ViewBinding(final TasksFragment target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.task_list, "field 'recyclerView'", RecyclerView.class);
    target.srl = Utils.findRequiredViewAsType(source, R.id.srl, "field 'srl'", SwipeRefreshLayout.class);
    view = Utils.findRequiredView(source, R.id.fab, "field 'fab' and method 'onClick'");
    target.fab = Utils.castView(view, R.id.fab, "field 'fab'", FloatingActionButton.class);
    view2131361931 = view;
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
    TasksFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.srl = null;
    target.fab = null;

    view2131361931.setOnClickListener(null);
    view2131361931 = null;
  }
}
