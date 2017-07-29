package cn.vitem.webmagic.trademark;

import org.apache.commons.collections.map.HashedMap;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.utils.HttpConstant;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by vitem on 2017/7/22.
 * <p>
 */
public class RequestBuilder {



    public static Request build(String anNum,String anType,String pageNum,String sum,String countPage){

        Map<String, Object> params = new HashedMap();
        params.put("gmBean.anNum", anNum);
        params.put("gmBean.anType",anType);
        params.put("gmBean.regNum", "");
        params.put("gmBean.pageNum", "");
        params.put("pagenum", pageNum);
        params.put("pagesize", "15");
        params.put("sum", sum);
        params.put("countpage", countPage);
        params.put("goNum", "1".equals(pageNum)?"1":(Integer.valueOf(pageNum)-1)+"");

        String encoding = "UTF-8";
        HttpRequestBody requestBody = null;
        try {
            //String requestBodyString = "gmBean.anNum=1560&gmBean.anType=-1&gmBean.regNum=&gmBean.pageNum=&pagenum=2&pagesize=15&sum=17357&countpage=1158&goNum=1";
            //                            sum=17357&pagenum=1&countpage=1158&gmBean.pageNum=null&gmBean.anType=-1&goNum=1&gmBean.anNum=1560&gmBean.regNum=null&pagesize=15
            //requestBody = new HttpRequestBody(requestBodyString.getBytes(encoding),HttpRequestBody.ContentType.FORM,encoding);
            requestBody = HttpRequestBody.form(params, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Request request = new Request();
        request.setMethod(HttpConstant.Method.POST);
        request.setRequestBody(requestBody);
        request.setUrl("http://sbcx.saic.gov.cn:9080/tmois/wsggcx_getGgaoMainlate.xhtml");
        request.addHeader("Host", "sbcx.saic.gov.cn:9080");
        request.addHeader("Referer", "http://sbcx.saic.gov.cn:9080/tmois/wsggcx_getGgaoMainFirst.xhtml?bid=54A29078477E5020E053640B503A5020&anNum=1560&anType=-1");

        return  request;
    }

    public static Request build(Page page) {
        return build(PageUtils.getAnNum(page),PageUtils.getAnType(page),PageUtils.getPageNum(page),PageUtils.getSum(page),String.valueOf(PageUtils.getCountPage(page)));
    }
}
