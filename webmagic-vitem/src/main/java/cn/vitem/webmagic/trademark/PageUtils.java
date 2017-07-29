package cn.vitem.webmagic.trademark;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectable;

import java.util.HashMap;

/**
 * Created by vitem on 2017/7/23.
 * <p>
 */
public class PageUtils {

    public static HashMap<String,String> anTypeNameMap = new HashMap<String, String>();

    static {

        anTypeNameMap.put("TMZCSQ","商标初步审定公告");
        anTypeNameMap.put("TMJTSQ","集体商标初步审定公告");
        anTypeNameMap.put("TMZMSQ","证明商标初步审定公告");
        anTypeNameMap.put("TMZCZC","商标注册公告（一）");
        anTypeNameMap.put("TMQTZC","商标注册公告（二）");
        anTypeNameMap.put("TMJTZC","集体商标注册公告");
        anTypeNameMap.put("TMZMZC","证明商标注册公告");
        anTypeNameMap.put("TMZRSQ","商标转让/移转公告");
        anTypeNameMap.put("TMBMSQ","商标注册人/申请人名义及地址变更公告");
        anTypeNameMap.put("TMBGSQ","变更商标代理机构公告");
        anTypeNameMap.put("TMGZSQ","商标更正公告");
        anTypeNameMap.put("TMXZSQ","注册商标续展公告");
        anTypeNameMap.put("TMXKSQ","商标使用许可合同备案公告");
        anTypeNameMap.put("TMXKBG","商标使用许可变更公告");
        anTypeNameMap.put("TMZYSQ","商标质权登记公告");
        anTypeNameMap.put("TMZCZX","注册商标注销公告");
        anTypeNameMap.put("TMXZZX","注册商标未续展注销公告");
        anTypeNameMap.put("TMCXSQ","注册商标撤销公告");
        anTypeNameMap.put("TMXGWX","注册商标宣告无效公告");
        anTypeNameMap.put("TMZCCH","商标注册申请撤回公告");
        anTypeNameMap.put("TMWXGG","无效公告");
        anTypeNameMap.put("TMZCYS","商标注册证遗失声明公告");
        anTypeNameMap.put("TMSDGG","送达公告");
        anTypeNameMap.put("TMZRZJ","集体/证明商标申请人名义地址/成员名单管理规则转让/移转公告");


    }

    public static Selectable getCenter(Page page){
        return page.getHtml().xpath("//center");
    }

    public static String getPageNum(Page page){
        return getCenter(page).xpath("//input[@id=\"pagenum\"]").css("input","value").get();
    }

    public static String getAnNum(Page page){
        return page.getHtml().xpath("//input[@id=\"anNum\"]").css("input","value").get();
    }

    public static String getAnType(Page page){
        return page.getHtml().xpath("//input[@checked=\"checked\"]").css("input","value").get();
    }

    public static Integer getCountPage(Page page){
        return  Integer.valueOf(getCenter(page).xpath("//input[@id=\"countpage\"]").css("input","value").get());
    }

    public static String getSum(Page page){
        return  getCenter(page).xpath("//input[@id=\"sum\"]").css("input","value").get();
    }

    public static String getPageKey(Page page){
        String pageAnType = getAnType(page);
        return getPageKey( page,  pageAnType);
    }


    public static String getPageKey(Page page, String pagePageNum) {
        String pageAnNum = getAnNum(page);
        String pageAnType = getAnType(page);
        return "tm:"+pageAnNum+":"+pageAnType+":"+pagePageNum;
    }
}
