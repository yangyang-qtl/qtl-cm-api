package com.quantil.cm.api.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.quantil.cm.api.domain.PurgeTask;
import com.quantil.cm.api.domain.PurgeTaskContent;
import com.quantil.cm.api.domain.PurgeTaskStatistics;
import com.quantil.cm.api.domain.PurgeTaskStatus;
import com.quantil.cm.api.dto.ErrorDTO;
import com.quantil.cm.api.dto.FileHeaderDTO;
import com.quantil.cm.api.dto.MessageDTO;
import com.quantil.cm.api.dto.PurgeRequestDTO;
import com.quantil.cm.api.dto.PurgeRequestEntityDTO;
import com.quantil.cm.api.dto.PurgeStatusClassifyDTO;
import com.quantil.cm.api.dto.PurgeStatusDTO;
import com.quantil.cm.api.dto.PurgeSummaryDTO;
import com.quantil.cm.api.dto.PurgeTaskDTO;
import com.quantil.cm.api.dto.PurgeTaskPortalDTO;
import com.quantil.cm.api.dto.PurgeTaskQueryDTO;
import com.quantil.cm.api.dto.SubSummaryDTO;
import com.quantil.cm.api.dto.TaskDTO;
import com.quantil.cm.api.framework.exception.BadRequestException;
import com.quantil.cm.api.framework.exception.NoFoundException;
import com.quantil.cm.api.framework.util.BeanCopyUtil;
import com.quantil.cm.api.framework.util.UuidUtil;
import com.quantil.cm.api.mapper.PurgeTaskContentMapper;
import com.quantil.cm.api.mapper.PurgeTaskMapper;
import com.quantil.cm.api.mapper.PurgeTaskStatisticsMapper;
import com.quantil.cm.api.mapper.PurgeTaskStatusMapper;
import com.quantil.cm.api.rocketmq.RocketmqProducer;





@Service
public class PurgeTaskService {
    
    private final static String WAITIING = "waiting";
    private final static String FILE = "file";
    private final static String DIR = "dir";
    private final static String CACHE_KEY = "cacheKey";
    private final static String STAGING = "staging";
    private final static String PRODUCTION = "production";
    private final static String CUSTOMER_ID = "purge-customer-id";
    private final static String CUSTOMER_NAME = "purge-customer-name";
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String DATE_FORMAT_RFC = "yyyy-MM-dd'T'HH:mm:ssZ";
    private final static String FROM = "dateFrom";
    private final static String TO = "dateTo";
    private final static String ID_EXCEPTION = "PurgeIdNoFound";
    private final static String OFFSET_EXCEPTION = "InvalidListOffset";
    private final static String LIMIT_EXCEPTION = "InvalidListLimit";
    private final static String FROM_EXCEPTION = "InvalidDateFrom";
    private final static String TO_EXCEPTION = "InvalidDateTo";
    private final static String SORT_EXCEPTION = "InvalidSortField";
    private final static String ORDER_EXCEPTION = "InvalidOrder";
    private final static String COMMIT_TIME = "commit_time";
    private final static String ASC = "asc";
    private final static String DESC = "desc";
    
    private static Map<String, String> ORDERMAP = new HashMap<String, String> (){
        private static final long serialVersionUID = 1L;

    {
        put("submissionTime","commit_time");
        put("finishTime","finish_time");
    }};
    
    @Autowired
    private PurgeTaskMapper purgeTaskMapper;
    @Autowired
    private PurgeTaskContentMapper purgeTaskContentMapper;
    @Autowired
    private PurgeTaskStatusMapper purgeTaskStatusMapper;
    @Autowired
    private PurgeTaskStatisticsMapper purgeTaskStatisticsMapper;
    @Autowired
    private RocketmqProducer rocketmqProducer;
    
    @Autowired
    private HttpServletRequest request;
    
    /**
     * create Purge task 
     * @param purgeTaskDTO
     * @return
     * @throws Exception 
     */
    @Transactional
    public PurgeTask create(PurgeTaskDTO purgeTaskDTO) throws Exception{
       PurgeTask purgeTask = new PurgeTask();
       PurgeTaskStatistics purgeTaskStatistics = new PurgeTaskStatistics();
       Set<String> hostSet = new HashSet<String>();
       
       BeanUtils.copyProperties(purgeTaskDTO, purgeTask);
       purgeTask.setId(UuidUtil.randomUUID()); 
       purgeTask.setStatus(WAITIING);
       purgeTask.setCommitTime(new Date());
       purgeTask.setCustomerId(request.getHeader(CUSTOMER_ID));
       purgeTask.setCustomerName(request.getHeader(CUSTOMER_NAME));
       purgeTaskMapper.insert(purgeTask);
       
       String headers = "";
       if(!ObjectUtils.isEmpty(purgeTaskDTO.getFileHeaders())){
           headers = JSON.toJSONString(purgeTaskDTO.getFileHeaders());
          
       }
      
       
       if(!ObjectUtils.isEmpty(purgeTaskDTO.getDirUrls())){
           PurgeTaskContent purgeTaskContent = new PurgeTaskContent();
           purgeTaskContent.setId(UuidUtil.randomUUID());
           purgeTaskContent.setPurgeTaskId(purgeTask.getId());
           purgeTaskContent.setType(DIR);
           purgeTaskContent.setUrls(JSON.toJSONString(purgeTaskDTO.getDirUrls()));
           purgeTaskStatistics.setDirEntries(purgeTaskDTO.getDirUrls().size());
           addHost(hostSet, purgeTaskDTO.getDirUrls());
           purgeTaskContentMapper.insert(purgeTaskContent);
       }
       
       if(!ObjectUtils.isEmpty(purgeTaskDTO.getFileUrls())){
           PurgeTaskContent purgeTaskContent = new PurgeTaskContent();
           purgeTaskContent.setId(UuidUtil.randomUUID());
           purgeTaskContent.setPurgeTaskId(purgeTask.getId());
           purgeTaskContent.setType(FILE);
           purgeTaskContent.setHeaders(headers);
           purgeTaskContent.setUrls(JSON.toJSONString(purgeTaskDTO.getFileUrls()));
           purgeTaskStatistics.setFileEntries(purgeTaskDTO.getFileUrls().size());
           addHost(hostSet, purgeTaskDTO.getFileUrls());
           purgeTaskContentMapper.insert(purgeTaskContent);
       }
       
       if(!ObjectUtils.isEmpty(purgeTaskDTO.getCacheKey())){
           PurgeTaskContent purgeTaskContent = new PurgeTaskContent();
           purgeTaskContent.setId(UuidUtil.randomUUID());
           purgeTaskContent.setPurgeTaskId(purgeTask.getId());
           purgeTaskContent.setType(CACHE_KEY);
           purgeTaskContent.setUrls(JSON.toJSONString(purgeTaskDTO.getCacheKey()));
           purgeTaskStatistics.setCacheKeyEntries(purgeTaskDTO.getCacheKey().size());
           addHost(hostSet, purgeTaskDTO.getCacheKey());
           purgeTaskContentMapper.insert(purgeTaskContent);
       }
       
       String hostNames = "";
       for(String hostname : hostSet){
           hostNames = hostNames + hostname + ",";
       }
       if(!StringUtils.isEmpty(hostNames)){
           hostNames = hostNames.substring(0, hostNames.length()-1);
       }
       purgeTaskStatistics.setHostname(hostNames);
       purgeTaskStatistics.setPurgeTaskId(purgeTask.getId());
       purgeTaskStatistics.setId(UuidUtil.randomUUID());
       purgeTaskStatisticsMapper.insert(purgeTaskStatistics);
       String jsonString = JSONObject.toJSONString(purgeTaskDTO);
       rocketmqProducer.sendMessage(jsonString);
       return  purgeTask;
    }
    
    /**
     * search a PurgeTaskStatus
     * @param taskId
     * @return
     */
    public PurgeStatusDTO getPurgeTaskStatus(String taskId){
        PurgeStatusDTO purgeStatusDTO = new PurgeStatusDTO();
        
        PurgeTask purgeTask = purgeTaskMapper.selectById(taskId);
        if(null == purgeTask){
            throw new NoFoundException(ID_EXCEPTION);
        }
        purgeStatusDTO.setAction(purgeTask.getAction());
        purgeStatusDTO.setName(purgeTask.getName());
        
        purgeStatusDTO.setSubmissionTime(formatDate(purgeTask.getCommitTime()));
        purgeStatusDTO.setFinishTime(formatDate(purgeTask.getFinishTime()));
        
        purgeStatusDTO.setTarget(purgeTask.getTarget());
        purgeStatusDTO.setStatus(purgeTask.getStatus());
        purgeStatusDTO.setCustomerId(purgeTask.getCustomerId());
        purgeStatusDTO.setSuccessRate(countSuccessRate(purgeTask.getSuccessCnt(), purgeTask.getTotalCnt()));
        List<PurgeTaskContent> contents = purgeTaskContentMapper.getPurgeTaskContentByTaskId(taskId);
       
        for(PurgeTaskContent purgeTaskContent : contents){
            List<String> urlList = switchStringToList(purgeTaskContent.getUrls());
            switch (purgeTaskContent.getType()) {
                case  FILE:
                    purgeStatusDTO.setFileUrls(urlList);
                    if(!StringUtils.isEmpty(purgeTaskContent.getHeaders())){
                        List<FileHeaderDTO> list = JSONObject.parseArray(purgeTaskContent.getHeaders(), FileHeaderDTO.class);
                        purgeStatusDTO.setFileHeaders(list);
                    }
                    break;
                case  DIR:
                    purgeStatusDTO.setDirUrls(urlList);
                    break;
                case  CACHE_KEY:
                    purgeStatusDTO.setCacheKey(urlList); 
                    break;
            }
        }
        
        PurgeTaskStatus purgeTaskStatus = purgeTaskStatusMapper.getPurgeTaskStatusByTaskId(taskId);
        if(null != purgeTaskStatus){
            if(!StringUtils.isEmpty(purgeTaskStatus.getReason())){
                List<ErrorDTO> errorList = JSONObject.parseArray(purgeTaskStatus.getReason(), ErrorDTO.class);
                purgeStatusDTO.setErrors(errorList);
            }
        }
        
        return purgeStatusDTO;
    }
    
    /**
     * search a PurgeTaskStatusList
     * @return
     */
    public List<PurgeStatusClassifyDTO> queryPurgeStatus() {
        List<PurgeStatusClassifyDTO> PurgeStatusClassifyList = new ArrayList<PurgeStatusClassifyDTO>();
        List<TaskDTO> stagingTaskList = new ArrayList<TaskDTO>();
        List<TaskDTO> productionTaskList = new ArrayList<TaskDTO>();
        List<PurgeTask> purgeTaskList = purgeTaskMapper.queryPurgeTask();
        for(PurgeTask purgeTask : purgeTaskList){
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setPurgeId(purgeTask.getId());
            taskDTO.setFinishTime(formatDate(purgeTask.getFinishTime()));
            taskDTO.setStatus(purgeTask.getStatus());
            taskDTO.setSuccessRate(countSuccessRate(purgeTask.getSuccessCnt(), purgeTask.getTotalCnt()));
            PurgeTaskStatus purgeTaskStatus = purgeTaskStatusMapper.getPurgeTaskStatusByTaskId(purgeTask.getId());
            List<MessageDTO> messageList = new ArrayList<MessageDTO> ();
            if(null != purgeTaskStatus){
                if(!StringUtils.isEmpty(purgeTaskStatus.getReason())){
                    List<ErrorDTO> errorList = JSONObject.parseArray(purgeTaskStatus.getReason(), ErrorDTO.class);
                    for(ErrorDTO error : errorList){
                        MessageDTO message = new MessageDTO();
                        message.setCode(error.getErrorCode());
                        message.setDetails(error.getErrorMessage());
                        messageList.add(message);
                    }
                }
            }
            taskDTO.setMessage(messageList);
            if(STAGING.equals(purgeTask.getTarget())){
                stagingTaskList.add(taskDTO);
            }
            if(PRODUCTION.equals(purgeTask.getTarget())){
                productionTaskList.add(taskDTO);
            }
        }
        
        if(!ObjectUtils.isEmpty(stagingTaskList)){
            PurgeStatusClassifyDTO purgeStatusClassifyDTO = new PurgeStatusClassifyDTO();
            purgeStatusClassifyDTO.setTarget(STAGING);
            purgeStatusClassifyDTO.setTasks(stagingTaskList);
            PurgeStatusClassifyList.add(purgeStatusClassifyDTO);
        }
        
        if(!ObjectUtils.isEmpty(productionTaskList)){
            PurgeStatusClassifyDTO purgeStatusClassifyDTO = new PurgeStatusClassifyDTO();
            purgeStatusClassifyDTO.setTarget(PRODUCTION);
            purgeStatusClassifyDTO.setTasks(productionTaskList);
            PurgeStatusClassifyList.add(purgeStatusClassifyDTO);
        }
        
        return PurgeStatusClassifyList;
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
    public PurgeRequestEntityDTO getPurgeRequest(String startdate, String enddate, String offsetTpl, String limitTpl, String sortBy, String sortOrder) {
        int offset = offsetToInteger(offsetTpl);
        int limit = limitToInteger(limitTpl);
        PurgeRequestEntityDTO purgeRequestEntityDTO = new PurgeRequestEntityDTO();
        List<PurgeRequestDTO> purgeRequestDTOList = new ArrayList<PurgeRequestDTO>();
        String OrderName = findSort(sortBy);
        String order = findOrder(sortOrder);
        List<PurgeTask> purgeTaskList = purgeTaskMapper.queryPurgeTaskWithCondition(RFCFormatDate(startdate, FROM), RFCFormatDate(enddate, TO), offset, limit, OrderName, order);
        if(!ObjectUtils.isEmpty(purgeTaskList)){
            purgeRequestEntityDTO.setCount(purgeTaskList.size());
            for(PurgeTask purgeTask : purgeTaskList){
                PurgeRequestDTO purgeRequest = new PurgeRequestDTO();
                purgeRequest.setId(purgeTask.getId());
                purgeRequest.setFinishTime(formatDate(purgeTask.getFinishTime()));
                
                purgeRequest.setStatus(purgeTask.getStatus());
                purgeRequest.setSuccessRate(countSuccessRate(purgeTask.getSuccessCnt(), purgeTask.getTotalCnt()));
                purgeRequest.setTarget(purgeTask.getTarget());
                purgeRequest.setSubmissionTime(formatDate(purgeTask.getCommitTime()));
                PurgeTaskStatistics purgeTaskStatistics = purgeTaskStatisticsMapper.selectStatisticsByTaskId(purgeTask.getId());
                if(null != purgeTaskStatistics){
                    purgeRequest.setCacheKeyEntries(purgeTaskStatistics.getCacheKeyEntries());
                    purgeRequest.setDirEntries(purgeTaskStatistics.getDirEntries());
                    purgeRequest.setFileEntries(purgeTaskStatistics.getFileEntries());
                    if(!StringUtils.isEmpty(purgeTaskStatistics.getHostname())){
                        String [] hostnameArray = purgeTaskStatistics.getHostname().split(",");
                        List<String> hostnames = Arrays.asList(hostnameArray);
                        purgeRequest.setHostnames(hostnames);
                    }
                }
                purgeRequestDTOList.add(purgeRequest);
            }
            purgeRequestEntityDTO.setPurgeRequests(purgeRequestDTOList);
        }
       
        return purgeRequestEntityDTO;
    }
    
    /**
     * search Purge Summary
     * @param startdate
     * @param enddate
     * @return
     */
    public PurgeSummaryDTO getPurgeSummary(String startdate, String enddate)  {
        PurgeSummaryDTO purgeSummaryDTO = new PurgeSummaryDTO();
        SubSummaryDTO stagingSummary = new SubSummaryDTO();
        SubSummaryDTO productionSummary = new SubSummaryDTO();
        List<PurgeTask> purgeTaskList = purgeTaskMapper.queryPurgeTaskWithCondition(RFCFormatDate(startdate, FROM), RFCFormatDate(enddate, TO), 0, 100, COMMIT_TIME, ASC);
        purgeSummaryDTO.setRequests(purgeTaskList.size());
        for(PurgeTask purgeTask : purgeTaskList){
            PurgeTaskStatistics purgeTaskStatistics = purgeTaskStatisticsMapper.selectStatisticsByTaskId(purgeTask.getId());
           if(null != purgeTaskStatistics){
                if(STAGING.equals(purgeTask.getTarget())){
                    stagingSummary = countSubRequest(stagingSummary, purgeTask, purgeTaskStatistics);
                }else{
                    productionSummary = countSubRequest(productionSummary, purgeTask, purgeTaskStatistics);
                }
                countRequest(purgeSummaryDTO, purgeTask, purgeTaskStatistics);
            }
        }
        purgeSummaryDTO.setStaging(stagingSummary);
        purgeSummaryDTO.setProduction(productionSummary);
        return purgeSummaryDTO;
    }
    
    
    public List<PurgeTaskPortalDTO> queryPurgeTaskForPortal(PurgeTaskQueryDTO queryDTO, Page<PurgeTaskPortalDTO> page){
        List<PurgeTaskPortalDTO> purgeTaskPortalDTOs = new ArrayList<PurgeTaskPortalDTO> ();
        List<PurgeTask> purgeTasks = purgeTaskMapper.queryPurgeTaskForPortal(queryDTO, page);
        for(PurgeTask purgeTask : purgeTasks){
            PurgeTaskPortalDTO purgeTaskPortalDTO = new PurgeTaskPortalDTO();
            BeanCopyUtil.beanCopy(purgeTask, purgeTaskPortalDTO);
            double successRate = countSuccessRate(purgeTask.getSuccessCnt(), purgeTask.getTotalCnt());
            purgeTaskPortalDTO.setSuccessRate(successRate);
            purgeTaskPortalDTOs.add(purgeTaskPortalDTO);
        }
        return purgeTaskPortalDTOs;
    }
    
    /**
     * add host name to list
     * @param hostnameSet
     * @param urls
     * @return
     */
    private Set<String> addHost(Set<String> hostnameSet, List<String> urls){
        for(String url : urls){
            String host = "";
            if(url.contains("//") && url.length() >=2 ){
                url = url.substring(url.indexOf("//") + 2);
                host = url.substring(0, url.indexOf("/")); 
            }else{
                host = url; 
            }
            hostnameSet.add(host);
        }
        return hostnameSet;
    }
    
    private void countRequest(PurgeSummaryDTO purgeSummaryDTO, PurgeTask purgeTask, PurgeTaskStatistics purgeTaskStatistics){
        if(purgeTaskStatistics.getDirEntries() != 0){
            purgeSummaryDTO.addDirRequests(1);
            purgeSummaryDTO.addDirEntries(purgeTaskStatistics.getDirEntries());
            
        }
        if(purgeTaskStatistics.getFileEntries() != 0){
            purgeSummaryDTO.addFileRequests(1);
            purgeSummaryDTO.addFileEntries(purgeTaskStatistics.getFileEntries());
        }
    }
    
    private SubSummaryDTO countSubRequest(SubSummaryDTO subSummaryDTO, PurgeTask purgeTask, PurgeTaskStatistics purgeTaskStatistics){
        if(purgeTaskStatistics.getDirEntries() != 0){
            subSummaryDTO.addDirRequests(1);
            subSummaryDTO.addDirEntries(purgeTaskStatistics.getDirEntries());
            
        }
        if(purgeTaskStatistics.getFileEntries() != 0){
            subSummaryDTO.addFileRequests(1);
            subSummaryDTO.addFileEntries(purgeTaskStatistics.getFileEntries());
        }
        subSummaryDTO.addRequests(1);
        return subSummaryDTO;
    }
    
    
    /**
     * switch json string to list
     * @param content
     * @return
     */
    private List<String> switchStringToList(String content){
        List<String> list = JSONObject.parseArray(content, String.class);
        return list;
    }
    
    /**
     * calculate success rate
     * @param successCnt
     * @param totalCnt
     * @return
     */
    private double countSuccessRate(int successCnt, int totalCnt){
        double successRate = 0.0;
        if(totalCnt != 0){
            successRate = (successCnt * 100.0) / (totalCnt * 1.0);
        }
        BigDecimal doubleNum = new BigDecimal(successRate);
        double resNum = doubleNum.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        return resNum;
    }
    
    
    /**
     * return yyyy-MM-dd'T'HH:mm:ssZ formate date
     * @param time
     * @return
     */
    private String formatDate (Date time){
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_RFC); 
        String res = "";
        if(!StringUtils.isEmpty(time)){
            res = format.format(time);
        }
        return res;   
    }
    
    /**
     * return yyyy-MM-dd'T'HH:mm:ssZ formate date
     * @param time
     * @return
     */
    private String RFCFormatDate(String time, String dateType){
        String res = "";
        try {
            if(!StringUtils.isEmpty(time)){
                DateTime dateTime  = new DateTime(time);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
                res = simpleDateFormat.format(dateTime.toDate());
            }
          }
          catch(Exception e) {
              if(FROM.equals(dateType)){
                  throw new BadRequestException(FROM_EXCEPTION); 
              }else{
                  throw new BadRequestException(TO_EXCEPTION); 
              }
              
          }
        return res;
        
    }
    
    private int offsetToInteger(String offsetTpl){
        int  offset = 0;
        if(!StringUtils.isEmpty(offsetTpl)){
            try{
                offset = Integer.parseInt(offsetTpl);  
            }catch(Exception e){
                throw new BadRequestException(OFFSET_EXCEPTION); 
            }
            
        }
        if(offset < 0){
            throw new BadRequestException(OFFSET_EXCEPTION);
        }
        return offset;
    }
    
    private int limitToInteger(String limitTpl){
        int  limit = 100;
        if(!StringUtils.isEmpty(limitTpl)){
            try{
                limit = Integer.parseInt(limitTpl);  
            }catch(Exception e){
                throw new BadRequestException(LIMIT_EXCEPTION); 
            }
            
        }
        if(limit < 1 || limit >200){
            throw new BadRequestException(LIMIT_EXCEPTION);
        }
        return limit;
    }
    
    private String findOrder(String orderBy){
        if(StringUtils.isEmpty(orderBy)){
            return ASC;
        }
        if(ASC.equals(orderBy) || DESC.equals(orderBy)){
            return orderBy;
        }else{
            throw new BadRequestException(ORDER_EXCEPTION);
        }
    }
    
    private String findSort(String sortName){
        if(StringUtils.isEmpty(sortName)){
            return COMMIT_TIME;
        }
        String OrderName = ORDERMAP.get(sortName);
        if(!StringUtils.isEmpty(OrderName)){
            return OrderName;
        }else{
            throw new BadRequestException(SORT_EXCEPTION);
        }
    }
    
    
}
