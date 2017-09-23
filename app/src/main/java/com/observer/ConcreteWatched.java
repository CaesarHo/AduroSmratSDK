package com.observer;

/**
 * Created by best on 2017/3/16.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * 被观察者实现类
 */
public class ConcreteWatched implements  Watched{
    // 定义一个List来封装Watcher
    private List<Watcher> list = new ArrayList<>();
    @Override
    public void add(Watcher watcher) {
        list.add(watcher);
    }

    @Override
    public void remove(Watcher watcher) {
        list.remove(watcher);
    }

    @Override
    public void notifyWatcher(Entity entity) {
        for (Watcher watcher : list) {
            watcher.updateNotify(entity);
        }
    }
}
