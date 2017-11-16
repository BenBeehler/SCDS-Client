package com.benbeehler.scdsclient.listener.instance;

import java.net.ServerSocket;

public class Event {

	private ServerSocket server;
	private String data;

	public Event() {
	}
	
	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}
