package molab.util;

import java.io.File;

import molab.db.device.Device;
import molab.db.device.DeviceHandler;

public class BasicDevice {

	private int id;
	private String imei;
	private String server;
	private String serialNumber;
	private String manufacturer;
	private String model;
	private String os;
	private int width;
	private int height;
	private int isEmulator;
	private int state;
	private String css;

	public BasicDevice(int id) {
		this.id = id;
		Util util = Util.getInstance();
		Device device = new DeviceHandler().getDeviceInfo(id);
		this.manufacturer = device.getManufacturer();
		this.model = device.getModel();
		
		String imageURL = util.getConf().getProperty("BaseImageURL");
		String imageRealpath = util.getConf().getProperty("BaseImageRealpath");
		String home = imageURL.concat(manufacturer).concat("/").concat(model).concat("/");
		String realpath = imageRealpath.concat("/").concat(manufacturer).concat("/").concat(model).concat("/");
		if(isExists(realpath)) {
			css = home.concat("main.css");
		} else {
			css = imageURL.concat("x/main.css");
		}
	}

	private boolean isExists(String url) {
		return new File(url).exists();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getIsEmulator() {
		return isEmulator;
	}

	public void setIsEmulator(int isEmulator) {
		this.isEmulator = isEmulator;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}
	
}
