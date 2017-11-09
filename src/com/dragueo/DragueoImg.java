package com.dragueo;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

public class DragueoImg {
	private static Logger log = Logger.getLogger(DragueoImg.class);
	
	public static ImageIcon Image(String img) {	
	    Image image = null;
	    
		try {
			image = ImageIO.read(DragueoImg.class.getClass().getResource("/res/" + img));
			
			return new ImageIcon(image);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}
	
	public static ImageIcon Image(String img, int resize) {
	    Image image = null;
	    Image resizedImage = null;
	    
		try {
			image = ImageIO.read(DragueoImg.class.getClass().getResource("/res/" + img));
			resizedImage = image.getScaledInstance(resize, -1, Image.SCALE_FAST);
			
			return new ImageIcon(resizedImage);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}
}
