package com.quantil.cm.api.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;



public class PurgeTaskDTO {

    @NotBlank(message = "name can not be blank")
    private String name;
    
    private List<String> fileUrls;
    
    private List<FileHeaderDTO> fileHeaders;
    
    private List<String> dirUrls;
    
    private List<String> cacheKey;
    
    @NotBlank(message = "action can not be blank")
    @Pattern(regexp="delete|invalidate", message="The specified active does not exist")
    private String action;
    
    @NotBlank(message = "target can not be blank")
    @Pattern(regexp="production|staging", message="The specified active does not exist")
    private String target;

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
    
    
}
