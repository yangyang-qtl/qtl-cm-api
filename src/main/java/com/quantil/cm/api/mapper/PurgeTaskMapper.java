package com.quantil.cm.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.quantil.cm.api.domain.PurgeTask;
import com.quantil.cm.api.dto.PurgeTaskQueryDTO;

public interface PurgeTaskMapper extends BaseMapper<PurgeTask>{
    public List<PurgeTask> queryPurgeTask();
    
    public List<PurgeTask> queryPurgeTaskWithCondition(@Param(value="startdate") String startdate, @Param(value="enddate") String enddate, @Param(value="offset") int offset, @Param(value="limit") int limit, @Param(value="sortBy") String sortBy, @Param(value="sortOrder") String sortOrder);
    
    public List<PurgeTask> queryPurgeTaskForPortal(PurgeTaskQueryDTO queryDTO,  Page<?> page);
}
