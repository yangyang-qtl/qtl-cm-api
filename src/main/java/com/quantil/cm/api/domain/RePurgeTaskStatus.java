package com.quantil.cm.api.domain;

import com.baomidou.mybatisplus.annotations.TableField;

public class RePurgeTaskStatus {
    private String id;
    
    @TableField("re_purge_id")
    private String rePurgeId;
    
    @TableField("server_id")
    private String serverId;

    @TableField("task_id")
    private String taskId;
    
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRePurgeId() {
        return rePurgeId;
    }

    public void setRePurgeId(String rePurgeId) {
        this.rePurgeId = rePurgeId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
}
