package cn.stylefeng.guns.modular.note.controller;

import cn.stylefeng.guns.base.pojo.page.LayuiPageInfo;
import cn.stylefeng.guns.modular.note.entity.QxLogon;
import cn.stylefeng.guns.modular.note.model.params.QxLogonParam;
import cn.stylefeng.guns.modular.note.service.QxLogonService;
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
 * @Date 2020-02-19 10:20:52
 */
@Controller
@RequestMapping("/qxLogon")
public class QxLogonController extends BaseController {

    private String PREFIX = "/note/qxLogon";

    @Autowired
    private QxLogonService qxLogonService;

    /**
     * 跳转到主页面
     *
     * @author 
     * @Date 2020-02-19
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "/qxLogon.html";
    }

    /**
     * 新增页面
     *
     * @author 
     * @Date 2020-02-19
     */
    @RequestMapping("/add")
    public String add() {
        return PREFIX + "/qxLogon_add.html";
    }

    /**
     * 编辑页面
     *
     * @author 
     * @Date 2020-02-19
     */
    @RequestMapping("/edit")
    public String edit() {
        return PREFIX + "/qxLogon_edit.html";
    }

    /**
     * 新增接口
     *
     * @author 
     * @Date 2020-02-19
     */
    @RequestMapping("/addItem")
    @ResponseBody
    public ResponseData addItem(QxLogonParam qxLogonParam) {
        this.qxLogonService.add(qxLogonParam);
        return ResponseData.success();
    }

    /**
     * 编辑接口
     *
     * @author 
     * @Date 2020-02-19
     */
    @RequestMapping("/editItem")
    @ResponseBody
    public ResponseData editItem(QxLogonParam qxLogonParam) {
        this.qxLogonService.update(qxLogonParam);
        return ResponseData.success();
    }

    /**
     * 删除接口
     *
     * @author 
     * @Date 2020-02-19
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseData delete(QxLogonParam qxLogonParam) {
        this.qxLogonService.delete(qxLogonParam);
        return ResponseData.success();
    }

    /**
     * 查看详情接口
     *
     * @author 
     * @Date 2020-02-19
     */
    @RequestMapping("/detail")
    @ResponseBody
    public ResponseData detail(QxLogonParam qxLogonParam) {
        QxLogon detail = this.qxLogonService.getById(qxLogonParam.getId());
        return ResponseData.success(detail);
    }

    /**
     * 查询列表
     *
     * @author 
     * @Date 2020-02-19
     */
    @ResponseBody
    @RequestMapping("/list")
    public LayuiPageInfo list(QxLogonParam qxLogonParam) {
        return this.qxLogonService.findPageBySpec(qxLogonParam);
    }

}


