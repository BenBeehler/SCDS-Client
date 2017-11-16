package com.benbeehler.scdsclient.listener.instance;

import com.benbeehler.scdsclient.SCDSClient;
import com.benbeehler.scdsclient.listener.ClientDataSendEvent;
import com.benbeehler.scdsclient.listener.Listener;

public class DataSent extends ClientDataSendEvent implements Listener {

	@Override
	public void callEvent(Event event) {
		//PULL(ID)#VAL#SESSION
		String d = SCDSClient.getDelimeter();
		
		String data = event.getData();
		//ServerSocket server = event.getServer();
		
		System.out.println("Recieved -> " + data);
		
		String[] split = data.split(d);
		
		if(split[0].equals("ERR")) {
			if(split.length == 2) {
				System.out.println("Error -> " + split[1]);
			}
		} else if(split[0].equals("INFO")) {
			if(split.length == 2) {
				System.out.println("Info -> " + split[1]);
			}
		} else if(split[0].equals("PULLSTRING")) {
			if(split.length == 3) {
				String value = split[1];
				int session = Integer.parseInt(split[2]);
				
				SCDSClient.sessionMap.put(session, value);
			} else {
				System.out.println("Error -> Invalid Parameters");
			}
		} else if(split[0].equals("PULLINT")) {
			if(split.length == 3) {
				String value = split[1];
				int session = Integer.parseInt(split[2]);
				
				SCDSClient.sessionMap.put(session, value);
			} else {
				System.out.println("Error -> Invalid Parameters");
			}
		} else if(split[0].equals("PULLDOUBLE")) {
			if(split.length == 3) {
				String value = split[1];
				int session = Integer.parseInt(split[2]);
				
				SCDSClient.sessionMap.put(session, value);
			} else {
				System.out.println("Error -> Invalid Parameters");
			}
		} else if(split[0].equals("PULLFLOAT")) {
			if(split.length == 3) {
				String value = split[1];
				int session = Integer.parseInt(split[2]);
				
				SCDSClient.sessionMap.put(session, value);
			} else {
				System.out.println("Error -> Invalid Parameters");
			}
		} else if(split[0].equals("PULLLIST")) {
			if(split.length == 3) {
				String value = split[1];
				int session = Integer.parseInt(split[2]);
				
				SCDSClient.sessionMap.put(session, value);
			} else {
				System.out.println("Error -> Invalid Parameters");
			}
		} else if(split[0].equals("PULLBOOLEAN")) {
			if(split.length == 3) {
				String value = split[1];
				int session = Integer.parseInt(split[2]);
				
				SCDSClient.sessionMap.put(session, value);
			} else {
				System.out.println("Error -> Invalid Parameters");
			}
		}
	}

}
