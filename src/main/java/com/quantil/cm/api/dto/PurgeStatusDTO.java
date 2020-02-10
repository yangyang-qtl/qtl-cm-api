package com.quantil.cm.api.dto;

import java.util.List;

public class PurgeStatusDTO {
    
    private String name;
    
    private List<String> fileUrls;
    
    private List<FileHeaderDTO> fileHeaders;
    
    private List<String> dirUrls;
    
    private List<String> cacheKey;
    
    private String action;
    
    private String target;
    
    private String submissionTime;
    
    private String status;
    
    private String finishTime;
    
    private String customerId;
    
    private double successRate;
    
    private List<ErrorDTO> errors;


    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }

    public List<FileHeaderDTO> getFileHeaders() {
        return fileHeaders;
    }

    public void setFileHeaders(List<FileHeaderDTO> fileHeaders) {
        this.fileHeaders = fileHeaders;
    }

    public List<String> getDirUrls() {
        return dirUrls;
    }

    public void setDirUrls(List<String> dirUrls) {
        this.dirUrls = dirUrls;
    }

    public List<String> getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(List<String> cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<ErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDTO> errors) {
        this.errors = errors;
    }
    
    
}
