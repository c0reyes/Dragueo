package com.dragueo.port;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Enumeration;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.dragueo.Prop;

public class ComPortRead extends Thread implements SerialPortEventListener {	
	CommPortIdentifier portId;
    Enumeration portList;
    SerialPort serialPort;
    BufferedReader bufferedReader;
    boolean loop = true;
    int lines = 0;
    int d = 0;
	int count = 1;
	boolean ticket = false;
    
    static InputStream inputStream;
    static String messageBuffer = "";
    static String output_tmp;
    static String com;

    JTextArea textArea = new JTextArea();
    
    private static Logger log = Logger.getLogger(ComPortRead.class);
    
    public ComPortRead() {
    	com = Prop.loadProp().getProperty("com");
        output_tmp = "tmp";
    }
    
    public ComPortRead(JTextArea textArea) {
    	this.textArea = textArea;
    	com = Prop.loadProp().getProperty("com");
        output_tmp = "tmp";
    }
    
    public void terminate() {
    	if(serialPort != null) {
    		serialPort.close();
    		log.info("Stopped ComPortRead...");
    		textArea.append("Stopped ComPortRead...\n");
    	}
    }
    
    public void run() {
    	lines = Integer.parseInt(Prop.loadProp().getProperty("lines","8"));
    	if(!new File(output_tmp).exists()) {
    		new File(output_tmp).mkdir();
    		log.info("Creating directory: " + output_tmp);
    	}
    	
    	if(com == null) {
    		log.info("Incorrect port");
    		textArea.append("Incorrect port\n");
    		
    		log.info("Cant start ComPortRead");
    		textArea.append("Cant start ComPortRead\n");
    		return;
    	}
    	
        try {
        	log.info("Port: " + com);
        	log.info("Directory tmp: " + output_tmp);
        	
        	textArea.append("ComPortRead - Port: " + com + "\n");
        	textArea.append("ComPortRead - Directory tmp: " + output_tmp + "\n");
        	
        	String comtest = Prop.loadProp().getProperty("comTest");
        	if(comtest != null) {
        		log.info("com port test: " + comtest);	
            	textArea.append("ComPortRead - com port test: " + comtest + "\n");
            	textArea.append("ComPortRead - *** use port test to print ***\n");
        	}
        	
            portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                portId = (CommPortIdentifier) portList.nextElement();
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    if (portId.getName().equals(com)) {
                        serialPort = (SerialPort) portId.open("Dragueo - ComPortRead", 2000);
                        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        serialPort.addEventListener(this);
                        serialPort.notifyOnDataAvailable(true);
                        if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
	                        serialPort.notifyOnOutputEmpty(true);
	                        serialPort.notifyOnBreakInterrupt(true);
	                        serialPort.notifyOnCarrierDetect(true);
	                        serialPort.notifyOnCTS(true);
	                        serialPort.notifyOnDSR(true);
	                        serialPort.notifyOnOutputEmpty(true);
	                        serialPort.notifyOnOverrunError(true);
	                        serialPort.notifyOnRingIndicator(true);
                        }
                        //bufferedReader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                        inputStream = serialPort.getInputStream();
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex);
        }
    }
 
    /* @Override */
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
        	case SerialPortEvent.OE: 					// Overrun error.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Overrun error.");
        		}
        	case SerialPortEvent.FE: 					// Framing error.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Framing error.");
        		}
        	case SerialPortEvent.PE: 					// Parity error.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Parity error.");
        		}
        	case SerialPortEvent.CD: 					// Carrier detect.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Carrier detect.");
        		}
        	case SerialPortEvent.CTS: 					// Clear to send.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Clear to send.");
        		}
        	case SerialPortEvent.DSR: 					// Data set ready.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Data set ready.");
        		}
        	case SerialPortEvent.RI: 					// Ring indicator.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Ring indicator.");
        		}
        	case SerialPortEvent.BI: 					// Break interrupt.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Break interrupt.");
        		}
        	case SerialPortEvent.OUTPUT_BUFFER_EMPTY:   // Output buffer is empty.
        		if(Prop.loadProp().getProperty("checkDebug","false").equals("true")) {
        			log.info("Output buffer is empty.");
        		}
        		/* Posible modificacion para envia de archivo */
        		break;
            case SerialPortEvent.DATA_AVAILABLE: 		// Data available at the serial port.
                try {
                	int c = 0;
                	
                    StringBuffer data = new StringBuffer();;
                    
                	while(inputStream.available() > 0) {
                		c = inputStream.read();

                		if(c == 27) {
                    		log.info("BEGIN");
                    		textArea.append("BEGIN\n");
                    		messageBuffer = "";
                    		count = 1;
                    		ticket = true;
                    	}
                		
                		if(c != 13)
                			data.append((char)c);
                		
                		if(c == d && c == 10) {
                			count++;
                		}else{
                			count = 1;
                		}
                		d = c;
                		
                		// if(data.toString().matches(".*,.*:.*:.*")) {
                		// 4 = End of transmission
                		// 3 = end of text
                		// 2 = start of text
                		// 27 = escape
                		// 13 = carriage return
                		// 10 = line feed
                		if(count >= lines && !"".equals(messageBuffer) && ticket) {
                			File file = new File(output_tmp + "\\" + System.currentTimeMillis() + ".ticket");
                			FileWriter fileWritter;
                			BufferedWriter bufferWritter;
            		    
                			fileWritter = new FileWriter(output_tmp + "\\" + file.getName(),true);
                			bufferWritter = new BufferedWriter(fileWritter);
                			bufferWritter.write(messageBuffer);
                			bufferWritter.close();
                			fileWritter.close(); 
                			
                			log.info("END");
                    		textArea.append("END\n");
                    		
                    		count = 1;
                    		messageBuffer = "";
                    		ticket = false;
                		}
                	}
                	
                	log.info(data.toString().replaceAll("\r", "\n"));
            		textArea.append(data.toString().replaceAll("\r", "\n"));
            		
            		messageBuffer += data.toString() + "\n";
                } catch (Exception ex) {
                    log.error(ex);
                    textArea.append(ex.toString() + "\n");
                    ex.printStackTrace();
                }
                break;
        }
    }
}
