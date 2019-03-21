layui.use(['table','jquery','form'], function(){
	var table = layui.table;
	var $ = layui.jquery;
	var form = layui.form;
	<!-- 监听按钮 -->
	$('.layui-btn').on('click', function(){
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	var userId = $("#userId").val();
	var certificationId = $("#certificationId").val();
	var storeId = $("#storeId").val();
	var $ = layui.$, active = {
		ok: function(){
			layer.confirm('是否通过?', {icon: 3, title:'提示'}, function(index){
				layer.close(index);
				window.location.href="/offLineStore/verify?result=1&certificationId="+certificationId+"&userId="+userId+"&storeId="+storeId;
			});
		}
		,no: function(){
			var index = layer.open({
				type:1,
				content:$('#reason'),
				title:['审核失败原因',''],
				area:['400px','250px'],
				shade: 0,
		        maxmin: true,
		        skin:'layui-layer-molv',
			    btnAlign: 'c',
				btn:['确定','取消']
				,yes: function(index, layero){
					var content = $("#content").val();
					if(content=='')
					{
						layer.msg("请填写拒绝理由");
					}
					else
					{
						layer.confirm('是否拒绝?', {icon: 3, title:'提示'}, function(ind){
							layer.close(ind);
							layer.close(index);
							window.location.href="/offLineStore/verify?result=0&certificationId="+certificationId+"&userId="+userId+"&content="+content+"&storeId="+storeId;
						});
					}
				}
			});
			
		}
	};
});