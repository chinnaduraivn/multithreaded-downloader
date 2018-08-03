package com.masterthread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;

import com.slavethread.SlaveThread;

public class MasterThread implements Runnable {

	private static int numberOfUrl = 0;

	public MasterThread(int numberOfUrls) {
		numberOfUrl = numberOfUrls;
	}

	@Override
	public void run() {

		if (numberOfUrl < 1)
			throw new IllegalArgumentException("Number of urls cant be null");

		try {
			CyclicBarrier barrier = new CyclicBarrier(numberOfUrl + 1);
			Map<String, Long> sharedMap = new ConcurrentHashMap<>();

			List<SlaveThreadData> slaves = startSlaveThread(barrier, sharedMap);

			barrier.await();

			printSlaveResult(slaves, sharedMap);

		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}

	}

	private List<SlaveThreadData> startSlaveThread(CyclicBarrier barrier, Map<String, Long> sharedMap) {

		List<SlaveThreadData> slaveThreadDatas = new ArrayList<>();
		List<String> urls = getUrls();
		String name = "Thread ";
		int count = 1;

		for (String url : urls) {
			String localName = name + " " + count++;

			SlaveThread slaveThread = new SlaveThread(barrier, sharedMap, url);
			Thread slave = new Thread(slaveThread, localName);

			SlaveThreadData slaveThreadData = new SlaveThreadData(localName, url);
			slaveThreadDatas.add(slaveThreadData);
			slave.start();
		}

		return slaveThreadDatas;
	}

	private List<String> getUrls() {

		List<String> urls = new ArrayList<>();

		Scanner scanner = new Scanner(System.in);
		for (int i = 0; i < numberOfUrl; i++) {
			System.out.println("Enter the URL");
			String urlString = scanner.next();
			urls.add(urlString);
		}

		scanner.close();

		return urls;
	}

	private void printSlaveResult(List<SlaveThreadData> slaves, Map<String, Long> sharedMap) {
		if (isNullOrEmpty(slaves))
			return;
		for (SlaveThreadData slave : slaves) {
			System.out.println(slave.getName() + ": " + slave.getUrl() + " " + sharedMap.get(slave.getName()));
		}

	}

	@SuppressWarnings("rawtypes")
	private boolean isNullOrEmpty(List list) {
		return list == null || list.size() == 0;
	}

	private class SlaveThreadData {

		private String url;
		private String name;

		public SlaveThreadData(String name, String url) {
			super();
			this.name = name;
			this.url = url;

		}

		public String getUrl() {
			return url;
		}

		public String getName() {
			return name;
		}

	}

}
