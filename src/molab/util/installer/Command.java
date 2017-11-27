package molab.util.installer;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Command {

	public String getUUID() {
		ProcessBuilder pb = new ProcessBuilder(ideviceid());
		pb.redirectErrorStream(true);
		String uuid = null;
		try {
			Process p = pb.start();
			BufferedReader br = new BufferedReader(new InputStreamReader(p
					.getInputStream()));
			uuid = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return uuid;
	}

	public int install(String uuid, String ipa) {
		System.out.println("install " + ipa + " is starting...");
		ProcessBuilder pb = new ProcessBuilder(ideviceinstaller(uuid, ipa));
		pb.redirectErrorStream(true);
		Process p = null;
		int exit = 0;
		try {
			System.out.println("process is starting...");
			p = pb.start();
			p.getOutputStream().close();
			System.out.println("install feedback is reading...");
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String tmp1, tmp2 = null;
			System.out.println("writing logs to log file...");
			FileWriter fw = new FileWriter("/home/molab/log");
			while ((tmp1 = br.readLine()) != null) {
				tmp2 = tmp1;
				fw.write(tmp1 + "\r\n");
			}
			System.out.println("install is finishing...");
			exit = p.waitFor();
			System.out.println("install is finished!");
//			if(exit == 0  && !isComplete(tmp2)) {
//				exit = 1;
//			}
			fw.write("waitFor() is " + exit + "\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return exit;
	}
	
	private boolean isComplete(String str) {
		return str.indexOf("Complete") > -1;
	}

	public String[] ideviceid() {
		Parameters p = new Parameters();
		return p.addParam("/usr/local/bin/idevice_id").addParam("-l")
				.getParams();
	}

	public String[] ideviceinstaller() {
		Parameters p = new Parameters();
		return p.addParam("/usr/local/bin/ideviceinstaller").addParam("-l")
				.getParams();
	}

	public String[] ideviceinstaller(String uuid, String ipa) {
		Parameters p = new Parameters();
		return p.addParam("/usr/local/bin/ideviceinstaller").addParam("-U")
				.addParam(uuid).addParam("-i").addParam(ipa).getParams();
	}
}
