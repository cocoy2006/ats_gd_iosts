package molab.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import molab.db.device.Device;
import molab.db.device.DeviceHandler;

public class Known {

	private Map<Integer, BasicDevice> known = new HashMap<Integer, BasicDevice>();
	
	public Known() {
		known = new HashMap<Integer, BasicDevice>();
		List devices = new DeviceHandler().getIOSDevices();
		if(devices != null) {
			for(int i = 0; i < devices.size(); i++) {
				int id = ((Device) devices.get(i)).getId();
				known.put(id, new BasicDevice(id));
			}
		}
	}
	
	public BasicDevice getKnown(int id) {
		return known.get(id);
	}
}
