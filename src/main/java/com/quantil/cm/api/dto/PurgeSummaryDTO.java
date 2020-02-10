package com.quantil.cm.api.dto;

public class PurgeSummaryDTO {
    
    private int requests;
    
    private int fileRequests;
    
    private int dirRequests;
    
    private int fileEntries;
    
    private int dirEntries;
    
    private SubSummaryDTO staging;
    
    private SubSummaryDTO production;
    
    public PurgeSummaryDTO(){
        
        requests = 0;
        
        fileRequests = 0;
        
        dirRequests = 0;
        
        fileEntries = 0;
        
        dirEntries = 0;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public int getFileRequests() {
        return fileRequests;
    }

    public void setFileRequests(int fileRequests) {
        this.fileRequests = fileRequests;
    }

    public int getDirRequests() {
        return dirRequests;
    }

    public void setDirRequests(int dirRequests) {
        this.dirRequests = dirRequests;
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

    public SubSummaryDTO getStaging() {
        return staging;
    }

    public void setStaging(SubSummaryDTO staging) {
        this.staging = staging;
    }

    public SubSummaryDTO getProduction() {
        return production;
    }

    public void setProduction(SubSummaryDTO production) {
        this.production = production;
    }
    
    public void addFileRequests(int num){
        this.fileRequests = this.fileRequests + num;
    }
    
    public void addDirRequests(int num){
        this.dirRequests = this.dirRequests + num;
    }
    
    public void addFileEntries(int num){
        this.fileEntries = this.fileEntries + num;
    }
    
    public void addDirEntries(int num){
        this.dirEntries = this.dirEntries + num;
    }
    
}
