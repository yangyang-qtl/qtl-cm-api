package com.quantil.cm.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quantil.cm.api.dto.PurgeSummaryDTO;
import com.quantil.cm.api.service.PurgeTaskService;



@RestController
@RequestMapping("/purgeSummary")
public class PurgeSummaryController {
    @Autowired
    private PurgeTaskService purgeTaskService;
    
    /**
     * search Purge Summary
     * @param startdate
     * @param enddate
     * @return
     */
    @GetMapping
    public PurgeSummaryDTO getPurgeSummary(@RequestParam(value="startdate") String startdate, @RequestParam(value="enddate") String enddate)  {
        return purgeTaskService.getPurgeSummary(startdate, enddate);
    }
}
