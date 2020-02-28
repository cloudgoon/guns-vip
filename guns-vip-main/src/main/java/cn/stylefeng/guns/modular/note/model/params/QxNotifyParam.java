package cn.stylefeng.guns.modular.note.model.params;

import lombok.Data;
import cn.stylefeng.roses.kernel.model.validator.BaseValidatingParam;
import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2020-02-28
 */
@Data
public class QxNotifyParam implements Serializable, BaseValidatingParam {

    private static final long serialVersionUID = 1L;


    /**
     * 标识
     */
    private Long id;

    /**
     * 乐观锁
     */
    private Integer version;

    /**
     * 创建人
     */
    private Long createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private Long updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 删除标识
     */
    private Boolean deleted;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 是否有新的赞
     */
    private Boolean newLike;

    /**
     * 是否有新打赏
     */
    private Boolean newReward;

    /**
     * 是否有新评论
     */
    private Boolean newComment;

    /**
     * 新粉丝
     */
    private Boolean newFollow;

    /**
     * 新约单消息
     */
    private Boolean newInvite;

    @Override
    public String checkParam() {
        return null;
    }

}
