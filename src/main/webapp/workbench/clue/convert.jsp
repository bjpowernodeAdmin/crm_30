<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<script type="text/javascript">
	$(function(){
		
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});
		
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
       		autoclose: true,
       		todayBtn: true,
       		pickerPosition: "bottom-left"
		});
		
		//为  交易表单中  市场活动源 后面 的 "放大镜"图标绑定事件，打开搜索市场活动列表的模态窗口
		$("#searchActivityBtn").click(function(){
			
			$("#searchActivityModal").modal("show");
		
		})
		
		//为搜索市场活动的文本框绑定敲键盘事件，如果敲得是回车，则查询市场活动列表
		$("#aname").keydown(function(event){
			
			//如果敲得是回车键
			if(event.keyCode==13){
				
				//查询市场活动信息列表
				$.ajax({
			
					url : "workbench/clue/getActivityListByName.do",
					data : {
						
						"aname" : $.trim($("#aname").val())
						
					},
					type : "get",
					dataType : "json",
					success : function(data){
						
						/*
						
							data
								[{市场活动1},{2},{3}]
						
						*/
						
						var html = "";
						
						$.each(data,function(i,n){
							
							html += '<tr>';
							html += '<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
							html += '<td id="'+n.id+'">'+n.name+'</td>';
							html += '<td>'+n.startDate+'</td>';
							html += '<td>'+n.endDate+'</td>';
							html += '<td>'+n.owner+'</td>';
							html += '</tr>';
						
						})
						
						$("#activitySearchBody").html(html);
						
					}
				
				})
				
				
				
				
				//以上查询并展现完市场活动列表之后，我们应该将该方法强制终止
				return false;
				
			}
			
		
		})
		
		//为搜索市场活动的模态窗口的 提交 按钮绑定事件，提交单选框选中的市场活动源
		//将名字提交，将id提交
		$("#submitActivity").click(function(){
			
			var $xz = $("input[name=xz]:checked");
			
			if($xz.length==0){
				
				alert("请选择提交的市场活动");
			
			//肯定选了一条	
			}else{
				
				var id = $xz.val();
				//将选中的id赋值给市场活动源的隐藏域
				$("#activityId").val(id);
				
				//取得选中的市场活动名称
				var name = $("#"+id).html();
				//将取得的市场活动名称赋值给市场活动源的显示信息的文本框
				$("#activityName").val(name);
				
				//将模态窗口关闭掉
				$("#searchActivityModal").modal("hide");
				
				
			}
		
		
		})
		
		//为转换按钮绑定事件 执行线索的转换
		$("#convertBtn").click(function(){
			
			//workbench/clue/convert.do
			//发出传统请求
			/*
			
				请求分成两种情况，一种为需要创建交易的，一种为不需要创建交易的
				
				是否需要创建交易，在于 "为客户创建交易"的复选框有没有挑√
				
				不论是否需要创建交易，核心的业务仍然是线索的转换，请求的路径workbench/clue/convert.do是不变的
				
			*/
			
			
			//挑√了，需要创建交易
			if($("#isCreateTransaction").prop("checked")){
				
				//转换的同时，创建交易，除了为后台提供clueId让后台知道转换的是哪条记录之外，还需要为后台提供交易表单的信息
				//window.location.href = "workbench/clue/convert.do?clueId=${param.id}&name=xxx&money=xxx......";
				//如果是像上述的做法，参数过多，操作起来比较麻烦
				
				//我们可以以提交表单的形式，来发出workbench/clue/convert.do请求
				$("#tranForm").submit();
				
				
				
			//没有挑√，不需要创建交易	
			}else{
				
				window.location.href = "workbench/clue/convert.do?clueId=${param.id}";
				
			}
			
			
		
		
		})
		
		
	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="aname" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activitySearchBody">
							<!-- <tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr> -->
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivity">提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
		
		<!-- 
		
			以submit的形式正常提交下面的表单,参数以name为key的形式进行传递
				
				workbench/clue/convert.do?clueId=xxx&money=xxx&name=xxx&expectedDate=xxx&stage=xxx&activityId=xxx
		
		 -->
		
		<form action="workbench/clue/convert.do" method="post" id="tranForm">
		  
		  <input type="hidden" name="flag" value="a"/>
		  
		  <!-- 在表单中，提供一个clueId -->
		  <input type="hidden" name="clueId" value="${param.id}"/>
		
		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stageList}" var="s">
		    		<option value="${s.value}">${s.text}</option>
		    	</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="searchActivityBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
		    <input type="hidden" id="activityId" name="activityId"/>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" value="转换" id="convertBtn">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>