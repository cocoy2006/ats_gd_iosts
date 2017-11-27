<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="molab.util.Util"%>
<%@ page import="molab.db.device.*"%>
<%@ page import="java.io.File"%>

<html>
    <head>
        <link rel="stylesheet" type="text/css" href="styles/client.css"/>
        <link rel="stylesheet" type="text/css" href="styles/keyboard.css"/>
        <link rel="stylesheet" type="text/css" href="js/facebox/facebox.css"/>
        <script type="text/javascript" src="js/jquery/jquery.js"></script>
        <script type="text/javascript" src="js/facebox/facebox.js"></script>
        <script type="text/javascript" src="js/client.js"></script>
        <%
        String username = session.getAttribute("username").toString();
        String server = session.getAttribute("server").toString();
        String serialNumber = session.getAttribute("serialNumber").toString();
        String fromUrl = session.getAttribute("fromUrl").toString();
    	try {
    		int deviceId = Integer.parseInt(session.getAttribute("id").toString());
    		Device device = new DeviceHandler().loadDevice(deviceId);
    		String manufacturer = device.getManufacturer();
    		String model = device.getModel();
    		Util util = Util.getInstance();
    		String imageURL = util.getConf().getProperty("BaseImageURL");
    		String imageRealpath = util.getConf().getProperty("BaseImageRealpath");
    		String home = imageURL.concat(manufacturer).concat("/").concat(model).concat("/");
    		String realpath = imageRealpath.concat("/").concat(manufacturer).concat("/").concat(model).concat("/");
    		String css = "";
    		if(new File(realpath).exists()) {
    			css = home.concat("main.css");
    		} else {
    			css = imageURL.concat("x/main.css");
    		}
    		out.print("<link rel='stylesheet' type='text/css' href='".concat(css).concat("'/>"));
    	} catch(Exception e) {}
    	%>
    	<script type="text/javascript"> /* <![CDATA[ */
    		try {
                // Log in
                var data = "username=<%=serialNumber%>&password=molab";
                var xhr = new XMLHttpRequest();
                xhr.open("POST", "login", false);
                xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
                xhr.send(data);
                // Handle failures
                if (xhr.status != 200) throw new Error("服务器异常，请联系管理员!");
            } catch (e) {}
    	/* ]]> */ </script>		
        <title>iOSTS</title>
    </head>

    <body>
		<div class="tophead">
			<div class="logo">
				<br />
				<a> 全业务能力开放新技术研究-终端测试环境系统 </a>
			</div>
		</div>
		
		<form name="releaseLockForm" action="logout.jsp" method="post" style="display: none">
			<input name="username" value="<%=username %>" />			
			<input name="server" value="<%=server %>" />
			<input name="serialNumber" value="<%=serialNumber%>" />
			<input name="fromUrl" value="<%=fromUrl %>" />
		</form>
		
		<div class="newslist">
			<div class="w_tit">
				&nbsp;演示试用 <span class="more"><a style="color: #D54300" onClick="javascript:document.releaseLockForm.submit();"> 退回首页 </a></span>
			</div>
			<div>
				<div class="container-content-sidebar">
					<!-- 我的机时 -->
					<script type="text/javascript">
						var username = "<%=username %>";
						var server = "<%=server %>";
						var serialNumber = "<%=serialNumber %>";
						var fromUrl = "<%=fromUrl %>";
						var lefttime = <%=session.getAttribute("lefttime").toString() %> - 0;
						startupCountdownTimer(username, fromUrl, server, serialNumber, lefttime, 1,/*second*/ 10);
					</script>
					
					<!-- Operation tips -->
					<div class="newslist-sidebar">
						<div class="w_tit">
							操作提示
						</div>
						<div>
							<p class="news"><img src="images/a.gif"/>&nbsp;<font color="#D54300"> 左键表示点击屏幕 </font></p>
							<p class="news"><img src="images/a.gif"/>&nbsp;<font color="#D54300"> 右键表示按iPhone的“Home”键 </font></p>
							<p class="news"><img src="images/a.gif"/>&nbsp;<font color="#D54300"> 支持屏幕长按、滑动屏幕 </font></p>
							<p class="news"><img src="images/a.gif"/>&nbsp;<font color="#D54300"> 支持从电脑键盘输入字符 </font></p>
						</div>
					</div>
					
					<!-- Lefttime -->
					<div class="newslist-sidebar">
						<div class="w_tit">
							我的机时
						</div>
						<div id="resttime" style="position: relative; height: 26px;">
							<div id="hour" class="no" style="left: 0px; top: 0px;"></div><div class="units" style="left: 35px; top: 0px;"> 小时 </div>
							<div id="minute" class="no" style="left: 70px; top: 0px;"></div><div class="units" style="left: 105px; top: 0px;"> 分钟 </div>
							<div id="second" class="no" style="left: 140px; top: 0px;"></div><div class="units" style="left: 175px; top: 0px;"> 秒 </div>
						</div>						
					</div>
					
					<!-- Applications uploaded -->
					<div class="newslist-sidebar">
						<div class="w_tit">
							我的应用<span style="float: right;"><a href="#upfileDiv" rel="facebox">上传文件</a></span>
						</div>
						<div>
							<div id="apks" style="overflow-x:hidden; overflow-y: auto; min-height: 300px; max-height: 500px;">
								<!-- applications uploaded -->
							</div>
							<iframe name="hiddenApkFrame" id="hiddenApkFrame" style="display: none;"></iframe>
							<div style="height: 16px;">
								<span id="apkTips"></span><!-- show tips when synchronization & installation finish -->
							</div>
						</div>
					</div>
					<div id="upfileDiv" style="display: none;">
						<div class="faceboxHeader">
							<p>全业务能力开放新技术研究-终端测试环境系统 </p>
						</div><br>
						<form name="upfileForm" method="POST" enctype="multipart/form-data" target="hiddenFileFrame">
							<p class="browse"><input type="file" id="upfile" name="upfile"></p>
							<input type="button" class="btn" value=" 开 始 上 传 文 件 " onClick="uploadfile()"/>
							<input type="hidden" name="serialNumber" value="<%=serialNumber%>" />
						</form><hr/>
						<div id="upfileTips">提示：请不要在文件名中包含非英文字符，否则可能出现异常！</div>
					</div>
					<iframe name="hiddenFileFrame" id="hiddenFileFrame" style="display: none;"></iframe>
					
					<!-- Operation record -->
					<div class="newslist-sidebar">
						<div class="w_tit">
							历史记录
						</div>
						<div>
							<div id="historys" style="overflow: auto; min-height: 300px; max-height: 500px;">
								
							</div>
						</div>
					</div>
				</div>
				<div class="content content-font">
					<div class="contentbox-container">
						<div class="contentbox-noshading">
							<!-- Menu -->
					        <div id="menu" style="display: none;">
					
					            <!-- Clipboard -->
					            <button id="showClipboard">Show Clipboard</button>
					            <div id="clipboardDiv">
					                <h2>Clipboard</h2>
					                <p>
					                Text copied/cut within Guacamole will appear here. Changes to the text will affect the remote clipboard, and will be pastable within the remote desktop. Use the textbox below as an interface between the client and server clipboards.
					                </p>
					                <textarea rows="10" cols="40" id="clipboard"></textarea>
					            </div>
					
					            <button id="showKeyboard">Show Keyboard</button>
					            <button id="ctrlAltDelete">Ctrl-Alt-Delete</button>
					            <button id="logout">Logout</button>
					
					            <!-- Logo and status -->
					            <img id="status-logo" class="logo" src="images/guacamole-logo-24.png" alt="Guacamole" title="Guacamole 0.5.0"/>
					            <span id="state"></span>
					
					        </div>
					
							<div id="skin">
								<div id="screen">
									<!-- Display -->
							        <div id="display" class="guac-display guac-loading">
							            
							            <!-- Menu trigger -->
							            <div id="menuControl"></div>
							
							            <!-- On-screen keyboard -->
							            <div id="keyboardContainer"></div>
							
							        </div>
								</div>
							</div>
					
					        <!-- Error Dialog-->
					        <div id="errorDialog" class="errorDialogOuter">
					            <div class="errorDialogMiddle">
					                <div class="errorDialog">
					                    <p id="errorText"></p>
					                    <div class="buttons"><button id="reconnect"> 重新连接 </button></div>
					                </div>
					            </div>
					        </div>
					
					
					        <!-- guacamole-common-js scripts -->
					        <script type="text/javascript" src="guacamole-common-js/keyboard.js"></script>
					        <script type="text/javascript" src="guacamole-common-js/mouse.js"></script>
					        <script type="text/javascript" src="guacamole-common-js/layer.js"></script>
					        <script type="text/javascript" src="guacamole-common-js/tunnel.js"></script>
					        <script type="text/javascript" src="guacamole-common-js/guacamole.js"></script>
					        <script type="text/javascript" src="guacamole-common-js/oskeyboard.js"></script>
					
					        <!-- guacamole-default-webapp scripts -->
					        <script type="text/javascript" src="scripts/interface.js"></script>
					
					        <!-- Init -->
					        <script type="text/javascript"> /* <![CDATA[ */
					
					            // Instantiate client
					            var guac = new Guacamole.Client(
					                GuacamoleUI.display,
					                new Guacamole.HTTPTunnel("tunnel")
					            );
					
					            // Tie UI to client
					            GuacamoleUI.attach(guac);
					
					            try {
					
					                // Get ID
					                var id = window.location.search.substring(1);
					
					                // Connect client
					                guac.connect("id=" + id);
					                
					            }
					            catch (e) {
					                //GuacamoleUI.showError(e.message);
					                //document.releaseLockForm.submit();
					                alert(e.message);
					            }
					            
					            var timer = window.setInterval(function() {
					            	var device = $("#display canvas");
					            	if(device != undefined) {
					            		window.clearInterval(timer);
					            		device.mousedown(function(e) {
					            			start(e);
					            		});
					            		device.mouseup(function(e) {
					            			finish(e);
					            		});
					            	}
					            }, 100);
					
					            /* ]]> */ </script>			
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
    </body>
</html>