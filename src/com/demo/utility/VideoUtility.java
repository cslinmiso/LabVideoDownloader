package com.demo.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoUtility {
	//分割的tag
	public static final String splitTag = String.valueOf('\002');
	
	/**
	 * 轉換已編碼後的字串，編碼格式為UTF-8
	 * @param str
	 * @return
	 */
	public static String unescapeString(String str){
		String result=null;
		try {
			result = URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("轉碼錯誤："+e.getMessage());
			result = null;
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Regex 過濾器，找出需要字串
	 * @param target
	 * @param pattern
	 * @return target || null
	 */
	public static String regexFilter(String target, String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher match = p.matcher(target);
        if (match.find()) {
        	return match.group(1);
        }else{
        	return null; //找不到則回傳null
        }
	}
	/**
     * Java IO讀取檔案 (java io read file)
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static List<String> readFile(String fileName) throws IOException {
        List<String> rst = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), "UTF-8")));
            String in = null;
            while ((in = br.readLine()) != null) {
                rst.add(in);
            }
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            br.close();
        }
        return rst;
    }

    public static void writeFile(String realUrl, String path, long fileSize){
    	java.io.RandomAccessFile savedFile =null;
    	try {
			System.out.println("開始下載檔案...");
			URL u = new URL(realUrl);
			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();
//			就目前檔案大小設定欲下載檔案的位置
			connection.setRequestProperty("RANGE", "bytes="+ fileSize+"-");
//			設定UserAgent
			connection.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.1.4322)"); 
//			讀取InputStream
			InputStream input = connection.getInputStream();
//			建立RandomAccessFile
			savedFile = new java.io.RandomAccessFile(path, "rw");
			//移動檔案指針至目前大小
			savedFile.seek(fileSize);
			byte[] b = new byte[1024];
			int nRead;
			//readed用來儲存目前已存的檔案大小，所以初始值為傳入的檔案大小
			long readed=fileSize;
			//從InputStream讀入byte，nRead為實際讀入的byte長度
			while ((nRead = input.read(b, 0, 1024)) > 0) {
				//目前已下載的大小
				readed+=nRead;
//				System.out.println(readed);
				//將讀取的byte寫入檔案
				savedFile.write(b, 0, nRead);
			}
			//斷線
			connection.disconnect();
		} catch (Exception e) {

		}finally{
			if(savedFile!=null){
			//關閉檔案
				try {
					savedFile.close();
				} catch (IOException e) {

				}
			}
		}
    }
    
    public static void writeFile(List<String> data, File f) throws IOException {
        writeFile( data, f.getAbsolutePath());
    }

	 /**
	   *  Java IO寫入檔案  (java io write file)
	   * @param data
	   * @param fileName
	   * @throws IOException
	   */
	  public static void writeFile(List<String> data, String fileName) throws IOException {
	    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(fileName), "UTF-8"));
	    try{
	      for(String d:data){
	        bw.write(d);
	        bw.newLine();        
	      }
	      bw.flush();
	    }catch(IOException ioe){
	      throw ioe;
	    }finally{
	      bw.close();      
	    }    
	  }
	  
	/**
	 * Java IO寫入檔案 (java io write file)
	 * 
	 * @param data
	 * @param fileName
	 * @throws IOException
	 */
	public static void writeFile(InputStream inputData, String filePath)
			throws IOException {
		InputStream input= null;
		FileOutputStream fos =null;
		try{
          fos = new FileOutputStream(filePath);
         int size;
         byte[] buffer = new byte[1024];
         while ((size = inputData.read(buffer)) != -1) {
             fos.write(buffer, 0, size);
         }
         fos.flush();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			if(fos!=null){
	         fos.close();
			}
		}
	}
}
