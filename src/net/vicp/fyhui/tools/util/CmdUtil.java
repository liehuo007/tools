package net.vicp.fyhui.tools.util;

import java.io.IOException;

public class CmdUtil {

	public static void execBat() {
		
	}
	
	public static void execExe () {
		String cmdFile  = "C:\\Users\\Administrator\\Desktop>ceshi.bat";
		String cmdScript = "dir";
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec("cmd.exe");
			System.out.println(process);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
