/**
 * 权限管理js
 */
layui.use([ 'table', 'form' ], function() {
	var table = layui.table, form = layui.form;
	var $ = layui.jquery;
	// 监听工具条
	table.on('tool(demo)', function(obj) {
		var data = obj.data;
		if (obj.event === 'del') {
			layer.confirm('真的删除行么', function(index) {
				var url = $("#deleteUrl").val();
				$.post(url, {
					PERMISSIONS_ID : data.PERMISSIONS_ID
				}, function(d) {
					if (d > 0) {
						layer.msg("删除成功");
					} else {
						layer.msg("删除失败");
					}
				});
				obj.del();
				layer.close(index);
			});
		} else if (obj.event === 'setPERMISSIONS_URL') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl != null) {
				var PERMISSIONS_ID = data.PERMISSIONS_ID;
				layer.prompt({
					formType : 2,
					title : '修改 [' + data.PERMISSIONS_NAME + '] 的权限名',
					value : data.PERMISSIONS_URL
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						PERMISSIONS_ID : PERMISSIONS_ID,
						PERMISSIONS_URL : value,
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								PERMISSIONS_URL : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			} else {
				layer.msg("您没有修改权限");
			}
		} else if (obj.event === 'setPERMISSIONS_NAME') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl != null) {
				var PERMISSIONS_ID = data.PERMISSIONS_ID;
				layer.prompt({
					formType : 2,
					title : '修改 [' + data.PERMISSIONS_NAME + '] 的权限URL',
					value : data.PERMISSIONS_NAME
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						PERMISSIONS_ID : PERMISSIONS_ID,
						PERMISSIONS_NAME : value,
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								PERMISSIONS_NAME : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			} else {
				layer.msg("您没有修改权限");
			}
		}
	});
	form.on('select(menuPermissions)', function(data) {
		// 执行重载
		table.reload('permissionList', {
			page : {
				curr : 1
			// 重新从第 1 页开始
			},
			where : {
				MENU_ID : data.value,
			}
		});
	});
	var active = {
		addPERMISSIONS : function() {
			layer.open({
				type : 1,
				area : [ '360px', '285px' ],
				content : $('#addbox'),
				btn : [ '确认', '取消' ],
				btnAlign : 'c' // 按钮居中
				,
				shade : 0 // 不显示遮罩
				,
				yes : function() {
					var MENU_ID = $('#MENU_ID').val();
					var addPERMISSIONS_URL = $('#addPERMISSIONS_URL').val();
					var PERMISSIONS_NAME = $('#PERMISSIONS_NAME').val();
					var PERMISSIONS_URL = $("#PERMISSIONS_URL").val();
					var PERMISSIONS_CODE = $("#PERMISSIONS_CODE").val();
					$.post(addPERMISSIONS_URL, {
						MENU_ID : MENU_ID,
						PERMISSIONS_NAME : PERMISSIONS_NAME,
						PERMISSIONS_URL : PERMISSIONS_URL,
						PERMISSIONS_CODE : PERMISSIONS_CODE
					}, function(dd) {
						if (dd > 0) {
							// 执行重载
							table.reload('permissionList', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {
									MENU_ID : MENU_ID
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
			})
			$('#PERMISSIONS_NAME').val("");
			$('#PERMISSIONS_URL').val("");
			$('#PERMISSIONS_CODE').val("");
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
