<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="molab.util.Util" %>
<%@ page import="molab.util.Lock" %>
<%@ page import="molab.db.log.LogHandler" %>
<%@ page import="molab.db.device.DeviceHandler" %>
<html>
	<% 
	String username = request.getParameter("username");
	String server = request.getParameter("server");
	String serialNumber = request.getParameter("serialNumber");
	
	DeviceHandler handler = new DeviceHandler();
	if(handler.getState(server, serialNumber) == 1) {
		handler.setState(server, serialNumber, 0);
	}
	new LogHandler().newLog(username, server, serialNumber, "UNLOCK", System.currentTimeMillis());
	Util.getInstance().getLock().getLocks().get(serialNumber).destroy();
	
	response.sendRedirect(request.getParameter("fromUrl"));
	%>
</html>
