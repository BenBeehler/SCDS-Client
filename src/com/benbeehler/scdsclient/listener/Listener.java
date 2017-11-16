package com.benbeehler.scdsclient.listener;

import com.benbeehler.scdsclient.listener.instance.Event;

public interface Listener {

	public void callEvent(Event event);
	
}
