package cn.vitem.webmagic.ocr;

import com.baidu.aip.ocr.AipOcr;

/**
 * Created by vitem_acer on 2017/7/31.
 */
public class BaiduOCR {

    //vitem_wjq --mac
    static String APPID = "9952879";
    static String APIKEY = "4AmHvVS4jLYqAaguAGXXN044";
    static String SECRETKEY = "EYE7D1UA1pq2Y4OymueIS6c3eTCfLGbq";

    //ab219830* --windows
    static String WIN_APPID = "9952879";
    static String WIN_APIKEY = "4AmHvVS4jLYqAaguAGXXN044";
    static String WIN_SECRETKEY = "EYE7D1UA1pq2Y4OymueIS6c3eTCfLGbq";

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static AipOcr getBaiduOcrClient(){
        AipOcr aipOcr = null;
        if(OS.contains("windows")){
            aipOcr = new AipOcr(WIN_APPID,WIN_APIKEY,WIN_SECRETKEY);
        }else{
            aipOcr = new AipOcr(APPID,APIKEY,SECRETKEY);
        }
        return aipOcr;
    }
}
