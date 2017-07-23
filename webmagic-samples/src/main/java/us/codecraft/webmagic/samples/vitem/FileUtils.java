package us.codecraft.webmagic.samples.vitem;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by 01112026 on 14-7-29.
 */
public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 安全关闭输入流
     *
     * @param inputStream
     */
    public static void closeStream(InputStream inputStream) {
        if (inputStream == null) return;
        try {
            inputStream.close();
        } catch (IOException e) {

        }
    }

    /**
     * 安全关闭输出流
     *
     * @param outputStream
     */
    public static void closeStream(OutputStream outputStream) {
        if (outputStream == null) return;
        try {
            outputStream.close();
        } catch (IOException e) {

        }
    }

    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, file);
    }

    public static void moveFile(File filePath, File dirPath) throws IOException {
        org.apache.commons.io.FileUtils.moveFile(filePath, dirPath);
    }


    /**
     * 下载的具体处理
     *
     * @param path
     * @param fileName
     * @param request
     * @param response
     */
    public static void do4download(String path, String fileName, HttpServletRequest request, HttpServletResponse response) {
        RandomAccessFile oSavedFile = null;
        ServletOutputStream out = null;
        try {
            File file = new File(path);
            String range = request.getHeader("Range");
            long startByte = 0;
            long endByte = file.length() - 1;
            long totalByte = file.length();
            if (range != null && !"null".equals(range)) {
                range = range.split("=")[1];
                String[] p = range.split("-");
                if (p[0].isEmpty()) {
                    startByte = endByte - Long.parseLong(p[1]);
                } else {
                    startByte = Long.parseLong(p[0]);

                    if (p.length == 2 && !p[1].isEmpty()) {
                        endByte = Long.parseLong(p[1]);
                    }
                }
                response.setStatus(206);
                response.addHeader("Accept-Ranges", "bytes");
                response.addHeader("Content-Range", " bytes " + startByte + "-" + endByte + "/" + totalByte);
            }
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            //response.setContentType("application/x-download;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            //response.addHeader("Content-Length", "" + (endByte - startByte + 1));
            oSavedFile = new RandomAccessFile(file.getAbsolutePath(), "r");
            // 定位文件指针到 nPos 位置
            oSavedFile.seek(startByte);
            byte[] b = new byte[102400];
            // 从输入流中读入字节流，然后写到文件中
            int i = -1;
            long s = startByte;
            out = response.getOutputStream();
            while ((i = oSavedFile.read(b, 0, ((s + 102400) > endByte ? (int) (endByte - s + 1) : 102400))) > 0) {
                out.write(b, 0, i);
                s += i;
            }
            out.flush();

        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
            if (out != null) {
                IOUtils.closeQuietly(out);
            }
            if (oSavedFile != null) {
                IOUtils.closeQuietly(oSavedFile);
            }

        }
    }



    public static void copyFile(File filePath, File dirPath) throws IOException {
        org.apache.commons.io.FileUtils.copyFile(filePath, dirPath);
    }

    public static void creatTxtFile(String filePath) throws IOException {
        File filename = new File(filePath);
        if (!filename.exists()) {
            filename.createNewFile();
        }
    }

    public static void writeTxt(String path, String content, boolean flag) throws IOException {
        FileOutputStream fos = new FileOutputStream(path, flag);//true表示在文件末尾追加
        fos.write(content.getBytes("utf-8"));
        fos.close();//流要及时关闭
    }

    public static List<String> readLines(File file, String encoding)
            throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtils.readLines(in, encoding);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static FileInputStream openInputStream(File file)
            throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canRead()) {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else {
            throw new FileNotFoundException("File '" + file + "' does not exist");
        }
        return new FileInputStream(file);
    }

    public static List<String> readLines(File file)
            throws IOException {
        return readLines(file, null);
    }

    public static boolean isUTF8Format(byte[] bytes) {
        int fileStreamLength = bytes.length;
        int charByteCounter = 1;
        byte curByte;
        for (int i = 0; i < fileStreamLength; i++) {
            curByte = bytes[i];
            if (charByteCounter == 1) {
                if (curByte < 0 && curByte >= (byte) 0x80) {
                    while (((curByte <<= 1) & 0x80) != 0) {
                        charByteCounter++;
                    }
                    if (charByteCounter == 1 || charByteCounter > 6) {
                        return false;
                    }
                }
            } else if (charByteCounter > 1) {
                int b = curByte & 0xC0;
                if ((curByte & 0xC0) != 0x80) {
                    return false;
                }
                charByteCounter--;
            } else {
                return false;
            }
        }
        if (charByteCounter != 1) {
            return false;
        }
        return true;
    }

    public static String charEncoding(String filePath) {
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        byte[] buffer = new byte[1024];
        int readBytes = 0;
        String charEncoding = null;
        try {
            fis = new FileInputStream(filePath);
            readBytes = fis.read(buffer, 0, 1024);
            bos = new ByteArrayOutputStream();
            if (readBytes > 3) {
                if (buffer[0] == (byte) 0xEF && buffer[1] == (byte) 0xBB && buffer[2] == (byte) 0xBF) {
                    // UTF-8格式文本文件
                    return "UTF-8";
                }
                if (buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xFE) {
                    charEncoding = "UNICODE";
                } else if (buffer[0] == (byte) 0xFE && buffer[1] == (byte) 0xFF) {
                    charEncoding = "UNICODE";
                }
            }

            while (readBytes > 0) {
                bos.write(buffer, 0, readBytes);
                readBytes = fis.read(buffer, 0, 1024);
            }
            fis.close();
            fis = null;

            buffer = bos.toByteArray();

            if (charEncoding == null) {
                if (isUTF8Format(buffer)) {
                    charEncoding = "UTF-8";
                } else {
                    charEncoding = "GBK";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return charEncoding;
    }

    public static String charEncoding(InputStream fis) {
        ByteArrayOutputStream bos = null;
        byte[] buffer = new byte[1024];
        int readBytes = 0;
        String charEncoding = null;
        try {
            readBytes = fis.read(buffer, 0, 1024);
            bos = new ByteArrayOutputStream();
            if (readBytes > 3) {
                if (buffer[0] == (byte) 0xEF && buffer[1] == (byte) 0xBB && buffer[2] == (byte) 0xBF) {
                    // UTF-8格式文本文件
                    return "UTF-8";
                }
                if (buffer[0] == (byte) 0xFF && buffer[1] == (byte) 0xFE) {
                    charEncoding = "UNICODE";
                } else if (buffer[0] == (byte) 0xFE && buffer[1] == (byte) 0xFF) {
                    charEncoding = "UNICODE";
                }
            }

            while (readBytes > 0) {
                bos.write(buffer, 0, readBytes);
                readBytes = fis.read(buffer, 0, 1024);
            }
            fis.close();
            fis = null;

            buffer = bos.toByteArray();

            if (charEncoding == null) {
                if (isUTF8Format(buffer)) {
                    charEncoding = "UTF-8";
                } else {
                    charEncoding = "GBK";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return charEncoding;
    }

    public static void closeQuietly(InputStream fileInputStream) {
        IOUtils.closeQuietly(fileInputStream);
    }
    
    
    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }



}
