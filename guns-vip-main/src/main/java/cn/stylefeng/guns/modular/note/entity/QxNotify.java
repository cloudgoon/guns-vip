package cn.stylefeng.guns.modular.note.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2020-02-28
 */
@TableName("qx_notify")
public class QxNotify implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标识
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 乐观锁
     */
    @TableField("version")
    private Integer version;

    /**
     * 创建人
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private Date createdTime;

    /**
     * 更新人
     */
    @TableField("updated_by")
    private Long updatedBy;

    /**
     * 更新时间
     */
    @TableField("updated_time")
    private Date updatedTime;

    /**
     * 删除标识
     */
    @TableField("deleted")
    private Boolean deleted;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 是否有新的赞
     */
    @TableField("new_like")
    private Boolean newLike;

    /**
     * 是否有新打赏
     */
    @TableField("new_reward")
    private Boolean newReward;

    /**
     * 是否有新评论
     */
    @TableField("new_comment")
    private Boolean newComment;

    /**
     * 新粉丝
     */
    @TableField("new_follow")
    private Boolean newFollow;

    /**
     * 新约单消息
     */
    @TableField("new_invite")
    private Boolean newInvite;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getNewLike() {
        return newLike;
    }

    public void setNewLike(Boolean newLike) {
        this.newLike = newLike;
    }

    public Boolean getNewReward() {
        return newReward;
    }

    public void setNewReward(Boolean newReward) {
        this.newReward = newReward;
    }

    public Boolean getNewComment() {
        return newComment;
    }

    public void setNewComment(Boolean newComment) {
        this.newComment = newComment;
    }

    public Boolean getNewFollow() {
        return newFollow;
    }

    public void setNewFollow(Boolean newFollow) {
        this.newFollow = newFollow;
    }

    public Boolean getNewInvite() {
        return newInvite;
    }

    public void setNewInvite(Boolean newInvite) {
        this.newInvite = newInvite;
    }

    @Override
    public String toString() {
        return "QxNotify{" +
        "id=" + id +
        ", version=" + version +
        ", createdBy=" + createdBy +
        ", createdTime=" + createdTime +
        ", updatedBy=" + updatedBy +
        ", updatedTime=" + updatedTime +
        ", deleted=" + deleted +
        ", userId=" + userId +
        ", newLike=" + newLike +
        ", newReward=" + newReward +
        ", newComment=" + newComment +
        ", newFollow=" + newFollow +
        ", newInvite=" + newInvite +
        "}";
    }
}
