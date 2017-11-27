package molab.util;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import molab.db.device.DeviceHandler;
import molab.db.log.LogHandler;

public class DeviceWithTimer {
	
	private String username;
	private String server;
	private String serialNumber;
	private long firstTime;
	private long lastTime;
	private long totalTime;
	private Timer exceptionTimer;
	private Timer timeoutTimer;
	
	
	public DeviceWithTimer(String username, String server, String serialNumber, int exceptionTime, int timeoutTime) {
		this.username = username;
		this.server = server;
		this.serialNumber = serialNumber;
		this.lastTime = this.firstTime = System.currentTimeMillis();		
		this.totalTime = timeoutTime * 1000;
		
		exceptionTimer = new Timer();
		exceptionTimer.schedule(new ExceptionTimer(exceptionTime), 5 * 1000, exceptionTime * 60 * 1000);
		
		timeoutTimer = new Timer();
		timeoutTimer.schedule(new TimeoutTimer(), totalTime);
	}
	
	public long getFirstTime() {
		return firstTime;
	}
	
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
		System.out.println("[DeviceWithTimer setLastTime] lastTime = " + new Date(this.lastTime));
	}
	
	public long getRestTime() {
		return totalTime - System.currentTimeMillis() + firstTime;
	}
	
	public void destroy() {
		exceptionTimer.cancel();
		timeoutTimer.cancel();
	}
	
	class ExceptionTimer extends TimerTask {		
		private long exceptionTime;
		
		public ExceptionTimer(long exceptionTime) {
			this.exceptionTime = exceptionTime;
		}		
		
		@Override
		public void run() {
			if(isOver(lastTime, System.currentTimeMillis(), exceptionTime * 60 * 1000)) {
				new LogHandler().newLog(username, server, serialNumber, "UNLOCK", System.currentTimeMillis());
				new DeviceHandler().setState(server, serialNumber, 0);
			}
		}
		
		private boolean isOver(long previousTime, long currentTime, long overTime) {
			if((currentTime - previousTime) >= overTime) {
				return true;
			}
			return false;
		}
	}
	
	class TimeoutTimer extends TimerTask {
		
		@Override
		public void run() {
			new LogHandler().newLog(username, server, serialNumber, "UNLOCK", System.currentTimeMillis());
			new DeviceHandler().setState(server, serialNumber, 0);
		}
	}
}
