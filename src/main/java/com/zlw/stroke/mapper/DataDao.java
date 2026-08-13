package com.zlw.stroke.mapper;

import com.zlw.stroke.pojo.Data;
import com.zlw.stroke.pojo.DataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataDao {

    int insert(Data record);

    int insertSelective(Data record);

    List<Data> selectByExample(DataExample example);

    int updateByExampleSelective(@Param("record") Data record, @Param("example") DataExample example);

    int updateByExample(@Param("record") Data record, @Param("example") DataExample example);
}