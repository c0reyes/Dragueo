package com.dragueo.port;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

import org.apache.log4j.Logger;

public class ComPortWrite {
	static Enumeration portList;
    static CommPortIdentifier portId;
    static SerialPort serialPort;
    static OutputStream outputStream;
    
    private static Logger log = Logger.getLogger(ComPortWrite.class);
    
    public void write(String com, String text) {
    	portList = CommPortIdentifier.getPortIdentifiers();
    	
    	while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();

            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals(com)) {
                	 for (String line : text.split("\\n")) {
                		 //log.info("--> " + line);
	                    try {
	                        serialPort = (SerialPort)
	                        portId.open("Dragueo ComPortWrite", 2000);
	                    } catch (PortInUseException e) {System.out.println(e);}
	                    try {
	                        outputStream = serialPort.getOutputStream();
	                    } catch (IOException e) {}
	                    try {
	                        serialPort.setSerialPortParams(9600,
	                            SerialPort.DATABITS_8,
	                            SerialPort.STOPBITS_1,
	                            SerialPort.PARITY_NONE);
	                    } catch (UnsupportedCommOperationException e) {System.out.println(e);}
	                    try { 
	            				outputStream.write(line.getBytes());
	            				//outputStream.write(13);
	            				//outputStream.write(4);
	                    } catch (IOException e) {System.out.println(e);}
	                    serialPort.close();
	                    try {
	                    	Thread.sleep(200);
	                    }catch(Exception e) {}
                	}
                }
            }
        }
    }
}
