package com.risk.dolist;

/**
 * Created by redwan on 7/30/17.
 */

public class TaskCauseItem {
    int id;
    String task;
    String cause;

    public TaskCauseItem(String task, String cause) {
        this.task = task;
        this.cause = cause;
    }

    public TaskCauseItem(int _id,String task, String cause) {
        this.id = _id;
        this.task = task;
        this.cause = cause;
    }
    @Override
    public String toString() {
        return "Word='" + task + '\'' +
                ", Meaning='" + cause + '\'' +
                '}';
    }

    public String getTask() {
        return task;
    }

    public String getCause() {
        return cause;
    }
}
