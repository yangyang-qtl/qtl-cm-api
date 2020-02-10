package com.quantil.cm.api.domain;

import com.baomidou.mybatisplus.annotations.TableField;

public class PurgeTaskContent {
    private String id;
    
    private String urls;
    
    @TableField("purge_task_id")
    private String purgeTaskId;
    
    private String headers;
    
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getPurgeTaskId() {
        return purgeTaskId;
    }

    public void setPurgeTaskId(String purgeTaskId) {
        this.purgeTaskId = purgeTaskId;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
