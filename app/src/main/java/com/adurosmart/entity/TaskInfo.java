package com.adurosmart.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by best on 2016/9/12.
 */
public class TaskInfo implements Parcelable {
    private String id;
    private String title;
    private String content;
    private String userId;
    private String projectId;
    private String createTime;
    private String userName;
    private String userFace;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserFace() {
        return userFace;
    }

    public void setUserFace(String userFace) {
        this.userFace = userFace;
    }


    public static List<TaskInfo> parse(String json) {
        List<TaskInfo> entities = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            int c = array.length();
            for (int i = 0; i < c; i++) {
                TaskInfo entity = new TaskInfo();
                JSONObject o = (JSONObject) array.get(i);
                entity.setId(o.getString("id"));
                entity.setTitle(o.getString("text_title"));
                entity.setContent(o.getString("text_content"));
                entity.setUserId(o.getString("user_id"));
                entity.setProjectId(o.getString("project_id"));
                entity.setCreateTime(o.getString("create_time"));
                entity.setUserName(o.getString("name"));
                entity.setUserFace(o.getString("user_face"));
                entities.add(entity);
                }
            } catch (JSONException e) {
            e.printStackTrace();
        }
        return entities;
    }


    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {
        @Override
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }

        @Override
        public TaskInfo createFromParcel(Parcel source) {
            TaskInfo d = new TaskInfo();
            d.id = source.readString();
            d.content = source.readString();
            d.createTime = source.readString();
            d.projectId = source.readString();
            d.title = source.readString();
            d.userFace = source.readString();
            d.userId = source.readString();
            d.userName = source.readString();
            return d;
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(createTime);
        dest.writeString(projectId);
        dest.writeString(title);
        dest.writeString(userFace);
        dest.writeString(userId);
        dest.writeString(userName);
    }
}