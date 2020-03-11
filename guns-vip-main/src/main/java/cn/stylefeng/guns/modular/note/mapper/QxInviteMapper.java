package cn.stylefeng.guns.modular.note.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.stylefeng.guns.modular.note.entity.QxInvite;
import cn.stylefeng.guns.modular.note.model.params.QxInviteParam;
import cn.stylefeng.guns.modular.note.model.result.QxInviteResult;
import cn.stylefeng.guns.modular.note.pojo.QxInviteSearchPojo;
import cn.stylefeng.guns.modular.note.pojo.QxInviteUserPojo;

/**
 * <p>
 * 约单表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2019-11-18
 */
public interface QxInviteMapper extends BaseMapper<QxInvite> {

    /**
     * 获取列表
     *
     * @author 
     * @Date 2019-11-18
     */
    List<QxInviteResult> customList(@Param("paramCondition") QxInviteParam paramCondition);

    /**
     * 获取map列表
     *
     * @author 
     * @Date 2019-11-18
     */
    List<Map<String, Object>> customMapList(@Param("paramCondition") QxInviteParam paramCondition);

    /**
     * 获取分页实体列表
     *
     * @author 
     * @Date 2019-11-18
     */
    Page<QxInviteResult> customPageList(@Param("page") Page page, @Param("paramCondition") QxInviteParam paramCondition);

    /**
     * 获取分页map列表
     *
     * @author 
     * @Date 2019-11-18
     */
    Page<Map<String, Object>> customPageMapList(@Param("page") Page page, @Param("paramCondition") QxInviteParam paramCondition);
    
    /**
     * 获取约单所有用户信息
     * @param inviteId
     * @return
     */
    List<QxInviteUserPojo> getInviteUsers(@Param("inviteId") Long inviteId);

    @Select("SELECT a.* FROM qx_invite a WHERE a.status = 2 and (a.inviter = #{userId} or a.invitee = #{userId}) order by created_time desc")
    @ResultMap("BaseResultMap")
	List<QxInvite> getCurrentInvites(@Param("page") Page page, @Param("userId") Long userId);

    /**
     * 根据条件搜索附近约单
     */
	Page<List<QxInviteSearchPojo>> search(@Param("page") Page page, @Param("paramCondition") QxInviteParam paramCondition);

	/**
	 * 获取约单详情
	 * @param id
	 * @return
	 */
	QxInviteSearchPojo getInviteById(@Param("inviteId") Long id);

	/**
	 * 我报名的约单
	 * @param page
	 * @param requestUserId
	 * @return
	 */
    @Select("select a.* from qx_invite a inner join qx_invite_apply b on a.id = b.invite_id and b.status != '2'and b.user_id = #{requestUserId} order by b.created_time desc")
    @ResultMap("BaseResultMap")
	Page myApply(@Param("page")Page page, @Param("requestUserId")Long requestUserId);

    /**
     * 获取准备开始的约单
     * @param beforeTime
     * @return
     */
    @Select("SELECT a.* FROM qx_invite a WHERE a.status = 1 AND ADDTIME( now(), #{beforeTime} ) > a.invite_time AND NOW() < a.invite_time AND a.id NOT IN ( SELECT invite_id FROM qx_invite_notify b WHERE ADDTIME( b.created_time, '12:00:00' ) > NOW());")
    @ResultMap("BaseResultMap")
	List<QxInvite> getPrepareInviteList(@Param("beforeTime") String beforeTime);
}
