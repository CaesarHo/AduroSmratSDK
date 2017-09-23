package com.adurosmart;

import com.core.entity.AppDevice;
import com.core.entity.AppScene;

/**
 * Created by best on 2016/11/17.
 */

public class FirstEvent {
    private AppScene appScene;
    private AppDevice appDevice;

    public FirstEvent(AppScene appScene) {
        this.appScene = appScene;
    }

    public FirstEvent(AppDevice appDevice) {
        this.appDevice = appDevice;
    }

    public AppScene getAppScene() {
        return appScene;
    }

    public AppDevice getAppDevice() {
        return appDevice;
    }
}
