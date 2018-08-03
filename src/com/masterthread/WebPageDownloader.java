package com.masterthread;

public class WebPageDownloader {
	public static void main(String[] args) {
		MasterThread master = new MasterThread(2);
		Thread masterThread = new Thread(master);
		masterThread.start();

	}
}
