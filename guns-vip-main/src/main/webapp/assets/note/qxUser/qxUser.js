layui.use(['table', 'admin', 'ax', 'func'], function () {
    var $ = layui.$;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;

    /**
     * 用户表管理
     */
    var QxUser = {
        tableId: "qxUserTable"
    };

    /**
     * 初始化表格的列
     */
    QxUser.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, title: '标识'},
            {field: 'nickname', sort: false, title: '昵称'},
            {field: 'mobile', sort: false, title: '手机'},
            {field: 'sex', sort: true, title: '性别', width: 100, templet: function(d){
            	return d.sex == 0 ? '男' : '女';
            }},
            {field: 'score', sort: true, width: 100, title: '信用分'},
            {field: 'balance', sort: true, width: 150, title: '金币'},
            {field: 'createdTime', sort: true, title: '创建时间'},
            {align: 'center', field: 'status', sort: true, title: '状态', width: 100, templet: '#statusTpl'},
            {align: 'center', toolbar: '#tableBar', title: '操作'}
        ]];
    };

    /**
     * 点击查询按钮
     */
    QxUser.search = function () {
        var queryData = {};
        queryData['nickname'] = $("#nickname").val();
        queryData['mobile'] = $("#mobile").val();
        table.reload(QxUser.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    /**
     * 弹出添加对话框
     */
    QxUser.openAddDlg = function () {
        func.open({
            title: '添加用户表',
            content: Feng.ctxPath + '/qxUser/add',
            tableId: QxUser.tableId
        });
    };

    /**
    * 点击编辑
    *
    * @param data 点击按钮时候的行数据
    */
    QxUser.openEditDlg = function (data) {
        func.open({
            title: '修改用户表',
            content: Feng.ctxPath + '/qxUser/edit?id=' + data.id,
            tableId: QxUser.tableId
        });
    };

    /**
     * 导出excel按钮
     */
    QxUser.exportExcel = function () {
        var checkRows = table.checkStatus(QxUser.tableId);
        if (checkRows.data.length === 0) {
            Feng.error("请选择要导出的数据");
        } else {
            table.exportFile(tableResult.config.id, checkRows.data, 'xls');
        }
    };

    /**
     * 点击删除
     *
     * @param data 点击按钮时候的行数据
     */
    QxUser.onDeleteItem = function (data) {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/qxUser/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(QxUser.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除?", operation);
    };

    QxUser.changeUserStatus = function(id, checked) {
    	var ajax = new $ax(Feng.ctxPath + "/qxUser/changeUserStatus", function(data) {
    		Feng.success("修改状态成功!");
    	}, function(data) {
    		Feng.error("修改状态失败!");
    		table.reload(QxUser.tableId);
    	});
    	ajax.set({'id': id, 'status': checked});
    	ajax.start();
    }
    
    /**
     * 平台为用户充值
     */
    QxUser.chargeUser = function(data) {
    	func.open({
            title: '用户充值',
            content: Feng.ctxPath + '/qxUser/charge?userId=' + data.id,
            tableId: QxUser.tableId
        });
    }
    
    QxUser.chargeRecord = function(data) {
    	func.open({
    		title: '充值记录',
    		content: Feng.ctxPath + '/qxUser/chargeRecord?mobile=' + data.mobile,
    		tableId: QxUser.tableId
    	});
    }
    // 渲染表格
    var tableResult = table.render({
        elem: '#' + QxUser.tableId,
        url: Feng.ctxPath + '/qxUser/list',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: QxUser.initColumn()
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        QxUser.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        QxUser.openAddDlg();
    });

    // 导出excel
    $('#btnExp').click(function () {
        QxUser.exportExcel();
    });

    // 工具条点击事件
    table.on('tool(' + QxUser.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            QxUser.openEditDlg(data);
        } else if (layEvent === 'delete') {
            QxUser.onDeleteItem(data);
        } else if (layEvent === 'chargeUser') {
        	QxUser.chargeUser(data);
        } else if (layEvent === 'chargeRecord') {
        	QxUser.chargeRecord(data);
        }
    });
    
    form.on('switch(status)', function(obj){
    	var id = obj.elem.value;
    	var checked = obj.elem.checked ? '0':'1';
    	QxUser.changeUserStatus(id, checked);
    });
});
