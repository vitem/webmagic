package cn.vitem.webmagic.trademark.entity;

import us.codecraft.webmagic.Request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vitem on 2017/7/22.
 * <p>
 */
public class TradeMarkImage {

    private String id;
    private String url;
    private String filePath;
    private String anNum;
    private String anType;
    private String requestBody;
    private String postUrl;
    private String Referer;
    private String Host;
    private String pageNum;
    private String fileName;
    private String anTypeName;




    public TradeMarkImage(String url,String filePath,String anNum,String anType,
                          String requestBody,String postUrl,String Referer,String Host,String pageNum,String fileName){

        this.url = url;
        this.filePath = filePath;
        this.anNum = anNum;
        this.anType = anType;
        this.requestBody = requestBody;
        this.postUrl = postUrl;
        this.Referer = Referer;
        this.Host = Host;
        this.pageNum = pageNum;
        this.fileName = fileName;

    }

    public TradeMarkImage(){

    }

    public TradeMarkImage(Request request,String filePath,String anNum,String anType,String pageNum,String fileName) {
        this.url = request.getUrl();
        this.filePath = filePath;
        this.anNum = anNum;
        this.anType = anType;
        byte[] bytes =  request.getRequestBody().getBody();
        try {
            String requestBody = new String(bytes, "UTF-8");
            this.requestBody = requestBody;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.postUrl = request.getUrl();
        this.Referer = request.getHeaders().get("Referer");
        this.Host = request.getHeaders().get("Host");
        this.pageNum = pageNum;
        this.fileName = fileName;
    }

    public static List<TradeMarkImage> createTradeMark(Request request, List<String> filePaths, String anNum, String anType, String pageNum,String fileName) {
        List<TradeMarkImage> list = new ArrayList<TradeMarkImage>(filePaths.size());
        for (int i = 0; i < filePaths.size(); i++) {
            list.add(new TradeMarkImage(request,filePaths.get(i),anNum,anType,pageNum,fileName));
        }
        return list;
    }




    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAnNum() {
        return anNum;
    }

    public void setAnNum(String anNum) {
        this.anNum = anNum;
    }

    public String getAnType() {
        return anType;
    }

    public void setAnType(String anType) {
        this.anType = anType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getReferer() {
        return Referer;
    }

    public void setReferer(String referer) {
        Referer = referer;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAnTypeName() {
        return anTypeName;
    }

    public void setAnTypeName(String anTypeName) {
        this.anTypeName = anTypeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
