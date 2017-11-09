package com.dragueo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.dragueo.port.ComPortWrite;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.Toolkit;

public class DragueoSimulator extends JDialog {
	private final Action action = new SwingActionClose();
	private final Action send = new SwingActionSend();
	private final Action save = new SwingActionSave();
	
	private final JPanel contentPanel = new JPanel();
	JTextArea textArea = new JTextArea();
	JComboBox comboBox = null;
	
	private Properties prop = new Properties();
	
	/**
	 * Create the dialog.
	 */
	public DragueoSimulator() {
		prop = Prop.loadProp();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(DragueoSimulator.class.getResource("/res/icon.png")));
		setTitle("Dragueo - Simulator");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		{
			JLabel lblPuerto = new JLabel("Port");
			contentPanel.add(lblPuerto, "2, 2, right, default");
		}
		{
			List<String> ports = com.dragueo.port.Ports.listPorts();
			comboBox = new JComboBox(ports.toArray());
			contentPanel.add(comboBox, "4, 2, fill, default");
			
			if(prop.getProperty("comTest") != null) {
				int c = 0;
				for(String t : ports) {
					if(t.equals(prop.getProperty("comTest"))) {
						comboBox.setSelectedIndex(c);
					}
					c++;
				}
			}	
		}
		{
			JLabel lblData = new JLabel("Data");
			contentPanel.add(lblData, "2, 4");
		}
		{
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			contentPanel.add(scrollPane, "4, 4, fill, fill");
			{
				try { 
                	String sCurrentLine;
                	if( new File("testing.txt").exists() ){
                		BufferedReader br = new BufferedReader(new FileReader("testing.txt"));
        				while ((sCurrentLine = br.readLine()) != null) {
        					textArea.append(sCurrentLine + "\n");
        				} 
        				if(br != null) br.close();
                	}
                } catch (IOException e) {System.out.println(e);}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSave = new JButton("Save");
				btnSave.setAction(save);
				buttonPane.add(btnSave);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.setAction(send);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setAction(action);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private class SwingActionClose extends AbstractAction {
		public SwingActionClose() {
			putValue(NAME, "Close");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
	private class SwingActionSave extends AbstractAction {
		public SwingActionSave() {
			putValue(NAME, "Save");
		}
		public void actionPerformed(ActionEvent e) {
			Writer writer = null;
			try { 		
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("testing.txt"), "utf-8"));
				
				for (String line : textArea.getText().split("\\n")) {
    				writer.write(line + "\n");
    			}			
            } catch (IOException ee) {
            	System.out.println(ee);
            } finally {
				try {writer.close();} catch (Exception ex) {}
			}
			
			if(comboBox.getSelectedItem().toString() != null) {
				prop.setProperty("comTest", comboBox.getSelectedItem().toString());
				
				Prop.saveProp(prop);
			}
		}
	}
	
	private class SwingActionSend extends AbstractAction {
		public SwingActionSend() {
			putValue(NAME, "Send");
		}
		public void actionPerformed(ActionEvent e) {
			String com = null;
			String text = null;
			if(comboBox.getSelectedItem().toString() != null) {
				com = comboBox.getSelectedItem().toString();
			}
			if(textArea.getText() != null) {
				text = textArea.getText();
			}
			if(com != null && text != null) {
				ComPortWrite c = new ComPortWrite();
				c.write(com, text);
				c = null;
			}else{
				JOptionPane.showMessageDialog(null, "Error!!!");
			}
		}
	}
}
