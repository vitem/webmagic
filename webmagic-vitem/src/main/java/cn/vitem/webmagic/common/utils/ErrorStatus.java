package cn.vitem.webmagic.common.utils;

/**
 * Created by vitem_acer on 2017/7/31.
 */
public class ErrorStatus {

    public static int ok = 0;
    public static int long_width = 1;
    public static int long_height = 2;
    public static int LARGE = 3;

    public static int error = -1;

    private int code;

    public ErrorStatus(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static ErrorStatus buildLargeWidth(){
        return new ErrorStatus(long_width);
    }

    public static ErrorStatus buildLargeHeight(){
        return new ErrorStatus(long_height);
    }
    public static ErrorStatus buildLarge(){
        return new ErrorStatus(LARGE);
    }

    public static ErrorStatus buildError(){
        return new ErrorStatus(error);
    }

    public static ErrorStatus buildSuccess(){
        return new ErrorStatus(ok);
    }
}
