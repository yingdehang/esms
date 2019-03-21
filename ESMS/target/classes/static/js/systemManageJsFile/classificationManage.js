/**
 * 线上商品分类管理js
 */
var classification = 1;
layui
		.use(
				[ 'table', 'layer', 'layedit', 'form', 'upload' ],
				function() {
					var $ = layui.jquery, table = layui.table, layer = layui.layer;
					var layedit = layui.layedit, form = layui.form, upload = layui.upload;
					var oneClass = $("#oneClass"), twoClass = $("#twoClass"), threeClass = $("#threeClass");
					twoClass.hide();
					threeClass.hide();
					table.render({
						elem : '#classificationlist',
						url : '/classification/getClassIficationList/',
						page : true,
						id : 'classificationlist',
						cols : [ [ {
							title : '编号',
							fixed : 'right',
							align : 'center',
							align : 'center',
							toolbar : '#indexunmber'
						}, {
							title : '分类名称',
							field : 'CLASS_NAME',
							align : 'center',
							event : 'setCLASS_NAME'
						}, {
							title : 'icon/banner',
							field : 'ICON_BANNER',
							align : 'center',
							event : 'setICON_BANNER',
							toolbar : '#banner'
						}, {
							title : '操作',
							fixed : 'right',
							align : 'center',
							toolbar : '#chaozuo'
						} ] ]
					});
					// 监听工具条
					table
							.on(
									'tool(demoEvent)',
									function(obj) {
										var data = obj.data;
										if (obj.event === 'queryson') {
											$("#queryparent").show();
											++classification;
											if (classification == 3) {
												$(".addsonbutton").hide();
												twoClass.text(" >>"
														+ data.CLASS_NAME);
												threeClass.show();
											} else if (classification == 2) {
												oneClass.text(data.CLASS_NAME);
												twoClass.show();
											}
											var id = data.CLASSIFICATION_ID;
											$("#addclassificationPid").val(id);

											if (classification == 2) {
												table
														.render({
															elem : '#classificationlist',
															url : '/classification/getClassIficationList/',
															page : true,
															id : 'classificationlist',
															where : {
																PID : id
															},
															cols : [ [
																	{
																		title : '编号',
																		fixed : 'right',
																		align : 'center',
																		align : 'center',
																		toolbar : '#indexunmber'
																	},
																	{
																		title : '分类名称',
																		field : 'CLASS_NAME',
																		align : 'center',
																		event : 'setCLASS_NAME'
																	},
																	{
																		title : '操作',
																		fixed : 'right',
																		align : 'center',
																		toolbar : '#chaozuo'
																	} ] ]
														});
											} else {
												table
														.render({
															elem : '#classificationlist',
															url : '/classification/getClassIficationList/',
															page : true,
															id : 'classificationlist',
															where : {
																PID : id
															},
															cols : [ [
																	{
																		title : '编号',
																		fixed : 'right',
																		align : 'center',
																		align : 'center',
																		toolbar : '#indexunmber'
																	},
																	{
																		title : '分类名称',
																		field : 'CLASS_NAME',
																		align : 'center',
																		event : 'setCLASS_NAME'
																	},
																	{
																		title : 'icon/banner',
																		field : 'ICON_BANNER',
																		align : 'center',
																		event : 'setICON_BANNER',
																		toolbar : '#banner'
																	},
																	{
																		title : '操作',
																		fixed : 'right',
																		align : 'center',
																		toolbar : '#chaozuo'
																	} ] ]
														});
											}

										} else if (obj.event === 'del') {
											layer
													.confirm(
															'注：删除后将不可恢复<br/>确定删除吗？',
															function(index) {
																var classificationUrl = $(
																		"#deleteUrl")
																		.val();
																$
																		.post(
																				classificationUrl,
																				{
																					CLASSIFICATION_ID : data.CLASSIFICATION_ID
																				},
																				function(
																						d) {
																					if (d == 0) {
																						layer
																								.msg("操作成功");
																						obj
																								.del();
																						layer
																								.close(index);
																					} else if (d >= 1) {
																						layer
																								.msg("该类别下还有商品，无法删除");
																					} else {
																						layer
																								.msg("操作失败");
																					}
																				});
															});
										}
										// 修改分类名
										else if (obj.event === 'setCLASS_NAME') {
											var updateUrl = $("#updateUrl")
													.val();
											if (updateUrl == null) {
												layer.msg("您没有修改权限");
											} else {
												var CLASSIFICATION_ID = data.CLASSIFICATION_ID;
												layer
														.prompt(
																{
																	formType : 2,
																	title : '修改 ID 为 ['
																			+ CLASSIFICATION_ID
																			+ '] 的字典值',
																	value : data.CLASS_NAME
																},
																function(value,
																		index) {
																	$
																			.post(
																					updateUrl,
																					{
																						CLASSIFICATION_ID : CLASSIFICATION_ID,
																						ICON_BANNER : data.ICON_BANNER,
																						CLASS_NAME : value
																					},
																					function(
																							d) {
																						if (d > 0) {
																							layer
																									.msg("修改成功");
																							obj
																									.update({
																										CLASS_NAME : value
																									});
																						} else {
																							layer
																									.msg("系统错误，修改失败");
																						}
																					});
																	layer
																			.close(index);
																});
											}
										}
										// 修改banner
										else if (obj.event === 'setICON_BANNER') {
											var updateUrl = $("#updateUrl")
													.val();
											if (updateUrl == null) {
												layer.msg("您没有修改权限");
											} else {
												var CLASSIFICATION_ID = data.CLASSIFICATION_ID;
												$("#updatehide").hide();
												$("#istwo").show();
												var deleteBanner = data.ICON_BANNER;
												$("#showuploadBanner")
														.html(
																'<img id="showuploadBanner" src="'
																		+ deleteBanner
																		+ '" width="40px" height="40px" />');
												layer
														.open({
															type : 1,
															title : '修改商品分类banner',
															area : [ '360px',
																	'185px' ],
															content : $('#addbox'),
															btn : [ '确认', '取消' ],
															btnAlign : 'c' // 按钮居中
															,
															shade : 0 // 不显示遮罩
															,
															yes : function() {
																var ICON_BANNER = $(
																		'#ICON_BANNER')
																		.val();
																$
																		.post(
																				updateUrl,
																				{
																					CLASSIFICATION_ID : CLASSIFICATION_ID,
																					CLASS_NAME : data.CLASS_NAME,
																					ICON_BANNER : ICON_BANNER,
																					deleteBanner : deleteBanner
																				},
																				function(
																						dd) {
																					if (dd > 0) {
																						// 执行重载
																						table
																								.reload(
																										'classificationlist',
																										{
																											page : {
																												curr : 1
																											},
																											where : {
																												pid : data.CLASSIFICATION_ID
																											}
																										});
																					} else {
																						layer
																								.msg("系统错误，添加失败！！");
																					}
																				});
																layer
																		.closeAll();
																$("#updatehide")
																		.show();
															},
															btn2 : function() {
																layer
																		.closeAll();
																$("#updatehide")
																		.show();
															}
														});
												$('#CLASSIFICATION_NAME').val(
														"");
												$('#ICON_BANNER').val("");
												$("#showuploadBanner")
														.html(
																'<i class="layui-icon" id="showuploadBanner" style="font-size: 30px;line-height: 100%;">&#xe64a;</i>');
											}
										} else if (obj.event === "addsonclassification") {
											if (classification == 1) {
												$("#updatehide").show();
												$("#istwo").hide();
												layer
														.open({
															type : 1,
															area : [ '360px',
																	'185px' ],
															content : $('#addbox'),
															btn : [ '确认', '取消' ],
															btnAlign : 'c' // 按钮居中
															,
															shade : 0 // 不显示遮罩
															,
															yes : function() {
																var CLASSIFICATION_NAME = $(
																		'#CLASSIFICATION_NAME')
																		.val();
																var ICON_BANNER = $(
																		'#ICON_BANNER')
																		.val();
																var addPERMISSIONS_URL = $(
																		'#addPERMISSIONS_URL')
																		.val();
																$
																		.post(
																				addPERMISSIONS_URL,
																				{
																					CLASS_NAME : CLASSIFICATION_NAME,
																					ICON_BANNER : ICON_BANNER,
																					PID : data.CLASSIFICATION_ID,
																				},
																				function(
																						dd) {
																					if (dd > 0) {
																						// 执行重载
																						table
																								.reload(
																										'classificationlist',
																										{
																											page : {
																												curr : 1
																											},
																											where : {
																												pid : data.CLASSIFICATION_ID
																											}
																										});
																					} else {
																						layer
																								.msg("系统错误，添加失败！！");
																					}
																				});
																layer
																		.closeAll();
															},
															btn2 : function() {
																layer
																		.closeAll();
															}
														});
												$('#CLASSIFICATION_NAME').val(
														"");
												$('#ICON_BANNER').val("");
												$("#showuploadBanner")
														.html(
																'<i class="layui-icon" id="showuploadBanner" style="font-size: 30px;line-height: 100%;">&#xe64a;</i>');
											} else if (classification == 2) {
												$("#istwo").show();
												layer
														.open({
															type : 1,
															area : [ '360px',
																	'238px' ],
															content : $('#addbox'),
															btn : [ '确认', '取消' ],
															btnAlign : 'c' // 按钮居中
															,
															shade : 0 // 不显示遮罩
															,
															yes : function() {
																var CLASSIFICATION_NAME = $(
																		'#CLASSIFICATION_NAME')
																		.val();
																var ICON_BANNER = $(
																		'#ICON_BANNER')
																		.val();
																var addPERMISSIONS_URL = $(
																		'#addPERMISSIONS_URL')
																		.val();
																$
																		.post(
																				addPERMISSIONS_URL,
																				{
																					CLASS_NAME : CLASSIFICATION_NAME,
																					ICON_BANNER : ICON_BANNER,
																					PID : data.CLASSIFICATION_ID,
																				},
																				function(
																						dd) {
																					if (dd > 0) {
																						// 执行重载
																						table
																								.reload(
																										'classificationlist',
																										{
																											page : {
																												curr : 1
																											},
																											where : {
																												pid : data.CLASSIFICATION_ID
																											}
																										});
																					} else {
																						layer
																								.msg("系统错误，添加失败！！");
																					}
																				});
																layer
																		.closeAll();
															},
															btn2 : function() {
																layer
																		.closeAll();
															}
														});
												$('#CLASSIFICATION_NAME').val(
														"");
												$('#ICON_BANNER').val("");
												$("#showuploadBanner")
														.html(
																'<i class="layui-icon" id="showuploadBanner" style="font-size: 30px;line-height: 100%;">&#xe64a;</i>');
											} else if (classification == 3) {
												layer.msg("当前为第三级分类，无法添加子类");
											}
										}
									});
					var active = {
						reload : function() {
							var demoReload = $('#classificationName').val();
							// 执行重载
							table.reload('classificationlist', {
								page : {
									curr : 1
								},
								where : {
									CLASS_NAME : demoReload
								}
							});
						},
						reParentMenu : function() {
							var pid = $("#addclassificationPid").val();
							--classification;
							if (classification == 2) {
								twoClass.text(" >>二级分类");
								threeClass.hide();
							} else if (classification == 1) {
								oneClass.text("一级分类");
								twoClass.hide();
							}
							$
									.post(
											"/classification/queryclassificationParentId",
											{
												PID : pid
											},
											function(d) {
												if (classification == 1) {
													$("#queryparent").css(
															"display", "none");
												}
												// 执行重载
												if (classification == 2) {
													table
															.render({
																elem : '#classificationlist',
																url : '/classification/getClassIficationList/',
																page : true,
																id : 'classificationlist',
																where : {
																	PID : d
																},
																cols : [ [
																		{
																			title : '编号',
																			fixed : 'right',
																			align : 'center',
																			align : 'center',
																			toolbar : '#indexunmber'
																		},
																		{
																			title : '分类名称',
																			field : 'CLASS_NAME',
																			align : 'center',
																			event : 'setCLASS_NAME'
																		},
																		{
																			title : '操作',
																			fixed : 'right',
																			align : 'center',
																			toolbar : '#chaozuo'
																		} ] ]
															});
												} else {
													table
															.render({
																elem : '#classificationlist',
																url : '/classification/getClassIficationList/',
																page : true,
																id : 'classificationlist',
																where : {
																	PID : d
																},
																cols : [ [
																		{
																			title : '编号',
																			fixed : 'right',
																			align : 'center',
																			align : 'center',
																			toolbar : '#indexunmber'
																		},
																		{
																			title : '分类名称',
																			field : 'CLASS_NAME',
																			align : 'center',
																			event : 'setCLASS_NAME'
																		},
																		{
																			title : 'icon/banner',
																			field : 'ICON_BANNER',
																			align : 'center',
																			event : 'setICON_BANNER',
																			toolbar : '#banner'
																		},
																		{
																			title : '操作',
																			fixed : 'right',
																			align : 'center',
																			toolbar : '#chaozuo'
																		} ] ]
															});
												}
												$("#addclassificationPid").val(
														d);
											});
						},
						addclassification : function() {
							if (classification == 1 || classification == 3) {
								$("#istwo").show();
								layer.open({
									type : 1,
									area : [ '360px', '240px' ],
									content : $('#addbox'),
									btn : [ '确认', '取消' ],
									btnAlign : 'c' // 按钮居中
									,
									shade : 0 // 不显示遮罩
									,
									yes : function() {
										var CLASSIFICATION_NAME = $(
												'#CLASSIFICATION_NAME').val();
										var ICON_BANNER = $('#ICON_BANNER')
												.val();
										var addPERMISSIONS_URL = $(
												'#addPERMISSIONS_URL').val();
										var pid = $("#addclassificationPid")
												.val();
										$.post(addPERMISSIONS_URL, {
											CLASS_NAME : CLASSIFICATION_NAME,
											ICON_BANNER : ICON_BANNER,
											PID : pid
										}, function(dd) {
											if (dd > 0) {
												// 执行重载
												table.reload(
														'classificationlist', {
															page : {
																curr : 1
															// 重新从第 1 页开始
															},
															where : {
																PID : pid
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
								$('#CLASSIFICATION_NAME').val("");
								$('#ICON_BANNER').val("");
								$("#showuploadBanner")
										.html(
												'<i class="layui-icon" id="showuploadBanner" style="font-size: 30px;line-height: 100%;">&#xe64a;</i>');
							} else if (classification == 2) {
								$("#istwo").hide();
								layer.open({
									type : 1,
									area : [ '360px', '185px' ],
									content : $('#addbox'),
									btn : [ '确认', '取消' ],
									btnAlign : 'c' // 按钮居中
									,
									shade : 0 // 不显示遮罩
									,
									yes : function() {
										var CLASSIFICATION_NAME = $(
												'#CLASSIFICATION_NAME').val();
										var ICON_BANNER = $('#ICON_BANNER')
												.val();
										var addPERMISSIONS_URL = $(
												'#addPERMISSIONS_URL').val();
										var pid = $("#addclassificationPid")
												.val();
										$.post(addPERMISSIONS_URL, {
											CLASS_NAME : CLASSIFICATION_NAME,
											ICON_BANNER : ICON_BANNER,
											PID : pid
										}, function(dd) {
											if (dd > 0) {
												// 执行重载
												table.reload(
														'classificationlist', {
															page : {
																curr : 1
															},
															where : {
																pid : pid
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
								$('#CLASSIFICATION_NAME').val("");
								$('#ICON_BANNER').val("");
								$("#showuploadBanner")
										.html(
												'<i class="layui-icon" id="showuploadBanner" style="font-size: 30px;line-height: 100%;">&#xe64a;</i>');
							}
						}
					};
					var iconBanner = $("#ICON_BANNER").val();
					// 普通图片上传
					var uploadInst = upload
							.render({
								elem : '#test1',
								url : '/classification/uploadBanner/',
								data : {
									iconBanner : iconBanner
								},
								before : function(obj) {
									// 预读本地文件示例，不支持ie8
									obj.preview(function(index, file, result) {
										$('#demo1').attr('src', result); // 图片链接（base64）
									});
								},
								done : function(res) {
									$("#ICON_BANNER").val(res.data.src);
									iconBanner = res.data.src;
									$("#showuploadBanner")
											.html(
													'<img id="showuploadBanner" src="'
															+ iconBanner
															+ '" width="40px" height="40px" />');
									// 如果上传失败
									if (res.code > 0) {
										return layer.msg('上传失败');
									}
									// 上传成功
								},
								error : function() {
									// 演示失败状态，并实现重传
									var demoText = $('#demoText');
									demoText
											.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
									demoText.find('.demo-reload').on('click',
											function() {
												uploadInst.upload();
											});
								}
							});

					$('.demoTable .layui-btn').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});
					$('#addclassification .layui-btn').on('click', function() {
						var othis = $(this), method = othis.data('method');
						active[method] ? active[method].call(this, othis) : '';
					});
				});