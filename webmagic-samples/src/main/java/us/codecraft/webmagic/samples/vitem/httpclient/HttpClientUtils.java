package us.codecraft.webmagic.samples.vitem.httpclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vitem on 2016/11/19.
 */
public class HttpClientUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    private String url ;
    private String token;


    public HttpClientUtils (String url,String token){
        this.url = url;
        this.token = token;
    }



    /*public String getApi(String token){

        HttpUtils httpUtils = HttpUtils.get(concatUrl(Constant.baseApiUrl,url));
        httpUtils.addHeader("token",token);
        httpUtils.setContentEncoding("UTF-8");
        httpUtils.setContentType(ContentType.APPLICATION_JSON);
        ResponseWrap response = httpUtils.execute();
        int code = response.getStatusCode();
        String massage = null;
        boolean success = false;

        if(code<1000 && code/100 == 4){
            massage = "错误的请求";

        }else if(code<1000 && code/100 == 5){
            massage = "服务器内部错误";
        }else{
            jsonObject = response.getString("")getJson(JSONObject.class);
            massage = jsonObject.getString("massage");
            success =jsonObject.getBoolean("success");
            code = jsonObject.getInteger("code");
            if(success){
                jsonResult.setData(jsonObject.getJSONObject("data"));
            }else{
                jsonResult.setData("");
            }
        }

        jsonResult.setCode(code+"");
        jsonResult.setMessage(massage);
        jsonResult.setSuccess(success);


        return jsonResult;
    }
*/

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
