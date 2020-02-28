package cn.stylefeng.guns.modular.note.service;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.note.entity.QxNotify;
import cn.stylefeng.guns.modular.note.model.params.QxNotifyParam;
import cn.stylefeng.guns.modular.note.model.result.QxNotifyResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2020-02-28
 */
public interface QxNotifyService extends IService<QxNotify> {

    /**
     * 新增
     *
     * @author 
     * @Date 2020-02-28
     */
    void add(QxNotifyParam param);

    /**
     * 删除
     *
     * @author 
     * @Date 2020-02-28
     */
    void delete(QxNotifyParam param);

    /**
     * 更新
     *
     * @author 
     * @Date 2020-02-28
     */
    void update(QxNotifyParam param);

    /**
     * 查询单条数据，Specification模式
     *
     * @author 
     * @Date 2020-02-28
     */
    QxNotifyResult findBySpec(QxNotifyParam param);

    /**
     * 查询列表，Specification模式
     *
     * @author 
     * @Date 2020-02-28
     */
    List<QxNotifyResult> findListBySpec(QxNotifyParam param);

    /**
     * 查询分页数据，Specification模式
     *
     * @author 
     * @Date 2020-02-28
     */
     LayuiPageInfo findPageBySpec(QxNotifyParam param);

}
