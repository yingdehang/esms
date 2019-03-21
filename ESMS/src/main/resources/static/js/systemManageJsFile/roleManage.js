/**
 * 角色管理js
 */
layui
		.use(
				[ 'table', 'layer', 'layedit', 'form', 'element' ],
				function() {
					var $ = layui.jquery, table = layui.table, layer = layui.layer;
					var layedit = layui.layedit, form = layui.form, element = layui.element;
					// 创建一个编辑器
					var editIndex = layedit.build('LAY_demo_editor');
					var roleName, menuName;
					// 监听工具条
					table
							.on(
									'tool(demoEvent)',
									function(obj) {
										var data = obj.data;
										if (obj.event === 'del') {
											layer
													.confirm(
															'注:角色删除后将不可恢复<br/>确定删除吗？',
															function(index) {
																var RoleUrl = $(
																		"#deleteUrl")
																		.val();
																$
																		.post(
																				RoleUrl,
																				{
																					ROLE_ID : data.ROLE_ID
																				},
																				function(
																						d) {
																					if (d > 0) {
																						layer
																								.msg("操作成功");
																						obj
																								.del();
																						layer
																								.close(index);
																					} else {
																						layer
																								.msg("操作失败");
																					}
																				});
															});
										}
										// 查询父id为0的主菜单
										else if (obj.event === 'seeRoleMenuPermissions') {
											// 删除tab项
											element.tabDelete('demo', '1');
											element.tabDelete('demo', '2');
											element.tabDelete('demo', '3');

											roleName = data.ROLE_NAME;
											// 新增一个Tab项
											element
													.tabAdd(
															'demo',
															{
																title : '【角色：'
																		+ roleName
																		+ '】的主菜单权限',
																content : '<table class="layui-hide" id="menupermissions" lay-filter="demoEvent"></table>',
																id : 1
															})
											// 切换到这个tab
											element.tabChange('demo', '1');

											var roleId = data.ROLE_ID;
											$("#roleId").val(roleId);
											var assignUrl = $("#assignUrl")
													.val();
											// 方法级渲染
											table
													.render({
														elem : '#menupermissions',
														url : '/role/getrolemenulist/',
														cellMinWidth : 80,
														cols : [ [
																{
																	field : 'MENU_NAME',
																	title : '菜单名称',
																},
																{
																	title : '是否分配此菜单',
																	toolbar : '#ishaveparentmenu'
																} ] ],
														where : {
															roleId : roleId
														}
													});
										}
										// 通過主菜单的id查询子菜单
										else if (obj.event === 'seeMenuSonPermissions') {
											element.tabDelete('demo', '2');
											element.tabDelete('demo', '3');

											menuName = data.MENU_NAME;
											// 新增一个Tab项
											element
													.tabAdd(
															'demo',
															{
																title : '【主菜单：'
																		+ menuName
																		+ '】的二级菜单权限',
																content : '<table class="layui-hide" id="menuSonPermissions" lay-filter="demoEvent"></table>',
																id : 2
															})
											// 切换到这个tab
											element.tabChange('demo', '2');
											var roleId = $("#roleId").val();
											// 方法级渲染
											table.render({
												elem : '#menuSonPermissions',
												url : '/role/getrolemenulist/',
												cellMinWidth : 80,
												cols : [ [ {
													field : 'MENU_NAME',
													title : '菜单名称',
												}, {
													title : '是否分配此菜单',
													toolbar : '#ishavemenu'
												} ] ],
												where : {
													pid : data.MENU_ID,
													roleId : roleId
												}
											});
										}
										// 查看并分配子菜单权限
										else if (obj.event === 'seePermissions') {
											element.tabDelete('demo', '3');
											// 新增一个Tab项
											element
													.tabAdd(
															'demo',
															{
																title : '【二级菜单：'
																		+ data.MENU_NAME
																		+ '】的操作权限',
																content : '<table class="layui-hide" id="permissions" lay-filter="demoEvent"></table>',
																id : 3
															})
											// 切换到这个tab
											element.tabChange('demo', '3');

											var roleId = $("#roleId").val();
											// 方法级渲染
											table
													.render({
														elem : '#permissions',
														url : '/role/getrolemenupermissions/',
														cellMinWidth : 80,
														cols : [ [
																{
																	field : 'PERMISSIONS_NAME',
																	title : '权限名称',
																},
																{
																	title : '是否分配此权限',
																	toolbar : '#menuPermissions'
																} ] ],
														where : {
															menuId : data.MENU_ID,
															roleId : roleId
														}
													});
										}
										// 修改菜单名称
										else if (obj.event === 'setROLR_NAME') {
											var updateUrl = $("#updateUrl")
													.val();
											if (updateUrl == null) {
												layer.msg("您没有修改权限");
											} else {
												var ROLE_ID = data.ROLE_ID;
												layer
														.prompt(
																{
																	formType : 2,
																	title : '修改 ID 为 ['
																			+ ROLE_ID
																			+ '] 的角色名称',
																	value : data.ROLE_NAME
																},
																function(value,
																		index) {
																	layer
																			.close(index);
																	// 同步更新表格和缓存对应的值
																	$
																			.post(
																					updateUrl,
																					{
																						ROLE_ID : ROLE_ID,
																						ROLE_NAME : value,
																					},
																					function(
																							d) {
																						if (d > 0) {
																							layer
																									.msg("修改成功");
																							obj
																									.update({
																										ROLE_NAME : value
																									});
																						} else {
																							layer
																									.msg("系统错误，修改失败");
																						}
																					});
																});
											}
										}
									});
					var active = {
						reload : function() {
							$("#menupermissions")
									.html(
											'<table class="layui-hide" id="menupermissions" lay-filter="demoEvent"></table>');
							var demoReload = $('#roleName').val();
							// 执行重载
							table.reload('rolelist', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {
									ROLE_NAME : demoReload
								}
							});
						},
						addRole : function() {
							$(".layui-hide").html("");
							layer.open({
								type : 1,
								area : [ '360px', '178px' ],
								content : $('#addbox'),
								btn : [ '确认', '取消' ],
								btnAlign : 'c' // 按钮居中
								,
								shade : 0 // 不显示遮罩
								,
								yes : function() {
									var ROLE_NAME = $('#ROLE_NAME').val();
									var addPERMISSIONS_URL = $(
											"#addPERMISSIONS_URL").val();
									$.post(addPERMISSIONS_URL, {
										ROLE_NAME : ROLE_NAME
									}, function(dd) {
										if (dd > 0) {
											// 执行重载
											table.reload('rolelist', {
												page : {
													curr : 1
												// 重新从第 1 页开始
												},
												where : {}
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
							$('#ROLE_NAME').val("");
						},
						fanhui : function() {

						}
					};
					// 监听指定开关
					form.on('switch(switchTest)', function(data) {
						var is = this.checked;
						roleId = $("#roleId").val();
						if (is) {
							$.post("/role/addRolePermissions", {
								permissionsId : data.value,
								roleId : roleId
							}, function(d) {
								if (d > 0) {
									layer.msg("操作成功");
								} else {
									layer.msg("操作失败");
								}
							});
						} else {
							$.post("/role/deleteRolePermissions", {
								permissionsId : data.value,
								roleId : roleId
							}, function(d) {
								if (d > 0) {
									layer.msg("取消角色权限成功");
								} else {
									layer.msg("操作失败");
								}
							});
						}
					});
					$('.demoTable .layui-btn').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});
					$('#addrole .layui-btn').on('click', function() {
						var othis = $(this), method = othis.data('method');
						active[method] ? active[method].call(this, othis) : '';
					});
				});
