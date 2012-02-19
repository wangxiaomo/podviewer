package app;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Reader {
	/*
	 * 
	 * 根据软件的模式来决定 pod 的位置来源于 local or internet.
	 * 
	 * 
	 */
	public static int LOCAL_TYPE = 0;
	public static int INTERNET_TYPE = 1;
	
	public int model_type;
	
	public Reader(int type){
		this.model_type = type;
	}
	
	public int get_reader_type(){
		return model_type;
	}
	
	public void set_reader_type(int type){
		this.model_type = type;
	}
	
	public ArrayList<String> read(String uri){
		if(model_type == LOCAL_TYPE){
			return read_from_local(uri);
		}else{
			return read_from_internet(uri);
		}
	}
	
	public ArrayList<String> read_from_local(String uri){
		ArrayList<String> ret = new ArrayList<String>();
		try {
			FileInputStream in = new FileInputStream(uri);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in)
			);
			String line;
			while((line = reader.readLine()) != null){
				ret.add(line+"\n");
			}
			in.close();
		}catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "File Not Found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException!");
		}
		return ret;
	}
	
	public ArrayList<String> read_from_internet(String uri){
		/*
		 * 
		 * 从 internet 读取后缓存到 tmp 文件夹下.
		 *TODO:并加以时间戳防止冲突.
		 * 
		 */
		ArrayList<String> ret = new ArrayList<String>();
		try {
			Socket s = new Socket(uri, 8080);
			InputStream inStream = s.getInputStream();
			Scanner in = new Scanner(inStream);
			/*TODO: 跨平台 文件格式 链接 */
			String output_file = "tmp/temp";
			FileOutputStream outStream = new FileOutputStream(output_file);
			PrintWriter out = new PrintWriter(outStream);
			while(in.hasNextLine()){
				String line = in.nextLine() + "\n";
				ret.add(line);
				out.print(line);
			}
			out.close();
			outStream.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unknown Host Exception!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException!");
		}
		return ret;
	}
}
