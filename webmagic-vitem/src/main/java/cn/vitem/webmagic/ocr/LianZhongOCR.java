package cn.vitem.webmagic.ocr;

import cn.vitem.webmagic.common.httpclient.HttpUtils;
import cn.vitem.webmagic.common.httpclient.ResponseWrap;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class LianZhongOCR {

	public static  String UPDOAD_URL="http://v1-http-api.jsdama.com/api.php?mod=php&act=upload";
	public static  String ERR_URL="http://v1-http-api.jsdama.com/api.php?mod=php&act=error";

	public static String checkCode(String filePath) {

		String BOUNDARY = "---------------------------68163001211748"; //boundary就是request头和上传文件内容的分隔符

		//String filePath="D:\\codeTemp2.jpg";//本地验证码图片路径
		Map<String, String> paramMap = getParamMap();
		try {
			URL url=new URL(UPDOAD_URL);
			HttpURLConnection connection=(HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("content-type", "multipart/form-data; boundary="+BOUNDARY);
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);

			OutputStream out = new DataOutputStream(connection.getOutputStream());
			// 普通参数
			if (paramMap != null) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Entry<String, String>> iter = paramMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry<String,String> entry = iter.next();
					String inputName = entry.getKey();
					String inputValue = entry.getValue();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\""
							+ inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// 图片文件
			if (filePath != null) {
				File file = new File(filePath);
				String filename = file.getName();
				String contentType = "image/jpeg";//这里看情况设置
				StringBuffer strBuf = new StringBuffer();
				strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
				strBuf.append("Content-Disposition: form-data; name=\""
						+ "upload" + "\"; filename=\"" + filename+ "\"\r\n");
				strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
				out.write(strBuf.toString().getBytes());
				DataInputStream in = new DataInputStream(
						new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				in.close();
			}
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			//读取URLConnection的响应
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			while (true) {
				int rc = in.read(buf);
				if (rc <= 0) {
					break;
				} else {
					bout.write(buf, 0, rc);
				}
			}
			in.close();
			//结果输出
			String code = new String(bout.toByteArray());
			JSONObject jsonObject = JSON.parseObject(code);
			String result = jsonObject.getJSONObject("data").getString("val");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Map<String, String> getParamMap() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_name", "nmghjtyu");
		paramMap.put("user_pw", "zxc---8899");
		paramMap.put("yzm_minlen", "5");
		paramMap.put("yzm_maxlen", "5");
		paramMap.put("yzmtype_mark", "1013");
		paramMap.put("zztool_token", "nmghjtyu");
		return paramMap;
	}

	private static Map<String, String> builderErrParam(String codeId) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("user_name", "nmghjtyu");
		paramMap.put("user_pw", "zxc---8899");
		paramMap.put("yzm_id", codeId);
		return paramMap;
	}



	public static void reportError(String codeId){
		Map<String, String> errMap = builderErrParam(codeId);

		HttpUtils httpUtils = HttpUtils.post(ERR_URL);
		httpUtils.setContentEncoding("UTF-8");
		httpUtils.setContentType(ContentType.APPLICATION_JSON);
		httpUtils.setParameters(errMap);
		ResponseWrap response = httpUtils.execute();
		String result = response.getString();
		JSONObject jsonResult = JSON.parseObject(result);
		String code = jsonResult.getString("result");
		String data = jsonResult.getString("data");

		System.out.println(code);
		//System.out.println(ascii2native(data));
		return ;

	}

	public static void main(String[] a){
		reportError("8384380507");

	}

	public static String ascii2native(String ascii) {
		int n = ascii.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0, j = 2; i < n; i++, j += 6) {
			String code = ascii.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}

}