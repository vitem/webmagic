package cn.vitem.webmagic.ocr;

import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by vitem_acer on 2017/7/31.
 */
public class BaiduOCR {

    //vitem_wjq --mac
    static String APPID = "9952879";
    static String APIKEY = "4AmHvVS4jLYqAaguAGXXN044";
    static String SECRETKEY = "EYE7D1UA1pq2Y4OymueIS6c3eTCfLGbq";

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static AipOcr getBaiduOcrClient(){
        AipOcr aipOcr = new AipOcr(APPID,APIKEY,SECRETKEY);
        return aipOcr;
    }

    public static void baiduAPI(){
        AipOcr client = BaiduOCR.getBaiduOcrClient();
        String genFilePath = "D:/data/trademark_cut/1561/TMZCSQ/TMCXSQ5520E94F0EBCC098E053640B5030C098.png";
        //String genFilePath = "D:/data/tm_test/tm_test/imgCut/dest/TMSDGG-5520E94F0FA4C098E053640B5030C098-cut.png";
        JSONObject genRes = client.basicGeneral(genFilePath, new HashMap<String, String>());
        System.out.println(genRes.toString(2));

    }
}
