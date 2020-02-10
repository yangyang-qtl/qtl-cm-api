package com.quantil.cm.api.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.quantil.cm.api.domain.PurgeTaskContent;

public interface PurgeTaskContentMapper extends BaseMapper<PurgeTaskContent>{
    public List<PurgeTaskContent> getPurgeTaskContentByTaskId(@Param("purgeTaskId") String purgeTaskId);
}
