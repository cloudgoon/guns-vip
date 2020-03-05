package cn.stylefeng.guns.modular.note.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.core.constant.ProjectConstants.SMS_CODE;
import cn.stylefeng.guns.core.constant.ProjectConstants.USER_PAY_LOG_TYPE;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.core.util.NoticeHelper;
import cn.stylefeng.guns.modular.note.dto.QxPayResult;
import cn.stylefeng.guns.modular.note.entity.QxGift;
import cn.stylefeng.guns.modular.note.entity.QxNote;
import cn.stylefeng.guns.modular.note.entity.QxNoteComment;
import cn.stylefeng.guns.modular.note.entity.QxNoteLike;
import cn.stylefeng.guns.modular.note.entity.QxUser;
import cn.stylefeng.guns.modular.note.entity.QxUserNote;
import cn.stylefeng.guns.modular.note.mapper.QxGiftMapper;
import cn.stylefeng.guns.modular.note.mapper.QxNoteCommentMapper;
import cn.stylefeng.guns.modular.note.mapper.QxNoteLikeMapper;
import cn.stylefeng.guns.modular.note.mapper.QxNoteMapper;
import cn.stylefeng.guns.modular.note.mapper.QxUserMapper;
import cn.stylefeng.guns.modular.note.mapper.QxUserNoteMapper;
import cn.stylefeng.guns.modular.note.model.params.QxNoteParam;
import cn.stylefeng.guns.modular.note.model.result.QxNoteResult;
import  cn.stylefeng.guns.modular.note.service.QxNoteService;
import cn.stylefeng.roses.core.util.ToolUtil;

/**
 * <p>
 * 私密日记 服务实现类
 * </p>
 *
 * @author 
 * @since 2019-11-18
 */
@Service
public class QxNoteServiceImpl extends ServiceImpl<QxNoteMapper, QxNote> implements QxNoteService {

	@Resource
	private QxUserNoteMapper qxUserNoteMapper;
	
	@Resource
	private QxUserMapper qxUserMapper;
	
	@Resource
	private QxCoinHelper qxCoinHelper;
	
	@Resource
	private QxPayLogHelper qxPayLogHelper;
	
	@Resource
	private QxNoteLikeMapper qxNoteLikeMapper;
	
	@Resource
	private QxNoteCommentMapper qxNoteCommentMapper;
	
	@Resource
	private QxGiftMapper qxGiftMapper;
	
	@Resource
	private NoticeHelper noticeHelper;
	
    @Override
    public void add(QxNoteParam param){
        QxNote entity = getEntity(param);
        this.save(entity);
    }

    @Override
    public void delete(QxNoteParam param){
        this.removeById(getKey(param));
    }

    @Override
    public void update(QxNoteParam param){
        QxNote oldEntity = getOldEntity(param);
        QxNote newEntity = getEntity(param);
        ToolUtil.copyProperties(newEntity, oldEntity);
        this.updateById(newEntity);
    }

    @Override
    public QxNoteResult findBySpec(QxNoteParam param){
        return null;
    }

    @Override
    public List<QxNoteResult> findListBySpec(QxNoteParam param){
        return null;
    }

    @Override
    public LayuiPageInfo findPageBySpec(QxNoteParam param){
        Page pageContext = getPageContext();
        IPage page = this.baseMapper.customPageList(pageContext, param);
        return LayuiPageFactory.createPageInfo(page);
    }

    private Serializable getKey(QxNoteParam param){
        return param.getId();
    }

    private Page getPageContext() {
        return LayuiPageFactory.defaultPage();
    }

    private QxNote getOldEntity(QxNoteParam param) {
        return this.getById(getKey(param));
    }

    private QxNote getEntity(QxNoteParam param) {
        QxNote entity = new QxNote();
        ToolUtil.copyProperties(param, entity);
        return entity;
    }

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void rewardNote(Long requestUserId, Long userId, Long noteId, Long giftId) {
		QxPayResult payResult = qxCoinHelper.payCoin(requestUserId, userId, giftId, false);
		qxPayLogHelper.createPayLog(payResult.getPayerId(), payResult.getPrice(), USER_PAY_LOG_TYPE.REWARD_OUT);
		qxPayLogHelper.createPayLog(payResult.getPayeeId(), payResult.getPrice(), USER_PAY_LOG_TYPE.REWARD_IN);
		qxPayLogHelper.rewardNoteLog(requestUserId, noteId, giftId);
		QxNote note = this.getById(noteId);
		note.setGiftCount(note.getGiftCount() + 1);
		this.updateById(note);
		// 自动添加打赏评论
		addOfficalComment(requestUserId, noteId, giftId, false);
		// 添加消息
		Map<String, Object> extras = new HashMap<>();
		QxUser operator = qxUserMapper.selectById(requestUserId);
		QxGift gift = qxGiftMapper.selectById(giftId);
		extras.put("userId", operator.getId());
		extras.put("avatar", operator.getAvatar());
		extras.put("nickname", operator.getNickname());
		extras.put("noteId", note.getId());
		extras.put("notePics", note.getImages());
		extras.put("giftName", gift.getName());
		extras.put("giftImage", gift.getImage());
		extras.put("giftPrice", gift.getPrice());
		noticeHelper.saveNoteNotice(requestUserId, note.getUserId(), SMS_CODE.REWARD, extras);
	}
	
	/**
	 * 添加官方评论
	 * @param userId
	 * @param noteId
	 * @param content
	 */
	private void addOfficalComment(Long userId, Long noteId, Long giftId, Boolean unlock) {
		// 获取礼物
		QxGift gift = qxGiftMapper.selectById(giftId);
		String action = "";
		if (unlock) {
			action = "解锁了";
		} else {
			action = "打赏了";
		}
		QxNoteComment comment = new QxNoteComment();
		comment.setCreatedBy(userId);
		comment.setNoteId(noteId);
		
		comment.setContent(action + gift.getName() + "(" + gift.getPrice() +"金币)");
		comment.setOfficial(true);
		qxNoteCommentMapper.insert(comment);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void unlockNote(Long requestUserId, Long userId, Long noteId, Long giftId) {
		QxPayResult payResult = qxCoinHelper.payCoin(requestUserId, userId, giftId, false);
		qxPayLogHelper.createPayLog(payResult.getPayerId(), payResult.getPrice(), USER_PAY_LOG_TYPE.NOTE_OUT);
		qxPayLogHelper.createPayLog(payResult.getPayeeId(), payResult.getPrice(), USER_PAY_LOG_TYPE.NOTE_IN);
		QxNote note = this.getById(noteId);
		note.setWatchCount(note.getWatchCount()+1);
		this.updateById(note);
		// 添加用户解锁记录
		QxUserNote userNote = new QxUserNote();
		userNote.setNoteId(noteId);
		userNote.setUserId(requestUserId);
		qxUserNoteMapper.insert(userNote);
		// 添加解锁记录
		addOfficalComment(requestUserId, noteId, giftId, true);
	}

	@Override
	public QxNote like(Long requestUserId, Long noteId) {
		QueryWrapper<QxNoteLike> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("note_id", noteId).eq("created_by", requestUserId);
		int count = qxNoteLikeMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw new ServiceException("不能重复点赞");
		}
		QxNoteLike like = new QxNoteLike();
		like.setCreatedBy(requestUserId);
		like.setNoteId(noteId);
		qxNoteLikeMapper.insert(like);
		
		QxNote note = this.getById(noteId);
		note.setFavoriteCount(note.getFavoriteCount() + 1);
		this.updateById(note);
		// 添加消息
		Map<String, Object> extras = new HashMap<>();
		QxUser operator = qxUserMapper.selectById(requestUserId);
		extras.put("userId", operator.getId());
		extras.put("avatar", operator.getAvatar());
		extras.put("nickname", operator.getNickname());
		extras.put("noteId", note.getId());
		extras.put("notePics", note.getImages());
		noticeHelper.saveNoteNotice(requestUserId, note.getUserId(), SMS_CODE.LIKE, extras);
		
		return note;
	}

	@Override
	public Page rewardUsers(Page page, Long noteId) {
		return this.baseMapper.rewardUsers(page, noteId);
	}

	@Override
	public Page listNotes(Page page, Long requestUserId, String keywords) {
		return this.baseMapper.listNotes(page, requestUserId, keywords);
	}
	@Override
	public Page followList(Page page, Long requestUserId, String keywords) {
		return this.baseMapper.followList(page, requestUserId, keywords);

	}
}
