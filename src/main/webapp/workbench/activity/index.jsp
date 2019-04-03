<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


<script type="text/javascript">

	$(function(){
		
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
    		autoclose: true,
    		todayBtn: true,
    		pickerPosition: "bottom-left"
		});
		
		//为创建按钮绑定事件，为了打开模态窗口
		$("#addBtn").click(function(){
			
			/*
			
				操作模态窗口的开启和关闭
				
					通过id找到指定模态窗口的jquery对象，通过调用modal方法，指定参数
															  show：打开模态窗口
															  hide：关闭模态窗口
				
			
			*/
			
			
			//发出ajax请求，走后台，目的是为了取得用户信息列表List<User> uList
			//通过uList为所有者的下拉框提供可选项
			//打开模态窗口
			
			$.ajax({
			
				url : "workbench/activity/getUserList.do",
				type : "get",
				dataType : "json",
				success : function(data){
					
					/*
					
						data
							[{用户1},{用户2},{用户3}...]
					
					*/
					
					var html = "<option></option>";
					
					//每一个n，就是每一个用户的json对象
					$.each(data,function(i,n){
						
						html += "<option value='"+n.id+"'>"+n.name+"</option>";
					
					})
					
					//将用户信息，铺在所有者的select下拉框中
					$("#create-owner").html(html);
					
					
					//取得当前登录的用户的id
					//el表达式能够应用在js中，但是！！！el表达式必须要套用在字符串中
					var id = "${user.id}";
					
					//将当前登录的用户的id值，赋值给所有者的下拉框上
					$("#create-owner").val(id);
					
					//以上处理完所有者下拉框后，打开模态窗口
					$("#createActivityModal").modal("show");
					
				}
			
			})
			
			
			
		
		})
		
		
		//为保存按钮绑定事件，执行市场活动的添加操作
		$("#saveBtn").click(function(){
			
			//以ajax形式提交表单，表单中数据，是以ajax的参数的形式提交到后台
			$.ajax({
			
				url : "workbench/activity/save.do",
				data : {
					
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())

					
				},
				type : "post",
				dataType : "json",
				success : function(data){
					
					/*
					
						data
							{"success" : true/false}
					
					*/
					
					if(data.success){
						
						//添加成功
						
						//将添加操作的模态窗口中的数据都清空掉
						//提交表单
						//$("#activityAddForm").submit();
						//重置表单
						//这种做法不行，jquery没有提供重置表单的方法
						//$("#activityAddForm").reset();
						//虽然jquery没有提供重置表单的方法，但是我们的原生js的dom提供了重置表单的方法
						/*
						
							将表单的jquery对象转换为dom对象
								jquery对象[0]
						
							dom对象转换成jquery对象
								$(dom)
						
						
						*/
						$("#activityAddForm")[0].reset();
						
						//刷新列表，后续再做
						//pageList(1,2);
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						
						//关闭添加操作模态窗口
						$("#createActivityModal").modal("hide");
						
						
					}else{
						
						//添加失败
						alert("添加市场活动失败");
						
					}
					
					
				}
			
			})
		
		})
		
		//页面加载完毕后，展现市场活动信息列表
		pageList(1,2);
		
		//为查询按钮绑定事件，做条件查询操作
		$("#searchBtn").click(function(){
			
			//每一次触发查询按钮，将查询框中的信息保存到隐藏域中
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			
			pageList(1,2);
		
		})
		
		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function(){
			
			$("input[name=xz]").prop("checked",this.checked);
		
		})
		
		//为内容中的复选框绑定事件，触发全选的复选框
		/* $("input[name=xz]").click(function(){
			
			alert(123);
		
		}) */
		
		//以上操作不行
		/*
		
			因为所有name=xz的input元素都是我们通过js动态拼接生成的
			使用js动态拼接生成的元素不能像我们以前那样直接绑定事件
			我们要使用的是on的形式来绑定事件
			
			语法：
				$(需要绑定的元素的有效的父级元素).on(触发事件的方式，需要绑定的元素，回调函数)
			
		*/
		
		$("#activityBody").on("click",$("input[name=xz]"),function(){
			
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		
		})
		
		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function(){
			
			var $xz = $("input[name=xz]:checked");
			
			if($xz.length==0){
				
				alert("请选择需要删除的记录");
			
			//肯定挑√了，而且有可能√了多条 	
			}else{
				
				//alert("执行删除操作");
				
				//workbench/activity/delete.do?id=A0001&id=A0002&id=A0003
				
						
				if(confirm("确定要删除所选中的记录吗？")){
					
					var param = "";
					
					for(var i=0;i<$xz.length;i++){
						
						param += "id="+$($xz[i]).val();
						
						//如果不是最后一条记录
						if(i<$xz.length-1){
							
							param += "&";
							
						}
						
					}		
					
					
					$.ajax({
				
						url : "workbench/activity/delete.do",
						data : param,
						type : "post",
						dataType : "json",
						success : function(data){
							
							if(data.success){
								
								//删除成功
								
								//将全选框的√灭掉
								$("#qx").prop("checked",false);
								
								//刷新市场活动列表
								//pageList(1,2);
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
								
							}else{
								
								alert("删除市场活动失败");
								
							}
							
						}
					
					})
					
					
				}		
						
							
			
			}
			 
		
		})
		
		//为修改按钮绑定事件，打开修改操作的模态窗口
		$("#editBtn").click(function(){
			
			var $xz = $("input[name=xz]:checked");
			
			if($xz.length==0){
				
				alert("请选择需要修改的记录");
			
			}else if($xz.length>1){
				
				alert("只能选中一条记录执行修改");
				
				
			//肯定挑√了，而且肯定√了一条	
			}else{
				
				//需要修改的记录的id，就是挑√的复选框的value值
				var id = $xz.val();
				
				//发送ajax请求到后台，从后台取得用户信息列表和需要修改的记录的详细信息
				$.ajax({
			
					url : "workbench/activity/getUserListAndActivity.do",
					data : {
						
						"id" : id
						
					},
					type : "get",
					dataType : "json",
					success : function(data){
						
						/*
						
							data
								{"uList":[{用户1},{2},{3}],"a":{市场活动}}
						
						*/
						
						//搭建所有者下拉框
						var html = "<option></option>";
						$.each(data.uList,function(i,n){
							
							html += "<option value='"+n.id+"'>"+n.name+"</option>";
						
						})
						
						$("#edit-owner").html(html);
						
						//搭建需要修改记录的原有的基础数据
						$("#edit-owner").val(data.a.owner);
						$("#edit-name").val(data.a.name);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-cost").val(data.a.cost);
						$("#edit-description").val(data.a.description);
						$("#edit-id").val(data.a.id);
						
						//以上基础数据搭建完毕后，打开修改操作的模态窗口
						$("#editActivityModal").modal("show");
						
					}
				
				})
				
				
			}
			
		
		})
		
		//为更新按钮绑定事件，执行修改操作
		$("#updateBtn").click(function(){
			
			//以ajax形式提交表单，表单中数据，是以ajax的参数的形式提交到后台
			$.ajax({
			
				url : "workbench/activity/update.do",
				data : {
					
					"id" : $.trim($("#edit-id").val()),
					"owner" : $.trim($("#edit-owner").val()),
					"name" : $.trim($("#edit-name").val()),
					"startDate" : $.trim($("#edit-startDate").val()),
					"endDate" : $.trim($("#edit-endDate").val()),
					"cost" : $.trim($("#edit-cost").val()),
					"description" : $.trim($("#edit-description").val())

					
				},
				type : "post",
				dataType : "json",
				success : function(data){
					
					if(data.success){
						
						//刷新列表，后续再做
						//pageList(1,2);
						
						/*
						
							$("#activityPage").bs_pagination('getOption', 'currentPage')
								表示操作后停留在当前页面
							$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
								表示操作后维持每页展现的记录数	
						
						
						*/
						
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
				,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						
						//关闭修改操作模态窗口
						$("#editActivityModal").modal("hide");
						
						
					}else{
						
						//修改失败
						alert("修改市场活动失败");
						
					}
					
					
				}
			
			})
			
		
		})
		
		
	});
	
	/*
	
		pageList方法的触发时机（都什么情况下调用该方法刷新市场活动信息列表）:
			
		（1）点击左边的菜单  "市场活动" 打开 activity/index.jsp这个页面，该页面加载完毕后，自动触发pageList方法，刷新市场活动列表
		（2）点击 "查询" 按钮，触发pageList方法，刷新市场活动列表
		（3）点击 分页组件的时候，触发pageList方法，刷新市场活动列表
		（4）添加，修改，删除 操作后，触发pageList方法，刷新市场活动列表
		
		pageNo：当前页的页码（第几页）
		pageSize：每页展现多少条记录
		
		这两个参数，是所有关系性数据库分页操作中，所必须的两个参数
 		
		除了以上的两个参数之外，我们还需要为后台传递哪些参数呢？
		还需要传递 条件查询的4个参数
		name
		owner
		startDate
		endDate
		
		
	
	*/
	function pageList(pageNo,pageSize){
		
		//发送ajax请求到后台，取得数据（市场活动信息列表），展现数据
		
		//从隐藏域中将值取出，铺在搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));
		
		
		$.ajax({
			
			url : "workbench/activity/pageList.do",
			data : {
				
				"pageNo" : pageNo,
				"pageSize" :pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
				
				
			},
			type : "get",
			dataType : "json",
			success : function(data){
				
				//alert(data);
				
				/*
				
					data
						{"total":100,"dataList":[{市场活动1},{市场活动2},{市场活动3}]}
				
				*/
				
				var html = "";
				
				$.each(data.dataList,function(i,n){
					
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
                    html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
					
				})
				
				$("#activityBody").html(html);
				
				//计算总页数
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
				
				//展示完市场活动信息列表之后，进行分页相关操作
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true, 
					
					//在触发分页 相关事件（例如点击上一页，下一页，第N页）  的时候 触发该方法
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
			   });

				
			}
		
		})
		
		
	}
	
	
</script>
</head>
<body>
	
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
						
						<input type="hidden" id="edit-id"/>
						
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  
								  
								  
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-endDate">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
						
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
								  
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!-- 
					
						data-dismiss="modal"
							将模态窗口关闭掉
					
					 -->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <!-- 
				  
				  	注意：type改为button！！！
				  
				   -->
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  
				  <!-- 
				  
				  		data-toggle="modal"（modal指的就是模态窗口）
				  			表示点击该按钮，要打开一个模态窗口（模态框）
				  		
				  		data-target="#createActivityModal"
				  			表示通过以找id的形式，找到需要打开的模态窗口
				  		
				  		但是，在实际项目开发中，我们不是像这样以在元素中，直接写属性属性值的方式，来触发模态窗口
				  		
				  		我们需要将这两句话暂时干掉，只要是按钮，按钮所触发的行为应该由我们自己为他们绑定事件来决定
				  		
				   -->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<!-- <tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.html';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr> -->
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
				
				
			</div>
			
		</div>
		
	</div>
</body>
</html>