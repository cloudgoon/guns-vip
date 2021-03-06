package cn.stylefeng.guns.modular.note.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.core.constant.ProjectConstants.USER_PAY_LOG_TYPE;
import cn.stylefeng.guns.core.constant.ProjectConstants.USER_PRODUCT_STATUS;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.modular.note.entity.QxProduct;
import cn.stylefeng.guns.modular.note.entity.QxUser;
import cn.stylefeng.guns.modular.note.entity.QxUserProduct;
import cn.stylefeng.guns.modular.note.mapper.QxProductMapper;
import cn.stylefeng.guns.modular.note.mapper.QxUserMapper;
import cn.stylefeng.guns.modular.note.mapper.QxUserProductMapper;
import cn.stylefeng.guns.modular.note.model.params.QxProductParam;
import cn.stylefeng.guns.modular.note.model.result.QxProductResult;
import  cn.stylefeng.guns.modular.note.service.QxProductService;
import cn.stylefeng.roses.core.util.ToolUtil;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author 
 * @since 2019-11-18
 */
@Service
public class QxProductServiceImpl extends ServiceImpl<QxProductMapper, QxProduct> implements QxProductService {

	@Resource
	private QxUserMapper qxUserMapper;
	
	@Resource
	private QxPayLogHelper qxPayLogHelper;
	
	@Resource
	private QxUserProductMapper qxUserProductMapper;
	
    @Override
    public void add(QxProductParam param){
        QxProduct entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QxProductParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QxProductParam param){
        QxProduct oldEntity = getOldEntity(param);
        QxProduct newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QxProductResult findBySpec(QxProductParam param){
        return null;
    }

    @Override
    public List<QxProductResult> findListBySpec(QxProductParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(QxProductParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(QxProductParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private QxProduct getOldEntity(QxProductParam param) {
        return this.getById(getKey(param));
    }

    private QxProduct getEntity(QxProductParam param) {
        QxProduct entity = new QxProduct();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

	@Override
	public void exchange(Long userId, Long productId, Long addressId) {
		QxProduct product = this.baseMapper.selectById(productId);
		QxUser user = qxUserMapper.selectById(userId);
		if (product.getStock() <= 0) {
			throw new ServiceException("商品库存不足，无法兑换");
		}
		updateChange(user, product.getPrice());
		// 商品库存更新
		product.setStock(product.getStock()-1);
		this.updateById(product);
		// 流水
		qxPayLogHelper.createPayLog(userId, product.getPrice(), USER_PAY_LOG_TYPE.BUY_PRODUCT_OUT);
		// 创建用户申请单
		saveUserProduct(userId, productId, addressId);
	}
	
	public void updateChange(QxUser user, Integer price) {
		if (user.getBalance() < price) {
			throw new ServiceException("金币不足，无法兑换");
		}
//		Integer finalFreeze = user.getFreeze() - price;
//		if (finalFreeze > 0) {
//			user.setFreeze(finalFreeze);
//		} else {
//			user.setFreeze(0);
//			user.setBalance(user.getBalance()+finalFreeze);
//		}
		user.setBalance(user.getBalance() - price);
		qxUserMapper.updateById(user);
	}
	
	public void saveUserProduct(Long userId, Long productId, Long addressId) {
		QxUserProduct userProduct = new QxUserProduct();
		userProduct.setProductId(productId);
		userProduct.setUserId(userId);
		userProduct.setAddressId(addressId);
		userProduct.setStatus(USER_PRODUCT_STATUS.UN_HANDLE);
		qxUserProductMapper.insert(userProduct);
	}

	@Override
	public Page myExchange(Page page, Long requestUserId) {
		return this.baseMapper.myExchange(page, requestUserId);
	}
}
