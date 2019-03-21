/**
 * 系统用户管理js
 */
layui.use([ 'table', 'layer', 'layedit', 'form' ], function() {
	var $ = layui.jquery, table = layui.table, layer = layui.layer;
	var layedit = layui.layedit, form = layui.form;
	// 创建一个编辑器
	var editIndex = layedit.build('LAY_demo_editor');
	// 监听工具条
	table.on('tool(demoEvent)', function(obj) {
		var data = obj.data;
		if (obj.event === 'del') {
			layer.confirm('注:用户删除后将不可恢复<br/>确定删除吗？', function(index) {
				var deleteUrl = $("#deleteUrl").val();
				$.post(deleteUrl, {
					USERS_ID : data.USERS_ID
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
		} else if (obj.event === 'assignRole') {
			layer.open({
				type : 2,
				title : '角色分配',
				area : [ '380px', '440px' ],
				content : [
						'/systemUser/gotoAssignRole?userId=' + data.USERS_ID,
						'yes' ]
			});
		} else if (obj.event === 'UPDATESTAE') {
			var STATE = data.IS_SYSTEM;
			var state = "USER_LEVEL_SYSTEM";
			if (STATE == state) {
				state = "USER_LEVEL_APP";
			}
			$.post("/systemUser/updateState", {
				USERS_ID : data.USERS_ID,
				STATE : state
			}, function(d) {
				if (d > 0) {
					layer.msg("操作成功");
					// 执行重载
					table.reload('systemUserlist', {
						page : {
							curr : 1
						// 重新从第 1 页开始
						},
						where : {}
					});
				} else {
					layer.msg("操作失败");
				}
			});
		} else if (obj.event === 'setNICKNAME') {
			var updateUrl = $("#updateUrl").val();
			if (updateUrl == null) {
				layer.msg("您没有修改权限");
			} else {
				var USERS_ID = data.USERS_ID;
				layer.prompt({
					formType : 2,
					title : '修改 ID 为 [' + USERS_ID + '] 的用户昵称',
					value : data.NICKNAME
				}, function(value, index) {
					layer.close(index);
					// 同步更新表格和缓存对应的值
					$.post(updateUrl, {
						USERS_ID : USERS_ID,
						PASSWORD : data.PASSWORD,
						NICKNAME : value,
					}, function(d) {
						if (d > 0) {
							layer.msg("修改成功");
							obj.update({
								NICKNAME : value
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
			var NICKNAME = $('#NICKNAME').val();
			var PHONE = $('#PHONE').val();
			var STATE = $('#STATE').val();
			// 执行重载
			table.reload('systemUserlist', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					NICKNAME : NICKNAME,
					PHONE : PHONE,
					IS_SYSTEM : STATE
				}
			});
		}
	};
	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
});