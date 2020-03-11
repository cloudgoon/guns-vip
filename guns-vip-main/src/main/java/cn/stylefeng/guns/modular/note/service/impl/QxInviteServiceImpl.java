package cn.stylefeng.guns.modular.note.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.config.ConfigEntity;
import cn.stylefeng.guns.core.CommonUtils;
import cn.stylefeng.guns.core.DateUtils;
import cn.stylefeng.guns.core.constant.ProjectConstants;
import cn.stylefeng.guns.core.constant.ProjectConstants.ALERT_STATUS;
import cn.stylefeng.guns.core.constant.ProjectConstants.INVITE_APPLY_RESULT;
import cn.stylefeng.guns.core.constant.ProjectConstants.INVITE_APPLY_STATUS;
import cn.stylefeng.guns.core.constant.ProjectConstants.INVITE_OPERATE_TYPE;
import cn.stylefeng.guns.core.constant.ProjectConstants.INVITE_STATUS;
import cn.stylefeng.guns.core.constant.ProjectConstants.INVITE_TYPE;
import cn.stylefeng.guns.core.constant.ProjectConstants.NOTIFICATION_TYPE;
import cn.stylefeng.guns.core.constant.ProjectConstants.PUNISH_REASON;
import cn.stylefeng.guns.core.constant.ProjectConstants.SMS_CODE;
import cn.stylefeng.guns.core.constant.ProjectConstants.USER_PAY_LOG_TYPE;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.core.util.NoticeHelper;
import cn.stylefeng.guns.modular.note.dto.QxInviteCommentTo;
import cn.stylefeng.guns.modular.note.dto.QxInviteQueryTo;
import cn.stylefeng.guns.modular.note.dto.QxInviteTo;
import cn.stylefeng.guns.modular.note.dto.QxPayResult;
import cn.stylefeng.guns.modular.note.entity.QxAlert;
import cn.stylefeng.guns.modular.note.entity.QxComplaint;
import cn.stylefeng.guns.modular.note.entity.QxGift;
import cn.stylefeng.guns.modular.note.entity.QxInvite;
import cn.stylefeng.guns.modular.note.entity.QxInviteApply;
import cn.stylefeng.guns.modular.note.entity.QxInviteComment;
import cn.stylefeng.guns.modular.note.entity.QxInviteNotify;
import cn.stylefeng.guns.modular.note.entity.QxInviteOperate;
import cn.stylefeng.guns.modular.note.entity.QxUser;
import cn.stylefeng.guns.modular.note.mapper.QxGiftMapper;
import cn.stylefeng.guns.modular.note.mapper.QxInviteApplyMapper;
import cn.stylefeng.guns.modular.note.mapper.QxInviteMapper;
import cn.stylefeng.guns.modular.note.mapper.QxInviteNotifyMapper;
import cn.stylefeng.guns.modular.note.mapper.QxInviteOperateMapper;
import cn.stylefeng.guns.modular.note.mapper.QxUserMapper;
import cn.stylefeng.guns.modular.note.model.params.QxInviteParam;
import cn.stylefeng.guns.modular.note.model.result.QxInviteResult;
import cn.stylefeng.guns.modular.note.pojo.QxInviteSearchPojo;
import cn.stylefeng.guns.modular.note.pojo.QxInviteUserPojo;
import cn.stylefeng.guns.modular.note.service.QxAlertService;
import cn.stylefeng.guns.modular.note.service.QxComplaintService;
import cn.stylefeng.guns.modular.note.service.QxInviteCommentService;
import cn.stylefeng.guns.modular.note.service.QxInviteService;
import cn.stylefeng.roses.core.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 约单表 服务实现类
 * </p>
 *
 * @author
 * @since 2019-11-18
 */
@Slf4j
@Service
public class QxInviteServiceImpl extends ServiceImpl<QxInviteMapper, QxInvite> implements QxInviteService {

	@Resource
	private ConfigEntity configEntity;

	@Resource
	private QxUserMapper qxUserMapper;

	@Resource
	private QxInviteMapper qxInviteMapper;

	@Resource
	private QxInviteApplyMapper qxInviteApplyMapper;

	@Resource
	private QxInviteOperateMapper qxInviteOperateMapper;

	@Resource
	private QxComplaintService qxComplaintService;

	@Resource
	private QxInviteCommentService qxInviteCommentSerivce;
	
	@Resource
	private QxInviteNotifyMapper qxInviteNotifyMapper;

	@Resource
	private QxAlertService qxAlertService;
	
	@Resource
	private QxGiftMapper qxGiftMapper;

	@Resource
	private NoticeHelper noticeHelper;

	@Resource
	private QxCoinHelper qxCoinHelper;

	@Resource
	private QxPayLogHelper qxPayLogHelper;

	@Override
	public void add(QxInviteParam param) {
		QxInvite entity = getEntity(param);
		this.save(entity);
	}

	@Override
	public void delete(QxInviteParam param) {
		QxInvite entity = getEntity(param);
		entity.setDeleted(true);
		this.updateById(entity);
	}

	@Override
	public void update(QxInviteParam param) {
		QxInvite oldEntity = getOldEntity(param);
		QxInvite newEntity = getEntity(param);
		ToolUtil.copyProperties(newEntity, oldEntity);
		this.updateById(newEntity);
	}

	@Override
	public QxInviteResult findBySpec(QxInviteParam param) {
		return null;
	}

	@Override
	public List<QxInviteResult> findListBySpec(QxInviteParam param) {
		return null;
	}

	@Override
	public LayuiPageInfo findPageBySpec(QxInviteParam param) {
		Page pageContext = getPageContext();
		IPage page = this.baseMapper.customPageList(pageContext, param);
		return LayuiPageFactory.createPageInfo(page);
	}

	private Serializable getKey(QxInviteParam param) {
		return param.getId();
	}

	private Page getPageContext() {
		return LayuiPageFactory.defaultPage();
	}

	private QxInvite getOldEntity(QxInviteParam param) {
		return this.getById(getKey(param));
	}

	private QxInvite getEntity(QxInviteParam param) {
		QxInvite entity = new QxInvite();
		ToolUtil.copyProperties(param, entity);
		return entity;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addInvite(Long requestUserId, QxInviteTo inviteTo) {
		QxInvite invite = new QxInvite();
		BeanUtils.copyProperties(inviteTo, invite);
		invite.setInviter(requestUserId);
		invite.setSn(CommonUtils.getSerialNumber());
		invite.setStatus(INVITE_STATUS.WAIT_MATCH);
		this.baseMapper.insert(invite);
		// 如果是我请客，则冻结我的礼物金币
		if (INVITE_TYPE.ACTIVE.equals(inviteTo.getInviteType())) {
			qxCoinHelper.freeze(requestUserId, invite.getGiftId(), USER_PAY_LOG_TYPE.INVITE_OUT);
		}
		// 如果是TA请客，则冻结我的违约金
		else if (INVITE_TYPE.PASSIVE.equals(inviteTo.getInviteType())) {
			int punishCoin = qxCoinHelper.getPunishCoin(invite.getGiftId());
			qxCoinHelper.freezeCoin(requestUserId, punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT);
		}
		else {
			throw new ServiceException("不支持的约单类型");
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void apply(Long currentUserId, Long inviteId) {
		QxInvite invite = this.baseMapper.selectById(inviteId);
		// 如果是TA请客，则需冻结报名人的金币
		if (INVITE_TYPE.PASSIVE.equals(invite.getInviteType())) {
			qxCoinHelper.freeze(currentUserId, invite.getGiftId(), USER_PAY_LOG_TYPE.INVITE_OUT);
		}
		// 如果是我请客，则需冻结报名人违约金
		else if (INVITE_TYPE.ACTIVE.equals(invite.getInviteType())) {
			int punishCoin = qxCoinHelper.getPunishCoin(invite.getGiftId());
			qxCoinHelper.freezeCoin(currentUserId, punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT);
		}
		else {
			throw new ServiceException("不支持的约单类型");
		}
		QxInviteApply inviteApply = new QxInviteApply();
		inviteApply.setUserId(currentUserId);
		inviteApply.setInviteId(inviteId);
		inviteApply.setStatus(INVITE_APPLY_STATUS.UN_SURE);
		qxInviteApplyMapper.insert(inviteApply);
		// 发送报名通知
		QxUser user = qxUserMapper.selectById(invite.getInviter());
		Map<String, String> pairs = new HashMap<>();
		Map<String, String> extras = new HashMap<>();
		noticeHelper.push(user.getMobile(), SMS_CODE.INVITE_APPLY, pairs, extras);
	}

	@Override
	public List<QxInviteUserPojo> applicants(Long inviteId) {
		return this.baseMapper.getInviteUsers(inviteId);
	}

	@Override
	public void choose(Long inviteId, Long userId) {
		chooseApply(inviteId, userId);
		saveChooseOperate(inviteId);
		updateInviteStatus(inviteId, userId, INVITE_STATUS.MATCHED);
		notifyInvitee(inviteId);
	}
	
	/**
	 * 保存选择动作记录
	 * @param inviteId
	 */
	private void saveChooseOperate(Long inviteId) {
		QxInvite invite = this.getById(inviteId);
		createInviteOperate(invite.getId(), invite.getInviter(), INVITE_OPERATE_TYPE.CHOOSE_APPLY);
	}
	
	/**
	 * 保存同意动作记录
	 * @param inviteId
	 */
	private void saveAgreeOperate(Long inviteId) {
		QxInvite invite = this.getById(inviteId);
		createInviteOperate(invite.getId(), invite.getInvitee(), INVITE_OPERATE_TYPE.AGREE_INVITE);
	}

	public void chooseApply(Long inviteId, Long userId) {
		// 更新选中的报名状态
		UpdateWrapper<QxInviteApply> chooseUpdateWrapper = new UpdateWrapper<>();
		chooseUpdateWrapper.eq("invite_id", inviteId).eq("user_id", userId);
		QxInviteApply model = new QxInviteApply();
		model.setStatus(INVITE_APPLY_STATUS.AGREE);
		qxInviteApplyMapper.update(model, chooseUpdateWrapper);
		// 更新未选中的报名状态
		UpdateWrapper<QxInviteApply> rejectUpdateWrapper = new UpdateWrapper<>();
		rejectUpdateWrapper.eq("invite_id", inviteId).ne("user_id", userId);
		QxInviteApply rejectModel = new QxInviteApply();
		rejectModel.setStatus(INVITE_APPLY_STATUS.REJECT);
		qxInviteApplyMapper.update(rejectModel, rejectUpdateWrapper);

		QxInvite invite = this.baseMapper.selectById(inviteId);
		List<QxInviteUserPojo> list = this.baseMapper.getInviteUsers(inviteId);
		for (QxInviteUserPojo inviteUser : list) {
			if (INVITE_APPLY_STATUS.REJECT.equals(inviteUser.getStatus())) {
				if (INVITE_TYPE.PASSIVE.equals(invite.getInviteType())) {
					// 如果是TA请客，则在报名时，需将金币退还至原账户
					qxCoinHelper.unfreeze(inviteUser.getUserId(), invite.getGiftId(), USER_PAY_LOG_TYPE.INVITE_IN);
				} else {
					// 我请客，解冻报名押金
					int punishCoin = qxCoinHelper.getPunishCoin(invite.getGiftId());
					qxCoinHelper.unfreezeCoin(inviteUser.getUserId(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_IN);
				}
			}
		}
	}

	public void updateInviteStatus(Long invitedId, Long invitee, String status) {
		UpdateWrapper<QxInvite> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", invitedId);
		QxInvite model = new QxInvite();
		if (invitee != null) {
			model.setInvitee(invitee);
		}
		model.setStatus(status);
		this.baseMapper.update(model, updateWrapper);
	}

	private void notifyInvitee(Long inviteId) {
		List<QxInviteUserPojo> list = this.baseMapper.getInviteUsers(inviteId);
		for (QxInviteUserPojo inviteUser : list) {
			int tag = 0;
			String account = inviteUser.getMobile();
			Map<String, String> pairs = new HashMap<>();
			Map<String, String> extras = new HashMap<>();

			if (INVITE_APPLY_STATUS.AGREE.equals(inviteUser.getStatus())) {
				// 发送选中消息
				tag = SMS_CODE.INVITE_SUCCESS;
				extras.put("type", NOTIFICATION_TYPE.INVITE_CHOOSE_NOTIFY);
				extras.put("data", INVITE_APPLY_RESULT.SUCCESS);
				log.info("User " + inviteUser.getMobile() + "被选中");
			} else {
				// 发送落选消息
				tag = SMS_CODE.INVITE_FAIL;
				extras.put("type", NOTIFICATION_TYPE.INVITE_CHOOSE_NOTIFY);
				extras.put("data", INVITE_APPLY_RESULT.FAIL);
				log.info("User " + inviteUser.getMobile() + "未被选中");
			}
			noticeHelper.push(account, tag, pairs, extras);
		}
	}

	@Override
	public void agree(Long inviteId) {
		QxInvite invite = this.baseMapper.selectById(inviteId);
		Long invitee = invite.getInvitee();
		if (INVITE_TYPE.PASSIVE.equals(invite.getInviteType())) {
			// TA请客，冻结礼物金币
			qxCoinHelper.freeze(invitee, invite.getGiftId(), USER_PAY_LOG_TYPE.INVITE_OUT);
		} else {
			// 我请客，冻结我的违约金
			int punishCoin = qxCoinHelper.getPunishCoin(invite.getGiftId());
			qxCoinHelper.freezeCoin(invitee, punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT);
		}
		// 保存同意记录
		saveAgreeOperate(inviteId);
		chooseApply(inviteId, invitee);
		updateInviteStatus(inviteId, null, INVITE_STATUS.MATCHED);
		notifyInvitee(inviteId);
	}

	@Override
	public void reject(Long inviteId) {
		updateInviteStatus(inviteId, null, INVITE_STATUS.CANCEl);
		notifyInvitee(inviteId);
	}

	@Override
	public List<QxInvite> getCurrentInvites(Page page, Long userId) {
		return this.baseMapper.getCurrentInvites(page, userId);
	}

	/**
	 * 确认见面，即约单结束，将金币转给对方
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void start(Long inviteId, Long requestUserId) {
		createInviteOperate(inviteId, requestUserId, INVITE_OPERATE_TYPE.CONFIRM_START);
		if (checkOtherSideOperate(inviteId, requestUserId, INVITE_OPERATE_TYPE.CONFIRM_START)) {
			changeQxInviteStatus(inviteId, INVITE_STATUS.FINISH);
			payCoin(inviteId, requestUserId);
		}
	}

	public void createInviteOperate(Long inviteId, Long userId, String type) {
		QxInviteOperate inviteOperate = new QxInviteOperate();
		inviteOperate.setInviteId(inviteId);
		inviteOperate.setUserId(userId);
		inviteOperate.setType(type);
		qxInviteOperateMapper.insert(inviteOperate);
	}

	public Boolean checkOtherSideOperate(Long inviteId, Long userId, String type) {
		QueryWrapper<QxInviteOperate> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("invite_id", inviteId).eq("type", type).ne("user_id", userId);
		int count = qxInviteOperateMapper.selectCount(queryWrapper);
		return count > 0;
	}
	
	/**
	 * 检查用户是否操作约单
	 * @param inviteId
	 * @param userId
	 * @param type
	 * @return
	 */
	public Boolean checkOperate(Long inviteId, Long userId, String type) {
		QueryWrapper<QxInviteOperate> queryWrapper = new QueryWrapper<QxInviteOperate>();
		queryWrapper.eq("invite_id", inviteId).eq("type", type).eq("user_id", userId);
		int count = qxInviteOperateMapper.selectCount(queryWrapper);
		return count > 0;
	}

	public void changeQxInviteStatus(Long inviteId, String status) {
		UpdateWrapper<QxInvite> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", inviteId);
		QxInvite model = new QxInvite();
		model.setStatus(status);
		this.baseMapper.update(model, updateWrapper);
	}

	@Override
	public void finish(Long inviteId, Long requestUserId) {
		createInviteOperate(inviteId, requestUserId, INVITE_OPERATE_TYPE.CONFIRM_FINISH);
		if (Boolean.TRUE.equals(checkOtherSideOperate(inviteId, requestUserId, INVITE_OPERATE_TYPE.CONFIRM_FINISH))) {
			changeQxInviteStatus(inviteId, INVITE_STATUS.FINISH);
		}
	}

	public void payCoin(Long inviteId, Long requestUserId) {
		QxInvite invite = this.getById(inviteId);
		Long payerId;
		Long payeeId;
		if (invite.getInviteType().equals(INVITE_TYPE.ACTIVE)) {
			payerId = invite.getInviter();
			payeeId = invite.getInvitee();
		} else {
			payerId = invite.getInvitee();
			payeeId = invite.getInviter();
		}
		int punishCoin = qxCoinHelper.getPunishCoin(invite.getGiftId());
		if (INVITE_TYPE.ACTIVE.equals(invite.getInviteType())) {
			// 主动约，退报名人的违约金
			qxCoinHelper.unfreezeCoin(invite.getInvitee(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_IN); // 报名方退回违约金
		} else {
			// 被动约，退发起人违约金
			qxCoinHelper.unfreezeCoin(invite.getInviter(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_IN); // 报名方退回违约金
		}
		QxPayResult payResult = qxCoinHelper.payCoin(payerId, payeeId, invite.getGiftId(), true);
		qxPayLogHelper.createPayLog(payResult.getPayerId(), payResult.getPrice(), USER_PAY_LOG_TYPE.INVITE_OUT);
		qxPayLogHelper.createPayLog(payResult.getPayeeId(), payResult.getPrice(), USER_PAY_LOG_TYPE.INVITE_IN);
	}

	@Override
	public void complaint(Long inviteId, Long userId, String reason) {
		QxComplaint complaint = new QxComplaint();
		complaint.setInviteId(inviteId);
		complaint.setUserId(userId);
		complaint.setReason(reason);
		qxComplaintService.save(complaint);
		UpdateWrapper<QxInvite> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", inviteId).set("status", INVITE_STATUS.COMPLAINT);
		this.baseMapper.update(null, updateWrapper);
	}

	@Override
	public void comment(Long userId, QxInviteCommentTo commentTo) {
		QxInvite invite = this.getById(commentTo.getInviteId());
		if (!INVITE_STATUS.FINISH.equals(invite.getStatus())) {
			throw new ServiceException("约单未结束，不能评价哦");
		}
		if (!(userId.equals(invite.getInviter()) || userId.equals(invite.getInvitee()))) {
			throw new ServiceException("只能评价自己的约单");
		}
		QxInviteComment inviteComment = new QxInviteComment();
		BeanUtils.copyProperties(commentTo, inviteComment);
		inviteComment.setCommenterId(userId);
		if (userId.equals(invite.getInviter())) {
			inviteComment.setCommenteeId(invite.getInvitee());
		} else {
			inviteComment.setCommenteeId(invite.getInviter());
		}
		qxInviteCommentSerivce.save(inviteComment);
		// 更新被评价用户信用分
		QxUser commentee = qxUserMapper.selectById(inviteComment.getCommenteeId());
		commentee.setScore(caculateScore(commentee.getScore(), commentTo.getScore()));
		qxUserMapper.updateById(commentee);
	}

	public Integer caculateScore(Integer currentScore, Integer commentScore) {
		Integer resultScore = currentScore + commentScore;
		return Math.max(0, resultScore);
	}

	@Override
	public void alert(Long userId, String emergencyContact, QxInvite invite) {
		Long otherUserId = userId.equals(invite.getInviter()) ? invite.getInvitee() : invite.getInviter();
		QxUser currentUser = qxUserMapper.selectById(userId);
		QxUser otherUser = qxUserMapper.selectById(otherUserId);
		saveAlert(userId, invite.getId());
		sendAlert(invite, currentUser, otherUser, emergencyContact);
	}

	public void saveAlert(Long userId, Long inviteId) {
		QxAlert alert = new QxAlert();
		alert.setUserId(userId);
		alert.setInviteId(inviteId);
		alert.setStatus(ALERT_STATUS.UNHANDLE);
		qxAlertService.save(alert);
	}

	public void sendAlert(QxInvite invite, QxUser currentUser, QxUser otherUser, String emergencyContact) {
		// 用户${contact}向您发送了一键报警，请及时与对方联系
		Map<String, String> pairs = new HashMap<>();
		pairs.put("contact", currentUser.getMobile());
		noticeHelper.send(emergencyContact, SMS_CODE.EMERGENCY, pairs);
	}

	@Override
	public Page<List<QxInviteSearchPojo>> search(Page page, QxInviteQueryTo inviteQueryTo) {
		QxInviteParam param = new QxInviteParam();
		BeanUtils.copyProperties(inviteQueryTo, param);
		return this.baseMapper.search(page, param);
	}

	@Override
	public int getMyInviteCount(Long userId) {
		QueryWrapper<QxInvite> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("inviter", userId);
		return this.baseMapper.selectCount(queryWrapper);
	}

	@Override
	public int getInviteMeCount(Long userId) {
		QueryWrapper<QxInvite> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("invitee", userId);
		return this.baseMapper.selectCount(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void cancel(Long requestUserId, Long inviteId) {
		QxInvite invite = this.baseMapper.selectById(inviteId);
		String inviteStatus = invite.getStatus();
		int punishCoin = qxCoinHelper.getPunishCoin(invite.getGiftId());
		// 检查状态
		if (!(INVITE_STATUS.WAIT_MATCH.equals(inviteStatus) || INVITE_STATUS.MATCHED.equals(inviteStatus))) {
			throw new ServiceException("约单已开始，不能取消");
		}
		// 获得取消方
		String reason = null;
		if (invite.getInviter().equals(requestUserId)) {
			reason = PUNISH_REASON.INVITER;
		} else {
			reason = PUNISH_REASON.INVITEE;
		}
		// 约单未配对，只能发起人取消
		if (INVITE_STATUS.WAIT_MATCH.equals(inviteStatus)) {
			List<QxInviteUserPojo> list = this.baseMapper.getInviteUsers(invite.getId());
			if (INVITE_TYPE.ACTIVE.equals(invite.getInviteType())) {
				// 主动约，解冻发起人金币；
				qxCoinHelper.unfreeze(invite.getInviter(), invite.getGiftId(), USER_PAY_LOG_TYPE.INVITE_OUT);
				// 解冻报名人违约金
				for (QxInviteUserPojo inviteUser : list) {
					qxCoinHelper.unfreezeCoin(inviteUser.getUserId(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT);
				}
			} else {
				// 被动约，解冻发起人违约金
				qxCoinHelper.unfreezeCoin(invite.getInviter(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT);
				// 解冻报名人礼物
				for (QxInviteUserPojo inviteUser : list) {
					qxCoinHelper.unfreeze(inviteUser.getUserId(), invite.getGiftId(), USER_PAY_LOG_TYPE.INVITE_OUT);
				}
			}
		} else {
			// 约单已配对，双发均可取消
			handleUnstartPunish(invite, reason);
		}
		// 发送取消约单通知
		notifyInviteCancel(invite, reason);
		// 取消约单
		invite.setStatus(INVITE_STATUS.CANCEl);
		this.baseMapper.updateById(invite);
	}
	
	public void notifyInviteCancel(QxInvite invite, String reason) {
		if (PUNISH_REASON.INVITER.equals(reason)) {
			// 发起人取消，通知报名人
			List<QxInviteUserPojo> list = this.baseMapper.getInviteUsers(invite.getId());
			for (QxInviteUserPojo inviteUser : list) {
				if (INVITE_APPLY_STATUS.REJECT != inviteUser.getStatus()) {
					Map<String, String> pairs = new HashMap<>();
					pairs.put("operate", "报名");
					Map<String, String> extras = new HashMap<>();
					noticeHelper.push(inviteUser.getMobile(), SMS_CODE.INVITE_CANCEL, pairs, extras);
				}
			}
		} else {
			// 报名人取消，通知发起人
			Map<String, String> pairs = new HashMap<>();
			pairs.put("operate", "发起");
			Map<String, String> extras = new HashMap<>();
			QxUser inviter = qxUserMapper.selectById(invite.getInviter());
			noticeHelper.push(inviter.getMobile(), SMS_CODE.INVITE_CANCEL, pairs, extras);
		}
	}

	@Override
	public QxInviteSearchPojo getInviteById(Long id) {
		return this.baseMapper.getInviteById(id);
	}

	/**
	 * 超过24小时没有配对的单，就取消
	 */
	@Override
	public void closeWaitInvite() {
		QueryWrapper<QxInvite> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("status", ProjectConstants.INVITE_STATUS.WAIT_MATCH).lt("invite_time",
				DateUtils.addDay(new Date(), -1));
		List<QxInvite> unmatchedInvites = this.baseMapper.selectList(queryWrapper);
		for (QxInvite invite : unmatchedInvites) {
			invite.setStatus(ProjectConstants.INVITE_STATUS.CANCEl);
			this.baseMapper.updateById(invite);
			// 未配对的单，主动约发起人礼物金额冻结
			if (INVITE_TYPE.ACTIVE.equals(invite.getInviteType())) {
				qxCoinHelper.unfreeze(invite.getInviter(), invite.getGiftId(), USER_PAY_LOG_TYPE.INVITE_OUT);
			} else {
				// 被动约，发起人违约金解冻
				int punishCoin = qxCoinHelper.getPunishCoin(invite.getGiftId());
				qxCoinHelper.unfreezeCoin(invite.getInviter(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT);
			}
		}
	}

	@Override
	public Page myApply(Page page, Long requestUserId) {
		return this.baseMapper.myApply(page, requestUserId);
	}

	/**
	 * 超过1小时未见面的约单
	 */
	@Override
	public void closeUnstartInvite() {
		QueryWrapper<QxInvite> queryWrapper = new QueryWrapper<QxInvite>();
		queryWrapper.eq("status", INVITE_STATUS.MATCHED).lt("invite_time", DateUtils.addHour(new Date(), -1));
		List<QxInvite> unfinishedInvites = this.baseMapper.selectList(queryWrapper);
		for (QxInvite invite : unfinishedInvites) {
			String reason = checkPunishReason(invite);
			handleUnstartPunish(invite, reason);
		}
	}
	
	public void handleUnstartPunish(QxInvite invite, String reason) {
		Long giftId = invite.getGiftId();
		QxGift gift = qxGiftMapper.selectById(giftId);
		int punishCoin = qxCoinHelper.getPunishCoin(giftId);
		if (PUNISH_REASON.BOTH.equals(reason)) {
			// 发起方，报名方金币原路返回
			if (INVITE_TYPE.ACTIVE.equals(invite.getInviteType())) {
				qxCoinHelper.unfreeze(invite.getInviter(), giftId, USER_PAY_LOG_TYPE.INVITE_OUT); // 发起方退回礼物
				qxCoinHelper.unfreezeCoin(invite.getInvitee(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT); // 报名方退回违约金
			} else if (INVITE_TYPE.PASSIVE.equals(invite.getInviteType())) {
				qxCoinHelper.unfreeze(invite.getInvitee(), giftId, USER_PAY_LOG_TYPE.INVITE_OUT); // 报名方退回礼物
				qxCoinHelper.unfreezeCoin(invite.getInviter(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_OUT); // 发起方退回违约金
			} else {
				throw new ServiceException("不支持的约单类型");
			}
		} else if (PUNISH_REASON.INVITER.equals(reason)) {
			if (INVITE_TYPE.ACTIVE.equals(invite.getInviteType())) {
				// 发起方付违约金给报名方，同时两方解冻剩下的金币
				qxCoinHelper.payPunishCoin(invite.getInviter(), invite.getInvitee(), punishCoin);
				qxCoinHelper.unfreezeCoin(invite.getInviter(), gift.getPrice()-punishCoin, USER_PAY_LOG_TYPE.INVITE_IN);
				// 报名方解冻自己的违约金
				qxCoinHelper.unfreezeCoin(invite.getInvitee(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_IN);
			} else if (INVITE_TYPE.PASSIVE.equals(invite.getInviteType())) {
				// 发起方付违约金给报名方，然后解冻报名方礼物金
				qxCoinHelper.payPunishCoin(invite.getInviter(), invite.getInvitee(), punishCoin);
				qxCoinHelper.unfreezeCoin(invite.getInvitee(), gift.getPrice(), USER_PAY_LOG_TYPE.INVITE_IN);
			} else {
				throw new ServiceException("不支持的约单类型");
			}
		} else {
			if (INVITE_TYPE.ACTIVE.equals(invite.getInviteType())) {
				// 报名方付违约金给发起方，解冻发起方礼物金额
				qxCoinHelper.payPunishCoin(invite.getInvitee(), invite.getInviter(), punishCoin);
				qxCoinHelper.unfreezeCoin(invite.getInviter(), gift.getPrice(), USER_PAY_LOG_TYPE.INVITE_IN);
			} else if (INVITE_TYPE.PASSIVE.equals(invite.getInviteType())) {
				// 报名方付违约金给发起方，同时两方解冻剩下的金币
				qxCoinHelper.payPunishCoin(invite.getInvitee(), invite.getInviter(), punishCoin);
				qxCoinHelper.unfreezeCoin(invite.getInvitee(), gift.getPrice()-punishCoin, USER_PAY_LOG_TYPE.INVITE_IN);
				
				qxCoinHelper.unfreezeCoin(invite.getInviter(), punishCoin, USER_PAY_LOG_TYPE.COMPENSATION_IN);
			} else {
				throw new ServiceException("不支持的约单类型");
			}
		}
		invite.setStatus(ProjectConstants.INVITE_STATUS.CANCEl);
		this.baseMapper.updateById(invite);
	}
	
	private String checkPunishReason(QxInvite invite) {
		Boolean isInviterStart = checkOperate(invite.getId(), invite.getInviter(), INVITE_OPERATE_TYPE.CONFIRM_START);
		Boolean isInviteeStart = checkOperate(invite.getId(), invite.getInvitee(), INVITE_OPERATE_TYPE.CONFIRM_START);
		if (isInviterStart == Boolean.FALSE && isInviteeStart == Boolean.FALSE) {
			return PUNISH_REASON.BOTH;
		} else if (isInviterStart == Boolean.FALSE) {
			return PUNISH_REASON.INVITER;
		} else if (isInviteeStart == Boolean.FALSE) {
			return PUNISH_REASON.INVITEE;
		} else {
			throw new ServiceException("约单状态和操作记录不一致");
		}
	}

	@Override
	public void notifyPrepareStartInvite() {
		List<QxInvite> prepareInviteList = this.baseMapper.getPrepareInviteList(configEntity.getInviteBeforeTime());
		List<String> userAccounts = new ArrayList<>();  
		List<Long> inviteIds = new ArrayList<>();
		for (QxInvite invite : prepareInviteList) {
			QxUser inviter = qxUserMapper.selectById(invite.getInviter());
			QxUser invitee = qxUserMapper.selectById(invite.getInvitee());
			userAccounts.add(inviter.getMobile());
			userAccounts.add(invitee.getMobile());
			inviteIds.add(invite.getId());
		}
		for (String account : userAccounts) {
			Map<String, String> pairs = new HashMap<>();
			Map<String, String> extras = new HashMap<>();
			noticeHelper.push(account, SMS_CODE.PREPARE_INVITE, pairs, extras);
			log.info("约单开始提醒已发送: " + account);
		}
		for (Long inviteId : inviteIds) {
			QxInviteNotify entity = new QxInviteNotify();
			entity.setInviteId(inviteId);
			qxInviteNotifyMapper.insert(entity);
		}
	}
}
