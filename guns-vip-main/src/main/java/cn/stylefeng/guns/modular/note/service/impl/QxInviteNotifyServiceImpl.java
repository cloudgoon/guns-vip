package cn.stylefeng.guns.modular.note.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.note.entity.QxInviteNotify;
import cn.stylefeng.guns.modular.note.mapper.QxInviteNotifyMapper;
import cn.stylefeng.guns.modular.note.model.params.QxInviteNotifyParam;
import cn.stylefeng.guns.modular.note.model.result.QxInviteNotifyResult;
import  cn.stylefeng.guns.modular.note.service.QxInviteNotifyService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 拼单提醒 服务实现类
 * </p>
 *
 * @author 
 * @since 2020-03-01
 */
@Service
public class QxInviteNotifyServiceImpl extends ServiceImpl<QxInviteNotifyMapper, QxInviteNotify> implements QxInviteNotifyService {

    @Override
    public void add(QxInviteNotifyParam param){
        QxInviteNotify entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QxInviteNotifyParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QxInviteNotifyParam param){
        QxInviteNotify oldEntity = getOldEntity(param);
        QxInviteNotify newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QxInviteNotifyResult findBySpec(QxInviteNotifyParam param){
        return null;
    }

    @Override
    public List<QxInviteNotifyResult> findListBySpec(QxInviteNotifyParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(QxInviteNotifyParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(QxInviteNotifyParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private QxInviteNotify getOldEntity(QxInviteNotifyParam param) {
        return this.getById(getKey(param));
    }

    private QxInviteNotify getEntity(QxInviteNotifyParam param) {
        QxInviteNotify entity = new QxInviteNotify();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
