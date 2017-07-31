package cn.vitem.webmagic.common.exception;

/**
 * Created by vitem on 2017/7/29.
 * <p>
 */
public class MagicException extends RuntimeException {

    public static String UPLOAD_FILE_EXT_INVALID = "1001";

    private String errorCode;
    private String errorMessage;
    private String messageId;

    public MagicException(String code,  String message) {
        super(code.concat(" ").concat(message==null?"":message));
        this.errorCode = code;
        this.errorMessage  = message;
        this.messageId = null;
    }
}
