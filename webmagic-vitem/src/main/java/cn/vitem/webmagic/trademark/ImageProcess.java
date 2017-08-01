package cn.vitem.webmagic.trademark;

import cn.vitem.webmagic.common.Constant;
import cn.vitem.webmagic.common.utils.FileTools;
import cn.vitem.webmagic.common.utils.ImageUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vitem on 2017/8/1.
 * <p>
 */
@Data
public class ImageProcess {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String anNum;

    private String anNumPaths;

    private int x = 160;
    private int y = 274;
    private int width = 100;
    private int height = 1180;
    private int capacity = 30;

    /**
     * 图片处理，需要裁剪的裁剪，不需要裁剪的copy到需要合并的目录
     * @param anNum
     */
    public ImageProcess(String anNum) {

        this.anNum = anNum;
        this.anNumPaths = String.format("%s/%s/%s", Constant.DATA_ROOT_PATH, Constant.DATA_BUINESS_ORIGIN, anNum);
    }

    public static ImageProcess build(String anNum) {
        return new ImageProcess(anNum);
    }

    public void processImage() {
        File tradeMarkFile = new File(anNumPaths);
        File[] anNumFiles = tradeMarkFile.listFiles();
        
        //按照公告期聚合后，此次循环是公告类型
        for (int i = 0; i < anNumFiles.length; i++) {
            File anTypeFile = anNumFiles[i];
            String typeName = anTypeFile.getName();
            if (!Constant.TYPE_MAP.containsKey(typeName)) {
                continue;
            }
            File[] images = anTypeFile.listFiles();
            cutImage(typeName, images);
            mergeImage(images,4);
        }
    }

    /**
     * 把类型下需要裁剪的图片按照之前约定好的规则处理到新的文件夹
     * @param typeName
     * @param images
     */
    private void cutImage(String typeName, File[] images) {
        for (int j = 0; j < images.length; j++) {
            File image = images[j];
            if (!image.getName().contains(".png")) {
                continue;
            }
            String originPath = image.getAbsolutePath();
            String destImagePath = originPath.replace(Constant.DATA_BUINESS_ORIGIN, Constant.DATA_BUINESS_CUT);
            //图片裁剪
            if ("1".equals(Constant.TYPE_MAP.get(typeName))) {
                destImagePath = destImagePath.replace(".png", "-cut.png");
                ImageUtils.cutImg(image, new File(destImagePath), x, y, width, height);
                    logger.info("cut ：{}",originPath);
            } else
                //图片copy
                if ("0".equals(Constant.TYPE_MAP.get(typeName))) {
                    FileTools.channelCopy(image, new File(destImagePath));
                    logger.info("copy ：{}",image.getAbsolutePath());
                }
        }
    }

    private void mergeImage( File[] images,int capacity) {
        Map<Integer,List<String>> mergeMap = new HashMap<Integer, List<String>>();
        for (int k = 0; k < images.length; k++) {
            File image = images[k];
            if (!image.getName().contains(".png")) {
                continue;
            }
            List<String> filePathList = null;
            int index = k/capacity;
            if(!mergeMap.containsKey(index)){
                filePathList = new ArrayList<String>();
                mergeMap.put(index,filePathList);
            }else{
               filePathList = mergeMap.get(index);
            }
            String srcImagePath = image.getAbsolutePath().replace(Constant.DATA_BUINESS_ORIGIN, Constant.DATA_BUINESS_CUT);
            filePathList.add(srcImagePath);

        }
        for (Map.Entry<Integer, List<String>> entry : mergeMap.entrySet()) {
            Integer index = entry.getKey();
            List<String> filePathList = entry.getValue();

            String targetFilePath = null;
            if(filePathList.size()>0){
                String path = filePathList.get(0);
                File file = new File(path);
                String fileName = file.getName();
                targetFilePath = path.replace(Constant.DATA_BUINESS_CUT, Constant.DATA_BUINESS_MERGE);
                targetFilePath = targetFilePath.replace(fileName,"");
                String targetFileName = new String(targetFilePath);
                targetFileName = targetFileName.split(Constant.DATA_BUINESS_MERGE)[1];
                targetFileName = targetFileName.replace("\\","_");
                targetFileName = targetFileName.replace("/","_");
                targetFileName = targetFileName.startsWith("_")?targetFileName.substring(1,targetFileName.length()):targetFileName;
                targetFileName = index==0?targetFileName.substring(0,targetFileName.length()-1):targetFileName+index;
                //targetFileName = targetFileName.endsWith("_")?targetFileName.substring(0,targetFileName.length()-1):targetFileName;
                targetFilePath = String.format("%s%s.png",targetFilePath,targetFileName);
                String[] filePaths = new String[filePathList.size()];
                for (int i = 0; i < filePathList.size(); i++) {
                    filePaths[i]=filePathList.get(i);
                    logger.info("merge ：{}",filePathList.get(i));
                }
                ImageUtils.mergeImages(filePaths,1,targetFilePath);
            }
        }

    }

    public static void main(String[] a) {
        ImageProcess imgCutProcess = ImageProcess.build("1561");
        imgCutProcess.processImage();
    }
}
