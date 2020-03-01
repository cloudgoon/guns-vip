package cn.stylefeng.guns.core.schedue.threads;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import cn.stylefeng.guns.modular.note.service.QxInviteService;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableScheduling
@Slf4j
public class SchedulerManager implements SchedulingConfigurer, Runnable{
	private static final long interval = 10 * 60 * 1000;

	@Resource
	private QxInviteService qxInviteService;
	
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addFixedRateTask(this, interval);
	}
	
	@Override
	public void run() {
		// 关闭未配对订单
		try {
			qxInviteService.closeWaitInvite();
		} catch (Exception e) {
			log.error("定时任务（未配对拼单关闭）", e);
		}
		// 关闭未开始订单
		try {
			qxInviteService.closeUnstartInvite();
		} catch (Exception e) {
			log.error("定时任务（未开始订单关闭）", e);
		}
		// 未开始约单提前提醒
		try {
			qxInviteService.notifyPrepareStartInvite();
		} catch (Exception e) {
			log.error("定时任务（提醒拼单即将开始）", e);
		}
	}
}
