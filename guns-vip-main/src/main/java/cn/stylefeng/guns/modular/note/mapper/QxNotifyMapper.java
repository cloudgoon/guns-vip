package cn.stylefeng.guns.modular.note.mapper;

import cn.stylefeng.guns.modular.note.entity.QxNotify;
import cn.stylefeng.guns.modular.note.model.params.QxNotifyParam;
import cn.stylefeng.guns.modular.note.model.result.QxNotifyResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-02-28
 */
public interface QxNotifyMapper extends BaseMapper<QxNotify> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2020-02-28
     */
    List<QxNotifyResult> customList(@Param("paramCondition") QxNotifyParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2020-02-28
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QxNotifyParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2020-02-28
     */
    Page<QxNotifyResult> customPageList(@Param("page") Page page, @Param("paramCondition") QxNotifyParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2020-02-28
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QxNotifyParam paramCondition);

}
