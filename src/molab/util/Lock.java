package molab.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import molab.db.device.Device;
import molab.db.device.DeviceHandler;

public class Lock {

	private Map<String, DeviceWithTimer> locks = new HashMap<String, DeviceWithTimer>();
	private DeviceHandler handler = new DeviceHandler();
	
	public Lock() {
		locks = new HashMap<String, DeviceWithTimer>();
		handler = new DeviceHandler();
		List devices = handler.getIOSDevices();
		if(devices != null) {
			for(int i = 0; i < devices.size(); i++) {
				locks.put(((Device) devices.get(i)).getSerialNumber(), null);
			}
		}
	}

	public boolean applyLock(String username, String server, String serialNumber, int lefttime) {
		Util util;
		int exceptionTime;
		DeviceWithTimer device;
		switch(handler.isAvailable(username, server, serialNumber)) {
			case 0: 
				util = Util.getInstance();
				exceptionTime = Integer.parseInt(util.getConf().getProperty("exceptionTime"));
				device = new DeviceWithTimer(username, server, serialNumber, exceptionTime, lefttime);
				locks.put(serialNumber, device);
				handler.setState(server, serialNumber, 1);
				return true;
			case 1: case 4:
				return false;
			case 3:
				handler.setState(server, serialNumber, 0);
				return false;
			case 5:
				util = Util.getInstance();
				exceptionTime = Integer.parseInt(util.getConf().getProperty("exceptionTime"));
				device = new DeviceWithTimer(username, server, serialNumber, exceptionTime, lefttime);
				locks.put(serialNumber, device);
				return true;
		}
		return false;
	}
	
//	public boolean updateLastTime(String serialNumber, long lastTime) {
//		if(locks.containsKey(serialNumber)) {
//			synchronized(locks) {
//				locks.get(serialNumber).setLastTime(lastTime);
//			}
//			return true;
//		}
//		return false;
//	}
	
	public Map<String, DeviceWithTimer> getLocks() {
		synchronized(locks) {
			return locks;
		}
	}
}