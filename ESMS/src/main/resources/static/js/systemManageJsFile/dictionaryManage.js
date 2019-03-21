/**
 * 字典管理js
 */
layui.use([ 'table', 'layer', 'layedit', 'form' ], function() {
	var $ = layui.jquery, table = layui.table, layer = layui.layer;
	var layedit = layui.layedit, form = layui.form;
	// 监听工具条
	table.on('tool(demoEvent)', function(obj) {
		var data = obj.data;
		if (obj.event === 'queryson') {
			$("#queryparent").show();
			var id = data.DICTIONARY_ID;
			$("#addDictionaryPid").val(id);
			layer.msg('ID：' + id + '的查看操作');
			// 执行重载
			table.reload('dictionarylist', {
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
				var dictionaryUrl = $("#deleteUrl").val();
				$.post(dictionaryUrl, {
					DICTIONARY_ID : data.DICTIONARY_ID
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
		} else if (obj.event === 'setDICTIONARY_VALUE') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var DICTIONARY_ID = data.DICTIONARY_ID;
				layer.prompt({
					formType : 2,
					title : '修改 ID 为 [' + DICTIONARY_ID + '] 的字典值',
					value : data.DICTIONARY_VALUE
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						DICTIONARY_ID : DICTIONARY_ID,
						DICTIONARY_TYPE : data.DICTIONARY_TYPE,
						DESCRIBES : data.DESCRIBES,
						DICTIONARY_VALUE : value
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								DICTIONARY_VALUE : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			}
		} else if (obj.event === 'setDICTIONARY_TYPE') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var DICTIONARY_ID = data.DICTIONARY_ID;
				layer.prompt({
					formType : 2,
					title : '修改 ID 为 [' + DICTIONARY_ID + '] 的字典类型',
					value : data.DICTIONARY_TYPE
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						DICTIONARY_ID : DICTIONARY_ID,
						DICTIONARY_TYPE : value,
						DESCRIBES : data.DESCRIBES,
						DICTIONARY_VALUE : data.DICTIONARY_VALUE
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								DICTIONARY_TYPE : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			}
		} else if (obj.event === 'setDESCRIBES') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var DICTIONARY_ID = data.DICTIONARY_ID;
				layer.prompt({
					formType : 2,
					title : '修改 ID 为 [' + DICTIONARY_ID + '] 的字典描述',
					value : data.DESCRIBES
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						DICTIONARY_ID : DICTIONARY_ID,
						DICTIONARY_TYPE : data.DICTIONARY_TYPE,
						DESCRIBES : value,
						DICTIONARY_VALUE : data.DICTIONARY_VALUE
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								DESCRIBES : value
							});
						} else {
							layer.msg("系统错误，修改失败");
						}
					});
				});
			}
		} else if (obj.event === "addsondictionary") {
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
					var DICTIONARY_TYPE = $('#DICTIONARY_TYPE').val();
					var DICTIONARY_VALUE = $('#DICTIONARY_VALUE').val();
					var DESCRIBES = $("#DESCRIBES").val();
					var addPERMISSIONS_URL = $('#addPERMISSIONS_URL').val();
					$.post(addPERMISSIONS_URL, {
						DICTIONARY_TYPE : DICTIONARY_TYPE,
						DESCRIBES : DESCRIBES,
						PID : data.DICTIONARY_ID,
						DICTIONARY_VALUE : DICTIONARY_VALUE
					}, function(dd) {
						if (dd > 0) {
							// 执行重载
							table.reload('dictionarylist', {
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
			$('#DICTIONARY_TYPE').val("");
			$('#DICTIONARY_VALUE').val("");
			$('#DESCRIBES').val("");
		}
	});
	var active = {
		reload : function() {
			var demoReload = $('#dictionaryName');
			// 执行重载
			table.reload('dictionarylist', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					DESCRIBES : demoReload.val()
				}
			});
		},
		reParentMenu : function() {
			var pid = $("#addDictionaryPid").val();
			$.post("/dictionary/queryDictionaryParentId", {
				PID : pid
			}, function(d) {
				if (d == '0') {
					$("#queryparent").css("display", "none");
				}
				// 执行重载
				table.reload('dictionarylist', {
					page : {
						curr : 1
					// 重新从第 1 页开始
					},
					where : {
						PID : d
					}
				});
				$("#addDictionaryPid").val(d);
			});
		},
		addDictionary : function() {
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
					var DICTIONARY_TYPE = $('#DICTIONARY_TYPE').val();
					var DICTIONARY_VALUE = $('#DICTIONARY_VALUE').val();
					var DESCRIBES = $("#DESCRIBES").val();
					var addPERMISSIONS_URL = $('#addPERMISSIONS_URL').val();
					var pid = $("#addDictionaryPid").val();
					$.post(addPERMISSIONS_URL, {
						DICTIONARY_TYPE : DICTIONARY_TYPE,
						DESCRIBES : DESCRIBES,
						PID : pid,
						DICTIONARY_VALUE : DICTIONARY_VALUE
					}, function(dd) {
						if (dd > 0) {
							// 执行重载
							table.reload('dictionarylist', {
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
			$('#DICTIONARY_TYPE').val("");
			$('#DICTIONARY_VALUE').val("");
			$('#DESCRIBES').val("");
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