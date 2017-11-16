package com.benbeehler.scdsclient;

public class CustomThread extends Thread {

	private AsyncReturnable runnable;
	
	public CustomThread(AsyncReturnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public void run() {
		runnable.run();
	}
	
	public String get() {
		return runnable.get();
	}
	
	public AsyncReturnable getRunnable() {
		return runnable;
	}
}
