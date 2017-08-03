package cn.vitem.webmagic.sipo;

import cn.vitem.webmagic.common.Constant;
import cn.vitem.webmagic.ocr.LianZhongOCR;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by vitem on 2017/8/3.
 * <p>
 */
public class SipoProcess {
    public static void main(String[] args) throws IOException {

        String SIPO_TARGET_URL = "http://epub.sipo.gov.cn/patentoutline.action";

        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //设置webClient的相关参数
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setMaxInMemory(webClient.getOptions().getMaxInMemory()*5);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        //模拟浏览器打开一个目标网址
        HtmlPage rootPage = webClient.getPage(SIPO_TARGET_URL);
        //<Cookie> cookieSet = webClient.getCookieManager().getCookies();
        HtmlImage valiCodeImg = (HtmlImage) rootPage.getElementById("vci");
        valiCodeImg.saveAs(new File(Constant.SIPO_CHECK_CODE_PATH));
        String text = LianZhongOCR.checkCode(Constant.SIPO_CHECK_CODE_PATH);

        //<input type="text" name="vct" autocomplete="off" autofocus>
        //<input type="submit" name="Submit" value="继续">

        HtmlForm htmlForm = rootPage.getForms().get(0);
        HtmlInput snInput = htmlForm.getInputsByName("vct").get(0);
        snInput.click();
        snInput.type(text);
//        snInput.setValueAttribute(text);
//        snInput.setDefaultValue(text);
//        snInput.setNodeValue(text);
        //HtmlInput submitInput = htmlForm.getInputByName("Submit");
        HtmlElement submit = htmlForm.getInputByName("Submit");
        HtmlSubmitInput submitButton = (HtmlSubmitInput) submit;
        HtmlPage page = submitButton.click();
        System.out.println(page);
    }

}
