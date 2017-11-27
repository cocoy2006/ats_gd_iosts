package molab.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Dir {

	public void newDir(String parent, String dirName) {

		String dir = parent.concat(File.separator).concat(dirName);
		File directory = new File(dir);
		if (directory.isDirectory()) {

		} else {
			directory.mkdir();
			System.out.println("Ŀ¼ '" + dirName + "' �����ɹ�!");
		}
	}

	public static void main(String[] args) {
		BufferedReader br = null;
		try {
			Process proc = Runtime.getRuntime().exec("ideviceinstaller -l");
			br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			@SuppressWarnings("unused")
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(br.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
