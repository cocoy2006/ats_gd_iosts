package molab.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import molab.util.Conf;
import molab.util.Expire;
import molab.util.Known;
import molab.util.Lock;
import molab.util.Util;

public class LoadOnStartup extends HttpServlet implements ServletContextListener {

	public void contextInitialized(ServletContextEvent arg0) {
		
		//��ʼ��Adb����
		Util util = Util.getInstance();
		
//		Expire expire = new Expire();
		Expire expire = util.getExpire();
		boolean isExpire = expire.init(arg0.getServletContext().getRealPath("/WEB-INF/"));
		if(isExpire) System.exit(0);
//		util.setExpire(expire);
		//��ʼ��ʱ�����
//		Conf conf = new Conf();
		Conf conf = util.getConf();
		conf.setProperty("exceptionTime", arg0.getServletContext().getInitParameter("exceptionTime"));
		conf.setProperty("timeoutTime", arg0.getServletContext().getInitParameter("timeoutTime"));
		conf.setProperty("runTestcaseResult", arg0.getServletContext().getInitParameter("runTestcaseResult"));
		//��ʼ����վURL
		conf.setProperty("BaseURL", "/iOSTS");
		//��ȡ��ĻĿ¼
//		conf.setProperty("BaseScreenCapturedURL", conf.getProperty("BaseURL").concat("/screenCaptured/"));
//		conf.setProperty("BaseScreenCapturedRealpath", arg0.getServletContext().getRealPath("/screenCaptured/"));
		//��վͼƬĿ¼
		conf.setProperty("BaseImageURL", conf.getProperty("BaseURL").concat("/image/"));
		conf.setProperty("BaseImageRealpath", arg0.getServletContext().getRealPath("/image/"));
		//CSS�ļ�Ŀ¼
//		conf.setProperty("BaseCssURL", conf.getProperty("BaseURL").concat("/css/"));
//		conf.setProperty("BaseCssRealpath", arg0.getServletContext().getRealPath("/css/"));
		//�ϴ��ļ�Ŀ¼
		conf.setProperty("BaseUploadRealpath", arg0.getServletContext().getRealPath("/uploaded/"));
		//��ʷ�ļ�Schema�ļ�Ŀ¼
//		conf.setProperty("BaseSchemaRealpath", arg0.getServletContext().getRealPath("/schema/"));
		//�Զ����Խ��Ŀ¼
//		conf.setProperty("BaseAutotestResultRealpath", arg0.getServletContext().getRealPath("/autotestResult/"));
		//��ʼ����֪�豸����
//		conf.setProperty("temp", arg0.getServletContext().getRealPath("/temp/"));
//		util.setConf(conf);
		
//		Configuration cfg = new Configuration();
//		SessionFactory factory = cfg.configure().buildSessionFactory();
//		util.setFactory(factory);
		
		Lock lock = new Lock();
		util.setLock(lock);
		
//		Known known = new Known();
//		util.setKnown(known);
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}
}
