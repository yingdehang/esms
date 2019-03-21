/**
 * 店家商品管理js
 * 
 * @returns
 */
layui
		.use(
				[ 'element', 'layer', 'table' ],
				function() {
					var element = layui.element, layer = layui.layer;
					var table = layui.table, $ = layui.jquery;
					var storeId = $("#storeId").val();
					var goodsState = $("#goodsState").val();
					var batchShelves = $("#batchShelves"), batchDown = $("#batchDown");
					var batchDelete = $("#batchDelete");
					if (goodsState == 'GOODS_STATE_DSH') {
						batchDown.hide();
					}
					// 一些事件监听
					element.on('tab(goodsState)', function(data) {
						batchShelves.hide();
						batchDown.hide();
						batchDelete.hide();
						// 已上架商品
						if (data.index == 0) {
							goodsState = 'GOODS_STATE_UP';
							batchDown.show();
						}
						// 已下架商品
						else if (data.index == 1) {
							goodsState = 'GOODS_STATE_DOWN';
							batchShelves.show();
							batchDelete.show();
						}
						// 待审核商品
						else if (data.index == 2) {
							goodsState = 'GOODS_STATE_DSH';
							batchDelete.show();
						}
						// 审核失败商品
						else if (data.index == 3) {
							goodsState = 'GOODS_STATE_FAIL';
							batchDelete.show();
						}
						// 强制下架
						else if (data.index == 4) {
							goodsState = 'GOODS_STATE_QZ_DOWN'
							batchDelete.show();
						}
						// 执行重载
						table.reload('goodsList', {
							page : {
								curr : 1
							// 重新从第 1 页开始
							},
							where : {
								GOODS_STATE : goodsState
							}
						});
					});
					// 初始表格加载已上架商品
					table.render({
						elem : '#goodslist',
						url : '/goodsManage/getGoodsList',
						page : true,
						id : 'goodsList',
						where : {
							GOODS_STATE : goodsState,
							storeId : storeId
						},
						cols : [ [ {
							type : 'checkbox',
							fixed : 'left',
							width : '5%'
						}, {
							fixed : true,
							title : '编号',
							align : 'center',
							width : '5%',
							templet : function(d) {
								return d.LAY_INDEX;
							}
						}, {
							field : 'googsInfo',
							align : 'center',
							toolbar : '#goodsInfo',
							title : '商品信息'
						}, {
							field : 'INVENTORY',
							title : '库存',
							align : 'center',
							width : '8%',
							sort : true,
							event : 'xiugaikucun'
						}, {
							field : 'PRESENT_PRICE',
							title : '单价',
							align : 'center',
							width : '8%',
							sort : true,
							event : 'xiugaidanjia'
						}, {
							field : 'PURCHASE_QUANTITY',
							title : '销量',
							align : 'center',
							width : '8%',
							sort : true,
							event : 'xiugaixiaoliang'
						}, {
							field : 'GOODS_STATE',
							title : '商品状态',
							align : 'center',
							width : '10%',
							toolbar : '#xsGoodsState'
						}, {
							field : 'STORE_SORT',
							event : 'xiugaisort',
							width : '8%',
							sort : true,
							title : '排序'
						}, {
							fixed : 'right',
							align : 'center',
							width : '10%',
							toolbar : '#toolbar',
							title : '操作'
						} ] ]
					});

					// 监听表格复选框选择
					table.on('checkbox(goodsList)', function(obj) {
						console.log(obj)
					});
					// 监听工具条
					table
							.on(
									'tool(operating)',
									function(obj) {
										var data = obj.data;
										// 查看详情
										if (obj.event === 'detail') {
											window.location.href = "/goodsManage/queryGoodsInfo?storeId="
													+ storeId
													+ "&goodsId="
													+ data.GOODSID;
										}
										// 上架
										else if (obj.event === 'shangjia') {
											$
													.post(
															"/goodsManage/updateGoodsState",
															{
																GOODSID : data.GOODSID,
																GOODS_STATE : "GOODS_STATE_UP"
															},
															function(da) {
																if (da > 0) {
																	layer
																			.msg("上架成功");
																	// 执行重载
																	table
																			.reload(
																					'goodsList',
																					{
																						page : {
																							curr : 1
																						}
																					});
																} else {
																	layer
																			.msg("系统错误，请稍后再试");
																}
															});
										}
										// 下架
										else if (obj.event === 'xiajia') {
											$
													.post(
															"/goodsManage/updateGoodsState",
															{
																GOODSID : data.GOODSID,
																GOODS_STATE : "GOODS_STATE_DOWN"
															},
															function(da) {
																if (da > 0) {
																	layer
																			.msg("商品下架成功");
																	// 执行重载
																	table
																			.reload(
																					'goodsList',
																					{
																						page : {
																							curr : 1
																						// 重新从第
																						// 1
																						// 页开始
																						}
																					});
																} else {
																	layer
																			.msg("系统错误，请稍后再试");
																}
															});
										}
										// 修改排序
										else if (obj.event === 'xiugaisort') {
											layer
													.prompt(
															{
																formType : 2,
																title : '默认为0,请输入一个整数数字用于排序',
																value : data.STORE_SORT
															},
															function(value,
																	index) {
																layer
																		.close(index);
																if (!isNaN(value)) {
																	$
																			.post(
																					"/goodsManage/updateGoodsSort",
																					{
																						GOODSID : data.GOODSID,
																						STORE_SORT : value
																					},
																					function(
																							da) {
																						if (da > 0) {
																							layer
																									.msg("修改商品排序成功");
																							obj
																									.update({
																										STORE_SORT : value
																									});
																						} else {
																							layer
																									.msg("修改失败");
																						}
																					});

																} else {
																	layer
																			.msg("请输入正确的数字");
																}
															});
										}
										// 修改库存
										else if (obj.event === 'xiugaikucun') {
											layer
													.prompt(
															{
																formType : 2,
																title : '修改商品库存,最多9999件',
																value : data.INVENTORY
															},
															function(value,
																	index) {
																layer
																		.close(index);
																var inventory = value;
																if (inventory
																		.match(/^\d+$/)) {
																	if (inventory > 9999
																			|| inventory < 0) {
																		layer
																				.msg("库存数只允许输入0~9999之间的正整数");
																	} else {
																		// 同步更新表格和缓存对应的值
																		$
																				.post(
																						"/goodsManage/updateGoodsInventory",
																						{
																							GOODSID : data.GOODSID,
																							INVENTORY : value
																						},
																						function(
																								d) {
																							if (d > 0) {
																								layer
																										.msg("修改成功");
																								// 执行重载
																								table
																										.reload(
																												'goodsList',
																												{
																													page : {
																														curr : 1
																													// 重新从第
																													// 1
																													// 页开始
																													}
																												});
																								obj
																										.update({
																											INVENTORY : value
																										});
																							} else {
																								layer
																										.msg("系统错误，修改失败");
																							}
																						});

																	}
																} else {
																	layer
																			.msg("请输入数字");
																}
															});
										}

										// 修改销量
										else if (obj.event === 'xiugaixiaoliang') {
											layer
													.prompt(
															{
																formType : 2,
																title : '修改商品销量',
																value : data.PURCHASE_QUANTITY
															},
															function(value,
																	index) {
																layer
																		.close(index);
																if (value
																		.match(/^\d+$/)) {
																	// 同步更新表格和缓存对应的值
																	$
																			.post(
																					"/goodsManage/updateGoodsXiaoLiang",
																					{
																						GOODSID : data.GOODSID,
																						newXiaoliang : value,
																						agoXiaoliang : data.PURCHASE_QUANTITY,
																						storeId : storeId
																					},
																					function(
																							d) {
																						if (d > 0) {
																							layer
																									.msg("修改成功");
																							obj
																									.update({
																										PURCHASE_QUANTITY : value
																									});
																						} else {
																							layer
																									.msg("系统错误，修改失败");
																						}
																					});

																} else {
																	layer
																			.msg("请输入正确的正整数销量");
																}
															});
										}

										// 修改单价
										else if (obj.event === 'xiugaidanjia') {
											layer
													.prompt(
															{
																formType : 2,
																title : '修改商品单价',
																value : data.PRESENT_PRICE
															},
															function(value,
																	index) {
																layer
																		.close(index);
																var price = value;
																if (price
																		.match(/^\d{1,10}(\.\d{1,2})?$/)) {
																	// 同步更新表格和缓存对应的值
																	$
																			.post(
																					"/goodsManage/updateGoodsPRICE",
																					{
																						GOODSID : data.GOODSID,
																						ORIGINAL_PRICE : data.PRESENT_PRICE,
																						PRESENT_PRICE : value
																					},
																					function(
																							d) {
																						if (d > 0) {
																							layer
																									.msg("修改成功");
																							obj
																									.update({
																										PRESENT_PRICE : value
																									});
																						} else {
																							layer
																									.msg("系统错误，修改失败");
																						}
																					});

																} else {
																	layer
																			.msg("请输入正确的价格");
																}
															});
										}
										// 刪除
										else if (obj.event === 'del') {
											layer
													.confirm(
															'真的删除行么',
															function(index) {
																$
																		.post(
																				"/goodsManage/DeleteGoods",
																				{
																					GOODSID : data.GOODSID
																				},
																				function(
																						da) {
																					if (da > 0) {
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
										// 重新编辑
										else if (obj.event === 'edit') {
											window.location.href = "/goodsManage/toUpdategoodsHtml?storeId="
													+ storeId
													+ "&goodsId="
													+ data.GOODSID
										}
									});
					active = {
						// 批量上架
						batchShelves : function() { // 获取选中数据
							var checkStatus = table.checkStatus('goodsList'), data = checkStatus.data;
							var len = data.length;
							if (len > 0) {
								var goodsIdArray = new Array(len);
								for (var i = 0; i < data.length; i++) {
									goodsIdArray[i] = data[i].GOODSID;
								}
								$.post("/goodsManage/batchOperation", {
									goodsIds : goodsIdArray,
									GOODS_STATE : 'GOODS_STATE_UP'
								}, function(d) {
									if (d > 0) {
										layer.msg("批量上架申请成功");
										// 执行重载
										table.reload('goodsList', {
											page : {
												curr : 1
											// 重新从第 1 页开始
											}
										});
									} else {
										layer.msg("批量上架失败");
									}
								});
							} else {
								layer.msg("您未选择商品");
							}
						},
						// 批量下架
						batchDown : function() { // 获取选中数目
							var checkStatus = table.checkStatus('goodsList'), data = checkStatus.data;
							var len = data.length;
							if (len > 0) {
								var goodsIdArray = new Array(len);
								for (var i = 0; i < data.length; i++) {
									goodsIdArray[i] = data[i].GOODSID;
								}
								$.post("/goodsManage/batchOperation", {
									goodsIds : goodsIdArray,
									GOODS_STATE : 'GOODS_STATE_DOWN'
								}, function(d) {
									if (d > 0) {
										layer.msg("批量下架申请成功");
										// 执行重载
										table.reload('goodsList', {
											page : {
												curr : 1
											// 重新从第 1 页开始
											}
										});
									} else {
										layer.msg("批量下架失败");
									}
								});
							} else {
								layer.msg("您未选择商品");
							}
						},
						// 批量删除
						batchDelete : function() { // 验证是否全选
							var checkStatus = table.checkStatus('goodsList'), data = checkStatus.data;
							var len = data.length;
							if (len > 0) {
								layer.confirm('确定批量删除选中商品吗？', function(index) {
									var goodsIdArray = new Array();
									for (var i = 0; i < data.length; i++) {
										goodsIdArray[i] = data[i].GOODSID;
									}
									$.post("/goodsManage/batchDelete", {
										goodsIds : goodsIdArray,
									}, function(d) {
										if (d > 0) {
											layer.msg("批量删除成功");
											// 执行重载
											table.reload('goodsList', {
												page : {
													curr : 1
												// 重新从第 1 页开始
												}
											});
										} else {
											layer.msg("批量删除失败");
										}
									});
									layer.close(index);
								});
							} else {
								layer.msg("您未选择商品");
							}
						},
						// 批量导出
						batchExport : function() {
							var checkStatus = table.checkStatus('goodsList'), data = checkStatus.data;
							var len = data.length;
							if (len > 0) {
								var goodsIdArray = new Array();
								for (var i = 0; i < data.length; i++) {
									goodsIdArray[i] = data[i].GOODSID;
								}
								$
										.post(
												"/goodsManage/batchExport",
												{
													goodsIds : goodsIdArray,
												},
												function(d) {
													window.location.href = "/goodsManage/batchExport2?goodsIds="
															+ d;
												});
							} else {
								layer.msg("您未选择商品");
							}
						},
						// 导出所有
						batchExportAll : function() {
							$
									.post(
											"/goodsManage/batchExport",
											{
												storeId : storeId
											},
											function(d) {
												if (d == "") {
													layer
															.msg("您的商店中暂无商品，请添加商品");
												} else {
													window.location.href = "/goodsManage/batchExport2?goodsIds="
															+ d;
												}
											});
						},
						// 表格重载
						relocad : function() {
							var goodsName = $("#goodsName").val();
							// 执行重载
							table.reload('goodsList', {
								page : {
									curr : 1
								// 重新从第 1 页开始
								},
								where : {
									GOODS_NAME : goodsName
								}
							});
						}
					};

					$('.demoTable .layui-btn').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});
				});