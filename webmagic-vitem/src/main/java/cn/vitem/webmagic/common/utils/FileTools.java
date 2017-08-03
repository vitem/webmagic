package cn.vitem.webmagic.common.utils;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by vitem_acer on 2017/8/1.
 */
public class FileTools {

    public static String PNG = "png";
    public static String JPG = "jpg";
    public static String PNG_EXT = ".png";
    public static String TXT = "txt";
    public static String TXT_EXT = ".txt";
    public static String CUT_EXT = "-cut";
    public static String CUT_PNG_EXT = "-cut.png";



    public static void channelCopy(File srcFile, File targetFile) {
        createParentDir(targetFile);
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(srcFile);
            fo = new FileOutputStream(targetFile);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createParentDir( File file){
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
    }

    public static void writeText(String text,String filePath){
        int off = 0;
        OutputStreamWriter recognizeOSW = null;
        try {
            recognizeOSW = new OutputStreamWriter(new FileOutputStream(filePath));
            recognizeOSW.write(text,off,text.length());
            recognizeOSW.flush();
            recognizeOSW.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void downImg(String imgUrl){
//        try {
//            //实例化url
//            URL url = new URL(imgUrl);
//            //载入图片到输入流
//            java.io.BufferedInputStream bis = new BufferedInputStream(url.openStream());
//            //实例化存储字节数组
//            byte[] bytes = new byte[100];
//            //设置写入路径以及图片名称
//            File file = new File(Constant.SIPO_CHECK_CODE_PATH);
//            FileTools.createParentDir(file);
//            OutputStream bos = new FileOutputStream(file);
//            int len;
//            while ((len = bis.read(bytes)) > 0) {
//                bos.write(bytes, 0, len);
//            }
//            bis.close();
//            bos.flush();
//            bos.close();
//            //关闭输出流
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
