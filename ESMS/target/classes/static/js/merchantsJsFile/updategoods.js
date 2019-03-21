/**
 * 商品修改js
 */
layui.use([ 'element', 'form', 'upload','layedit','table'], function() {
	var element = layui.element,table=layui.table, form = layui.form, $ = layui.jquery,upload = layui.upload,layedit = layui.layedit;
	var storeId=$("#storeId").val();
	var goodsId=$("#goodsId").val();
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
		    ,where:{
		    	goodsId:goodsId
		    }
		  });
	 table.render({
		 elem: '#goodsPhotoList'
			    ,url: '/goodsManage/goodsPhotoList/'
			    ,cols: [[
			      {toolbar:'#PHOTONAME', title: '图片名',width:'19%'}
			      ,{align : 'center', toolbar : '#toolbar',style:'height:80px;', width:'19%',title : '操作'}
			    ]]
			    ,id: 'goodsPhotoList'
			    ,where:{
			    	goodsId:goodsId
			    }
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
		var goodsId=data.field.goodsId;
		if(null==goodsId || goodsId==""){
			layer.msg("请添加商品属性");
			return false;
		}
		return true;
	});
	// 属性table
	table.on('tool(goodsAttributeList)', function(obj){
		  var data = obj.data; // 获得当前行数据
		  var layEvent = obj.event; // 获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		  var tr = obj.tr; // 获得当前行 tr 的DOM对象
		  if(layEvent === 'del'){ // 删除
		    var attributeId=data.ATTRIBUTE_ID;
		    var attributeName=data.ATTRIBUTE_NAME;
		    var attributeValue=data.ATTRIBUTE_VALUE;
		    if(null!=attributeId&&attributeId!=""){
				 var delattri= $("#deleteAttributes").val();
				 delattri += attributeId+","+attributeName+","+attributeValue+";"
				 $("#deleteAttributes").val(delattri);
		    }
		    obj.del(); // 删除对应行（tr）的DOM结构，并更新缓存
	    	layer.close(index);
		  }
	});
	// 图片table
	table.on('tool(goodsPhotoList)', function(obj){
		var data = obj.data; // 获得当前行数据
		var layEvent = obj.event; // 获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		var tr = obj.tr; // 获得当前行 tr 的DOM对象
		if(layEvent === 'del'){ // 删除
			layer.confirm('真的删除商品图片吗？', function(index){
				var photourl=data.PHOTO_URL;
				var photos=$("#deletephotos").val();
				photos += photourl+";";
				$("#deletephotos").val(photos);
				obj.del(); // 删除对应行（tr）的DOM结构，并更新缓存
				layer.close(index);
			});
		}
	});
	active = {
		// 添加商品属性
		addAttribute : function() {
			var goodsId=$("#goodsId").val();
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
					  var delattributes=$("#deleteAttributes").val();
					  var addAttributes=$("#addAttributes").val();
					  addAttributes += attributeName+","+attributeValue+";";
					  $("#addAttributes").val(addAttributes);
					  if(null==attributeName||attributeName==""){
						  layer.msg("属性名不能为空");
						  return false;
					  }
					  if(null==attributeValue||attributeValue==""){
						  layer.msg("属性值不能为空");
						  return false;
					  }
					  // 执行重载
					  table.reload('goodsAttributeList', {
						  where: {
							  addAttributes:addAttributes,
							  delattributes:delattributes,
							  goodsId:goodsId
						  }
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
		// 批量上传
		batchUpload : function() {
			layer.msg("批量上传");
		},  
		// 获取编辑器内容
		content: function(){
	      $("#DETAILS").val(layedit.getContent(index));
	      layer.msg("富文本内容修改成功");
	    },
		// 取消修改商品
	    cancelUpdateGoods:function(){
	    	var goodsphotos=$("#goodsphotos").val();
	    	var testgoodsId=$("#testgoodsId").val();
	    	// 跳转店家商品管理首页
	    	window.location.href="/goodsManage/cancelUpdateGoods?storeId="+storeId;
	    },
	    // 修改商品分类
	    updateClassification:function(){
	    	$("#classificationbox").hide();
	    	$("#classificationupdatebox").show();
	    }
	}

	  // 多文件列表示例
	  var demoListView = $('#demoList')
	  ,uploadListIns = upload.render({
	    elem: '#goodsImguploadbutton'
	    ,url: '/goodsManage/uploadGoodsPhoto'
	    ,multiple: true
	    ,auto: false
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
	          uploadListIns.config.elem.next()[0].value = ''; // 清空 input file
																// 值，以免删除后出现同名文件不可选
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
