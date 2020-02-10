package com.quantil.cm.api.domain;

import com.baomidou.mybatisplus.annotations.TableField;

public class PurgeTaskStatistics {
    private String id;
    
    @TableField("purge_task_id")
    private String purgeTaskId;
    
    private String hostname;
    
    @TableField("file_entries")
    private int fileEntries;
    
    @TableField("dir_entries")
    private int dirEntries;
    
    @TableField("cache_key_entries")
    private int cacheKeyEntries;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPurgeTaskId() {
        return purgeTaskId;
    }

    public void setPurgeTaskId(String purgeTaskId) {
        this.purgeTaskId = purgeTaskId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getFileEntries() {
        return fileEntries;
    }

    public void setFileEntries(int fileEntries) {
        this.fileEntries = fileEntries;
    }

    public int getDirEntries() {
        return dirEntries;
    }

    public void setDirEntries(int dirEntries) {
        this.dirEntries = dirEntries;
    }

    public int getCacheKeyEntries() {
        return cacheKeyEntries;
    }

    public void setCacheKeyEntries(int cacheKeyEntries) {
        this.cacheKeyEntries = cacheKeyEntries;
    }
    
}
