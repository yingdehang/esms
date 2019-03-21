layui.use(['table','jquery','form'], function(){
	var table = layui.table;
	var $ = layui.jquery;
	var form = layui.form;
	<!-- 表格加载 -->
	table.render({
		elem: '#offLineStoreVerify'
		,url: '/offLineStore/getOffLineStore'
		,page: true
		,cols:[[
			{field:'STORE_NAME',title:'店铺名称',width:120}
			,{field:'userPhone',title:'用户电话',width:120}
			,{field:'PHONE',title:'联系方式',width:120}
			,{field:'SERVICE_PHONE',title:'客服电话',width:120}
			,{field:'area',title:'地区'}
			,{field:'ADDR',title:'详细地址'}
			,{field:'CLASSIFICATION_NAME',title:'店铺分类'}
			,{field:'LEVELS',title:'店铺等级'}
			,{field:'BUSINESS_LICENSE',title:'营业执照',toolbar: '#business_license'}
			,{field:'ID_CARD_UP',title:'店家身份证正面',toolbar: '#id_card'}
			,{field:'ID_CARD_DON',title:'店家身份证反面',toolbar: '#id_card_don'}
			,{field:'HEAD_PHOTO',title:'门头照片',toolbar: '#head_photo'}
			,{field:'TO_APPLY_FOR_TIME',title:'申请时间'}
			,{field:'AUDIT_STATE',title:'审核状态'}
			,{fixed: 'right', width:150, align:'center', toolbar: '#toolbar',title:'操作'}
		]]
	});
	<!-- 监听搜索按钮 -->
	$('#search .layui-btn').on('click', function(){
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	<!-- 搜索 -->
	var storeName = $("#storeName");
	var storeClass = $("#storeClass");
	var phone = $("#phone");
	var $ = layui.$, active = {
		<!-- 搜索时执行重载-->
		reload: function(){
			//执行重载
			table.reload('offLineStoreVerify', {
				where: {
					storeName:storeName.val(),
					storeClass:storeClass.val(),
					phone:phone.val()
				}
				,page:{
					curr:1
				}
			});
		}
		//查询认证记录
		,record: function(){
			window.location.href="/offLineStore/toRecord";
		}
	};
	<!-- 监听工具条操作 -->
	table.on('tool(operation)', function(obj){ 
		var userId = $("#userId").val();
		var data = obj.data; //获得当前行数据
		var layEvent = obj.event;
		if(layEvent === 'verify'){ //审核
			window.location.href="/offLineStore/toVerify?certificationId="+data.STORE_CERTIFICATION_ID+"&userId="+userId;
		}
	});
});
function showPhoto(t) {
	layer.open({
		type : 1,
		title : false,
		maxmin: true, //开启最大化最小化按钮
	    area: ['1200px', '700px'],
		anim : 0,
		skin : 'layui-layer-nobg', //没有背景色
		shadeClose : true,
		content : '<img src="'+t.src+'" />'
	});
}