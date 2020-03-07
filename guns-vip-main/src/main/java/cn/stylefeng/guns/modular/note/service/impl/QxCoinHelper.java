package cn.stylefeng.guns.modular.note.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import cn.stylefeng.guns.config.ConfigEntity;
import cn.stylefeng.guns.core.CommonUtils;
import cn.stylefeng.guns.core.constant.ProjectConstants.COST_RATE_TYPE;
import cn.stylefeng.guns.core.constant.ProjectConstants.USER_PAY_LOG_TYPE;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.modular.note.dto.QxPayResult;
import cn.stylefeng.guns.modular.note.entity.QxCostRate;
import cn.stylefeng.guns.modular.note.entity.QxGift;
import cn.stylefeng.guns.modular.note.entity.QxUser;
import cn.stylefeng.guns.modular.note.mapper.QxCostRateMapper;
import cn.stylefeng.guns.modular.note.mapper.QxGiftMapper;
import cn.stylefeng.guns.modular.note.mapper.QxUserMapper;

@Component
public class QxCoinHelper {

	@Resource
	private ConfigEntity configEntity;
	
	@Resource
	private QxGiftMapper qxGiftMapper;

	@Resource
	private QxUserMapper qxUserMapper;

	@Resource
	private QxCostRateMapper qxCostRateMapper;

	@Resource
	private QxPayLogHelper qxPayLogHelper;

	public QxPayResult payCoin(Long payerId, Long payeeId, Long giftId, boolean payInvite) {
		// 检查用户金币是否足够打赏
		QxGift gift = qxGiftMapper.selectById(giftId);
		QxUser payUser = qxUserMapper.selectById(payerId);
		Integer giftPrice = gift.getPrice();
		QxUser payeeUser = qxUserMapper.selectById(payeeId);

		if (payInvite) { // 如果是支付约单，则从冻结金额中扣除
			if (payUser.getFreeze() < gift.getPrice()) {
				throw new ServiceException("金币余额不足，请先充值");
			}
			// 金币转账到对方账户
			payUser.setFreeze(payUser.getFreeze() - giftPrice);
			payeeUser.setBalance(payeeUser.getBalance() + giftPrice);
		} else { // 否则打赏，送礼物从余额中扣除
			if (payUser.getBalance() < gift.getPrice()) {
				throw new ServiceException("金币余额不足，请先充值");
			}
			// 金币转账到对方账户
			payUser.setBalance(payUser.getBalance() - giftPrice);
			payeeUser.setBalance(payeeUser.getBalance() + giftPrice);
		}

		qxUserMapper.updateById(payUser);
		qxUserMapper.updateById(payeeUser);

		QxPayResult payResult = new QxPayResult();
		payResult.setSn(CommonUtils.getSerialNumber());
		payResult.setPayerId(payerId);
		payResult.setPayeeId(payeeId);
		payResult.setPrice(giftPrice);
		return payResult;
	}
	
	/**
	 * 支付违约金
	 * @param payerId
	 * @param payeeId
	 * @param coin
	 * @return
	 */
	public QxPayResult payPunishCoin(Long payerId, Long payeeId, Integer coin) {
		QxUser payUser = qxUserMapper.selectById(payerId);
		QxUser payeeUser = qxUserMapper.selectById(payeeId);
		if (payUser.getFreeze() < coin) {
			throw new ServiceException("冻结金币余额不足");
		}
		// 金币转账到对方账户
		payUser.setFreeze(payUser.getFreeze() - coin);
		payeeUser.setBalance(payeeUser.getBalance() + coin);
		qxUserMapper.updateById(payUser);
		qxUserMapper.updateById(payeeUser);
		// 记录日志
		qxPayLogHelper.createPayLog(payerId, coin, USER_PAY_LOG_TYPE.COMPENSATION_OUT);
		qxPayLogHelper.createPayLog(payeeId, coin, USER_PAY_LOG_TYPE.COMPENSATION_IN);

		QxPayResult payResult = new QxPayResult();
		payResult.setSn(CommonUtils.getSerialNumber());
		payResult.setPayerId(payerId);
		payResult.setPayeeId(payeeId);
		payResult.setPrice(coin);
		return payResult;
	}

	/**
	 * 货币和现金转换
	 * 
	 * @param coinCount
	 * @return
	 */
	public BigDecimal caculateWithdrawAmount(int coinCount) {
		BigDecimal coinRate = getRateByType(COST_RATE_TYPE.COIN_RATE);
		BigDecimal realAmount = new BigDecimal(coinCount).multiply(coinRate); // 金币兑换成现金
		if (realAmount.compareTo(BigDecimal.ONE) < 0) {
			throw new ServiceException("提现金额必须大于1元");
		}
		// 只能提取整数
		return realAmount.setScale(0, BigDecimal.ROUND_DOWN);
	}
	
	/**
	 * 获取最终提现金额
	 * @param amount
	 * @return
	 */
	public BigDecimal getWithdrawAmount(BigDecimal amount) {
		BigDecimal withdrawRate = getRateByType(COST_RATE_TYPE.WITHDRAW_RATE);
		BigDecimal withdrawAmount = amount.multiply(BigDecimal.ONE.subtract(withdrawRate));
		return withdrawAmount;
	}

	BigDecimal getRateByType(String type) {
		QueryWrapper<QxCostRate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("type", type);
		QxCostRate costRate = qxCostRateMapper.selectOne(queryWrapper);
		return costRate.getRate();
	}

	/**
	 * 冻结礼物对应金币
	 * 
	 * @param user
	 * @param amount
	 * @param type
	 */
	public void freeze(Long userId, Long giftId, String type) {
		QxGift gift = qxGiftMapper.selectById(giftId);
		freezeCoin(userId, gift.getPrice(), type);
	}

	/**
	 * 解冻礼物金币
	 */
	public void unfreeze(Long userId, Long giftId, String type) {
		QxGift gift = qxGiftMapper.selectById(giftId);
		unfreezeCoin(userId, gift.getPrice(), type);
	}
	
	/**
	 * 冻结金币
	 * @param userId
	 * @param coinCount
	 */
	public void freezeCoin(Long userId, Integer coinCount, String type) {
		QxUser user = qxUserMapper.selectById(userId);
		if (user.getBalance() < coinCount) {
			throw new ServiceException("金币不足，请充值");
		}
		user.setBalance(user.getBalance() - coinCount);
		user.setFreeze(user.getFreeze() + coinCount);
		qxUserMapper.updateById(user);
		qxPayLogHelper.createPayLog(userId, coinCount, type);
	}
	
	/**
	 * 解冻金币
	 */
	public void unfreezeCoin(Long userId, Integer coinCount, String type) {
		QxUser user = qxUserMapper.selectById(userId);
		if (user.getFreeze() < coinCount) {
			throw new ServiceException("冻结金币不足，无法解冻");
		}
		user.setBalance(user.getBalance() + coinCount);
		user.setFreeze(user.getFreeze() - coinCount);
		qxUserMapper.updateById(user);
		qxPayLogHelper.createPayLog(userId, coinCount, type);
	}
	
	/**
	 * 获取违约金金币
	 * @param giftId
	 * @return
	 */
	public int getPunishCoin(Long giftId) {
		QxGift gift = qxGiftMapper.selectById(giftId);
		int punishCoin =  (int)Math.floor(gift.getPrice() * configEntity.getPunishmentRate());
		return punishCoin;
	}
}
