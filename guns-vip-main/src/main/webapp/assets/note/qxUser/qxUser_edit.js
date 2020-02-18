/**
 * 详情对话框
 */
var QxUserInfoDlg = {
    data: {
        version: "",
        createdBy: "",
        createdTime: "",
        updatedBy: "",
        updatedTime: "",
        deleted: "",
        mobile: "",
        nickname: "",
        age: "",
        height: "",
        sex: "",
        avatar: "",
        status: "",
        score: "",
        balance: "",
        freeze: "",
        inviteCode: "",
        parentId: ""
    }
};

layui.use(['form', 'admin', 'ax', 'upload'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var upload = layui.upload;

    //让当前iframe弹层高度适应
    // admin.iframeAuto();

    //获取详情信息，填充表单
    var ajax = new $ax(Feng.ctxPath + "/qxUser/detail?id=" + Feng.getUrlParam("id"));
    var result = ajax.start();
    form.val('qxUserForm', result.data);
    $("#img1").attr('src', result.data.avatar);

    // 普通图片上传
    upload.render({
    	elem: '#avatarBtn',
    	url: Feng.ctxPath + "/api/file/upload",
    	before: function(obj) {
    		obj.preview(function(index, file, result){
    			$('#img1').attr('src', result);
    		});
    	},
    	done: function(res) {
    		$('#avatar').val(res.data);
    		Feng.success('图片上传成功');
    	},
    	error: function() {
    		Feng.error('图片上传失败');
    	}
    });
    
    //表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/qxUser/editItem", function (data) {
            Feng.success("更新成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();

        }, function (data) {
            Feng.error("更新失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();

        return false;
    });

});