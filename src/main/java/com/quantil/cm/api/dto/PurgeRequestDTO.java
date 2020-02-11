package com.quantil.cm.api.dto;

import java.util.List;

public class PurgeRequestDTO {
    
    private String id;
    
    private List<String> hostnames;
    
    private int fileEntries;
    
    private int dirEntries;
    
    private int cacheKeyEntries;
    
    private String status;
    
    private String target;
    
    private double successRate;
    
    private String finishTime;
    
    private String submissionTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getHostnames() {
        return hostnames;
    }

    public void setHostnames(List<String> hostnames) {
        this.hostnames = hostnames;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }
    
}
