package com.quantil.cm.api.mapper;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.quantil.cm.api.domain.PurgeTaskStatistics;

public interface PurgeTaskStatisticsMapper extends BaseMapper<PurgeTaskStatistics>{
    public PurgeTaskStatistics selectStatisticsByTaskId(@Param("purgeTaskId") String purgeTaskId);
}
