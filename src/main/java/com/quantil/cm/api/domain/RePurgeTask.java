package com.quantil.cm.api.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

public class RePurgeTask {
    private String id;
    
    @TableField("server_id")
    private String serverId;
    
    @TableField("server_name")
    private String serverName;

    @TableField("server_ip")
    private String serverIp;
    
    private String operator;
    
    private Date from;
    
    private Date end;
    
    @TableField("commit_time")
    private Date commitTime; 
    
    @TableField("finish_time")
    private Date finishTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
    
  
}
