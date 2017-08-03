package cn.vitem.webmagic.common.httpclient;

import cn.vitem.webmagic.common.Constant;
import cn.vitem.webmagic.common.utils.FileTools;
import org.apache.http.cookie.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Set;

/**
 * @author Created by vitem on 2016/11/19.
 */
public class HttpClientUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    public static void main(String[] a) throws IOException {
        String cookie = "_gscu_2029180466=0107325133iq4897; _gscu_1718069323=01073250btzume50; WEB=20111116; _gscu_884396235=01073604djxfe718; _gscbrs_884396235=1; Hm_lvt_06635991e58cd892f536626ef17b3348=1501073605,1501770262; Hm_lpvt_06635991e58cd892f536626ef17b3348=1501770271; _gscbrs_7281245=1; _gscu_7281245=01073605b9rpzf18; JSESSIONID=6E06D5E6E6A63F516A2EAC521639E83D";
        //getSIPOcheckCode(cookieSet);
    }

    public static void getSIPOcheckCode(Set<Cookie> cookieSet) throws IOException {
        HttpUtils httpUtils = HttpUtils.get(Constant.SIPO_CHECK_CODE_URL);
        httpUtils.setContentEncoding("UTF-8");
        Cookie[] cookies = new Cookie[cookieSet.size()];

        httpUtils.addCookie(cookies);
        //httpUtils.setContentType(ContentType.APPLICATION_JSON);
        //httpUtils.setParameters(errMap);
        ResponseWrap response = httpUtils.execute();
        InputStream inputStream = response.getInputStream();
        File file = new File(Constant.SIPO_CHECK_CODE_PATH);
        FileTools.createParentDir(file);
        OutputStream  outputStream = new FileOutputStream(file);
        byte[] b = new byte[1024];
        while((inputStream.read(b)) != -1){
            outputStream.write(b);
        }
        inputStream.close();
        outputStream.close();

        return ;
    }
   /* public InputStream getApi(String token,ContentType contentType){

        HttpUtils httpUtils = HttpUtils.get(concatUrl(Constant.baseApiUrl,url));
        httpUtils.addHeader("token",token);
        httpUtils.setContentEncoding("UTF-8");
        httpUtils.setContentType(contentType);
        ResponseWrap response = httpUtils.execute();
        return response.getInputStream();
    }

    public InputStream getApi(String token,ContentType contentType,String contentEncoding){

        HttpUtils httpUtils = HttpUtils.get(concatUrl(Constant.baseApiUrl,url));
        httpUtils.addHeader("token",token);
        httpUtils.setContentEncoding(contentEncoding);
        httpUtils.setContentType(contentType);
        ResponseWrap response = httpUtils.execute();

        return response.getInputStream();
    }*/





}
