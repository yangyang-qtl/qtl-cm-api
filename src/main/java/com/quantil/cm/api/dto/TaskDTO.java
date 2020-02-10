package com.quantil.cm.api.dto;

import java.util.List;


public class TaskDTO {
    
    private String purgeId;
    
    private String status;
    
    private double successRate;
    
    private String internalMessages;
    
    private String finishTime;
    
    private List<MessageDTO> message;

    public String getPurgeId() {
        return purgeId;
    }

    public void setPurgeId(String purgeId) {
        this.purgeId = purgeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public String getInternalMessages() {
        return internalMessages;
    }

    public void setInternalMessages(String internalMessages) {
        this.internalMessages = internalMessages;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public List<MessageDTO> getMessage() {
        return message;
    }

    public void setMessage(List<MessageDTO> message) {
        this.message = message;
    }
    
   
}
