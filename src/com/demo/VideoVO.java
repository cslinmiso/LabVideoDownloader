package com.demo;

public class VideoVO {
	public String url;
	public String title;
	public String context;
	public String fileName;
	public String videoUrl;

	public  VideoVO() {
	}
	public  VideoVO(String[] stringArr) {
		this.setFileName(stringArr[0]);
		this.setTitle(stringArr[1]);
		this.setContext(stringArr[2]);
		this.setUrl(stringArr[3]);
		this.setVideoUrl(stringArr[4]);
	}
	
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
