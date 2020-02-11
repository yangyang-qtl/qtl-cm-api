package com.quantil.cm.api.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.quantil.cm.api.domain.PurgeTask;
import com.quantil.cm.api.dto.PurgeRequestEntityDTO;
import com.quantil.cm.api.dto.PurgeStatusClassifyDTO;
import com.quantil.cm.api.dto.PurgeStatusDTO;
import com.quantil.cm.api.dto.PurgeTaskDTO;
import com.quantil.cm.api.dto.PurgeTaskPortalDTO;
import com.quantil.cm.api.dto.PurgeTaskQueryDTO;
import com.quantil.cm.api.service.PurgeTaskService;



@RestController
@RequestMapping("/purges")
public class PurgeTaskController {
    
    @Autowired
    private PurgeTaskService purgeTaskService;
    
    /**
     * create Purge task 
     * @param purgeTaskDTO
     * @return
     * @throws Exception 
     */
    @PostMapping
    public ResponseEntity<String> createFilter(@RequestBody @Valid PurgeTaskDTO purgeTaskDTO) throws Exception{
        PurgeTask purgeTask = purgeTaskService.create(purgeTaskDTO);
        return ResponseEntity.created(URI.create("/purges/"+purgeTask.getId())).build();
    }
    
    /**
     * search a PurgeTaskStatus
     * @param taskId
     * @return
     */
    @GetMapping("{purgeId}")
    public PurgeStatusDTO getPurgeStatus(@PathVariable("purgeId") String purgeId) {
        return purgeTaskService.getPurgeTaskStatus(purgeId);
    }
   
   /**
    * search a PurgeTaskStatusList
    * @return
    */
    @GetMapping("/status")
    public List<PurgeStatusClassifyDTO> queryPurgeStatus() {
        return purgeTaskService.queryPurgeStatus();
    }
    
    /**
     * search Purge Request
     * @param startdate
     * @param enddate
     * @param offset
     * @param limit
     * @param sortBy
     * @param sortOrder
     * @return
     */
    @GetMapping
    public PurgeRequestEntityDTO getPurgeRequest(@RequestParam(value="startdate") String startdate, @RequestParam(value="enddate") String enddate, @RequestParam(value="offset") String offset, @RequestParam(value="limit") String limit, @RequestParam(value="sortBy") String sortBy, @RequestParam(value="sortOrder") String sortOrder)  {
        return purgeTaskService.getPurgeRequest(startdate, enddate, offset, limit, sortBy, sortOrder);
    }
    
    /**
     * search Purge Request
     * @param queryDTO
     * @param page
     * @return
     */
    @GetMapping("/portal")
    public List<PurgeTaskPortalDTO> queryPurgeTaskForPortal(PurgeTaskQueryDTO queryDTO, Page<PurgeTaskPortalDTO> page)  {
        return purgeTaskService.queryPurgeTaskForPortal(queryDTO, page);
    }
}
