package com.benbeehler.scdsclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

	private Socket connection;
	private DataInputStream input;
	private DataOutputStream output;
	
	public Connection() {
		
	}
	
	public void connect(String address, int port) throws IOException {
		connection = new Socket(address, port);
		input = new DataInputStream(connection.getInputStream());
		output = new DataOutputStream(connection.getOutputStream());
		System.out.println("Connected to " + address);
	}

	public Socket getConnection() {
		return connection;
	}

	public void setConnection(Socket connection) {
		this.connection = connection;
	}

	public DataOutputStream getOutput() {
		return output;
	}

	public DataInputStream getInput() {
		return input;
	}
}
