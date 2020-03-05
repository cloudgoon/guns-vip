package cn.stylefeng.guns.modular.note.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import cn.stylefeng.guns.base.pojo.page.LayuiPageFactory;
import cn.stylefeng.guns.core.ResultGenerator;
import cn.stylefeng.guns.core.constant.ProjectConstants.NOTICE_TYPE;
import cn.stylefeng.guns.core.constant.ProjectConstants.SMS_CODE;
import cn.stylefeng.guns.modular.note.dvo.QxNoticeVo;
import cn.stylefeng.guns.modular.note.dvo.QxNotifyVo;
import cn.stylefeng.guns.modular.note.entity.QxNotice;
import cn.stylefeng.guns.modular.note.entity.QxNotify;
import cn.stylefeng.guns.modular.note.service.QxNoticeService;
import cn.stylefeng.guns.modular.note.service.QxNotifyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/notice")
public class ApiNoticeController extends ApiBaseController {

	@Resource
	private QxNoticeService qxNoticeService;
	
	@Resource
	private QxNotifyService qxNotifyService;
	
	@RequestMapping("/list")
	public Object list() {
		String account = getUser().getMobile();
		Page page = LayuiPageFactory.defaultPage();
		QueryWrapper<QxNotice> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("account", account).eq("type", NOTICE_TYPE.PUSH).in("tag", Arrays.asList(SMS_CODE.INVITE_FAIL, SMS_CODE.INVITE_SUCCESS)).orderByDesc("created_time");
		qxNoticeService.page(page, queryWrapper);
		log.info("/api/notice/list");
		return ResultGenerator.genSuccessResult(page);
	}
	
	@RequestMapping("/like")
	public Object like() {
		String account = getUser().getMobile();
		Page page = LayuiPageFactory.defaultPage();
		QueryWrapper<QxNotice> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("account", account).eq("type", NOTICE_TYPE.PUSH).in("tag", Arrays.asList(SMS_CODE.LIKE)).orderByDesc("created_time");
		qxNoticeService.page(page, queryWrapper);
		List<QxNoticeVo> list = createNoticeVos(page.getRecords());
		page.setRecords(list);
		log.info("/api/notice/list");
		return ResultGenerator.genSuccessResult(page);
	}
	
	@RequestMapping("/reward")
	public Object reward() {
		String account = getUser().getMobile();
		Page page = LayuiPageFactory.defaultPage();
		QueryWrapper<QxNotice> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("account", account).eq("type", NOTICE_TYPE.PUSH).in("tag", Arrays.asList(SMS_CODE.REWARD)).orderByDesc("created_time");
		qxNoticeService.page(page, queryWrapper);
		List<QxNoticeVo> list = createNoticeVos(page.getRecords());
		page.setRecords(list);
		log.info("/api/notice/list");
		return ResultGenerator.genSuccessResult(page);
	}
	
	@RequestMapping("/comment")
	public Object comment() {
		String account = getUser().getMobile();
		Page page = LayuiPageFactory.defaultPage();
		QueryWrapper<QxNotice> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("account", account).eq("type", NOTICE_TYPE.PUSH).in("tag", Arrays.asList(SMS_CODE.COMMENT)).orderByDesc("created_time");
		qxNoticeService.page(page, queryWrapper);
		List<QxNoticeVo> list = createNoticeVos(page.getRecords());
		page.setRecords(list);
		log.info("/api/notice/list");
		return ResultGenerator.genSuccessResult(page);
	}
	
	@RequestMapping("/setNotifyRead")
	public Object setNotifyRead(String type) {
		UpdateWrapper<QxNotify> updateWrapper = new UpdateWrapper<QxNotify>();
		updateWrapper.eq("user_id", getRequestUserId()).set(type, false);
		qxNotifyService.update(updateWrapper);
		return ResultGenerator.genSuccessResult();
	}
	
	private List<QxNoticeVo> createNoticeVos(List<QxNotice> records) {
		List<QxNoticeVo> list = new ArrayList<>();
		for (QxNotice notice : records) {
			list.add(createNoticeVo(notice));
		}
		return list;
	}
	
	private QxNoticeVo createNoticeVo(QxNotice notice) {
		QxNoticeVo vo = new QxNoticeVo();
		Map<String, Object> extras = new HashMap<>();
		vo.setId(notice.getId());
		vo.setCreatedTime(notice.getCreatedTime());
		List<String> extraArray = new ArrayList<String>(Arrays.asList(notice.getContent().split(";")));
		for (String item : extraArray) {
			String[] itemArray = item.split("=");
			extras.put(itemArray[0], itemArray[1]);
		}
		vo.setExtras(extras);
		return vo;
	}
	
	
	@PostMapping("/getNotify")
	public Object getNotify() {
		QueryWrapper<QxNotify> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", getRequestUserId());
		QxNotify notify = qxNotifyService.getOne(queryWrapper);
		QxNotifyVo  vo = new QxNotifyVo();
		BeanUtils.copyProperties(notify, vo);
		return ResultGenerator.genSuccessResult(vo);
	}
	
	@RequestMapping("/detail")
	public Object detail(Long id) {
		QxNotice notice = qxNoticeService.getById(id);
		log.info("/api/notice/detail, id=" + id);
		return ResultGenerator.genSuccessResult(notice);
	}
	
	@RequestMapping("/read")
	public Object read(Long id) {
		UpdateWrapper<QxNotice> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id).set("status_read", true);
		qxNoticeService.update(updateWrapper);
		log.info("/api/notice/read, id=" + id);
		return ResultGenerator.genSuccessResult();
	}
	
	@RequestMapping("/delete")
	public Object delete(Long id) {
		qxNoticeService.removeById(id);
		log.info("/api/notice/delete, id=" + id);
		return ResultGenerator.genSuccessResult();
	}
}
