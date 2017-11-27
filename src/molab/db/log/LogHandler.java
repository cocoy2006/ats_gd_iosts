package molab.db.log;

import java.util.List;

import molab.db.SQL;
import molab.db.device.Device;
import molab.db.user.User;
import molab.util.Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class LogHandler {

	private SessionFactory factory;
	
	public LogHandler() {
		factory = Util.getInstance().getFactory();
	}
	
	public boolean newLog(String username, String server, String serialNumber, String operation, long time) {
		Session session = null;
		Transaction ts = null;
		try {
			session = factory.openSession();
			ts = session.beginTransaction();		
			
			User u = (User) session.createQuery(String.format(SQL.SELECT_USER, username)).uniqueResult();
			Log log = new Log();
			log.setUser_id(u.getId());
			if(server != null && serialNumber != null) {
				Device d = (Device) session.createQuery(String.format(SQL.SELECT_DEVICE, server, serialNumber)).uniqueResult();
				log.setDevice_id(d.getId());
			}
			if(time == 0) time = System.currentTimeMillis();
			log.setOperation(operation);
			log.setTime(time);
			session.save(log);
			
			ts.commit();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			ts.rollback();
			return false;
		} finally {
			session.close();
		}
	}
	
	public List getLogs(String username, long startTime, long endTime) {
		Session session = null;
		Transaction ts = null;
		try {
			session = factory.openSession();
			ts = session.beginTransaction();		
			
			User u = (User) session.createQuery(String.format(SQL.SELECT_USER, username)).uniqueResult();
			List list = session.createQuery(String.format(SQL.SELECT_LOGS_WITH_USER_ID, u.getId(), startTime, endTime)).list();
			
			ts.commit();
			return list;
		} catch(Exception e) {
			e.printStackTrace();
			ts.rollback();
			return null;
		} finally {
			session.close();
		}
	}
	
	public List getLogs(String server, String serialNumber, long startTime, long endTime) {
		Session session = null;
		Transaction ts = null;
		try {
			session = factory.openSession();
			ts = session.beginTransaction();		
			
			Device d = (Device) session.createQuery(String.format(SQL.SELECT_DEVICE, server, serialNumber)).uniqueResult();
			List list = session.createQuery(String.format(SQL.SELECT_LOGS_WITH_DEVICE_ID, d.getId(), startTime, endTime)).list();
			
			ts.commit();
			return list;
		} catch(Exception e) {
			e.printStackTrace();
			ts.rollback();
			return null;
		} finally {
			session.close();
		}
	}
}
