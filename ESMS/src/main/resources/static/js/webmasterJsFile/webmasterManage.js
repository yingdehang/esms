/**
 * 站长管理js
 */
layui
		.use(
				[ 'element', 'table', 'form' ],
				function() {
					var element = layui.element, table = layui.table;
					var form = layui.form;
					var $ = layui.$;
					/* 加载table */
					table.render({
						elem : '#webmasterlist',
						url : '/webmasterManage/getWebmasterData/',
						cols : [ [ {
							toolbar : '#indexnumber',
							title : '序号',
							fixed : 'left',
							align : 'center'
						}, {
							field : 'pcarea',
							title : '省、市、区',
							align : 'center'
						}, {
							toolbar : '#zhanzhangxinxi',
							title : '站长信息',
							align : 'center'
						}, {
							field : 'ZZTIME',
							title : '成为站长时间',
							align : 'center'
						}, {
							field : 'WEBMASTER_MONEY',
							title : '站长账户金额',
							align : 'center'
						}, {
							toolbar : '#toolbar',
							title : '操作',
							fixed : 'right',
							align : 'center'
						} ] ],
						id : 'webmasterlist',
						page : true
					});
					// 区域加载
					/* 区域联动start */
					form.on('select(province)', function(data) {
						$("#city").empty();
						$("#area").empty();
						$("#city").append("<option value=''>请选择市</option>");
						$("#area").append("<option value=''>请选择县/区</option>");
						$.post("/webmasterManage/queryAreaList", {
							pid : data.value
						}, function(list) {
							var l = eval(list);
							for (var i = 0; i < l.length; i++) {
								$("#city").append(
										"<option value='" + l[i].CITY_CODE
												+ "'>" + l[i].CITY_NAME
												+ "</option>");
							}
							form.render('select');
						});
					});
					form.on('select(city)', function(data) {
						$("#area").empty();
						$("#area").append("<option value=''>请选择县/区</option>");
						$.post("/webmasterManage/queryAreaList", {
							pid : data.value
						}, function(list) {
							var l = eval(list);
							for (var i = 0; i < l.length; i++) {
								$("#area").append(
										"<option value='" + l[i].CITY_CODE
												+ "'>" + l[i].CITY_NAME
												+ "</option>");
							}
							form.render('select');
						});
					});
					/* 联动end */

					// 查询
					var active = {
						relocad : function() {
							var province = $('#province').val();
							var city = $('#city').val();
							var area = $('#area').val();
							var phone = $('#phone').val();
							var userName = $('#userName').val();
							// 执行重载
							table.reload('webmasterlist', {
								page : {
									curr : 1
								},
								where : {
									province : province,
									city : city,
									area : area,
									phone : phone,
									userName : userName
								}
							});
						},
						settlementRecords : function() {
							window.location.href = "/webmasterManage/tomasterSettlementRecordHtml";
						},
						addWebmaster : function() {
							layer
									.open({
										type : 1,
										title : '添加站长',
										content : $('#addwebmasterbox'),
										area : [ '638px', '360px' ],
										btn : [ '确定', '取消' ],
										yes : function(index, layero) {
											var userphone = $("#userphone")
													.val();
											var area = $("#area1").val();
											var zzyqr = $("#zzyqr").val();
											if (null == area || area == "") {
												layer.msg("请先选择正确的区域");
												return false;
											} else {
												var myreg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
												if (!myreg.test(userphone)) {
													layer.msg("请输入正确的手机号码");
													return false;
												} else {
													$
															.post(
																	"/webmasterManage/addWebmaster",
																	{
																		userphone : userphone,
																		area : area,
																		zzyqr : zzyqr
																	},
																	function(d) {
																		if (d > 0) {
																			layer.msg("添加站长成功");
																			table.reload(
																							'webmasterlist',
																							{
																								page : {
																									curr : 1
																								}
																							});

																		} else {
																			layer.msg("系统错误，请联系管理");
																		}
																	});
												}
											}
											layer.closeAll();
										},
										btn2 : function(index, layero) {
											layer.closeAll();
										},
										btnAlign : 'c'
									});
							var userphone = $("#userphone").val();
							$("#area1").val("");
							$("#userphone").val("");
							$("#zzyqr").val("");
							$("#onephone").css("color", "#222222");
							$("#twophone").css("color", "#222222");
							$("#addwebmastertishi").html(
									"<span id='addwebmastertishi'></span>");
							$("#zzyqrtishi").html(
									"<span id='zzyqrtishi'></span>");
						}
					};
					$('.demoTable .layui-btn').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});

					// 监听工具条
					table.on('tool(operating)',
									function(obj) {
										var data = obj.data;
										if (obj.event === 'detail') {
											window.location.href = "/webmasterManage/toRunningWaterHtml?userID="
													+ data.USERS_ID
										} else if (obj.event === 'del') {
											layer.confirm('确定撤销站长：'
																	+ data.UC_NAMES
																	+ '，吗？',
															function(index) {
																var USERS_ID = data.USERS_ID;
																$.post("/webmasterManage/updateISZZ",
																				{
																					USERS_ID : USERS_ID,
																					IS_ZZ : '2'
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
										} else if (obj.event === 'edit') {
											var USERS_ID = data.USERS_ID;
											layer
													.open({
														type : 1,
														title : '结算金额',
														content : $('#jesbox'),
														area : [ '400px',
																'330px' ],
														btn : [ '确定', '取消' ],
														yes : function(index,
																layero) {
															var money = $(
																	"#jinge")
																	.val();
															var cname = $(
																	"#cname")
																	.val();
															if (money
																	.match(/^\d{1,10}(\.\d{1,2})?$/)) {
																var yue = data.WEBMASTER_MONEY;
																if (money <= yue) {
																	var je = yue
																			- money;
																	// 同步更新表格和缓存对应的值
																	$
																			.post(
																					"/webmasterManage/zhanzhangjiesuan",
																					{
																						USERS_ID : USERS_ID,
																						money : money,
																						cname : cname
																					},
																					function(
																							d) {
																						if (d >= 0) {
																							layer
																									.msg("修改成功");
																							obj
																									.update({
																										WEBMASTER_MONEY : je
																									});
																						} else {
																							layer
																									.msg("系统错误，结算失败");
																						}
																					});
																	layer
																			.closeAll();
																} else {
																	layer
																			.msg("结算金额不能大于该站长账户余额");
																}
															} else {
																layer
																		.msg("请输入正确的金额");
															}
														},
														btn2 : function(index,
																layero) {
															layer.closeAll();
														},
														btnAlign : 'c'
													});
											$("#jinge").val("");
											$("#cname").val("");
										}
									});
				});
function iszhuceyonghu(t) {
	var phone = t.value;
	var myreg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
	if (!myreg.test(phone)) {
		$("#addwebmastertishi")
				.html(
						"<span id='addwebmastertishi' style='color:red'>请输入正确的手机号</span>");
		$("#onephone").css("color", "red");
	} else {
		$
				.post(
						"/webmasterManage/VerifyUserIsHave",
						{
							userPhone : t.value
						},
						function(d) {
							if (d == 1) {
								$("#addwebmastertishi")
										.html(
												"<span id='addwebmastertishi' style='color:#FF850E;'>该用户已经成为其他区域站长，重新添加将会取消过去区域的站长，成为当前区域站长</span>");
								$("#onephone").css("color", "#FF850E");
							} else if (d == 0) {
								$("#addwebmastertishi")
										.html(
												"<span id='addwebmastertishi' style='color:red;'>该用户未注册，请先注册用户并实名认证</span>");
								$("#onephone").css("color", "red");
							} else if (d == 2) {
								$("#addwebmastertishi")
										.html(
												"<span id='addwebmastertishi' style='color:red;'>该用户未实名认证,请先实名认证后在添加该用户为站长</span>");
								$("#onephone").css("color", "red");
							} else if (d == 3) {
								$("#addwebmastertishi")
										.html(
												"<span id='addwebmastertishi' style='color:green;'>用户手机号，认证通过</span>");
								$("#onephone").css("color", "green");
							}
						});
	}
}
function queryAreaList1(t) {
	$("#city1").empty();
	$("#area1").empty();
	$("#city1").append("<option value=''>请选择市</option>");
	$("#area1").append("<option value=''>请选择县/区</option>");
	$.post("/webmasterManage/queryAreaList", {
		pid : t.value
	}, function(list) {
		var l = eval(list);
		for (var i = 0; i < l.length; i++) {
			$("#city1").append(
					"<option value='" + l[i].CITY_CODE + "'>" + l[i].CITY_NAME
							+ "</option>");
		}
	});
}
function queryAreaList2(t) {
	$("#area1").empty();
	$("#area1").append("<option value=''>请选择县/区</option>");
	$.post("/webmasterManage/queryAreaList", {
		pid : t.value
	}, function(list) {
		var l = eval(list);
		for (var i = 0; i < l.length; i++) {
			$("#area1").append(
					"<option value='" + l[i].CITY_CODE + "'>" + l[i].CITY_NAME
							+ "</option>");
		}
	});
}
function queryAreaList3(t) {
	$
			.post(
					"/webmasterManage/VerifyThatThereIs",
					{
						areaId : t.value
					},
					function(d) {
						if (d > 0) {
							$("#addwebmastertishi")
									.html(
											"<span id='addwebmastertishi' style='color:red'>该区域已有站长，请先取消该区域的站长，在添加该区的站长</span>");
							$("#area1").val("");
						} else {
							$("#addwebmastertishi")
									.html(
											"<span id='addwebmastertishi' style='color:green'>该区域验证通过</span>");
						}
					});
}

function yonghuishave(t) {
	var phone = t.value;
	var myreg = /^[1][2,3,4,5,6,7,8,9][0-9]{9}$/;
	if (!myreg.test(phone)) {
		$("#addwebmastertishi")
				.html(
						"<span id='addwebmastertishi' style='color:red;'>请输入正确的手机号</span>");
		$("#twophone").css("color", "red");
	} else {
		$.post(
						"/webmasterManage/VerifyUserIsHave",
						{
							userPhone : t.value
						},
						function(d) {
							if (d > 0) {
								$("#zzyqrtishi").html(
										"<span id='zzyqrtishi'></span>");
								$("#twophone").css("color", "green");
							} else {
								$("#zzyqrtishi")
										.html(
												"<span id='zzyqrtishi' style='color:yollew;'>邀请人手机号不存在</span>");
							}
						});
	}
}
