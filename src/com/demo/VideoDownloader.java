package com.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import com.demo.utility.VideoUtility;

public class VideoDownloader {

	/**
	 * Saves the content of the {@code url} to a file with the name
	 * {@code filename}
	 * 
	 * @param filename
	 * @param url
	 */
	public void saveVideo(String filename, String url) {
		String tempDir = System.getProperty("java.io.tempdir");
		String separator = File.pathSeparator;
		String fileFormat = ".mp4";
		final String filePath = tempDir + separator + filename + fileFormat;
		final long filesize;
		RemoteFile rf = getRemoteFile(url);
		long remoteSize = rf.size;
		final String realUrl = rf.realUrl;
		// 得到redirect後實際的URL，若不這樣取得，測試時下載youku以及talkshowCN時會有錯誤
		// System.out.println("實際URL"+realUrl);
		// System.out.println("暫存檔案大小" + remoteSize);
		File f = new File(filePath);
		if (f.exists()) {
			filesize = f.length();
		} else {
			filesize = 0;
		}
		System.out.println("現有檔案大小為：" + filesize);
		if (filesize < remoteSize) {
			new Thread() {
				public void run() {
					VideoUtility.writeFile(realUrl, filePath, filesize);
				}
			}.start();
		}else{
			System.out.println("同樣的檔案已經存在。");
		}
	}

	/**
	 * 
	 * 取得遠端檔案方法
	 * @param url
	 * @return RemoteFile
	 */
	public static RemoteFile getRemoteFile(String url) {
		long size = 0;
		String realUrl = "";
		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(url))
					.openConnection();
			size = conn.getContentLength();
			// 檔案大小
			realUrl = conn.getURL().toString();
			// 實際URL
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//建立暫存檔案並回傳
		RemoteFile rf = new RemoteFile(size, realUrl);
		return rf;
	}
}

class RemoteFile {
	// RemoteFile 儲存取回暫存檔案，存放檔案大小以及實際URL
	long size;
	String realUrl;

	RemoteFile(long size, String realUrl) {
		this.size = size;
		this.realUrl = realUrl;
	}

}
