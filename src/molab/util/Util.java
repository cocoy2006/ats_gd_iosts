package molab.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Util {
	private static Util util;
	private SessionFactory factory;
	private Conf conf;
	private Expire expire;
	private Lock lock;
	private Known known;

	private Util() {
		factory = new Configuration().configure().buildSessionFactory();
		conf = new Conf();
		expire = new Expire();
		// lock = new Lock();
	}

	public static Util getInstance() {
		if (util == null) {
			synchronized (Util.class) {
				util = new Util();
			}
		}
		return util;
	}

	public void setFactory(SessionFactory factory) {
		this.factory = factory;
	}

	public SessionFactory getFactory() {
		return factory;
	}

	public void setConf(Conf conf) {
		this.conf = conf;
	}

	public Conf getConf() {
		return conf;
	}

	public void setExpire(Expire expire) {
		this.expire = expire;
	}

	public Expire getExpire() {
		return expire;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public Lock getLock() {
		return lock;
	}

	public void setKnown(Known known) {
		this.known = known;
	}

	public Known getKnown() {
		return known;
	}

}
