package eval;

import java.io.File;

/*
 * Check the OS is Win, Mac or Linux
 */
public  final class EvalUtil
{
    private static String OS = null;
    
    public static String getOsName()
    {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }
    public static boolean isWindows()
    {
        return getOsName().startsWith("Windows");
    }

    public static boolean isMac() {
        return getOsName().startsWith("Mac");
    }

    public static boolean isLinux() {
        return getOsName().startsWith("Linux");
    }
    
    // Create Logs
	public static void creatLogDir(String LogsDir) {
		
		File logsFDir = new File(LogsDir);
		if (logsFDir.mkdirs()) {
		    System.out.format("Directory %s has been created.", logsFDir.getAbsolutePath());

		} else if (logsFDir.isDirectory()) {
		    System.out.format("Directory %s has already been created.", logsFDir.getAbsolutePath());

		} else {
		    System.out.format("Directory %s could not be created.", logsFDir.getAbsolutePath());
		}
	}
	
	//main
    public static void main(String args[]){
        System.out.println(EvalUtil.getOsName());
        System.out.println(EvalUtil.isMac());
    }
}