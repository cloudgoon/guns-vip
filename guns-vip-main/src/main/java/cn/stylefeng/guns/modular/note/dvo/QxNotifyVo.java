package cn.stylefeng.guns.modular.note.dvo;

import lombok.Data;

@Data
public class QxNotifyVo {
	private Long id;
	private Boolean newLike;
	private Boolean newReward;
	private Boolean newComment;
	private Boolean newFollow;
	private Boolean newInvite;
}
