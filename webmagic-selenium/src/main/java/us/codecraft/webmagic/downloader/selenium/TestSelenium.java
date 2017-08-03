package us.codecraft.webmagic.downloader.selenium;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;

import java.util.UUID;

/**
 * Created by vitem on 2017/6/18.
 * <p>
 */
public class TestSelenium implements Task{

    public  static  void  main(String[] a){
        String url = "http://epub.sipo.gov.cn/patentoutline.action";
        Downloader downloader = new SeleniumDownloader();
        Request request = new Request();
        request.setUrl(url);
        Page page = downloader.download(request,new TestSelenium());
        // <input type="text" name="vct" autocomplete="off" autofocus>
        //<input type="submit" name="Submit" value="继续">

        System.out.println();
    }


    @Override
    public String getUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Site getSite() {
        return Site.me()
                .setDomain("epub.sipo.gov.cn")
                .setCharset("utf-8").
                        setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
    }
}
