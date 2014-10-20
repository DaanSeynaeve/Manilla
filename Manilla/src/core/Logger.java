package core;

public class Logger {
	
	private static StringBuilder log = new StringBuilder();
	private static boolean print = false;
	private static boolean remember = false;
	
	public static void setPrinting(boolean doPrint) {
		Logger.print = doPrint;
	}
	
	public static void setRemember(boolean doRemember) {
		Logger.remember = doRemember;
	}
	
	public static void log(String msg) {
		if(remember) {
			log.append(msg + "\n");
		}
		if(print) {
			System.out.println(msg);
		}
	}
	
	public static void log(Object o) {
		log(o.toString());
	}
	
	public static String getLog() {
		return log.toString();
	}

}
