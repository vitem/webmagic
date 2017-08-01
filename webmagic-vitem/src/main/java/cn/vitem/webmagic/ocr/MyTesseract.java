package cn.vitem.webmagic.ocr;

import com.baidu.aip.ocr.AipOcr;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by vitem on 2017/7/30.
 * <p>
 */
public class MyTesseract {

    public static String tesseractCommand = "/usr/local/brew/Cellar/tesseract/3.05.01/bin/tesseract";

    public static String getCaptureText() throws IOException {
        String result = "";
        BufferedReader bufReader = null;

        String testImgPath = "/Users/vitem/data/tm_test/imgCut/dest";
        File fileDir = new File(testImgPath);
        File[] files = fileDir.listFiles();
        if(files==null){
            System.out.println("no files ,please check dest path ");
            return null;
        }
        for(File file : files){
            String destFileName = file.getName();
            if(destFileName.indexOf("-cut.")<0){
                continue;
            }
            String outPath =file.getParent()+"/"+ destFileName.substring(0, destFileName.lastIndexOf("."));
            Runtime runtime = Runtime.getRuntime();
            String command = tesseractCommand + " " +  file.getAbsolutePath() + " " + outPath ;
            Process ps = runtime.exec(command);
            try {
                ps.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File resultFile = new File(outPath + ".txt");
            bufReader = new BufferedReader(new FileReader(file));
            String temp = "";
            StringBuffer sb = new StringBuffer();
            while ((temp = bufReader.readLine()) != null) {
                sb.append(temp);
            }
            // 文字结果
            result = sb.toString();
            if (StringUtils.isNotBlank(result))
                result = result.replaceAll(" ", "");
        }

        return result;
    }

    public static void main(String[] a){
//        try {
//            //getCaptureText();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        baiduAPI();

    }

    public static void baiduAPI(){
        AipOcr client = BaiduOCR.getBaiduOcrClient();
        String genFilePath = "/Users/vitem/Downloads/CheckCodeYunSuan.jpg";
        //String genFilePath = "D:/data/tm_test/tm_test/imgCut/dest/TMSDGG-5520E94F0FA4C098E053640B5030C098-cut.png";
        JSONObject genRes = client.basicGeneral(genFilePath, new HashMap<String, String>());
        System.out.println(genRes.toString(2));

    }
}
