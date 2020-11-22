package com.dragueo.http;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.apache.log4j.Logger;


public class TimeParse {
	static Hashtable<String, String> months = new Hashtable<String, String>();
	static String entry = "0:0";
	static String tree_speed = "0:0";
	static String dialin = "0:0";
	static String reaction = "0:0";
	static String ft60 = "0:0";
	static String ft330 = "0:0";
	static String ft660 = "0:0";
	static String mph1 = "0:0";
	static String ft1320 = "0:0";
	static String mph = "0:0";
	static String first = "";
	static String status = "";
	static String fecha = "";
	private static Logger log = Logger.getLogger(TimeParse.class);
	
	public TimeParse() {
		months.put("Enero", "01");
		months.put("Febrero", "02");
		months.put("Marzo", "03");
		months.put("Abril", "04");
		months.put("Mayo", "05");
		months.put("Junio", "06");
		months.put("Julio", "07");
		months.put("Agosto", "08");
		months.put("Septiembre", "09");
		months.put("Octubre", "10");
		months.put("Noviembre", "11");
		months.put("Diciembre", "12");
	}
	
	public String parse(FileReader f) {
		try {
			BufferedReader br = new BufferedReader(f);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				if( sCurrentLine.indexOf("Entry") > -1 ) {
					entry = sCurrentLine.replaceAll("(\\w+)\\s+Entry\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf("TREE SPEED") > -1 ) {
					tree_speed = sCurrentLine.replaceAll("(\\w+)\\s+TREE SPEED\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf("DIAL-IN") > -1 ) {
					dialin = sCurrentLine.replaceAll("(\\w+)\\s+DIAL-IN\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf("REACTION") > -1 ) {
					reaction = sCurrentLine.replaceAll("-", "").replaceAll("(\\w+)\\s+REACTION\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf(" 60 ") > -1 ) {
					ft60 = sCurrentLine.replaceAll("(\\w+)\\s+60\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf(" 330 ") > -1 ) {
					ft330 = sCurrentLine.replaceAll("(\\w+)\\s+330\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf(" 660 ") > -1 ) {
					ft660 = sCurrentLine.replaceAll("(\\w+)\\s+660\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf("MPH1") > -1 ) {
					mph1 = sCurrentLine.replaceAll("(\\w+)\\s+MPH1\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf(" 1320 ") > -1 ) {
					ft1320 = sCurrentLine.replaceAll("(\\w+)\\s+1320\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf("MPH") > -1 ) {
					mph = sCurrentLine.replaceAll("(\\w+)\\s+MPH\\s+(\\w+)", "$1:$2");
				}
				if( sCurrentLine.indexOf("FIRST") > -1 ) {
					first = sCurrentLine.replaceAll("(\\w*)\\s+FIRST\\s+(\\w*)", "$1:$2");
				}
				if( sCurrentLine.indexOf("STATUS") > -1 ) {
					status = sCurrentLine.replaceAll("(\\w*)\\s+STATUS\\s+(\\w*)", "$1:$2");
					status = status.replaceAll("Autostart", "");
				}
				if(sCurrentLine.matches(".*,.*:.*:.*")) {
					SimpleDateFormat parse = new SimpleDateFormat("MM d, yyyy hh:mm:ss aa");
					String t = sCurrentLine.replaceAll("\\s{2,}", " ");
					String m = t.substring(0, t.indexOf(" "));
					t = months.get(m) + " " + t.substring(t.indexOf(" "), t.length());
					fecha = t;
					Date date = parse.parse(t);
				}
			} 
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		
		try {
			String r = "";
			
			if(entry.trim().split(":").length > 0) {
				r += "entryLeft=" + (entry.trim().split(":"))[0]
				 + "&entryRight=" + (entry.trim().split(":"))[1];
			}
			
			if(tree_speed.trim().split(":").length > 0) {
				r += "&treespeedLeft=" + (tree_speed.trim().split(":"))[0]
				 + "&treespeedRight=" + (tree_speed.trim().split(":"))[1];
			}
						 
			if(dialin.trim().split(":").length > 0) {			
				r += "&dialinLeft=" + (dialin.trim().split(":"))[0]
				 + "&dialinRight=" + (dialin.trim().split(":"))[1];
			}
			
			if(reaction.trim().split(":").length > 0) {
				r += "&reactionLeft=" + (reaction.trim().split(":"))[0]
				 + "&reactionRight=" + (reaction.trim().split(":"))[1];
			}
			
			if(ft60.trim().split(":").length > 0) {
				r += "&ft60Left=" + (ft60.trim().split(":"))[0]
				 + "&ft60Right=" + (ft60.trim().split(":"))[1];
			}
			
			if(ft330.trim().split(":").length > 0) {
				r += "&ft330Left=" + (ft330.trim().split(":"))[0]
				 + "&ft330Right=" + (ft330.trim().split(":"))[1];
			}
			
			if(ft660.trim().split(":").length > 0) {
				r += "&ft660Left=" + (ft660.trim().split(":"))[0]
				 + "&ft660Right=" + (ft660.trim().split(":"))[1];
			}
			
			if(mph1.trim().split(":").length > 0) {
				r += "&mph1Left=" + (mph1.trim().split(":"))[0]
				 + "&mph1Right=" + (mph1.trim().split(":"))[1];
			}
			
			if(ft1320.trim().split(":").length > 0) {
				r += "&ft1320Left=" + (ft1320.trim().split(":"))[0]
				 + "&ft1320Right=" + (ft1320.trim().split(":"))[1];		 
			}
			
			if(mph.trim().split(":").length > 0) {
				r += "&mphLeft=" + (mph.trim().split(":"))[0]
				 + "&mphRight=" + (mph.trim().split(":"))[1];
			}
			
			if(first.trim().split(":").length > 0) {
				r += "&firstLeft=" + (first.trim().split(":"))[0]
				 + "&firstRight=" + (first.trim().split(":"))[1];
			}
			
			if(status.trim().split(":").length > 0) {
				r += "&statusLeft=" + (status.trim().split(":"))[0]
				 + "&statusRight=" + (status.trim().split(":"))[1];
			}
				 
			r += "&fecha=" + fecha.replaceAll(" ","%20").trim();
			
			return r;
		}catch(Exception e) {
			log.error(e.toString(), e);
			return null;
		}
	}
}
