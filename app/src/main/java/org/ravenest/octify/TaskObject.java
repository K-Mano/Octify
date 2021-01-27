package org.ravenest.octify;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TaskObject {
    public static class Model {
        @SerializedName("Task")
        @Expose
        public Task task;
    }
    public static class Task {
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("priority")
        @Expose
        public int priority;
        @SerializedName("date")
        @Expose
        public Long date;
        @SerializedName("time")
        @Expose
        public Long time;
    }
}
