/**
 * 菜单管理js
 */
layui.use([ 'table', 'layer', 'layedit', 'form' ], function() {
	var $ = layui.jquery, table = layui.table, layer = layui.layer;
	var layedit = layui.layedit, form = layui.form;
	// 创建一个编辑器
	var editIndex = layedit.build('LAY_demo_editor');
	// 监听工具条
	table.on('tool(demoEvent)', function(obj) {
		var data = obj.data;
		if (obj.event === 'queryson') {
			$("#addMenu").css("display", "none");
			$("#queryparent").show();
			var id = data.MENU_ID;
			layer.msg('ID：' + id + '的查看操作');
			// 执行重载
			table.reload('menulist', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					pid : id
				}
			});
		} else if (obj.event === 'del') {
			layer.confirm('注：删除父菜单将会删除该类的全部子菜单<br/>确定删除吗？', function(index) {
				var menuUrl = $("#deleteUrl").val();
				$.post(menuUrl, {
					menuId : data.MENU_ID
				}, function(d) {
					if (d > 0) {
						layer.msg("操作成功");
						obj.del();
						layer.close(index);
					} else {
						layer.msg("操作失败");
					}
				});
			});
		} else if (obj.event === 'edit') {
			// 多窗口模式，层叠置顶
			layer.open({
				type : 1,
				area : [ '360px', '235px' ],
				content : $('#addbox'),
				btn : [ '确认', '取消' ],
				btnAlign : 'c' // 按钮居中
				,
				shade : 0 // 不显示遮罩
				,
				yes : function() {
					var menuName = $('#MENU_NAME').val();
					var menuUrl = $('#menuUrl').val();
					var addPERMISSIONS_URL = $('#addPERMISSIONS_URL').val();
					$.post(addPERMISSIONS_URL, {
						MENU_NAME : menuName,
						PID : data.MENU_ID,
						MENU_Url : menuUrl
					}, function(d) {
						if (d > 0) {
							// 执行重载
							table.reload('menulist', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {
									pid : 0
								}
							});
						} else {
							layer.msg("系统错误，添加失败！！");
						}
					});
					var menuName = $('#MENU_NAME').val("");
					var menuUrl = $('#menuUrl').val("javascript:;");
					layer.closeAll();
				},
				btn2 : function() {
					var menuName = $('#MENU_NAME').val("");
					var menuUrl = $('#menuUrl').val("javascript:;");
					layer.closeAll();
				}
			});
		} else if (obj.event === 'setMENU_URL') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var menuId = data.MENU_ID;
				layer.prompt({
					formType : 2,
					title : '修改 ID 为 [' + menuId + '] 的菜单链接',
					value : data.MENU_URL
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						MENU_ID : menuId,
						MENU_NAME : data.MENU_NAME,
						MENU_URL : value
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								MENU_URL : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			}
		} else if (obj.event === 'setMENU_SORT') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var menuId = data.MENU_ID;
				layer.prompt({
					formType : 2,
					title : '修改菜单排序,越小越排序越先',
					value : data.MENU_SORT
				}, function(value, index) {
					var msger = "^\\d+$";
					if (value.match(msger)) {
						// 同步更新表格和缓存对应的值
						$.post("/menu/updateSort", {
							MENU_ID : menuId,
							MENU_SORT : value
						}, function(d) {
							if (d > 0) {
								layer.msg("修改成功");
								obj.update({
									MENU_SORT : value
								});
							} else {
								layer.msg("系统错误，修改失败");
							}
						});
						layer.close(index);
					} else {
						layer.msg("请输入一个正整数");
					}

				});
			}
		} else if (obj.event === 'setMENU_NAME') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var menuId = data.MENU_ID;
				layer.prompt({
					formType : 2,
					title : '修改 ID 为 [' + menuId + '] 的菜单名称',
					value : data.MENU_NAME
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						MENU_ID : menuId,
						MENU_NAME : value,
						MENU_URL : data.MENU_URL
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								MENU_NAME : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			}
		}
	});
	var active = {
		reload : function() {
			var demoReload = $('#menuName');
			// 执行重载
			table.reload('menulist', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					MENU_NAME : demoReload.val()
				}
			});
		},
		reParentMenu : function() {
			$("#addMenu").show();
			$("#queryparent").css("display", "none");
			// 执行重载
			table.reload('menulist', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					pid : 0
				}
			});
		},
		addParentMenu : function(othis) {
			var type = othis.data('type'), text = othis.text();
			layer.open({
				type : 1,
				offset : type // 具体配置参考：http://www.layui.com/doc/modules/layer.html#offset
				,
				id : 'layerDemo' + type // 防止重复弹出
				,
				area : [ '360px', '235px' ],
				content : $('#addbox'),
				btn : [ '确认', '取消' ],
				btnAlign : 'c' // 按钮居中
				,
				shade : 0 // 不显示遮罩
				,
				yes : function() {
					var menuName = $('#MENU_NAME').val();
					var menuUrl = $('#menuUrl').val();
					var addPERMISSIONS_URL = $('#addPERMISSIONS_URL').val();
					$.post(addPERMISSIONS_URL, {
						MENU_NAME : menuName,
						PID : '0',
						MENU_Url : menuUrl
					}, function(d) {
						if (d > 0) {
							// 执行重载
							table.reload('menulist', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {
									pid : 0
								}
							});
						} else {
							layer.msg("系统错误，添加失败！！");
						}
					});
					layer.closeAll();
				},
				btn2 : function() {
					layer.closeAll();
				}
			});
			var menuName = $('#MENU_NAME').val("");
			var menuUrl = $('#menuUrl').val("javascript:;");
		}
	};

	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	$('#addMenu .layui-btn').on('click', function() {
		var othis = $(this), method = othis.data('method');
		active[method] ? active[method].call(this, othis) : '';
	});
});