package com.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.demo.utility.VideoUtility;

public class Main {
    
    static Log logger = LogFactory.getLog(Main.class);

    public static void main(String[] args) {

//        if (args == null || args.length != 2) {
//            logger.warn("It needs two args: filename and video-url");
//            return;
//        }

//        String filename = args[0];
//        String youtubeUrl = args[1];

        VideoUrlParser parser = new VideoUrlParser();
//        String url = parser.parse(youtubeUrl);
        try {
			List list = parser.parseTalkshowCNList("http://www.talkshowcn.com/list/conan/date/1");
//        	List list = VideoUtility.readFile("C:\\Conan\\VideoDetail.txt");
			for (int i = 0; i < list.size(); i++) {
//				VideoVO vo =(VideoVO) list.get(i);
//				String url = vo.getVideoUrl();
//				String fileName= vo.getFileName();
//				String[] VideoArr =  String.valueOf(list.get(i)).split(VideoUtility.splitTag);
//				String url = VideoArr[4];
//				String fileName= VideoArr[0];
		        if (url != null&&!"null".equals(url)) {
		            VideoDownloader downloader = new VideoDownloader();
		            downloader.saveVideo(fileName, url);
		        }
//				System.out.println(""+string);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        if (url != null) {
//            VideoDownloader downloader = new VideoDownloader();
//            downloader.saveVideo(filename, url);
//        }
    }
}
