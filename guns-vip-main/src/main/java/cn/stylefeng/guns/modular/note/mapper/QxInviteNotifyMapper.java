package cn.stylefeng.guns.modular.note.mapper;

import cn.stylefeng.guns.modular.note.entity.QxInviteNotify;
import cn.stylefeng.guns.modular.note.model.params.QxInviteNotifyParam;
import cn.stylefeng.guns.modular.note.model.result.QxInviteNotifyResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 拼单提醒 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2020-03-01
 */
public interface QxInviteNotifyMapper extends BaseMapper<QxInviteNotify> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2020-03-01
     */
    List<QxInviteNotifyResult> customList(@Param("paramCondition") QxInviteNotifyParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2020-03-01
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QxInviteNotifyParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2020-03-01
     */
    Page<QxInviteNotifyResult> customPageList(@Param("page") Page page, @Param("paramCondition") QxInviteNotifyParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2020-03-01
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QxInviteNotifyParam paramCondition);

}
