package cn.vitem.webmagic.trademark;

import cn.vitem.webmagic.common.db.DBHelper;
import cn.vitem.webmagic.common.httpclient.HttpUtils;
import cn.vitem.webmagic.common.httpclient.ResponseWrap;
import cn.vitem.webmagic.common.redis.CacheUtils;
import cn.vitem.webmagic.common.utils.DownloadImage;
import cn.vitem.webmagic.trademark.entity.TradeMarkImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

/**
 * @author code4crafter@gmail.com <br>
 *         Date: 13-5-20
 *         Time: 下午5:31
 */
public class TMProcessor implements PageProcessor {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected String site;

    protected String prefix = "http://sbcx.saic.gov.cn:9080/tmois/";

    protected String imgPath = "/Users/vitem/data/trademark";

    @Override
    public void process(Page page) {

        processImg(page);

        String anNum = PageUtils.getAnNum(page);
        String sum = PageUtils.getSum(page);
        String pageNum = PageUtils.getPageNum(page);
        Integer countPage =PageUtils.getCountPage(page);
        String anType = PageUtils.getAnType(page);

        if("-1".equalsIgnoreCase(anType)){
            List<Selectable> anTypeSelectables = page.getHtml().xpath("//input[@type=\"radio\"]").nodes();

            //处理 anType公告类型 和 pageNum=1 的情况
            for(Selectable anTypeSelectable: anTypeSelectables){
                String type = anTypeSelectable.css("input","value").get();
                if(!"-1".equals(type) && !"GGML".equalsIgnoreCase(type)){
                        Request request = RequestBuilder.build(anNum,type,pageNum,sum,String.valueOf(countPage));
                        page.addTargetRequest(request);
                }
            }
        }else{
            if("1".equalsIgnoreCase(pageNum)){
                //页码从第二开始  因为上面已经处理1的情况
                for (int i = 2; i <= countPage ; i++) {
                    pageNum = String.valueOf(i);
                    String cacheKey = PageUtils.getPageKey(page,pageNum);
                    if(!CacheUtils.containsKey(cacheKey)){
                        Request request = RequestBuilder.build(anNum,anType,pageNum,sum,String.valueOf(countPage));
                        page.addTargetRequest(request);
                    }

                }
            }
        }
    }

    private void processImg( Page page) {

        String anType = PageUtils.getAnType(page);
        String anNum = PageUtils.getAnNum(page);
        String pageNum = PageUtils.getPageNum(page);
        Integer countPage =PageUtils.getCountPage(page);
        if("-1".equalsIgnoreCase(anType) || "GGML".equalsIgnoreCase(anType)){
            return ;
        }

        Selectable importPhotoSelectables = page.getHtml().xpath("//table[@class=\"import_photo\"]");
        List<Selectable> imgSelectables = importPhotoSelectables.xpath("//img").nodes();
        for (int i = 0; i < imgSelectables.size(); i++) {
            String imgSrc = imgSelectables.get(i).css("img","src").get();
            String imgUrl = prefix + imgSrc;
            String imgId = imgSrc.substring(imgSrc.indexOf("id=")+3,imgSrc.length());
            String fileName = imgId + ".png";
            String fullPath =imgPath+"/"+anNum+"/"+anType+"/"+fileName;
            File file = new File(fullPath);
            if(file.exists()){
                return ;
            }
            HttpUtils httpUtils = HttpUtils.get(imgUrl);
            httpUtils.addHeader("Host", "sbcx.saic.gov.cn:9080");
            httpUtils.addHeader("Referer", "http://sbcx.saic.gov.cn:9080/tmois/wsggcx_getGgaoMainFirst.xhtml?bid=54A29078477E5020E053640B503A5020&anNum=1560&anType=TMZCZX");
            httpUtils.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
           ResponseWrap response = httpUtils.execute();
            InputStream inputStream = response.getInputStream();
            try {
                DownloadImage.download(inputStream, fullPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TradeMarkImage tradeMark = new TradeMarkImage(page.getRequest(),fullPath, anNum, anType, pageNum,fileName);
            logger.info("operate ----> anType="+anType+" , pageNum= "+pageNum+" / "+countPage +", index =" + i);
            DBHelper.insert(tradeMark);
        }

        String cacheKey = PageUtils.getPageKey(page);
        CacheUtils.put(cacheKey,"1");


        return ;
    }

    @Override
    public Site getSite() {
        return Site.me()
                .setDomain("sbcx.saic.gov.cn")
                .setCharset("utf-8").
                        setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
    }

    public static void main(String[] args) {

        String anNum = "1561";
        String anType = "-1";
        String pageNum = "1";
        String sum = "17859";
        String countPage = "1191";

        Request request = RequestBuilder.build(anNum,anType,pageNum,sum,countPage);
        SpiderPost spiderPost = SpiderPost.create(new TMProcessor());
        String uuid = UUID.randomUUID().toString();
        spiderPost.addRequest(request).setUUID(uuid).run();

    }
}
