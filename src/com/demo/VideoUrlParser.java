package com.demo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
					.ignoreContentType(true).userAgent("IE/6.0") // 設置User-Agent
					.cookie("auth", "token") // 設置 cookie
					.timeout(10000) // 設置連接超時時間
					.get();
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
		//開啟連線
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
		pattern = "title=(.*?)&";  		// String titleStr = "title=";
		title = VideoUtility.regexFilter(videoInfo, pattern);
		// 取得url_encoded_fmt_stream_map的內容
		pattern = "url_encoded_fmt_stream_map=(.*?)&";
		videoInfo = VideoUtility.regexFilter(videoInfo, pattern);
		// url解碼後的資訊
		String decodedInfo = VideoUtility.unescapeString(videoInfo);
		// 建立videoInfor 陣列
		String[] videoInfoArr = decodedInfo.split(",");
		String targetVideoInfo = VideoUtility.unescapeString(videoInfoArr[0]);
		pattern = "url=(.*?)(fallback_host=|quality=|type=.+&)";
		// pattern = "url=(.*?)?";
		// 取得實際下載url
		String urlString = VideoUtility.regexFilter(targetVideoInfo, pattern);
		pattern = "signature=(.*?)&";
		String signature = VideoUtility.regexFilter(targetVideoInfo, pattern);
		
		//組合字串並加入必要參數
		urlString = urlString + "&signature=" + signature + "&title=" + title;
		
		return urlString;
	}

	public String parseTalkshowCN(String urlString) throws IOException {
		// 建立連線
		Document doc = getDocument(urlString);
		String dlURL = doc.select(".download a").attr("href");
		doc = getDocument(dlURL);
		String videoURL = doc.select(".link").attr("href");
		if ("".equals(videoURL)) {
			videoURL = null;
		}
		return videoURL;
	}

	public List parseTalkshowCNList(String urlString) throws IOException {

		// 建立連線
		Document doc = getDocument(urlString);

		// 取得文章總篇數
		Elements articleCountString = doc.select(".endPage");
		Integer articleCount = 0;
		// 宣告變數本文總頁數
		Elements articlePagesSource;
		Integer articlePages = 1;
		String articlePagesLink = urlString.substring(0,
				urlString.lastIndexOf("/") + 1);
		if (articleCountString.size() != 0) {
			try {
				Pattern regex = Pattern.compile("\\d.+");
				Matcher regexMatcher = regex.matcher(articleCountString
						.attr("href"));
				if (regexMatcher.find()) {
					articlePages = Integer.parseInt(regexMatcher.group());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		Elements link;
		List linkList = new ArrayList();
		List<String> listData = new ArrayList<String>();
		// 若有兩頁以上就執行此迴圈

		for (int i = 0; i < articlePages; i++) {
			urlString = articlePagesLink + (i + 1);
			doc = getDocument(urlString);

			// 文章進入title節點的class name
			link = doc.select(".hide_unit_9");

			for (Element e : link) {
				StringBuffer sb = new StringBuffer();
				Elements aTag = e.select("H2 > a");
				String absUrl = aTag.get(0).absUrl("href");
				String videoUrl = parseTalkshowCN(absUrl);
				String fileName = absUrl.substring(absUrl.lastIndexOf("/") + 1);
				String title = aTag.get(0).text();
				String context = e.select(".m-txt").get(0).ownText();
				sb.append(fileName).append(VideoUtility.splitTag);
				sb.append(title).append(VideoUtility.splitTag);
				sb.append(context).append(VideoUtility.splitTag);
				sb.append(absUrl).append(VideoUtility.splitTag);
				sb.append(videoUrl);

				System.out.println(fileName + "--" + absUrl + "----" + title);
				System.out.println(context + "\n" + videoUrl);
				if (absUrl != null) {
					linkList.add(new VideoVO(new String[] { fileName, title,
							context, absUrl, videoUrl }));
					listData.add(sb.toString());
				}
			}
		}

		File file = new File("C:\\Conan\\VideoDetail.txt");
		VideoUtility.writeFile(listData, file);

		// 取得文章連結
		// link = doc.select("div .yom-mod .story .txt a").not(".more");
		// --1.在你解析文档时确保有指定base URI，然后
		// String relHref = link.attr("href"); // == "/"
		// --2.使用 abs: 属性前缀来取得包含base URI的绝对路径。
		// String absHref = link.attr("abs:href"); //
		// "http://www.open-open.com/"
		//

		if (articleCount == 0) {
			articleCount = linkList.size();
		}
		System.out.println("共 " + articleCount + " 篇文章");

		return linkList;
	}
}
