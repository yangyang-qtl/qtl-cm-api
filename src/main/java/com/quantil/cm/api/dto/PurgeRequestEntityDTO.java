package com.quantil.cm.api.dto;

import java.util.List;

public class PurgeRequestEntityDTO {
    
    private int count;
    
    private List<PurgeRequestDTO> purgeRequests;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PurgeRequestDTO> getPurgeRequests() {
        return purgeRequests;
    }

    public void setPurgeRequests(List<PurgeRequestDTO> purgeRequests) {
        this.purgeRequests = purgeRequests;
    }
    
}
