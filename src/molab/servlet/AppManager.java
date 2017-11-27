package molab.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import molab.util.Util;
import molab.util.installer.Command;

public class AppManager extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		HttpSession session = request.getSession();
		String serialNumber = session.getAttribute("serialNumber").toString();
		String username = session.getAttribute("username").toString();
		String appName = request.getParameter("appName");
		String app = Util.getInstance().getConf().getProperty("BaseUploadRealpath").concat("/")
			.concat(username).concat("/").concat(appName);
		// exec installer & return result
		String result = "<script type='text/javascript'>parent.installResult('" + new Command().install(serialNumber, app) + "')</script>";
		
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().print(result);
	}
}
