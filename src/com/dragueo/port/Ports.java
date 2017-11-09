package com.dragueo.port;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.comm.CommPortIdentifier;

import org.apache.log4j.Logger;

import com.dragueo.Prop;

/* http://smslib.org/download/ */

public class Ports {
	private static Logger log = Logger.getLogger(Ports.class);
	
	public static List<String> listPorts() {
		List<String> ports = new ArrayList<String>();
		
		try {
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            
            while (portList.hasMoreElements()) {
                CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
                
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                	if(Prop.loadProp().getProperty("checkDebug","false").equals("true"))
                		log.info("Serial port: " + portId.getName());
                	ports.add(portId.getName());
                } else if (portId.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
                	if(Prop.loadProp().getProperty("checkDebug","false").equals("true"))
                		log.info("Parallel port: " + portId.getName());
                } else {
                	if(Prop.loadProp().getProperty("checkDebug","false").equals("true"))
                		log.info("Other port: " + portId.getName());
                }
            }
        } catch (Exception ex) {
            log.error(ex);
        }
		
		return ports;
	}
}
