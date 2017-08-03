package cn.vitem.webmagic.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by vitem on 2017/8/1.
 * <p>
 */
public class Constant {

    public static String DATA_ROOT_PATH = "d:/data/";

    public static String DATA_BUINESS_ORIGIN = "trademark";

    public static String DATA_BUINESS_CUT = "trademark_cut";

    public static String DATA_BUINESS_MERGE = "trademark_merge";

    public static Map<String, String> TYPE_MAP = new LinkedHashMap<String, String>();

    public static String CHARSET_NAME = "UTF-8";

    public static String PATTERN = "^[0-9a-zA-Z]{5,11}$";

    public static String FILE_SPLIT = "\\.";

    public static String SIPO_CHECK_CODE_PATH ;

    public static String osName = System.getProperty("os.name").toLowerCase();

    static {

        SIPO_CHECK_CODE_PATH = Constant.osName.contains("windows")?"d:/develop/data/sipo/verify_code/vci.jpg":"/Users/vitem/data/sipo/verify_code/vci.jpg";

        //需要合成 需要裁剪
        TYPE_MAP.put("TMSDGG", "1"); //1
        TYPE_MAP.put("TMZCYS", "1");
        TYPE_MAP.put("TMWXGG", "1");
        TYPE_MAP.put("TMZCCH", "1");
        TYPE_MAP.put("TMXGWX", "1");
        TYPE_MAP.put("TMCXSQ", "1");
        TYPE_MAP.put("TMZCZX", "1");
        TYPE_MAP.put("TMZYSQ", "1");
        TYPE_MAP.put("TMXKBG", "1");
        TYPE_MAP.put("TMXKSQ", "1");
        TYPE_MAP.put("TMXZSQ", "1");
        TYPE_MAP.put("TMGZSQ", "1");
        TYPE_MAP.put("TMBGSQ", "1");
        TYPE_MAP.put("TMBMSQ", "1");
        TYPE_MAP.put("TMZRSQ", "1");
        TYPE_MAP.put("TMZMZC", "1");
        TYPE_MAP.put("TMJTZC", "1");
        TYPE_MAP.put("TMQTZC", "1");
        TYPE_MAP.put("TMZCZC", "1");
        //不处理此类数据
        TYPE_MAP.put("TMJTSQ", "2");
        //需要合成 但是不需要 裁剪
        TYPE_MAP.put("TMZCSQ", "1");

    }



}
