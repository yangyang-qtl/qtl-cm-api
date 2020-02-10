package com.quantil.cm.api.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

public class PurgeTaskStatus {
    
    private String id;
    
    @TableField("task_id")
    private String taskId;
    
    @TableField("server_name")
    private String serverName;

    @TableField("server_ip")
    private String serverIp;
    
    @TableField("server_id")
    private String serverId;
    
    @TableField("server_status")
    private String serverStatus; 
    
    private String state; 
    
    private String reason; 
    
    @TableField("created_time ")
    private Date createdTime ; 
    
    @TableField("updated_time")
    private Date updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    
    
}
