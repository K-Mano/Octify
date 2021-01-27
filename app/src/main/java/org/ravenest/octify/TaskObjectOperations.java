package org.ravenest.octify;

import com.google.gson.*;

import java.util.Date;

public class TaskObjectOperations {
    public TaskObject ToTaskObject(String json){
        TaskObject obj = new TaskObject();
        return obj;
    }

    public String ToJsonObject(String name, String desc, int pro, Long date, Long time) {

        TaskObject.Model model = new TaskObject.Model();
        model.task = new TaskObject.Task();

        model.task.name = name;
        model.task.description = desc;
        model.task.priority = pro;
        model.task.date = date;
        model.task.time = time;

        Gson gson = new Gson();
        return gson.toJson(model);
    }

    public String ToJsonObject(TaskObject.Model model) {
        Gson gson = new Gson();
        return gson.toJson(model);
    }
}
