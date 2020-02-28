package cn.stylefeng.guns.modular.note.controller;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.note.entity.QxNotify;
import cn.stylefeng.guns.modular.note.model.params.QxNotifyParam;
import cn.stylefeng.guns.modular.note.service.QxNotifyService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 控制器
 *
 * @author 
 * @Date 2020-02-28 15:46:07
 */
@Controller
@RequestMapping("/qxNotify")
public class QxNotifyController extends BaseController {

    private String PREFIX = "/qxNotify";

    @Autowired
    private QxNotifyService qxNotifyService;

    /**
     * 跳转到主页面
     *
     * @author 
     * @Date 2020-02-28
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/qxNotify.html";
    }

    /**
     * 新增页面
     *
     * @author 
     * @Date 2020-02-28
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/qxNotify_add.html";
    }

    /**
     * 编辑页面
     *
     * @author 
     * @Date 2020-02-28
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/qxNotify_edit.html";
    }

    /**
     * 新增接口
     *
     * @author 
     * @Date 2020-02-28
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(QxNotifyParam qxNotifyParam) {
        this.qxNotifyService.add(qxNotifyParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2020-02-28
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(QxNotifyParam qxNotifyParam) {
        this.qxNotifyService.update(qxNotifyParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2020-02-28
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(QxNotifyParam qxNotifyParam) {
        this.qxNotifyService.delete(qxNotifyParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2020-02-28
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(QxNotifyParam qxNotifyParam) {
        QxNotify detail = this.qxNotifyService.getById(qxNotifyParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2020-02-28
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(QxNotifyParam qxNotifyParam) {
        return this.qxNotifyService.findPageBySpec(qxNotifyParam);
    }

}


