package com.dragueo.http;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.dragueo.DragueoImg;
import com.dragueo.Prop;


public class Tickets extends Thread {
	private static Properties prop = new Properties();
	private static Logger log = Logger.getLogger(Tickets.class);

	JTextArea textArea = new JTextArea();
	DefaultTableModel model;
	JProgressBar connectionBar;
	String output_tmp = null;
	String output_done = null;
	boolean loop = true;
	int retry = 3;
	
	public Tickets(JTextArea textArea, DefaultTableModel model, JProgressBar connectionBar) {
    	this.textArea = textArea;
    	this.model = model;
    	this.connectionBar = connectionBar;
    	
		output_tmp = "tmp";
		output_done = "ticket_done";
	}
	
	public void terminate() {
		loop = false;
		log.info("Stopping Tickets...");
		textArea.append("Stopping Tickets...\n");
	}
	
	public String curDate() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		String DateToStr = format.format(date);
	
		return DateToStr;
	}
	
	public void run() {
		if(!new File(output_done).exists()) {
    		new File(output_done).mkdir();
    		log.info("Creating directory done: " + output_tmp);
    		textArea.append("Tickets - Creating directory done: " + output_tmp + "\n");
    	}
		
		log.info("Directory tmp: " + output_tmp);
		log.info("Directory done: " + output_done);
		
		textArea.append("Tickets - Directory temporary: " + output_tmp + "\n");
		textArea.append("Tickets - Directory done: " + output_done + "\n");
		
		File actual = new File(output_tmp);
		
		while (loop) {
			/* Proceso */	
	        for( File f : actual.listFiles()) {
	        	if(f.isFile() && f.length() > 0) {
	        		String string_query = null;
	        		if(!"true".equals(Prop.loadProp().getProperty("checkDump","false"))) {
	        			TimeParse timeparse = new TimeParse();
		        		FileReader filereader = null;
						try {
							filereader = new FileReader(f);
						} catch (FileNotFoundException e) {
							log.info(e.toString());
							textArea.append(e.toString() + "\n");
						}
						
						string_query = timeparse.parse(filereader);
						
						try {
							filereader.close();
						} catch (IOException e) {
							log.info(e.toString());
							textArea.append(e.toString() + "\n");
						}
	        		}

					Http http = new Http();
					int tmp = 0;
					
					if("true".equals(Prop.loadProp().getProperty("checkDump","false"))) {
						int c = 0;
						while(c < retry) {
							tmp = http.sendFile(f, Prop.loadProp().getProperty("code"));
							if(tmp != -2) {
								break;
							}
						}
					}else{
						if(string_query != null) {
							int c = 0;
							while(c < retry){
								tmp = http.sendCommand(string_query, Prop.loadProp().getProperty("code"));
								if(tmp != -2) {
									break;
								}
								c++;
							}
						}else{
							tmp = -1;
						}
					}
					
					ImageIcon status = null;
					
					if( tmp == -1 ) {
						//f.renameTo(new File(output_done + "\\" + f.getName()));
						f.delete();
						log.info("ticket: " + f.getName() + " ERROR");
						
						status = DragueoImg.Image("nogo.png",16);
					} else if( tmp == 200) {
						//f.renameTo(new File(output_done + "\\" + f.getName()));
						f.delete();
						log.info("ticket: " + f.getName() + " OK");
						
						connectionBar.setValue(100);
						connectionBar.setForeground(Color.GREEN);
						connectionBar.setBackground(Color.GREEN);
						
						status = DragueoImg.Image("go.png",16);
					}else if( tmp > 399 && tmp < 500) {
						log.info("error: " + tmp + ", ticket: " + f.getName());
						
						connectionBar.setValue(100);
						connectionBar.setForeground(Color.RED);
						connectionBar.setBackground(Color.RED);
						
						status = DragueoImg.Image("nogo.png",16);
					}else{
						log.info("error: " + tmp + ", ticket: " + f.getName());

						connectionBar.setValue(100);
						connectionBar.setForeground(Color.ORANGE);
						connectionBar.setBackground(Color.ORANGE);
						
						status = DragueoImg.Image("nogo.png",16);
					}
					
					model.insertRow(0, new Object[]{curDate(), f.getName(), String.valueOf(tmp), status});
	        	}
	            
	        }
	        
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
		
		log.info("Stopped Tickets...");
		textArea.append("Stopped Tickets...\n");
	}
}
