package com.quantil.cm.api.dto;

public class SubSummaryDTO {
    
    private int requests;
    
    private int fileRequests;
    
    private int dirRequests;
    
    private int fileEntries;
    
    private int dirEntries;
    
    public SubSummaryDTO(){
        
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
    
    public void addFileRequests(int num){
        this.fileRequests = this.fileRequests + num;
    }
    
    public void addRequests(int num){
        this.requests = this.requests + num;
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
