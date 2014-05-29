BUILD 指令
====

##配置Ant環境

從Ant官網下載所需ant的jar包

這裡以windows平台設定為例：

Windows下ANT用到的環境變量主要有2個，`ANT_HOME` 、`PATH`。

設定ANT_HOME指向ant的安裝目錄。

設定方法：
`ANT_HOME = C:/apache_ant_1.9.1`

將`%ANT_HOME%/bin; %ANT_HOME%/lib`添加到環境變數的path中。

設定方法：
`PATH = %PATH%; %ANT_HOME%/bin; %ANT_HOME%/lib`
   
## 編譯專案

於命令提示字元下，進入專案資料夾內。
`CD C:/VideoDownloader`

接著輸入
`ant`

ant就會根據build.xml內設定的腳本開始編譯。

編譯完成後的jar檔放置於`C:/VideoDownloader/store`內。


## 使用方法

使用者輸入下載指令，第 1 個參數是下載影片存檔的名稱，第 2 個參數是 YouTube 網址。

```
java -jar VideoDownloader.jar [filename] [youtube-url]
```

若網站parse成功則會出現開始下載檔案以及進度百分比之訊息提示。

