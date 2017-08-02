package cn.vitem.webmagic.ocr;


import cn.vitem.webmagic.common.Constant;
import cn.vitem.webmagic.common.utils.FileTools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 调用本地tesseractOCR识别图片信息
 *
 * @author evang
 */
public class TesseractOCR {

    private static final String LANG_OPTION = "-l"; // 英文字母小写l，并非数字1
    private static final String EOL = System.getProperty("line.separator");
    private static String tessPath_win = "D:/develop/Tesseract-OCR";
    // private String tessPath = new File("tesseract").getAbsolutePath();
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static String recognizeImage(File imageFile, String imageFormat) throws Exception {
        File tempImage = ImageIOHelper.createImage(imageFile, imageFormat);
        String outputPath = imageFile.getAbsolutePath().split(Constant.FILE_SPLIT)[0];
        File outputFile = new File(outputPath);

        List cmd = new ArrayList();
        if (OS.contains("windows")) {
            cmd.add(tessPath_win + "/tesseract.exe ");
        } else if (OS.contains("linux")) {
            cmd.add("tesseract");
        } else {
            cmd.add("tesseract");
        }
        cmd.add("");
        cmd.add(outputFile.getName());
        cmd.add(LANG_OPTION);
        //cmd.add("chi_tra");
        cmd.add("eng");
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());
        cmd.set(1, tempImage.getName());
        pb.command(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int w = process.waitFor();

        // 删除临时正在工作文件
        tempImage.delete();
        StringBuffer recognizeText = new StringBuffer();
        StringBuffer unRecognizeText = new StringBuffer();
        String outPutPath = outputFile.getAbsolutePath() + FileTools.TXT_EXT;
        if (w == 0) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(outPutPath), Constant.CHARSET_NAME ));
            String str;
            while ((str = in.readLine()) != null) {
                if(str.trim().length()>0){
                    if(str.matches(Constant.PATTERN)){
                        recognizeText.append(str).append(EOL);
                    }else{
                        unRecognizeText.append(str).append(EOL);
                    }
                }
            }
            in.close();
        } else {
            String msg;
            switch (w) {
                case 1:
                    msg = "Errors accessing files.There may be spaces in your image's filename.";
                    break;
                case 29:
                    msg = "Cannot recongnize the image or its selected region.";
                    break;
                case 31:
                    msg = "Unsupported image format.";
                    break;
                default:
                    msg = "Errors occurred.";
            }
            tempImage.delete();
            throw new RuntimeException(msg);
        }
        //new File(outPutPath);
        String recognizePath = String.format("%s-sn.%s",outPutPath.split(Constant.FILE_SPLIT)[0],outPutPath.split(Constant.FILE_SPLIT)[1]);
        FileTools.writeText(recognizeText.toString(),recognizePath);
        
        String unRecognizePath = String.format("%s-ur.%s",outPutPath.split(Constant.FILE_SPLIT)[0],outPutPath.split(Constant.FILE_SPLIT)[1]);
        FileTools.writeText(unRecognizeText.toString(),unRecognizePath);
        return unRecognizeText.toString();
    }

    public static  void main(String[] a) throws Exception {
        File file = new File("D:/data/trademark_merge/1561/TMZCSQ/1561_TMZCSQ_0.png");
        String text = recognizeImage(file, FileTools.PNG);
        System.out.println(text);
    }
}