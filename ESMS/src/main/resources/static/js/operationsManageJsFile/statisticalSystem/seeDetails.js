/**
 * 统计系统查看当月每日明细js
 */
layui
		.use(
				[ 'table', 'form' ],
				function() {
					var table = layui.table, $ = layui.jquery, form = layui.form;
					var month = $("#month").val();
					var area = $("#area"), allorarea = $("#allorarea");
					var areaId = area.val(), provinceId = $("#province").val();
					var cityId = $("#city").val();

					if (area.val() == '0') {
						$("#province1").hide();
						$("#city1").hide();
						$("#area1").hide();
					}
					form.on('select(allorarea)', function(data) {
						var value = data.value;
						if (value == '0') {
							// 当选择全部的时候。隐藏并清空地区联动下拉框
							$("#province").val("0");
							$("#city").empty();
							$("#area").empty();
							$("#province1").hide();
							$("#city1").hide();
							$("#area1").hide();
							area.val("0");
							form.render('select');
						} else {
							// 当选择区域时，显示地区联动下拉框
							$("#province1").show();
							$("#city1").show();
							$("#city").prepend(
									'<option value="0">请选择城市</option>');
							$("#area1").show();
							$("#area").prepend(
									'<option value="0">请选择县/区</option>');
						}
					});
					form.on('select(province)',
							function(data) {
								$("#city").empty();
								$("#area").empty();
								$("#city").prepend(
										'<option value="0">请选择城市</option>');
								$("#area").prepend(
										'<option value="0">请选择县/区</option>');
								$.post("/statisticalSystem/queryAreaList", {
									pid : data.value
								}, function(list) {
									var l = eval(list);
									for (var i = 0; i < l.length; i++) {
										$("#city").append(
												"<option value='"
														+ l[i].CITY_CODE + "'>"
														+ l[i].CITY_NAME
														+ "</option>");
									}
									form.render('select');
								});
							});
					form.on('select(city)', function(data) {
						$("#area").empty();
						$("#area").append("<option value=''>请选择县/区</option>");
						$.post("/statisticalSystem/queryAreaList", {
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
					var data1 = [ {
						type : 'checkbox',
						fixed : 'left',
						width : '3%'
					}, {
						title : '年、月、日',
						field : 'date',
						fixed : 'left',
						width : '130',
						align : 'center',
						sort : true
					}, {
						field : 'endTime',
						title : '结算时间',
						width : "180",
						align : 'center',
						sort : true
					}, {
						field : 'offlineWater',
						title : '线下货款',
						align : 'center'
					}, {
						field : 'onlineWater',
						title : '线上货款',
						align : 'center'
					}, {
						field : 'countWater',
						title : '总货款',
						align : 'center'
					}, {
						field : 'xsCommissionIncome',
						title : '线上佣金(含物流费)',
						width : "160",
						align : 'center'
					}, {
						field : 'xxCommissionIncome',
						title : '线下佣金',
						align : 'center'
					}, {
						field : 'commissionIncome',
						title : '总佣金',
						align : 'center'
					}, {
						field : 'countWaterAndCommissionIncome',
						title : '总流水(总货款+总佣金)',
						align : 'center',
						width : '180'
					}, {
						field : 'newUsers',
						title : '新注册用户',
						align : 'center'
					}, {
						field : 'newPassUsers',
						title : '新实名认证',
						align : 'center'
					}, {
						field : 'monthlyRecharge',
						title : '充值总额',
						align : 'center'
					}, {
						field : 'integralPresent',
						title : '积分赠送',
						align : 'center'
					}, {
						field : 'signIntegralConsumption',
						title : '签到积分消耗',
						align : 'center'
					}, {
						field : 'otherIntegralConsumption',
						title : '其它积分消耗',
						align : 'center'
					}, {
						field : 'signBonus',
						title : '签到奖励金',
						align : 'center'
					}, {
						field : 'integralChange',
						title : '积分变化',
						align : 'center'
					}, {
						field : 'logisticsServiceCharge',
						title : '物流服务费',
						align : 'center'
					}, {
						field : 'totalPlatformIntegral',
						title : '平台总积分',
						width : '130',
						align : 'center'
					}, {
						field : 'totalBalance',
						title : '平台总余额',
						width : '130',
						align : 'center'
					}, {
						field : 'totalMoney',
						title : '平台总零花钱',
						width : '130',
						align : 'center'
					}, {
						field : 'totalConsumptionCoupon',
						title : '平台总消费券',
						width : '130',
						align : 'center'
					}, {
						field : 'integralOverCommission',
						title : '积分/佣金',
						align : 'center'
					} ];
					var data2 = [ {
						type : 'checkbox',
						fixed : 'left',
						width : '3%'
					}, {
						title : '年、月、日',
						field : 'date',
						fixed : 'left',
						width : '130',
						align : 'center',
						sort : true
					}, {
						field : 'endTime',
						title : '结算时间',
						width : "180",
						align : 'center',
						sort : true
					}, {
						field : 'offlineWater',
						title : '线下货款',
						align : 'center'
					}, {
						field : 'onlineWater',
						title : '线上货款',
						align : 'center'
					}, {
						field : 'countWater',
						title : '总货款',
						align : 'center'
					}, {
						field : 'xsCommissionIncome',
						title : '线上佣金(含物流费)',
						width : "160",
						align : 'center'
					}, {
						field : 'xxCommissionIncome',
						title : '线下佣金',
						align : 'center'
					}, {
						field : 'commissionIncome',
						title : '总佣金',
						align : 'center'
					}, {
						field : 'countWaterAndCommissionIncome',
						title : '总流水(总货款+总佣金)',
						align : 'center',
						width : '180'
					}, {
						field : 'newUsers',
						title : '新注册用户',
						align : 'center'
					}, {
						field : 'newPassUsers',
						title : '新实名认证',
						align : 'center'
					}, {
						field : 'monthlyRecharge',
						title : '充值总额',
						align : 'center'
					}, {
						field : 'integralPresent',
						title : '积分赠送',
						align : 'center'
					}, {
						field : 'signIntegralConsumption',
						title : '签到积分消耗',
						width : "130",
						align : 'center'
					}, {
						field : 'otherIntegralConsumption',
						title : '其它积分消耗',
						width : "130",
						align : 'center'
					}, {
						field : 'signBonus',
						title : '签到奖励金',
						align : 'center'
					}, {
						field : 'integralChange',
						title : '积分变化',
						align : 'center'
					}, {
						field : 'logisticsServiceCharge',
						title : '物流服务费',
						align : 'center'
					} ];
					var data3;
					if (allorarea.val() == '0') {
						data3 = data1;
					} else {
						data3 = data2;
					}

					// 初始表格
					table.render({
						elem : '#runningwaterlist',
						url : '/statisticalSystem/getdayWaterlist',
						id : 'runningwaterlist',
						height : '500px',
						cellMinWidth : '120',
						where : {
							month : month,
							areaId : area.val()
						},
						cols : [ data3 ]
					});

					// 监听表格复选框选择
					table.on('checkbox(runningwaterlist)', function(obj) {
						console.log(obj)
					});
					active = {
						// 批量导出
						batchExport : function() {
							var checkStatus = table
									.checkStatus('runningwaterlist'), data = checkStatus.data;
							var len = data.length;
							if (len > 0) {
								var indexs = "";
								/* <![CDATA[ */
								for (var i = 0; i < data.length; i++) {
									indexs += data[i].date + ";";
								}
								/* ]]> */
								window.location.href = "/statisticalSystem/getDetailswaterDaochu?days="
										+ indexs + "&areaId=" + areaId;
							} else {
								layer.msg("您未选择需要导出的记录");
							}
						},
						relocad : function() {
							if (allorarea.val() == '0') {
								table.render({
									elem : '#runningwaterlist',
									url : '/statisticalSystem/getdayWaterlist',
									id : 'runningwaterlist',
									height : '500px',
									cellMinWidth : '120',
									where : {
										month : month,
										areaId : area.val()
									},
									cols : [ data1 ]
								});
								areaId = 0;
							} else {
								if (area.val() == '0') {
									layer.msg("请选择区域");
								} else {
									table
											.render({
												elem : '#runningwaterlist',
												url : '/statisticalSystem/getdayWaterlist',
												id : 'runningwaterlist',
												height : '500px',
												cellMinWidth : '120',
												where : {
													month : month,
													areaId : area.val()
												},
												cols : [ data2 ]
											});
									areaId = area.val();
								}
							}
						}
					};

					$('.demoTable .layui-btn').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});
				});