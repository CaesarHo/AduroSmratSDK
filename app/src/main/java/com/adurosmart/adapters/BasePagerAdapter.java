package com.adurosmart.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by best on 2016/11/9.
 */

public class BasePagerAdapter extends FragmentStatePagerAdapter{
    List<Fragment> mList;

    public BasePagerAdapter(FragmentManager fragmentManager,List<Fragment> list){
        super(fragmentManager);
        this.mList = list;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getArguments().getString("title");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }
}
