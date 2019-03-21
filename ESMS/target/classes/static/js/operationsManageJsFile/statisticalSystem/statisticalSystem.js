/**
 * 统计系统js
 */
// 基于准备好的dom，初始化echarts实例
var earningsTrend = echarts.init(document.getElementById('earningsTrend'));
var earningsTrendData = new Array();
var dataType, areaId = 0;
var allorarea = $("#allorarea"), relocad = $("#relocad"), seemore = $("#seemore");
var province = $("#province"), city = $("#city"), area = $("#area");

// 当选择全部的时候。隐藏并清空地区联动下拉框
function clearAreaSelect() {
	$("#province").val("0");
	$("#city").empty();
	$("#area").empty();
	$("#province1").hide();
	$("#city1").hide();
	$("#area1").hide();
}

// 当选择区域时，显示地区联动下拉框
function showAreaSelect() {
	$("#province1").show();
	$("#city1").show();
	$("#city").prepend('<option value="0">请选择城市</option>');
	$("#area1").show();
	$("#area").prepend('<option value="0">请选择县/区</option>');
}

relocad.click(function() {
	if (allorarea.val() == '0') {
		areaId = 0;
		queryData();
	} else {
		if ($("#area").val() == '0') {
			alert("请选择区域");
		} else {
			areaId = $("#area").val();
			queryData($("#area").val());
		}
	}
});

seemore.click(function() {
	window.location.href = "/statisticalSystem/toRunningWaterHtml?areaId="
			+ areaId + "&provinceId=" + province.val() + "&cityId="
			+ city.val();
});

// 请求后台查询数据
function queryData(t) {
	$.ajax({
		url : "/statisticalSystem/queryStatisticalData",
		async : false,
		data : {
			"areaId" : t
		},
		success : function(data) {
			$(".usersCount").text(data.usersCount);
			$("#userPassCount").text(data.userPassCount);
			$("#totalScore").text(data.totalScore);
			$("#newUserCount").text(data.newUserCount);
			$("#newUserPasscount").text(data.newUserPasscount);
			$("#xsStoreCount").text(data.xsStoreCount);
			$("#newXsStoreCount").text(data.newXsStoreCount);
			$("#xxStoreCount").text(data.xxStoreCount);
			$("#newXxStoreCount").text(data.newXxStoreCount);
			$("#commission").text(data.commission);
			$("#incentivePayments").text(data.incentivePayments);
			$("#integralPresent").text(data.integralPresent);
			$("#integralChange").text(data.integralChange);
			$("#rechargeAmount").text(data.rechargeAmount);
		}
	});
	// 佣金收益
	dataType = "qitian";
	$.post("/statisticalSystem/getEarningsTrendData", {
		dataType : dataType,
		areaId : t
	}, function(oneData) {
		earningsTrendData[0] = oneData.oneX;
		earningsTrendData[1] = oneData.onlineAndEntity;
		earningsTrendData[2] = oneData.Onlinetoreevenue;
		earningsTrendData[3] = oneData.entityShopevenue;
		optiontubiao1();
	});
	// 店铺等级分布
	$.post("/statisticalSystem/getShopGradeDistributionData", {
		areaId : t
	}, function(twoData) {
		shopGradeDistributionData = twoData;
		optiontubiao2();
	});
	// 会员充值金额占比
	$.post("/statisticalSystem/getmembershipRechargeAmountData", {
		areaId : t
	}, function(threeData) {
		membershipRechargeAmountData = threeData;
		optiontubiao3();
	});
	// 会员等级分布
	$.post("/statisticalSystem/membershipistributionData", {
		areaId : t
	}, function(fourData) {
		membershipistributionData = fourData;
		optiontubiao4();
		$("#onechuji").text(membershipistributionData[0]);
		$("#twozhongji").text(membershipistributionData[1]);
		$("#threegaoji").text(membershipistributionData[2]);
	});
}

function getData(t) {
	var va = t.value;
	$.post("/statisticalSystem/getEarningsTrendData", {
		dataType : va,
		areaId : areaId
	}, function(oneData) {
		earningsTrendData[0] = oneData.oneX;
		earningsTrendData[1] = oneData.onlineAndEntity;
		earningsTrendData[2] = oneData.Onlinetoreevenue;
		earningsTrendData[3] = oneData.entityShopevenue;
		optiontubiao1();
	});
}

// 佣金收益
function optiontubiao1() {
	// 指定图表的配置项和数据
	var option = {
		title : {
			text : '佣金收益'
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '总收益', '线上店收益', '实体店收益' ]
		},
		grid : {
			left : '3%',
			right : '4%',
			bottom : '3%',
			containLabel : true
		},
		toolbox : {
			feature : {
				saveAsImage : {}
			},
			right : '5%'
		},
		xAxis : {
			type : 'category',
			boundaryGap : false,
			data : earningsTrendData[0]
		},
		yAxis : {
			type : 'value'
		},
		series : [ {
			name : '总收益',
			type : 'line',
			data : earningsTrendData[1]
		}, {
			name : '线上店收益',
			type : 'line',
			data : earningsTrendData[2]
		}, {
			name : '实体店收益',
			type : 'line',
			data : earningsTrendData[3]
		} ]
	};
	// 使用刚指定的配置项和数据显示图表。
	earningsTrend.setOption(option);
}

// 店铺等级分布
var shopGradeDistribution = echarts.init(document
		.getElementById('shopGradeDistribution'));
var shopGradeDistributionData = new Array();
function optiontubiao2() {
	var option = {
		title : {
			text : '店铺等级分布',
			x : 'center'
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			left : 'left',
			data : [ '金牌店铺', '银牌店铺', '铜牌店铺' ]
		},
		series : [ {
			name : '线上',
			type : 'pie',
			radius : '30%',
			center : [ '28%', '50%' ],
			data : [ {
				value : shopGradeDistributionData[0],
				name : '金牌店铺'
			}, {
				value : shopGradeDistributionData[1],
				name : '银牌店铺'
			}, {
				value : shopGradeDistributionData[2],
				name : '铜牌店铺'
			} ]
		}, {
			name : '线下',
			type : 'pie',
			radius : '30%',
			center : [ '75%', '50%' ],
			data : [ {
				value : shopGradeDistributionData[3],
				name : '金牌店铺'
			}, {
				value : shopGradeDistributionData[4],
				name : '银牌店铺'
			}, {
				value : shopGradeDistributionData[5],
				name : '铜牌店铺'
			} ]
		} ]
	};
	// 使用刚指定的配置项和数据显示图表。
	shopGradeDistribution.setOption(option);
}
function eConsole(param) {
	var storeDJ = [ "STORE_DJ_GOLD", "STORE_DJ_SILVER", "STORE_DJ_COPPER" ];
	// var storeType = [ "STORE_XS_XS", "STORE_XS_XX" ];
	var m = param.seriesIndex;
	var n = param.dataIndex;
	if (m == 0) {
		window.location.href = "/onLineStore/toOnLineStoreList?storeDJ="
				+ storeDJ[n];
	} else if (m == 1) {
		// alert("线下店铺等级--" + storeDJ[n]);
		window.location.href = "/offLineStore/toOffLineStoreManage?storeDJ="
				+ storeDJ[n];
	}
}
shopGradeDistribution.on("click", eConsole);

// 会员充值金额占比
var membershipRechargeAmount = echarts.init(document
		.getElementById('membershipRechargeAmount'));
var membershipRechargeAmountData = new Array();
function optiontubiao3() {
	option = {
		title : {
			text : '会员充值金额占比',
			x : 'center'
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			bottom : 10,
			left : 'center',
			data : [ '初级会员', '中级会员', '高级会员' ]
		},
		series : [ {
			name : '充值金额比例',
			type : 'pie',
			radius : '36%',
			center : [ '50%', '60%' ],
			data : [ {
				value : membershipRechargeAmountData[0],
				name : '初级会员'
			}, {
				value : membershipRechargeAmountData[1],
				name : '中级会员'
			}, {
				value : membershipRechargeAmountData[2],
				name : '高级会员'
			} ],
			itemStyle : {
				emphasis : {
					shadowBlur : 10,
					shadowOffsetX : 0,
					shadowColor : 'rgba(0, 0, 0, 0.5)'
				}
			}
		} ]
	};
	// 使用刚指定的配置项和数据显示图表。
	membershipRechargeAmount.setOption(option);
}
function toUserManage(param) {
	var MEMBERSHIP_GRADE = [ "USER_DJ_PRIMARY", "USER_DJ_INTERMEDIATE",
			"USER_DJ_SENIOR" ];
	var n = param.dataIndex;
	window.location.href = "/appUser/toAppUserList?MEMBERSHIP_GRADE="
			+ MEMBERSHIP_GRADE[n];
}
membershipRechargeAmount.on("click", toUserManage);

// 会员等级分布
var membershipistribution = echarts.init(document
		.getElementById('membershipistribution'));
var membershipistributionData = new Array();
function optiontubiao4() {
	option = {
		title : {
			text : '会员等级分布图',
			x : 'center'
		},
		tooltip : {
			trigger : 'axis',
			axisPointer : { // 坐标轴指示器，坐标轴触发有效
				type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		legend : {
			data : '初级会员'
		},
		grid : {
			left : '3%',
			right : '4%',
			bottom : '3%',
			containLabel : true
		},
		xAxis : {
			type : 'value'
		},
		yAxis : {
			type : 'category',
			data : [ '初级会员', '中级会员', '高级会员' ]
		},
		series : [ {
			name : '人数',
			type : 'bar',
			stack : '总量',
			label : {
				normal : {
					show : true,
					position : 'right'
				}
			},
			data : membershipistributionData
		} ]
	};
	// 使用刚指定的配置项和数据显示图表。
	membershipistribution.setOption(option);
}
// 加载时执行
$(function() {
	// 统计总数据
	clearAreaSelect();
	queryData();
})