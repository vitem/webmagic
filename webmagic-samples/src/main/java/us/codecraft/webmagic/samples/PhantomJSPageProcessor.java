package us.codecraft.webmagic.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.PhantomJSDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dolphineor on 2014-11-21.
 * <p>
 * 以淘宝为例, 搜索冬装的相关结果
 */
public class PhantomJSPageProcessor implements PageProcessor {

    private static final String PHANTOMJS_EXEC_PATH = "/Users/vitem/develop/phantomjs/bin/phantomjs";

    private Site site = Site.me()
            .setDomain("http://www.tianyancha.com")
            .setCharset("UTF-8")
            .addHeader("Referer", "http://www.tianyancha.com/search?key=%E5%A4%A7%E8%BF%9E%E4%B8%87%E8%BE%BE%E9%9B%86%E5%9B%A2")
            .setRetryTimes(3).setSleepTime(1000);

    @Override
    public void process(Page page) {
        List<String> text1 = page.getHtml().css("ul.list-search").all();

        List<Selectable> nodes = page.getHtml().xpath("//ul[@class=\"list-search\"]/li").nodes();

//        nodes.forEach(new Consumer<Html>() { public void accept(final Html node) {
//            System.out.println(node.xpath("p[@clas=\"other\"]/span/text();"));
//        });

        List<Company> companies = new ArrayList<Company>();

//        nodes.parallelStream().forEach((final Selectable node) -> {
//            String state = node.xpath("p[@class=\"tit\"]/span/text();").toString();
//            String name = node.xpath("p[@class=\"tit\"]/a/text();").toString();
//            String time = node.xpath("p[@class=\"other\"]/time/text();").toString();
//
//            String person = null;
//            List<Selectable> ns = node.css("p.other").css("span").nodes();
//            person = ns.get(0).xpath("div/text()").toString();
//            String capital = ns.get(1).xpath("div/text()").toString();
//
//
//            String addr1 = node.xpath("p[@class=\"adr\"]/i/text();").toString();
//            String addr2 = node.xpath("p[@class=\"adr\"]/text();").toString();
//            addr2 = addr2.replace(addr2,"");
//            Company company = new Company();
//
//            companies.add(company);
//        });
        for (Selectable node : nodes) {

            Company company = new Company();

            String state = node.xpath("p[@class=\"tit\"]/span/text()").toString();
            String name = node.xpath("p[@class=\"tit\"]/a/text()").toString();
            String time = node.xpath("p[@class=\"other\"]/time/text()").toString();

            List<Selectable> ns = node.css("p.other").css("span").nodes();
            String person = null;
            if(ns.size()==2){

                person = ns.get(0).xpath("span/text()").toString();
                String capital = ns.get(1).xpath("span/text()").toString();
                company.setCapital(capital);
                company.setPersonName(person);
            }else{
                person = node.css("p.other").css("span").toString();

            }
            company.setPersonName(person);

            //String addr1 = node.xpath("p[@class=\"adr\"]/i").toString();
            String addr2 = node.xpath("p[@class=\"adr\"]/text()").toString();
            //addr2 = addr2.replace(addr2,"");


            company.setAddress(addr2);
            company.setName(name);
            company.setTime(time);
            company.setState(state);
            companies.add(company);

        }

        int a = companies.size();
        System.out.println(a);

    }

    @Override
    public Site getSite() {
        return site;
    }

//    public static void main(String[] args) throws Exception {
//        PhantomJSDownloader phantomDownloader = new PhantomJSDownloader().setRetryNum(3);
//
//        CollectorPipeline<ResultItems> collectorPipeline = new ResultItemsCollectorPipeline();
//
//        Spider.create(new PhantomJSPageProcessor())
//                .addUrl("http://s.taobao.com/search?q=%B6%AC%D7%B0&sort=sale-desc") //%B6%AC%D7%B0为冬装的GBK编码
//                .setDownloader(phantomDownloader)
//                .addPipeline(collectorPipeline)
//                .thread((Runtime.getRuntime().availableProcessors() - 1) << 1)
//                .run();
//
//        List<ResultItems> resultItemsList = collectorPipeline.getCollected();
//        System.out.println(resultItemsList.get(0).get("html").toString());
//    }


    public static void main(String[] args) throws Exception {
        PhantomJSDownloader phantomDownloader = new PhantomJSDownloader(PHANTOMJS_EXEC_PATH).setRetryNum(3);

        //CollectorPipeline<ResultItems> collectorPipeline = new ResultItemsCollectorPipeline();

        Spider.create(new PhantomJSPageProcessor())
                .addUrl("http://qiye.qianzhan.com/trade/q1998") //%B6%AC%D7%B0为冬装的GBK编码
                .setDownloader(phantomDownloader)
                //.addPipeline(collectorPipeline)
                .thread((Runtime.getRuntime().availableProcessors() - 1) << 1)
                .run();

        //List<ResultItems> resultItemsList = collectorPipeline.getCollected();
        //System.out.println(resultItemsList.get(0).get("html").toString());
    }

    protected  class Company{

        private String name;

        private String personName;

        private String time;

        private String address;

        private String capital;

        private String state;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCapital() {
            return capital;
        }

        public void setCapital(String capital) {
            this.capital = capital;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}
