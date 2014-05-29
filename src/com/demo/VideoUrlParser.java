package com.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.demo.utility.VideoUtility;

public class VideoUrlParser {

	private static final String FLV_RETRIEVER_URL = "http://kej.tw/flvretriever/youtube.php?videoUrl=";

	public static void main(String[] args) {
		String urlString = "https://www.youtube.com/watch?v=Er8lv1j-GZs&feature=youtu.be";
	}

	public static Document getDocument(String urlString) {
		Document doc = new Document("temp");
		try {
			doc = Jsoup.connect(urlString).data("query", "") // 請求参數
					.ignoreContentType(true).userAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.1.4322)") // 設置User-Agent
					.timeout(10000) // 設置連接超時時間
					.post();
		} catch (Exception ex) {
			System.out.println("連線失敗，錯誤訊息如下：" + ex.getMessage());
		}
		return doc;
	}

	/**
	 * Converts the url to the downloadable url
	 * 
	 * @param url
	 * @return downloadable url or null
	 */
	public String parse(String url) {
		// 開啟連線
		Document doc = getDocument(FLV_RETRIEVER_URL + url);
		// 取得影片連結資訊
		// 資訊位於 div#resultarea > a 中
		String videoInfo = doc.select("#resultarea").select("a").outerHtml();
		// 尋找href="開頭 ">下載此檔案 結尾的內容
		String pattern = "href=\"([^\">]+)\">下載此檔案";
		// 取得轉換前videoInfo連結
		videoInfo = VideoUtility.regexFilter(videoInfo, pattern);
		// 取得轉換後的videoInfo連結
		videoInfo = getDocument(videoInfo).text();
		// 取得影片標題
		String title = null;
		pattern = "title=(.*?)&"; // String titleStr = "title=";
		title = VideoUtility.regexFilter(videoInfo, pattern);
		// 取得url_encoded_fmt_stream_map的內容
		pattern = "url_encoded_fmt_stream_map=(.*)";
		String streamMap =  VideoUtility.regexFilter(videoInfo, pattern);	
		if(streamMap==null){
			System.out.println("網址解析失敗，請重新執行。");
			return null;
		}
		// url解碼後的資訊
		String decodedInfo = VideoUtility.unescapeString(streamMap);
		// 建立videoInfor 陣列
		String[] videoInfoArr = decodedInfo.split(",");
		String targetVideoInfo = VideoUtility.unescapeString(videoInfoArr[0]);
		// pattern = "url=(.*?)?";
		// 取得實際下載url
		String urlString = VideoUtility.regexFilter(targetVideoInfo, "url=(.*?)(?!\\S)");
		if (urlString != null) {
			// 取得itag 以供組合url
			String itag = VideoUtility.regexFilter(urlString, "(itag=.*?)&");
			urlString = urlString.replaceAll("itag=\\d+", "")
					.concat("&" + itag);
			String signature = VideoUtility.regexFilter(urlString, "signature=(.*?)&");
			// 組合字串並加入必要參數
			urlString = urlString + "&signature=" + signature + "&title="+ title;
		}
		return urlString;
	}

}
