package cn.stylefeng.guns.modular.note.model.result;

import java.io.Serializable;
import java.util.Date;

import cn.stylefeng.guns.core.constant.ProjectConstants;
import lombok.Data;

/**
 * <p>
 * 用户支付流水表
 * </p>
 *
 * @author 
 * @since 2019-11-18
 */
@Data
public class QxPayLogResult implements Serializable {

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
     * 用户名称
     */
    private String nickname;
    
    /**
     * 用户账号
     */
    private String mobile;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 付费类型 0-约单支出；1-约单汇入；2-打赏支出；3-打赏汇入；4-兑换商品支出；5-购买礼物支出；6-付费日记支出；7-付费日记汇入；8-违约金支出；9-违约金汇入；
     */
    private String type;
    
    public String getType() {
    	return ProjectConstants.FINANCE_LOG_MAP.get(this.type);
    }

}
