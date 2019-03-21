/**
 * 地址管理js
 */
layui.use([ 'table', 'layer', 'layedit', 'form' ], function() {
	var $ = layui.jquery, table = layui.table, layer = layui.layer;
	var layedit = layui.layedit, form = layui.form;
	// 监听工具条
	table.on('tool(demoEvent)', function(obj) {
		var data = obj.data;
		if (obj.event === 'queryson') {
			$("#queryparent").show();
			var id = data.CITY_CODE;
			$("#addaddressPid").val(id);
			layer.msg('ID：' + id + '的查看操作');
			// 执行重载
			table.reload('addresslist', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					PID : id
				}
			});
		} else if (obj.event === 'del') {
			layer.confirm('注：删除后将不可恢复<br/>确定删除吗？', function(index) {
				var addressUrl = $("#deleteUrl").val();
				$.post(addressUrl, {
					CITY_CODE : data.CITY_CODE
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
		} else if (obj.event === 'setCITY_NAME') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var CITY_CODE = data.CITY_CODE;
				layer.prompt({
					formType : 2,
					title : '修改 [' + data.CITY_PY + '] 的地区名吗',
					value : data.CITY_NAME
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						CITY_CODE : CITY_CODE,
						CITY_NAME : value,
						CITY_PY : data.CITY_PY,
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								CITY_NAME : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			}
		} else if (obj.event === 'setCITY_PY') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var CITY_CODE = data.CITY_CODE;
				layer.prompt({
					formType : 2,
					title : '修改 ID 为 [' + CITY_CODE + '] 的字典类型',
					value : data.CITY_PY
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						CITY_CODE : CITY_CODE,
						CITY_PY : value,
						CITY_NAME : data.CITY_NAME,
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								CITY_PY : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			}
		} else if (obj.event === "addsonaddress") {
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
					var CITY_NAME = $('#CITY_NAME').val();
					var CITY_PY = $('#CITY_PY').val();
					var addPERMISSIONS_URL = $('#addPERMISSIONS_URL').val();
					$.post(addPERMISSIONS_URL, {
						CITY_NAME : CITY_NAME,
						CITY_PY : CITY_PY,
						PID : data.CITY_CODE,
					}, function(dd) {
						if (dd > 0) {
							// 执行重载
							table.reload('addresslist', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {
									pid : data.CITY_CODE
								}
							});
						} else {
							layer.msg("系统错误，添加失败！！");
						}
					});
					$.post(addPERMISSIONS_URL, {

					}, function(d) {

					});
					layer.closeAll();
				},
				btn2 : function() {
					layer.closeAll();
				}
			})
			$('#address_TYPE').val("");
			$('#address_VALUE').val("");
			$('#DESCRIBES').val("");
		}
	});
	var active = {
		reload : function() {
			var demoReload = $('#addressName');
			// 执行重载
			table.reload('addresslist', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					CITY_NAME : demoReload.val()
				}
			});
		},
		reParentMenu : function() {
			var pid = $("#addaddressPid").val();
			$.post("/address/queryaddressParentId", {
				PID : pid
			}, function(d) {
				if (d == 1) {
					$("#queryparent").css("display", "none");
				}
				// 执行重载
				table.reload('addresslist', {
					page : {
						curr : 1
					// 重新从第 1 页开始
					},
					where : {
						PID : d
					}
				});
				$("#addaddressPid").val(d);
			});
		},
		addaddress : function() {
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
					var CITY_NAME = $('#CITY_NAME').val();
					var CITY_PY = $('#CITY_PY').val();
					var addPERMISSIONS_URL = $('#addPERMISSIONS_URL').val();
					var pid = $("#addaddressPid").val();
					$.post(addPERMISSIONS_URL, {
						CITY_NAME : CITY_NAME,
						CITY_PY : CITY_PY,
						PID : pid
					}, function(dd) {
						if (dd > 0) {
							// 执行重载
							table.reload('addresslist', {
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
			})
			$('#CITY_NAME').val("");
			$('#CITY_PY').val("");
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