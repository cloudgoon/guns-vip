package cn.stylefeng.guns.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import cn.stylefeng.guns.core.FileUtil;
import cn.stylefeng.guns.core.constant.ProjectConstants.SMS_CODE;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "note")
@PropertySource(value = "classpath:config.properties", encoding = "UTF-8")
@Data
public class ConfigEntity {
	private String secureUrls;
	// 上传文件
	private String baseUploadPath;
	private String imagesPath;
	private String packageImagePath;

	// 是否上线
	private Boolean isOnline;
	
	// 极光推送
	private String jpushMaster;
	private String jpushAppKey;
	// 阿里云
	private String aliyunAccessKeyId;
	private String aliyunSecret;
	private String aliyunBucketName;
	private String aliyunBucketUrl;
	private String aliyunImagesFolder;
	private String aliyunVoicesFolder;
	private String aliyunGiftFolder;
	// 融云
	private String rongCloudAppKey;
	private String rongCloudAppSecret;
	private String userDefaultAvatar;
	// 验证码长度
	private int codeLength;
	// 邀请码长度
	private int inviteCodeLength;
	// 验证码, 失效时间
	private int codeExpirationMin;
	// 验证码, 可重发时间
	private int codeIntervalMin;
	// 微信支付相关配置
	private String wxSpbillCreateIp;
	private String wxNotifyUrl;
	private Boolean wxCanWithdraw;
	private String wxServiceContact;
	
	// 支付宝支付配置
	private Boolean alipayCanWithdraw;
	
	// 约单搜索附近距离范围
	private int inviteRange;
	// 每天最多发送5单
	private int maxInvite;
	// 多少天不活跃
	private int unactiveDays;
	// 违约金比例
	private double punishmentRate;
	// 提前提醒用户约单开始
	private String inviteBeforeTime;
	
	// 短信
	private String smsDomain;
	private String smsSignName;
	private String smsTemplate;
	private String smsInviteSuccessTemplate;
	private String smsInviteFailTemplate;
	private String smsEmergencyTemplate;
	private String smsNotifyUnactiveUsersTemplate;
	// 邮箱
	private String emailAccountName;
	private String emailFromAlias;
	private String eTemplate;
	private String eInviteSuccessTemplate;
	private String eInviteFailTemplate;
	private String eEmergencyTemplate;
	private String eNotifyUnactiveUsersTemplate;
	private String eInvitePrepareTemplate;
	private String eInviteApplyTemplate;
	private String eInviteCancelTemplate;
	
	// 平台联系方式
	private String platformContact;
	
	public String getAbsoluteUploadPath() {
		return FileUtil.mergeDeployPath(baseUploadPath);
	}

	public String getTagName(Integer tag) {
		return getNoticeConfig(tag, false).get("tagName");
	}

	public String getTemplate(Integer tag, boolean isSms) {
		return getNoticeConfig(tag, isSms).get("template");
	}

	public String getSubject(Integer tag) {
		return "用户" + getTagName(tag);
	}

	public Map<String, String> getNoticeConfig(Integer tag, boolean isSms) {
		Map<String, String> map = new HashMap<>();
		if (SMS_CODE.INVITE_SUCCESS == tag) {
			map.put("template", isSms ? smsInviteSuccessTemplate : eInviteSuccessTemplate);
			map.put("tagName", "约单成功通知");
		} else if (SMS_CODE.INVITE_FAIL == tag) {
			map.put("template", isSms ? smsInviteFailTemplate : eInviteFailTemplate);
			map.put("tagName", "约单失败通知");
		} else if (SMS_CODE.EMERGENCY == tag) {
			map.put("template", isSms ? smsEmergencyTemplate : eEmergencyTemplate);
			map.put("tagName", "紧急通知");
		} else if (SMS_CODE.NOTIFY_UNACTIVE_USERS == tag) {
			map.put("template", isSms ? smsNotifyUnactiveUsersTemplate : eNotifyUnactiveUsersTemplate);
			map.put("tagName", "提醒用户");
		} else if (SMS_CODE.PREPARE_INVITE == tag) {
			map.put("template", eInvitePrepareTemplate);
			map.put("tagName", "约单即将开始提醒");
		} else if (SMS_CODE.INVITE_APPLY == tag) {
			map.put("template", eInviteApplyTemplate);
			map.put("tagName", "约单报名通知");
		} else if (SMS_CODE.INVITE_CANCEL == tag) {
			map.put("template", eInviteCancelTemplate);
			map.put("tagName", "约单取消通知");
		} else {
			map.put("template", isSms ? smsTemplate : eTemplate);
			map.put("tagName", "验证码");
		}
		
		return map;
	}
}
