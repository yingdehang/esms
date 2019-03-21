/**
 * 商品添加js
 */
layui.use([ 'element', 'form', 'upload','layedit','table'], function() {
	var element = layui.element,table=layui.table, form = layui.form, $ = layui.jquery,upload = layui.upload,layedit = layui.layedit;
	var storeId=$("#storeId").val();
	/* 属性table,start */
	 table.render({
		    elem: '#goodsAttributeList'
		    ,url: '/goodsManage/goodsAttributeList/'
		    ,cols: [[
		      {field:'ATTRIBUTE_NAME', title: '属性名',width:'19%'}
		      ,{field:'ATTRIBUTE_VALUE', title: '属性值',width:'59%'}
		      ,{align : 'center', toolbar : '#toolbar', width:'19%',title : '操作'}
		    ]]
		    ,id: 'goodsAttributeList'
		  });
	 /* 属性table,end */
	 
	/* 富文布编辑器start */
	// 设置
	layedit.set({
		  uploadImage: {
		    url: '/goodsManage/uploadGoodsPhoto' // 接口url
		    ,type: 'post' // 默认post
		  }
	});
	// 构建编辑器
	var index = layedit.build('goodsxiangqing');
	
	/* 商品分类联动start */
	form.on('select(getsonClass1)', function(data) {
		$("#classification2").empty();
		$("#classification3").empty();
		$("#classification2").append("<option value=''>请选择分类</option>");
		$("#classification3").append("<option value=''>请选择分类</option>");
		$.post("/goodsManage/getClassList", {
			pid : data.value,
			storeId:storeId
		}, function(list) {
			var l = eval(list);
			$("#selectbox1").show();
			for (var i = 0; i < l.length; i++) {
				$("#classification2").append(
						"<option value='" + l[i].CLASSIFICATION_ID + "'>"
								+ l[i].CLASS_NAME + "</option>");
			}
			form.render('select');
		});
	});
	form.on('select(getsonClass2)', function(data) {
		$("#classification3").empty();
		$.post("/goodsManage/getClassList", {
			pid : data.value
		}, function(list) {
			var l = eval(list);
			$("#selectbox2").show();
			for (var i = 0; i < l.length; i++) {
				$("#classification3").append(
						"<option value='" + l[i].CLASSIFICATION_ID + "'>"
								+ l[i].CLASS_NAME + "</option>");
			}
			form.render('select');
		});
	});
	/* 商品分类联动end */

	// 监听提交
	form.on('submit(demo1)', function(data) {
// alert(data.field.DETAILS);
		var GOODS_NAME=data.field.GOODS_NAME;
		if(GOODS_NAME.length>50){
			layer.msg("商品名称过长，请修改商品名称");
			return false;
		}
		var CLASSIFICATION_ID=data.field.CLASSIFICATION_ID;
		if(null==CLASSIFICATION_ID || CLASSIFICATION_ID==""){
			layer.msg("请选择商品分类");
			return false;
		}
		var PRESENT_PRICE=data.field.PRESENT_PRICE;
		var INVENTORY=data.field.INVENTORY;
		if(PRESENT_PRICE<=0){
			layer.msg("商品价格不能小于或等于零");
			return false;
		}
		if(INVENTORY<=0){
			layer.msg("商品库存不能小于或等于零");
			return false;
		}
		var goodsphotos=data.field.goodsphotos;
		if(goodsphotos.length==0){
			layer.msg("请添加商品图片");
			return false;
		}
		return true;
	});
	table.on('tool(goodsAttributeList)', function(obj){
		  var data = obj.data; // 获得当前行数据
		  var layEvent = obj.event; // 获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		  var tr = obj.tr; // 获得当前行 tr 的DOM对象
		  if(layEvent === 'del'){ // 删除
		    layer.confirm('真的删除行么', function(index){
		      $.post("/goodsManage/deleteAttribute",{
		    	  attributeId:data.ATTRIBUTE_ID
		      },function(dd){
		    	  obj.del(); // 删除对应行（tr）的DOM结构，并更新缓存
		    	  layer.close(index);
		      });
		    });
		  }
	});
	
	active = {
		// 添加商品属性
		addAttribute : function() {
			var testgoodsId=$("#testgoodsId").val();
			layer.open({
				  type: 1, 
				  content: $("#addbox"),
				  anim: 0,
				  area:['335px','225px'],
				  btnAlign: 'c',
				  btn: ['确定', '取消'],
				  yes: function(index, layero){
					  var attributeName=$("#attributeName").val();
					  var attributeValue=$("#attributeValue").val();
					  if(null==attributeName||attributeName==""){
						  layer.msg("属性名不能为空");
						  return false;
					  }
					  if(null==attributeValue||attributeValue==""){
						  layer.msg("属性值不能为空");
						  return false;
					  }
					  $.post("/goodsManage/addAttribute",{
						  goodsId:testgoodsId,
						  attributeName:attributeName,
						  attributeValue:attributeValue
					  },function(dd){
						  $("#testgoodsId").val(dd);
						  // 执行重载
					      table.reload('goodsAttributeList', {
					        where: {
					         goodsId:dd
					        }
					      });
					  });
					  layer.closeAll();
				  },
				  btn2: function(index, layero){
					  layer.closeAll();
				  }
			});
			$("#attributeName").val("");
			$("#attributeValue").val("");
		},
		// 获取编辑器内容
		content: function(){
	      $("#DETAILS").val(layedit.getContent(index));
	      layer.msg("编辑器内容保存成功");
	    },
		// 取消添加商品
	    CancelAddGoods:function(){
	    	var goodsphotos=$("#goodsphotos").val();
	    	var testgoodsId=$("#testgoodsId").val();
	    	// 跳转店家商品管理首页
	    	window.location.href="/goodsManage/CancelAddGoods?storeId="+storeId+"&goodsphotos="+goodsphotos+"&goodsId="+testgoodsId;
	    }
	}

	  // 多文件列表示例
	  var demoListView = $('#demoList')
	  ,uploadListIns = upload.render({
	    elem: '#goodsImguploadbutton'
	    ,url: '/goodsManage/uploadGoodsPhoto'
	    ,multiple: true
	    ,auto: false
	    ,accept: 'images' 
	    ,bindAction: '#testListAction'
	    ,choose: function(obj){   
	      var files = this.files = obj.pushFile(); // 将每次选择的文件追加到文件队列
	      // 读取本地文件
	      obj.preview(function(index, file, result){
	        var tr = $(['<tr id="upload-'+ index +'">'
	          ,'<td>'+ file.name +'</td>'
	          ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
	          ,'<td>等待上传</td>'
	          ,'<td>'
	          ,'<button type="button" class="layui-btn layui-btn-mini demo-reload layui-hide">重传</button>'
	          ,'<button type="button" class="layui-btn layui-btn-mini layui-btn-danger demo-delete">删除</button>'
	          ,'</td>'
	        ,'</tr>'].join(''));
	        
	        // 单个重传
	        tr.find('.demo-reload').on('click', function(){
	          obj.upload(index, file);
	        });
	        
	        // 删除
	        tr.find('.demo-delete').on('click', function(){
	          delete files[index]; // 删除对应的文件
	          tr.remove();
	          uploadListIns.config.elem.next()[0].value = ''; // 清空 input
																// file值，以免删除后出现同名文件不可选
	        });
	        demoListView.append(tr);
	      });
	    }
	    ,done: function(res, index, upload){
	      if(res.code == 0){ // 上传成功
	    	var goodsphotos=$("#goodsphotos").val();
	    	goodsphotos += res.data.src+";";
	    	$("#goodsphotos").val(goodsphotos);
	        var tr = demoListView.find('tr#upload-'+ index)
	        ,tds = tr.children();
	        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
	        tds.eq(3).html(''); // 清空操作
	        return delete this.files[index]; // 删除文件队列已经上传成功的文件
	      }
	      this.error(index, upload);
	    }
	    ,error: function(index, upload){
	      var tr = demoListView.find('tr#upload-'+ index)
	      ,tds = tr.children();
	      tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
	      tds.eq(3).find('.demo-reload').removeClass('layui-hide'); // 显示重传
	    }
	  });
	
	$('.demoTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
});

