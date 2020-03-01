package cn.stylefeng.guns.modular.note.controller;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.note.entity.QxInviteNotify;
import cn.stylefeng.guns.modular.note.model.params.QxInviteNotifyParam;
import cn.stylefeng.guns.modular.note.service.QxInviteNotifyService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 拼单提醒控制器
 *
 * @author 
 * @Date 2020-03-01 10:36:14
 */
@Controller
@RequestMapping("/qxInviteNotify")
public class QxInviteNotifyController extends BaseController {

    private String PREFIX = "/qxInviteNotify";

    @Autowired
    private QxInviteNotifyService qxInviteNotifyService;

    /**
     * 跳转到主页面
     *
     * @author 
     * @Date 2020-03-01
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/qxInviteNotify.html";
    }

    /**
     * 新增页面
     *
     * @author 
     * @Date 2020-03-01
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/qxInviteNotify_add.html";
    }

    /**
     * 编辑页面
     *
     * @author 
     * @Date 2020-03-01
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/qxInviteNotify_edit.html";
    }

    /**
     * 新增接口
     *
     * @author 
     * @Date 2020-03-01
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(QxInviteNotifyParam qxInviteNotifyParam) {
        this.qxInviteNotifyService.add(qxInviteNotifyParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2020-03-01
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(QxInviteNotifyParam qxInviteNotifyParam) {
        this.qxInviteNotifyService.update(qxInviteNotifyParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2020-03-01
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(QxInviteNotifyParam qxInviteNotifyParam) {
        this.qxInviteNotifyService.delete(qxInviteNotifyParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2020-03-01
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(QxInviteNotifyParam qxInviteNotifyParam) {
        QxInviteNotify detail = this.qxInviteNotifyService.getById(qxInviteNotifyParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2020-03-01
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(QxInviteNotifyParam qxInviteNotifyParam) {
        return this.qxInviteNotifyService.findPageBySpec(qxInviteNotifyParam);
    }

}


