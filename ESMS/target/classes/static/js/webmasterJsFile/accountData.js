/**
 * 店家商品管理js
 * 
 * @returns
 */

layui.use([ 'element', 'layer', 'table', 'laydate' ],
				function() {
					var element = layui.element, layer = layui.layer, table = layui.table, $ = layui.jquery, laydate = layui.laydate;
					laydate.render({
						elem : '#startTime',
						type : 'datetime'
					});
					laydate.render({
						elem : '#endTime',
						type : 'datetime'
					});
					var ETYPE = '1';
					// 一些事件监听
					element.on('tab(ETYPE)', function(data) {
						// 收入明细
						if (data.index == 0) {
							ETYPE = '1';
						}
						// 支出明细
						else if (data.index == 1) {
							ETYPE = '2';
						}
						// 执行重载
						table.reload('paymentlist', {
							page : {
								curr : 1
							// 重新从第 1 页开始
							},
							where : {
								ETYPE : ETYPE
							}
						});
					});
					// 初始表格加载已上架商品
					table.render({
						elem : '#paymentlist',
						url : '/webmaster/getPaymentlist',
						page : true,
						id : 'paymentlist',
						where : {
							ETYPE : '1',
						},
						cols : [ [ {
							type : 'checkbox',
							fixed : 'left',
							width : '5%'
						}, {
							title : '时间',
							field : 'ETIME',
							align : 'center',
							sort : true
						}, {
							field : 'EMONY',
							title : '金额',
							align : 'center',
							sort : true
						}, {
							field : 'CNAME',
							width : '40%',
							title : '事件名称',
							align : 'center'
						} ] ]
					});

					// 监听表格复选框选择
					table.on('checkbox(paymentlist)', function(obj) {
						console.log(obj)
					});
					active = {
						// 批量导出
						batchExport : function() {
							var checkStatus = table.checkStatus('paymentlist'), data = checkStatus.data;
							var len = data.length;
							if (len > 0) {
								var EZPIDs = "";
								for (var i = 0; i < data.length; i++) {
									EZPIDs += data[i].EZPID + ";";
								}
								window.location.href = "/webmaster/batchExport?EZPIDs="
										+ EZPIDs;
							} else {
								layer.msg("您未选择需要导出的记录");
							}
						},
						// 表格重载
						relocad : function() {
							var startTime = $("#startTime").val();
							var endTime = $("#endTime").val();
							// 执行重载
							table.reload('paymentlist', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {
									ETYPE : ETYPE,
									startTime : startTime,
									endTime : endTime
								}
							});
						}
					};

					$('.demoTable .layui-btn').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});
				});