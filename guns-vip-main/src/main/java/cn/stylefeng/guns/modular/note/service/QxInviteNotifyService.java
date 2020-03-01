package cn.stylefeng.guns.modular.note.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.note.entity.QxInviteNotify;
import cn.stylefeng.guns.modular.note.model.params.QxInviteNotifyParam;
import cn.stylefeng.guns.modular.note.model.result.QxInviteNotifyResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 拼单提醒 服务类
 * </p>
 *
 * @author 
 * @since 2020-03-01
 */
public interface QxInviteNotifyService extends IService<QxInviteNotify> {

    /**
     * 新增
     *
     * @author 
     * @Date 2020-03-01
     */
    void add(QxInviteNotifyParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2020-03-01
     */
    void delete(QxInviteNotifyParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2020-03-01
     */
    void update(QxInviteNotifyParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2020-03-01
     */
    QxInviteNotifyResult findBySpec(QxInviteNotifyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2020-03-01
     */
    List<QxInviteNotifyResult> findListBySpec(QxInviteNotifyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2020-03-01
     */
     LayuiPageInfo findPageBySpec(QxInviteNotifyParam param);

}
