package app;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;

public class viewer extends JFrame {
	
	public static int app_width = 800;
	public static int app_height = 570;
	
	public JPanel navi_panel;
	public JPanel content_panel;
	public JPanel status_panel;
	public JComboBox mode;
	public JTextField uri;
	public JButton go_btn;
	public JButton refresh_btn;
	public JButton code_btn;
	public JButton pod_btn;
	public JTextArea content;
	public JScrollPane scrollArea;
	public JTextArea code;
	public JScrollPane scrollCode;
	public JLabel mode_status;
	public JLabel view_status;
	public JLabel other_status;
	
	public Reader reader;
	
	public viewer(){
		this.init_ui_face();
		reader = new Reader(0);
		this.setFocusable(true);
		this.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.isControlDown() && event.getKeyCode() == KeyEvent.VK_R){
					click_refresh_btn();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}	
		});
	}
	
	public void init_ui_face(){
		this.setResizable(false);
		/* 输出在屏幕中间 */
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		this.setLocation(screenSize.width/4, screenSize.height/4);
		this.setSize(app_width, app_height);
		this.setTitle("Pod  View");
		
		this.setLayout(new FlowLayout(FlowLayout.LEADING));
		/* layout panel */
		navi_panel = new JPanel();
		this.add(navi_panel);
		content_panel = new JPanel();
		this.add(content_panel);
		status_panel = new JPanel();
		this.add(status_panel);
		
		/* navi_panel */
		mode = new JComboBox(new String[]{
				"Local",
				"Internet"
				}
		);
		mode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(mode.getSelectedItem().equals("Local")){
					other_status.setText("Local");
					uri.setText("");
					reader.set_reader_type(0);
				}else{
					other_status.setText("Internet");
					uri.setText("");
					reader.set_reader_type(1);
				}
			}
		});
		navi_panel.add(mode);
		uri = new JTextField();
		uri.setColumns(26);
		uri.setText("Input The URI");
		uri.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent event) {
				// TODO Auto-generated method stub
				int keyCode = event.getKeyCode();
				if(keyCode == KeyEvent.VK_ENTER){
					go_btn.doClick();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		uri.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent event) {
				// TODO Auto-generated method stub
				if(event.getClickCount()>=2){
					if(mode.getSelectedItem().equals("Local")){
						/* 打开 file dialog */
						JFileChooser chooser = new JFileChooser();
						chooser.setCurrentDirectory(new File("."));
						chooser.setMultiSelectionEnabled(false);
						chooser.showDialog(uri, "Select");
						uri.setText(chooser.getSelectedFile().getPath());
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub			
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		navi_panel.add(uri);
		go_btn = new JButton("Go!");
		go_btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				click_go_btn();
			}
		});
		navi_panel.add(go_btn);
		refresh_btn = new JButton("Refresh");
		refresh_btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				click_refresh_btn();
			}
		});
		navi_panel.add(refresh_btn);
		code_btn = new JButton("Source Code");
		code_btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				click_code_btn();
			}
		});
		navi_panel.add(code_btn);
		pod_btn = new JButton("Pod View");
		pod_btn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				click_pod_btn();
			}
		});
		navi_panel.add(pod_btn);
		
		/* content_panel */
		content = new JTextArea(30, 70);
		content.setTabSize(2);
		content.setEditable(false);
		content.setAutoscrolls(true);
		content.setLineWrap(true);
		scrollArea = new JScrollPane(content);
		scrollArea.setVisible(true);
		code = new JTextArea(30, 70);
		code.setTabSize(2);
		code.setEditable(false);
		code.setAutoscrolls(true);
		code.setLineWrap(true);
		scrollCode = new JScrollPane(code);
		code.setFocusable(true);
		code.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent event) {
				// TODO Auto-generated method stub
				if(event.isControlDown() && event.getKeyCode() == KeyEvent.VK_R){
					click_refresh_btn();
				}else if(event.isControlDown() && event.getKeyCode() == KeyEvent.VK_E){
					hacker_edit();
				}else if(event.isControlDown() && event.getKeyCode() == KeyEvent.VK_S){
					hacker_save();
				}else if(event.getKeyCode() == KeyEvent.VK_ESCAPE){
					hacker_normal();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}	
		});
		scrollArea.setVisible(false);
		
		content_panel.add(scrollArea);
		content_panel.add(scrollCode);
		
		/* status_panel */
		/*    为 status_panel 添加 border */
		//Border etched = BorderFactory.createEtchedBorder();
		//status_panel.setBorder(etched);
		mode_status = new JLabel("Normal");
		status_panel.add(mode_status);
		add_blanks(1);
		view_status = new JLabel("Code View");
		status_panel.add(view_status);
		add_blanks(1);
		other_status = new JLabel("Local");
		status_panel.add(other_status);
		add_blanks(4);
	}
	
	public void add_blanks(int count){
		for(int index = 0; index<count; index++){
			for(int i=0;i<3;i++){
				JLabel s = new JLabel("     ");
				status_panel.add(s);
			}
		}
	}
	
	/* 各种 event listener 事件 */
	public void click_go_btn(){
		content.setText("");
		code.setText("");
		/* 调用 code_btn 或 pod_btn click 事件 */
		//TODO:
		click_code_btn();
		code.setCaretPosition(0);
	}
	public void click_refresh_btn(){
		/* 先判断当前哪个 text_area 有效 */
		/*
		 *  type: 0 content area
		 *        1 code area       
		  *  
		 */
		int type = scrollArea.isVisible()?0:1;
		JTextArea target;
		String url = null;
		if(type == 0){
			target = content;
			/* podchecker */
			/* pod2text */
			/*   修改 url */
		}else{
			target = code;
			url = uri.getText();
		}
		ArrayList<String> ret = reader.read(url);
		content.setText("");
		code.setText("");
		for(String str: ret){
			target.append(str);
		}
		target.setCaretPosition(0);
	}
	public void click_code_btn(){
		scrollArea.setVisible(false);
		scrollCode.setVisible(true);
		if(code.getText().isEmpty()){
			/* 读取 */
			//TODO:
			if(uri.getText().equals("") || uri.getText().equals("Input The URI")){
				;
			}else{
				ArrayList<String> ret = reader.read(uri.getText());
				content.setText("");
				code.setText("");
				for(String str: ret){
					code.append(str);
				}
			}
		}
		view_status.setText("Code View");
		content_panel.repaint();
	}
	public void click_pod_btn(){
		scrollCode.setVisible(false);
		scrollArea.setVisible(true);
		if(content.getText().isEmpty()){
			/* 读取 */
			//TODO:
			//JOptionPane.showMessageDialog(null, "File Not Found!");
		}
		view_status.setText("Pod  View");
		content_panel.repaint();
	}

	/* hacker mode */
	public void hacker_normal(){
		mode_status.setText("Normal");
		code.setEditable(false);
	}
	public void hacker_edit(){
		mode_status.setText("Hacker Edit Mode!");
		code.setEditable(true);
	}
	public void hacker_save(){
		mode_status.setText("Hacker Save Mode!");;
	}
}
