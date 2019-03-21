/**
 * 结算记录js
 */
layui.use([ 'element', 'table', 'laydate', 'form' ], function() {
	var element = layui.element, table = layui.table;
	var form = layui.form;
	var laydate = layui.laydate, $ = layui.$;
	// 日期时间范围
	laydate.render({
		elem : '#startTime',
		type : 'datetime'
	});
	laydate.render({
		elem : '#endTime',
		type : 'datetime'
	});
	/* 加载table */
	table.render({
		elem : '#webmasterlist',
		url : '/webmasterManage/masterSettlementRecordList/',
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
			field : 'ETIME',
			title : '结算时间',
			align : 'center',
			sort : true
		}, {
			field : 'EMONY',
			title : '结算金额',
			align : 'center'
		} ] ],
		id : 'webmasterlist',
		page : true
	});

	/* 商品分类联动start */
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
						"<option value='" + l[i].CITY_CODE + "'>"
								+ l[i].CITY_NAME + "</option>");
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
						"<option value='" + l[i].CITY_CODE + "'>"
								+ l[i].CITY_NAME + "</option>");
			}
			form.render('select');
		});
	});
	/* 商品分类联动end */

	// 查询
	var active = {
		relocad : function() {
			var province = $('#province').val();
			var city = $('#city').val();
			var area = $('#area').val();
			var phone = $('#phone').val();
			var userName = $('#userName').val();
			var startTime = $('#startTime').val();
			var endTime = $('#endTime').val();
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
					userName : userName,
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
