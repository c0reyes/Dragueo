package com.dragueo.port;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* http://edn.embarcadero.com/article/31915 */

public class comm {
	String[] lib = {"comm.jar","javax.comm.properties"};
	String dll = "win32com.dll";
	
	String windir = null;
	String java_home = null;
	
	public comm() {
		Map<String, String> env = System.getenv();
		
        for (String envName : env.keySet()) {
        	if(envName.equals("windir")) {
        		windir = env.get(envName);
        	}
        	if(envName.equals("JAVA_HOME")) {
        		java_home = env.get(envName);
        	}
        }
	}
	
	public void check() {
		List<String> tt = new ArrayList<String>();
		
		if(java_home != null) {
			for(String t: lib) {
				tt.add(java_home + "\\lib\\" + t);
				tt.add(java_home + "\\jre\\lib\\ext\\" + t);
			}
		
			tt.add(java_home + "\\jre\\bin\\" + dll);
			tt.add(java_home + "\\jre\\bin\\" + dll);
		}else{
			System.out.println("%JAVA_HOME% NOK");
		}
		
		if(windir != null) {
			tt.add(windir + "\\System32\\" + dll);
		}else{
			System.out.println("%windir% NOK");
		}
		
		for(String t: tt) {
			if(new File(t).exists()) {
				System.out.println(t + " OK");
			}else{
				System.out.println(t + " NOK");
			}
		}
	}
	
	public void install() {
		
	}
}
