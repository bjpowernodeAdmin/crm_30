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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">

	$(function(){
		
		//如果当前页不是顶层窗口，则将当前页面设置为顶层窗口
		if(window.top!=window){
			window.top.location=window.location;
		}

		
		//页面加载完毕后，让用户名文本框自动取得焦点
		$("#loginAct").focus();
		
		
		//页面加载完毕后，将用户名的文本框清空
		$("#loginAct").val("");
		
		
		
		//为登录按钮绑定事件，做登录操作
		$("#submitBtn").click(function(){
			
			login();
		
		
		})
		
		//为窗口绑定事件，绑定敲键盘事件，如果敲的是回车键，做登录操作
		//event:通过该参数可以知道敲键盘的键是哪一个
		$(window).keydown(function(event){
			
			//alert("敲键盘了,敲得键位是："+event.keyCode);
			//如果敲的是回车键(keyCode码值为13)，做登录操作
			if(event.keyCode==13){
				
				login();
				
			}
			
			
		})
		
	
	})
	
	/*
	
		我们自己定义的function方法，一定要写在$(function)的外面
	
	*/
	function login(){
			
		//取得账号密码值
		//去除左右空格 $.trim(内容)
		var loginAct = $.trim($("#loginAct").val());
		var loginPwd = $.trim($("#loginPwd").val());
		
		//判断账号密码是否为空
		if(loginAct=="" || loginPwd==""){
			
			$("#msg").html("<font color='red'>账号密码都不能为空</font>");
			
			//如果账号密码为空了，需要将该方法强制终止，就应该继续向下执行其他的代码了
			//强制终止方法
			return false;
			
		}
		
		//如果以上通过，我们就需要去数据库中验证账号密码
		//发出ajax请求，在id=msg的span标签对中，局部刷新错误消息
		$.ajax({
			
			url : "settings/user/login.do",
			data : {
				
				"loginAct" : loginAct,
				"loginPwd" : loginPwd
				
			},
			type : "post",
			dataType : "json",
			success : function(data){
				
				/*
				
					data
						
						登录成功
						{"success":true}
						
						登录失败
						{"success":false,"msg":"?"}
						
				
				*/
				
				if(data.success){
					
					//登录成功
					//跳转到工作台初始页（欢迎页）
					window.location.href = "workbench/index.jsp";
					
				}else{
					
					//登录失败
					//需要再span标签对中，输出错误消息
					$("#msg").html(data.msg);
					
				}
				
				
			}
		
		})
		
		
	}
	

</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<!-- 
						注意：将按钮的类型设置为普通按钮，不设置默认就是提交表单
							type="button"
					
					 -->
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>