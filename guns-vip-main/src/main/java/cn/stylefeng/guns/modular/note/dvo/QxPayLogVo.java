package cn.stylefeng.guns.modular.note.dvo;

import java.util.Date;

import cn.stylefeng.guns.core.constant.ProjectConstants;
import lombok.Data;

@Data
public class QxPayLogVo {
	private Long id;
	private Integer amount;
	private String type;
	private Date createdTime;
	
	public void setType(String type) {
		String name = ProjectConstants.FINANCE_LOG_MAP.get(type);
		this.type = name;
	}
}
