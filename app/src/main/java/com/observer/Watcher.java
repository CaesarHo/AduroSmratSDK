package com.observer;

/**
 * Created by best on 2017/3/16.
 */

/**
 * 观察者接口
 */
public interface Watcher {
    //再定义一个用来获取更新信息接收的方法
    public void updateNotify(Entity entity);
}
