package cn.vitem.webmagic.trademark;

import cn.vitem.webmagic.common.Constant;
import cn.vitem.webmagic.common.utils.ImageUtils;
import lombok.Data;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vitem on 2017/8/1.
 * <p>
 */
@Data
public class ImageCutProcess {

    private String anNum ;

    private String anNumPaths ;

    private int x = 160;
    private int y = 274;
    private int width = 100;
    private int height = 1180;

    private static Map<String,String> anTypeMap = new LinkedHashMap<String, String>();

    static {
        //需要合成 需要裁剪
        anTypeMap.put("TMSDGG","1"); //1
        anTypeMap.put("TMZCYS","1");
        anTypeMap.put("TMWXGG","1");
        anTypeMap.put("TMZCCH","1");
        anTypeMap.put("TMXGWX","1");
        anTypeMap.put("TMCXSQ","1");
        anTypeMap.put("TMZCZX","1");
        anTypeMap.put("TMZYSQ","1");
        anTypeMap.put("TMXKBG","1");
        anTypeMap.put("TMXKSQ","1");
        anTypeMap.put("TMXZSQ","1");
        anTypeMap.put("TMGZSQ","1");
        anTypeMap.put("TMBGSQ","1");
        anTypeMap.put("TMBMSQ","1");
        anTypeMap.put("TMZRSQ","1");
        anTypeMap.put("TMZMZC","1");
        anTypeMap.put("TMJTZC","1");
        anTypeMap.put("TMQTZC","1");
        anTypeMap.put("TMZCZC","1");
        //不处理此类数据
        anTypeMap.put("TMJTSQ","2");
        //需要合成 但是不需要 裁剪
        anTypeMap.put("TMZCSQ","0");

    }

    //private String anNumPaths ;

    public ImageCutProcess (String anNum){
        this.anNum = anNum;
        this.anNumPaths = String.format("%s/%s/%s",Constant.DATA_ROOT_PATH,Constant.DATA_BUINESS_ORIGIN,anNum);
    }

    public static ImageCutProcess build(String anNum){
        return new ImageCutProcess(anNum);
    }

    public  void splitImage(){
        File tradeMarkFile = new File(anNumPaths);
        File[] anNumFiles = tradeMarkFile.listFiles();
        for (int i = 0; i < anNumFiles.length; i++) {
            File anTypeFile = anNumFiles[i];
            String typeName = anTypeFile.getName();
            if(!anTypeMap.containsKey(typeName)){
                continue;
            }
            if("1".equals(anTypeMap.get(typeName))){
                File[] images = anTypeFile.listFiles();
                for (int j = 0; j < images.length ; j++) {
                    File image = images[j];
                    if(!image.getName().contains(".png")){
                        continue;
                    }
                    String destImagePath = image.getAbsolutePath().replace(Constant.DATA_BUINESS_ORIGIN,Constant.DATA_BUINESS_CUT);
                    destImagePath = destImagePath.replace(".png","-cut.png");
                    File destImage = new File(destImagePath);
                    ImageUtils.cutImg(image,destImage,x,y,width,height);
                }
            }else if("0".equals(anTypeMap.get(typeName))){

            }



        }

    }

    public static void main(String[] a){
        ImageCutProcess imageCutProcess = ImageCutProcess.build("1561");
        imageCutProcess.splitImage();
    }




}
