package com.benbeehler.scdsclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.benbeehler.scdsclient.listener.ClientDataSendEvent;
import com.benbeehler.scdsclient.listener.ListenerManager;
import com.benbeehler.scdsclient.listener.instance.DataSent;
import com.benbeehler.scdsclient.listener.instance.Event;

public class SCDSClient {

	public static Connection CONNECTION;
	public static HashMap<Integer, Object> sessionMap = new HashMap<>();
	private static String delimeter = "#";
	public static boolean RUNNING = true;
	
	public static void init(String address, int port) throws IOException, InterruptedException, ExecutionException {
		ListenerManager.addListener(new DataSent());
		
		CONNECTION = new Connection();
		CONNECTION.connect(address, port);
		asynchronousListener();
	}
	
	public static Connection getConnection() {
		return CONNECTION;
	}
	
	public static void asynchronousListener() {
		new Thread(() -> {
			while(RUNNING) {
				try {
					if(CONNECTION != null) {
						if(CONNECTION.getConnection().isConnected()) {
							if(CONNECTION.getInput().available() > 0) {
								String UTF = CONNECTION.getInput().readUTF();
								
								ListenerManager.LISTENERS.stream().forEach(listener -> {
									if(listener instanceof ClientDataSendEvent) {
										Event event = new Event();
										event.setData(UTF);
										
										new Thread(() -> {
											listener.callEvent(event);
										}).start();
									}
								});
							} else {
							}
						} else {
						}
					} else {
					}
				} catch (IOException e) {
				}
			}
		}).start();
	}
	
	public static void stop() {
		RUNNING = false;
	}
	
	public static void sendProtocol(Protocol protocol, String[] data) throws IOException {
		StringBuilder builder = new StringBuilder();
		for(String string : data) {
			builder.append(getDelimeter() + string);
		}
		
		getConnection().getOutput().writeUTF(protocol.toString() + builder.toString());
	}
	
	public static void reConnect(String address, int port) throws IOException {
		getConnection().connect(address, port);
	}
	
	public static Object pullValue(String path, String password, Protocol protocol) throws IOException {
		int sessionID = new Random().nextInt(Integer.MAX_VALUE);
		
		String[] data = new String[] {
				path,
				String.valueOf(sessionID),
				password
		};
		
		sendProtocol(protocol, data);
		sessionMap.put(sessionID, "");
		
		CustomThread thread = new CustomThread(new AsyncReturnable() {
			
			boolean set = false;
			
			@Override
			public void run() {
				while(sessionMap.get(sessionID).equals("")) {
				}
				
				set = true;
			}
			
			@Override
			public String get() {
				if(set == true) {
					return sessionMap.get(sessionID).toString();
				} else {
					return get();
				}
			}
			
		});
		
		thread.run();
		
		return sessionMap.get(sessionID);
	}
	
	public static String pullString(String path, String password) throws IOException, InterruptedException, ExecutionException {
		return pullValue(path, password, Protocol.PULLSTRING).toString();
	}
	
	public static Integer pullInteger(String path, String password) throws IOException {
		return Integer.parseInt(pullValue(path, password, Protocol.PULLINT).toString());
	}
	
	public static Float pullFloat(String path, String password) throws IOException {
		return Float.parseFloat(pullValue(path, password, Protocol.PULLFLOAT).toString());
	}
	
	public static double pullDouble(String path, String password) throws IOException {
		return Double.parseDouble(pullValue(path, password, Protocol.PULLDOUBLE).toString());
	}
	
	public static List<String> pullStringList(String path, String password) throws IOException {
		return getStringList(pullValue(path, password, Protocol.PULLLIST).toString());
	}
	
	public static HashMap<String, String> pullMap(String path, String password) throws IOException {
		return parseMap(pullValue(path, password, Protocol.PULLMAP).toString());
	}
	
	public static boolean pullBoolean(String path, String password) throws IOException {
		String bool = pullValue(path, password, Protocol.PULLBOOLEAN).toString();
		
		if(bool.trim().toLowerCase().equals("true")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void update(String path, String value, String password) throws IOException {
		String[] data = new String[] {
				path,
				value,
				password
		};
		
		sendProtocol(Protocol.UPDATE, data);
	}

	public static String getDelimeter() {
		return delimeter;
	}

	public static void setDelimeter(String delimeter) {
		SCDSClient.delimeter = delimeter;
	}
	
	private static List<String> getStringList(String stringValue) {
		if(stringValue.startsWith("[") && stringValue.endsWith("]")) {
			String[] charArray = stringValue.split("");
			
			charArray[0] = "";
			charArray[charArray.length - 1] = "";
			
			StringBuilder sb = new StringBuilder();
			
			for(String ch : charArray) {
				sb.append(ch);
			}
			
			List<String> value = new ArrayList<>();
			
			for(String entry : sb.toString().split(",")) {
				value.add(entry);
			}
			
			return value;
		} else {
			List<String> empty = new ArrayList<>();
			return empty;
		}
	}
	
	private static HashMap<String, String> parseMap(String string) {
		if(isMap(string)) {
			HashMap<String, String> value = new HashMap<>();
			String[] array=string.split("");
			array[0]="";
			array[array.length-1]="";
			StringBuilder builder=new StringBuilder();
			for(String str:array) {
				builder.append(str);
			}
			string=builder.toString();
			array=string.split(",");
			for(String str:array) {
				if(str.contains("=")) {
					String split[]=str.split("=");
					if(split.length>1) {
						StringBuilder sb=new StringBuilder();
						for(int i=1; i<split.length;i++) {
							sb.append(split[i]);
						}
						
						value.put(split[0].trim(), sb.toString());
					}
				}
			}
			return value;
		} else {
			return new HashMap<String, String>();
		}
	}
	
	private static boolean isMap(String string) {
		string=string.trim();
		
		if(string.startsWith("{") && string.endsWith("}")) {
			return true;
		}
		
		return false;
	}
}