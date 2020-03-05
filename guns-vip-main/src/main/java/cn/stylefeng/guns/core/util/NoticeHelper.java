package cn.stylefeng.guns.core.util;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import cn.stylefeng.guns.config.ConfigEntity;
import cn.stylefeng.guns.core.DateUtils;
import cn.stylefeng.guns.core.constant.ProjectConstants.NOTICE_TYPE;
import cn.stylefeng.guns.core.constant.ProjectConstants.SMS_CODE;
import cn.stylefeng.guns.core.exception.ServiceException;
import cn.stylefeng.guns.core.notice.AliyunEmail;
import cn.stylefeng.guns.core.notice.AliyunSms;
import cn.stylefeng.guns.core.notice.JPushMsg;
import cn.stylefeng.guns.core.schedue.threads.ThreadsContext;
import cn.stylefeng.guns.modular.note.dto.NoticeEmailDto;
import cn.stylefeng.guns.modular.note.dto.NoticeSmsDto;
import cn.stylefeng.guns.modular.note.entity.QxNote;
import cn.stylefeng.guns.modular.note.entity.QxNotice;
import cn.stylefeng.guns.modular.note.entity.QxNotify;
import cn.stylefeng.guns.modular.note.entity.QxUser;
import cn.stylefeng.guns.modular.note.mapper.QxNoticeMapper;
import cn.stylefeng.guns.modular.note.mapper.QxNotifyMapper;
import cn.stylefeng.guns.modular.note.mapper.QxUserMapper;


@Component
public class NoticeHelper {
	@Resource
    private ConfigEntity configEntity;

	@Resource
	private AliyunSms aliyunSms;
	
	@Autowired
	private AliyunEmail aliyunEmail;
	
	@Autowired
	private JPushMsg jpushMsg;
	
	@Autowired
	private QxNoticeMapper noticeMapper;
	
	@Autowired
	private QxUserMapper userMapper;
	
	@Autowired
	private QxNotifyMapper notifyMapper;
	
    @Resource
    private ThreadsContext threadsContexts;
    
	public void send(String account, Integer tag, Map<String, String> pairs) {
		send(account, tag, pairs, new Date());
	}
	
	private void send(String account, Integer tag, Map<String, String> pairs, Date date) {
		if (needCheckInterval(tag) && interval(account) > 0) {
			throw new ServiceException(configEntity.getCodeIntervalMin() + "分钟后，才能再次获取！");
		}
		NoticeEmailDto eDto = new NoticeEmailDto(account, tag, pairs);
		NoticeSmsDto smsDto = new NoticeSmsDto(account, tag, pairs);
		Integer type = account.contains("@") ? NOTICE_TYPE.EMAIL : NOTICE_TYPE.SMS;
		QxNotice notice = saveNotice(account, eDto.getTextBody(configEntity.getTemplate(eDto.getTag(), false)), tag, type, date);
		if (account.contains("@")) {
			sendEmail(notice.getId(), eDto);
		} else {
			sendSms(notice.getId(), smsDto);
		}
	}
	
	public void push(String account, Integer tag, Map<String, String> pairs, Map<String, String>extras) {
		Date date = new Date();
		QxUser user = userMapper.getByAccount(account);
		NoticeEmailDto eDto = new NoticeEmailDto(account, tag, pairs);
		QxNotice notice = saveNotice(account, eDto.getTextBody(configEntity.getTemplate(eDto.getTag(), false)), tag, NOTICE_TYPE.PUSH, date);
		sendPush(user.getId(), notice.getId(), eDto, extras);
		updateNotify(user.getId(), tag);
	}
	
	private boolean needCheckInterval(Integer tag) {
		if (tag == SMS_CODE.LOGIN_OR_REGISTER) {
			return true;
		}
		return false;
	}
	
	public void saveNoteNotice(Long requestUserId, Long authorUserId, int tag, Map<String, Object>extras) {
		// 添加消息
		QxUser authorUser = userMapper.selectById(authorUserId);
		String content = extras.entrySet().stream().map(n -> n.getKey() + "=" + n.getValue()).collect(Collectors.joining(";"));
		saveNotice(authorUser.getMobile(), content, tag, NOTICE_TYPE.PUSH, new Date());
		updateNotify(authorUser.getId(), tag);
	}
	
	/**
	 * 更新提醒
	 * @param userId
	 * @param tag
	 */
	public void updateNotify(Long userId, int tag) {
		String column = "";
		switch(tag) {
			case SMS_CODE.LIKE:
				column = "new_like";
				break;
			case SMS_CODE.REWARD:
				column = "new_reward";
				break;
			case SMS_CODE.COMMENT:
				column = "new_comment";
				break;
			case SMS_CODE.FOLLOW:
				column = "new_follow";
				break;
			case SMS_CODE.INVITE_FAIL:
			case SMS_CODE.INVITE_SUCCESS:
				column = "new_invite";
				break;
			default:
				return;
		}
		UpdateWrapper<QxNotify> updateWrapper = new UpdateWrapper<QxNotify>();
		updateWrapper.eq("user_id", userId).set(column, true);
		notifyMapper.update(null, updateWrapper);
	}
	
	private QxNotice saveNotice(String account, String context, Integer tag, Integer type, Date date) {
		QxNotice model = new QxNotice();
		model.setType(type);
		model.setAccount(account);
		model.setContent(context);
		model.setTag(tag);
		model.setCreatedTime(date);
		noticeMapper.insert(model);
		return model;
	}
	
	private int interval(String account) {
		QueryWrapper<QxNotice> wrapper = new QueryWrapper<>();
		wrapper.eq("account", account)
				.and(f -> f.gt("created_time", DateUtils.addMin(new Date(), -configEntity.getCodeIntervalMin())));
		wrapper.orderByDesc("created_time");
		return noticeMapper.selectCount(wrapper);
	}
	
	private void sendSms(Long noticeId, NoticeSmsDto noticeSmsDto) {
		threadsContexts.submit("sms", new Runnable() {
			public void run() {
				String template = configEntity.getTemplate(noticeSmsDto.getTag(), true);
				aliyunSms.sendSms(noticeSmsDto.getAccount(), template , noticeSmsDto.getParameters());
				updateSuccess(noticeId);
			}
		});
	}

	private void sendEmail(Long noticeId, NoticeEmailDto noticeEmailDto) {
		threadsContexts.submit("email", new Runnable() {
			public void run() {
				String tagName = configEntity.getTagName(noticeEmailDto.getTag());
				String subject = configEntity.getSubject(noticeEmailDto.getTag());
				String template = configEntity.getTemplate(noticeEmailDto.getTag(), false);
				String textBody = noticeEmailDto.getTextBody(template); 
				aliyunEmail.sendText(noticeEmailDto.getAccount(), tagName, subject, textBody);
				updateSuccess(noticeId);
			}
		});
	}
	
	private void sendPush(Long userId, Long noticeId, NoticeEmailDto noticeEmailDto, Map<String, String>extras) {
		threadsContexts.submit("push", new Runnable() {
			public void run() {
				String template = configEntity.getTemplate(noticeEmailDto.getTag(), false);
				String textBody = noticeEmailDto.getTextBody(template);
				jpushMsg.sendPush(userId, textBody, extras);
				updateSuccess(noticeId);
			}
		});
	}
	
	private void updateSuccess(Long noticeId) {
		QxNotice model = new QxNotice();
		model.setId(noticeId);
		model.setStatusSend(true);
		model.setUpdatedTime(new Date());
		noticeMapper.updateById(model);
	}
}
