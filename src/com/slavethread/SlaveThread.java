package com.slavethread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SlaveThread implements Runnable {

	private CyclicBarrier barrier = null;
	private Map<String, Long> map = null;
	private String urlString;

	public SlaveThread(CyclicBarrier barrier, Map<String, Long> map, String urlString) {
		super();
		this.barrier = barrier;
		this.map = map;
		this.urlString = urlString;
	}

	@Override
	public void run() {

		URL url;
		InputStream is = null;
		BufferedReader br;
		long size = 0;
		String name = Thread.currentThread().getName();
		try {
			System.out.println("Started " + name);
			url = new URL(urlString);
			is = url.openStream();
			size = url.openConnection().getContentLengthLong();
			br = new BufferedReader(new InputStreamReader(is));

			while ((br.readLine()) != null) {
				// System.out.print (line);
			}

			map.put(name, size);

		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				barrier.await();
				if (is != null)
					is.close();
			} catch (IOException | InterruptedException | BrokenBarrierException ioe) {

			}
		}

		System.out.println("Ended  " + Thread.currentThread().getName());

	}

}
