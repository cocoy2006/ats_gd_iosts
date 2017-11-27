$(document).ready(function() {
	$("a[rel*=facebox]").facebox();
});

var g_username, g_fromUrl, g_server, g_serialNumber;
function startupCountdownTimer(username, fromUrl, server, serialNumber, lefttime, interval, period) {
	g_username = username;
	g_fromUrl = fromUrl;
	g_server = server;
	g_serialNumber = serialNumber;
	var c = period;
	window.setInterval(function() {
		lefttime--;
		dispCountdownTimer(lefttime - 0);
		if(lefttime - 0 == 0) {
			updateLefttime(0);
			logout();
		}
		if(--c == 0) {
			updateLefttime(lefttime);
			c = period;
		}
	}, interval * 1000);
}
function dispCountdownTimer(lefttime) {
	var h = Math.floor(lefttime / 3600);
	var temp = lefttime - h * 3600;
	var m = Math.floor(temp / 60);
	var s = temp - m * 60;
	if(m < 10) m = '0' + m;
	if(s < 10) s = '0' + s;
	$("#hour").html(h);
	$("#minute").html(m);
	$("#second").html(s);
}

function updateLefttime(lefttime) {
	$.ajax({
		url: "servlet/user/UpdateLefttime",
		data: "username=" + g_username + "&server=" + g_server + 
			"&serialNumber=" + g_serialNumber + "&lefttime=" + lefttime,
		success: function(data) {
			switch(data) {
				case 0: case "0": // do nothing
					break;
				case 1: case "1": // update lefttime
					break;
				case 2: case "2": // logout
					logout();
					break;
				case -1: case "-1": // error
					break;
			}
		}
	});
}

function logout() {	
	window.location = "logout.jsp?username=" + g_username + "&server=" + g_server +
		"&fromUrl=" + g_fromUrl + "&serialNumber=" + g_serialNumber;
}

function addHistory(msg) {
	var history = 
		"<p class='news itemsBg'>" +
		"	<img src='images/a.gif'/>&nbsp;" +
		"	<span>" + parseOperation(msg) + "</span>" +
		"	<span style='display: none;'>" + new Date().getTime() + "</span>" +
		"</p>";
	$("#historys").prepend(history);
}

var proTimer;
function uploadfile() {
	$("#facebox div[id='upfileTips']").html("");
	var file = $("#facebox input[name='upfile']").val();
	if(!file) {
		$("#facebox div[id='upfileTips']").html("<strong> 请选择文件！ </strong>");
		return;
	}
	$("#facebox div[id='upfileTips']").html("<strong> 正在上传... </strong>");
	var url = "servlet/Upfiles?way=upload";
	$("form[name='upfileForm']").attr("action", url).submit();
	
	proTimer = window.setInterval(function() {
		$.ajax({
			url: "servlet/UpProgress?roll="+Math.random(),
			success: function(data) {
				if(data == "100.0") {
					window.clearInterval(proTimer);
					$("#facebox div[id='upfileTips']").html("<strong> 上传成功 </strong>");
					addHistory("UPLOAD " + file);
				} else if(data == "-1.0") {
					window.clearInterval(proTimer);
				} else {
					$("#facebox div[id='upfileTips']").html("已经上传 <strong> "+data+"%</strong>");
				}
			}
		});
	}, 1000);
}
var apkFormCount = 1;
function uploadResult(msg) {
	window.clearInterval(proTimer);
	var tips = "<strong>" + msg + "<br/> 已经上传到服务器</strong>";
	$("#facebox div[id='upfileTips']").html(tips);
	// insert the record into 'Wo De Ying Yong'
	var new_apk = 
		" <div class='bg-grey003 itemsBg'>"
		+ "	<p class='news' style='width:198px'>"
		+ "		<img src='images/a.gif'/> " + msg + "<br/>"
		+ "	</p>"
		+ "	<span style='float:right; width:160px;'>"
		+ "		<form name='apkForm" + apkFormCount + "' method='POST' target='hiddenApkFrame'>"
//		+ "			<span>同步</span>"
		;
	if(isApkFile(msg)) {
		new_apk +=
			"		<a onClick=\"apkManager(2," + apkFormCount + ",'" + msg + "')\"> 安装 </a>"
			;
	} 
	new_apk +=
		"			<input type='hidden' name='appName' value='" + msg + "'/>"
		+ "		</form>"
		+ "	</span><br/>"
		+"</div>"
		;
	$("#apks").append(new_apk);
}
function apkManager(type, count, file) {
	var url, tips, history;
	switch(type) {
		case 1: // Sync
			url = "servlet/AppManager?way=1";
			tips = "正在同步...";
			history = "SYNC ";
			break;
		case 2: // Install
			if(!isApkFile(file)) {
				$("#apkTips").html("<strong> 文件格式错误！ </strong>");
				return;
			}
			url = "servlet/AppManager?way=2";
			tips = "正在安装...";
			history = "INSTALL ";
			break;
		case 3: // Reinstall
			if(!isApkFile(file)) {
				$("#apkTips").html("<strong> 文件格式错误！ </strong>");
				return;
			}
			url = "servlet/AppManager?way=3";
			tips = "正在安装...";
			history = "REINSTALL ";
			break;
	}
	var form = "apkForm" + count;
	$("form[name='" + form + "']").attr("action", url).submit();
	$("#apkTips").html(tips);
	// insert the record into 'Li Shi Ji Lu'
	history += file;
	addHistory(history);
}
function isApkFile(file) {
	return /(\.ipa|\.deb)$/.test(file);
}
function hasChinese(file) {
	return /.*[\u4e00-\u9fa5]+.*$/.test(file);
}
function installResult(msg) {
	switch(msg) {
		case "0": case 0:
			$("#apkTips").html("<strong> 安装成功！ </strong>");
			break;
		default:
			$("#apkTips").html("<strong> 安装失败！ </strong>");
			break;
	}
}

var pos1, pos2, time1, time2;
function start(e) {
	pos1 = getXY(e); time1 = new Date().getTime();
}

function finish(e) {
	pos2 = getXY(e); time2 = new Date().getTime();
	var cmd;
	if(3 == e.which) {
		if(isNearby(pos1, pos2)) {
			cmd = "PRESS HOME";
			if(isLongPress(time1, time2)) {
				//long press
				cmd += " DOWN";
//				cmd = "长按HOME键";
			} else {
				//short press
				cmd += " DOWN_AND_UP";
//				cmd = "短按HOME键";
			}
		}
	} else {
		if(isNearby(pos1, pos2)) {
			cmd = "TOUCH " + pos1.x + " " + pos1.y;
			if(isLongPress(time1, time2)) {
				//long press
				cmd += " DOWN";
//				cmd = "长按屏幕(" + pos1.x + ", " + pos1.y + ")";
			} else {
				//short press
				cmd += " DOWN_AND_UP";
//				cmd = "轻触屏幕(" + pos1.x + ", " + pos1.y + ")";
			}
		} else {
			//move
			var steps = 10, ms = 1000;
			cmd = "DRAG " + pos1.x + " " + pos1.y + " " + pos2.x + " " + pos2.y + " " + steps + " " + ms;
//			cmd = "滑动屏幕，从(" + pos1.x + ", " + pos1.y + ")到(" + pos2.x + ", " + pos2.y + ")";
		}
	}
	addHistory(cmd);
}

function isNearby(p1, p2) {
	if(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)) > 10) return false;
	return true;
}

function isLongPress(t1, t2) {
	if((t2 - t1) > 800) return true;
	return false;
}

function getXY(e) {
	e = window.event || e;
	var x = (e.offsetX === undefined) ? getOffset(e).offsetX : e.offsetX;
	var y = (e.offsetY === undefined) ? getOffset(e).offsetY : e.offsetY;
	var pos = {"x":x, "y":y};
	return pos;
}

function getOffset(e) {
	var target = e.target;
	if (target.offsetLeft === undefined) {
		target = target.parentNode;
	}
	var pageCoord = getPageCoord(target);
	var eventCoord = {x:window.pageXOffset + e.clientX, y:window.pageYOffset + e.clientY};
	var offset = {offsetX:eventCoord.x - pageCoord.x, offsetY:eventCoord.y - pageCoord.y};
	return offset;
}

function getPageCoord(element) {
	var coord = {x:0, y:0};
	while (element) {
		coord.x += element.offsetLeft;
		coord.y += element.offsetTop;
		element = element.offsetParent;
	}
	return coord;
}

function parseOperation(operation) {
	var ops = operation.split(" ");
	switch(ops[0]) {
		case "TOUCH":
			switch(ops[3]) {
				case "DOWN":
					operation = "长按屏幕";
					break;
				case "DOWN_AND_UP":
					operation = "点击屏幕";
					break;
			}
			operation +=  "(" + ops[1] + "," + ops[2] + ")";
			break;
		case "PRESS":
			if(ops[1] == "BACK" || ops[1] == "SEARCH" || ops[1] == "UP" || ops[1] == "DOWN"
				|| ops[1] == "LEFT" || ops[1] == "RIGHT" || ops[1] == "CENTER" || ops[1] == "ENTER") {
				operation = "点击" + ops[1] + "键";
			} else {
				switch(ops[2]) {
					case "DOWN":
						operation = "长按";
						break;
					case "DOWN_AND_UP":
						operation = "点击";
						break;
				}
				operation += ops[1] + "键";
			}
			break;
		case "DRAG":
			operation = "滑动屏幕(" + ops[1] + "," + ops[2] + ")到(" + ops[3] + "," + ops[4] + ")";
			break;
		case "TYPE":
			operation = "输入 " + ops[1];
			break;
		case "SYNC":
			operation = "同步 " + ops[1];
			break;
		case "INSTALL":
			operation = "安装 " + ops[1];
			break;
		case "REINSTALL":
			operation = "覆盖安装 " + ops[1];
			break;
		case "UPLOAD":
			operation = "上传 " + ops[1];
			break;
	}
	return operation;
}