package cn.stylefeng.guns.modular.note.dvo;

import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class QxNoticeVo {
	private Long id;
	private Date createdTime;
	private String content;
	private Map<String, Object> extras;
}
