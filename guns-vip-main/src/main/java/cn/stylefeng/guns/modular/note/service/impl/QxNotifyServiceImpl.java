package cn.stylefeng.guns.modular.note.service.impl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.note.entity.QxNotify;
import cn.stylefeng.guns.modular.note.mapper.QxNotifyMapper;
import cn.stylefeng.guns.modular.note.model.params.QxNotifyParam;
import cn.stylefeng.guns.modular.note.model.result.QxNotifyResult;
import  cn.stylefeng.guns.modular.note.service.QxNotifyService;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2020-02-28
 */
@Service
public class QxNotifyServiceImpl extends ServiceImpl<QxNotifyMapper, QxNotify> implements QxNotifyService {

    @Override
    public void add(QxNotifyParam param){
        QxNotify entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QxNotifyParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QxNotifyParam param){
        QxNotify oldEntity = getOldEntity(param);
        QxNotify newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QxNotifyResult findBySpec(QxNotifyParam param){
        return null;
    }

    @Override
    public List<QxNotifyResult> findListBySpec(QxNotifyParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(QxNotifyParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(QxNotifyParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private QxNotify getOldEntity(QxNotifyParam param) {
        return this.getById(getKey(param));
    }

    private QxNotify getEntity(QxNotifyParam param) {
        QxNotify entity = new QxNotify();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

}
