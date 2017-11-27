package molab.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import molab.db.log.LogHandler;
import molab.util.Util;

public class ApplyLock extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String serialNumber = request.getParameter("serialNumber");
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String fromUrl = request.getParameter("fromUrl");
		String server = request.getParameter("server");
		String id = request.getParameter("id");
		int lefttime = Integer.parseInt(request.getParameter("lefttime"));
		
		/** redirect way */
		session.setAttribute("serialNumber", serialNumber);
		session.setAttribute("id", id);
		session.setAttribute("username", username);
		session.setAttribute("server", server);
		session.setAttribute("fromUrl", fromUrl);
		session.setAttribute("lefttime", lefttime);
		
		Util util = Util.getInstance();
		
		if(util.getExpire().isExpire()) {
			response.sendRedirect(fromUrl);
		} else {
			if(Util.getInstance().getLock().applyLock(username, server, serialNumber, lefttime)) {
				// new log
				new LogHandler().newLog(username, server, serialNumber, "LOCK", System.currentTimeMillis());
//				response.sendRedirect("/iOSTS/index.jsp?username=" + username + "&server=" + server + "&serialNumber=" + serialNumber + "&fromUrl=" + fromUrl + "&id=" + id + "&lefttime=" + lefttime);
				response.sendRedirect("/iOSTS/client.jsp?DEFAULT");
			} else {
				response.sendRedirect(fromUrl);
			}
		}
	}

}
