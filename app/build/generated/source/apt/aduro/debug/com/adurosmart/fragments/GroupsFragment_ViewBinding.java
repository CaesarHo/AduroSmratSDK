// Generated code from Butter Knife. Do not modify!
package com.adurosmart.fragments;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.adurosmart.sdk.R;
import com.adurosmart.widget.CompatSeekBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class GroupsFragment_ViewBinding<T extends GroupsFragment> implements Unbinder {
  protected T target;

  private View view2131951877;

  private View view2131951881;

  @UiThread
  public GroupsFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.roomName = Utils.findRequiredViewAsType(source, R.id.room_name, "field 'roomName'", TextView.class);
    target.editTxt = Utils.findRequiredViewAsType(source, R.id.edit_txt, "field 'editTxt'", EditText.class);
    view = Utils.findRequiredView(source, R.id.add_groups, "field 'addGroups' and method 'onClick'");
    target.addGroups = Utils.castView(view, R.id.add_groups, "field 'addGroups'", Button.class);
    view2131951877 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.groupsList = Utils.findRequiredViewAsType(source, R.id.groups_list, "field 'groupsList'", ListView.class);
    target.srl = Utils.findRequiredViewAsType(source, R.id.srl, "field 'srl'", SwipeRefreshLayout.class);
    target.deviceList = Utils.findRequiredViewAsType(source, R.id.device_list, "field 'deviceList'", ListView.class);
    view = Utils.findRequiredView(source, R.id.group_state, "field 'group_state' and method 'onClick'");
    target.group_state = Utils.castView(view, R.id.group_state, "field 'group_state'", ImageView.class);
    view2131951881 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.compatSeekBar = Utils.findRequiredViewAsType(source, R.id.seekbar, "field 'compatSeekBar'", CompatSeekBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.roomName = null;
    target.editTxt = null;
    target.addGroups = null;
    target.groupsList = null;
    target.srl = null;
    target.deviceList = null;
    target.group_state = null;
    target.compatSeekBar = null;

    view2131951877.setOnClickListener(null);
    view2131951877 = null;
    view2131951881.setOnClickListener(null);
    view2131951881 = null;

    this.target = null;
  }
}
