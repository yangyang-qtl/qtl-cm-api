package com.quantil.cm.api.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.quantil.cm.api.domain.PurgeTaskStatus;

public interface PurgeTaskStatusMapper extends BaseMapper<PurgeTaskStatus>{
   public PurgeTaskStatus getPurgeTaskStatusByTaskId (@Param("taskId") String taskId);
}
