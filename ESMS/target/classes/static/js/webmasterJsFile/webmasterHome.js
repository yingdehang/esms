/**
 * 站长主页js
 */
// 站长收益趋势
// 基于准备好的dom，初始化echarts实例
var earningsTrend = echarts.init(document.getElementById('earningsTrend'));
var earningsTrendData = new Array();
var dataType;

function getData(t) {
	var va = t.value;
	$.post("/webmaster/getEarningsTrendData", {
		dataType : va
	}, function(oneData) {
		earningsTrendData[0] = oneData.oneX;
		earningsTrendData[1] = oneData.onlineAndEntity;
		earningsTrendData[2] = oneData.Onlinetoreevenue;
		earningsTrendData[3] = oneData.entityShopevenue;
		optiontubiao1();
	});
}

$(function() {
	// 站长收益趋势
	dataType = "qitian";
	$.post("/webmaster/getEarningsTrendData", {
		dataType : dataType
	}, function(oneData) {
		earningsTrendData[0] = oneData.oneX;
		earningsTrendData[1] = oneData.onlineAndEntity;
		earningsTrendData[2] = oneData.Onlinetoreevenue;
		earningsTrendData[3] = oneData.entityShopevenue;
		optiontubiao1();
	});
	// 店铺等级分布
	$.post("/webmaster/getShopGradeDistributionData", {}, function(twoData) {
		shopGradeDistributionData = twoData;
		optiontubiao2();
	});
	// 会员充值金额占比
	$.post("/webmaster/getmembershipRechargeAmountData", {},
			function(threeData) {
				membershipRechargeAmountData = threeData;
				optiontubiao3();
			});
	// 会员等级分布
	$.post("/webmaster/membershipistributionData", {}, function(fourData) {
		membershipistributionData = fourData;
		optiontubiao4();
		$("#onechuji").text(membershipistributionData[0]);
		$("#twozhongji").text(membershipistributionData[1]);
		$("#threegaoji").text(membershipistributionData[2]);
	});
})
// 站长收益趋势图
function optiontubiao1() {
	// 指定图表的配置项和数据
	var option = {
		title : {
			text : '站长收益趋势图'
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
			radius : '45%',
			center : [ '25%', '50%' ],
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
			radius : '45%',
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
			radius : '55%',
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