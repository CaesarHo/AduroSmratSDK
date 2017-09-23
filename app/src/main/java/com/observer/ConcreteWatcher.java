package com.observer;

/**
 * Created by best on 2017/3/16.
 */

/**
 * 观察者实现类(一般用在对应的Activity直接实现Watcher即可)
 */
public class ConcreteWatcher implements Watcher{
    @Override
    public void updateNotify(Entity entity) {
        int id = entity.getId();
        String name = entity.getName();
        String address = entity.getAddress();
        System.out.println("data = " + id + name + address);
    }
}
