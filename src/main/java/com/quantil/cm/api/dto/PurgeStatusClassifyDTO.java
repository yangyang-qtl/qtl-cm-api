package com.quantil.cm.api.dto;

import java.util.List;

public class PurgeStatusClassifyDTO {
    
    private String target;
    
    private List<TaskDTO> tasks;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }
    
}
