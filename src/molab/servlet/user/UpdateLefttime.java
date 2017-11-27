package molab.servlet.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import molab.db.user.UserHandler;

public class UpdateLefttime extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String server = request.getParameter("server");
		String serialNumber = request.getParameter("serialNumber");
		int lefttime = Integer.parseInt(request.getParameter("lefttime"));
		
		int result = new UserHandler().updateLefttime(username, server, serialNumber, lefttime);
		request.getSession().setAttribute("lefttime", lefttime);
		
		response.setContentType("application/xml;charset=utf-8");
		response.getWriter().print(result);
	}

}
