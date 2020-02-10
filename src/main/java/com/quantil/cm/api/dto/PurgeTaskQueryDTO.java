package com.quantil.cm.api.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class PurgeTaskQueryDTO {
    private String name;
    
    private String action;
    
    private String target;
    
    @JSONField(name = "customer-name") 
    private String customerName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
}
