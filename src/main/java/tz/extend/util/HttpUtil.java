package tz.extend.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tz.common.converter.JSONConverter;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 :
 * 설    명 : HTTP 처리에 필요한 UTILITY 클래스.
 * 작 성 자 : TZ
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class HttpUtil {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static final String Boundary = "--7d021a37605f0";

    /**
     * <pre>
     * callUrl 호출 하는 메소드
     * </pre>
     * @param urlStr
     * @param input
     * @param header
     * @throws Exception
     */
    public static String callUrl(String urlStr, Map input, Map header) throws Exception{
        try{
            return callUrl(urlStr, input, new ArrayList<Map<String, Object>>(), header);
        }catch(Exception e){
            logger.debug(e.toString());
            throw new Exception(e);
        }
    }

    /**
     * <pre>
     * URL을 호출 하는 메소드
     * </pre>
     * @param urlStr
     * @param input
     * @param mData
     * @param header
     * @return htmls
     * @throws Exception
     */
    public static String callUrl(String urlStr, Map input, List<Map<String, Object>> mData, Map header)
            throws Exception{
        String htmls = new String();
        try{
            String charset = StringUtil.getText(header.get("charset")); // euc-kr, utf-8 ..., noConvert

            String strParam = "";
            if(StringUtil.getText(header.get("Content-Type")).indexOf("application/json") > -1){
                JSONObject object = JSONConverter.convertMap2Json(input);
                strParam = object.toString();
                strParam = "{\"json\" : " + strParam + "}";
            }else{
                if(mData != null && mData.size() > 0){
                    for(int i = 0; i < mData.size(); i++){
                        strParam += urlEncode((HashMap)mData.get(i), charset) + "&";
                    }
                    if(strParam.length() > 0)
                        strParam = strParam.substring(0, strParam.length() - 1);
                }else{
                    strParam = urlEncode(input, charset);
                }
            }

            if(charset.equals("noConvert"))
                charset = "";
            String inboundCharset = StringUtil.getText(header.get("inboundCharset"));
            if(!inboundCharset.equals(""))
                charset = inboundCharset;

            htmls = callUrl(urlStr, strParam, header, charset);
        }catch(Exception e){
            logger.debug("callUrl : " + e.toString());
            throw new Exception(e);
        }
        return htmls;
    }

    /**
     * <pre>
     * URL을 호출 하는 메소드
     * </pre>
     * @param urlStr
     * @param strParam
     * @param header
     * @param charset
     * @return htmls
     * @throws Exception
     */
    public static String callUrl(String urlStr, String strParam, Map header, String charset) throws Exception{
        StringBuffer htmls = new StringBuffer();
        URL url = new URL(urlStr);
        URLConnection httpConn = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try{
            httpConn = url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setUseCaches(false);
            if(header != null) {
                Set dataKeySet = header.keySet();
                Iterator dataIterator = dataKeySet.iterator();
                while(dataIterator.hasNext()){
                    String dataKey = dataIterator.next().toString();
                    if(!dataKey.equals("charset") && !dataKey.equals("inboundCharset")){
                        httpConn.setRequestProperty(dataKey, header.get(dataKey).toString());
                        logger.debug("====setRequestProperty : " + dataKey + " => " + header.get(dataKey).toString());
                    }
                }
            }
            //out = new PrintWriter( httpConn.getOutputStream() );
            out = new PrintWriter(new OutputStreamWriter(httpConn.getOutputStream(), "utf-8"));
            out.print(strParam);
            logger.debug("====urlStr:" + urlStr);
            //logger.debug("====strParam:" + strParam);
            out.flush();

            InputStream is = httpConn.getInputStream();

            if(charset != null && !charset.equals(""))
                in = new BufferedReader(new InputStreamReader(is, charset), 8 * 1024);
            else
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);

            String line = null;
            while((line = in.readLine()) != null){
                htmls.append(line);
                htmls.append("\r\n");
            }
        }catch(Exception e){
            logger.debug(e.toString());
            throw new Exception(e);
        }finally{
            if(out != null)
                try{
                    out.close();
                }catch(Exception e){
                    logger.debug(e.toString());
                    throw new Exception(e);
                }
            if(in != null)
                try{
                    in.close();
                }catch(Exception e){
                    logger.debug(e.toString());
                    throw new Exception(e);
                }
        }
        return htmls.toString();
    }

    /**
     * <pre>
     * url encode 를 하는 메소드
     * </pre>
     * @param hash
     * @param charset
     * @return buf.toString()
     */
    public static String urlEncode(Map hash, String charset){
        if(hash == null)
            throw new IllegalArgumentException("argument is null");
        boolean isFirst = true;
        StringBuffer buf = new StringBuffer();

        Set dataKeySet = hash.keySet();
        Iterator dataIterator = dataKeySet.iterator();
        while(dataIterator.hasNext()){
            if(isFirst)
                isFirst = false;
            else
                buf.append('&');

            try{
                String dataKey = dataIterator.next().toString();
                String value = StringUtil.getText(hash.get(dataKey));

                if(charset != null && !charset.equals("") && !charset.equals("noConvert")){
                    buf.append(URLEncoder.encode(dataKey, charset));
                }else if(charset.equals("noConvert")){
                    buf.append(dataKey);
                }else{
                    buf.append(URLEncoder.encode(dataKey));
                }
                buf.append('=');
                if(charset != null && !charset.equals("") && !charset.equals("noConvert")){
                    buf.append(URLEncoder.encode(value, charset));
                }else if(charset.equals("noConvert")){
                    buf.append(value);
                }else{
                    buf.append(URLEncoder.encode(value));
                }
            }catch(UnsupportedEncodingException e){
                logger.error(e.getMessage());
            }
        }
        return buf.toString();
    }

    /**
     * <pre>
     * URL Decode를 하는 메소드
     * </pre>
     * @param buf
     * @param charset
     * @return
     */
    public static String urlDecode(String buf, String charset){
        try{
            if(charset != null && !charset.equals(""))
                return URLDecoder.decode(buf, charset);
            else
                return URLDecoder.decode(buf);
        }catch(UnsupportedEncodingException e){
            logger.error(e.getMessage());
        }
        return "";
    }

    /**
     * <pre>
     * URL Decode를 하는 메소드
     * </pre>
     * @param data
     * @param charset
     * @return data
     */
    public static Map urlDecode(Map data, String charset){
        try{
            Set dataKeySet = data.keySet();
            Iterator dataIterator = dataKeySet.iterator();
            while(dataIterator.hasNext()){
                String dataKey = dataIterator.next().toString();
                if(data.get(dataKey).getClass().toString().indexOf("java.lang.String") > -1){
                    try{
                        data.put(dataKey, URLDecoder.decode(data.get(dataKey).toString(), charset));
                    }catch(Exception e){
                        logger.debug(e.toString());
                    }
                }
            }
        }catch(Exception e){
            logger.debug(e.toString());
        }
        return data;
    }

    /**
     * <PRE>
     * TODO:    사용법
     *  String url = &quot;/test/test.jsp&quot;;
     * //일반적으로 text기반
     * byteResult = callUrlExt(url,null,false);
     * out.println(&quot;byteResult = &quot;+new String(byteResult));
     * //binary file
     * byteResult = callUrlExt(url,null,true);
     * 기능
     * - jsp,html 등 웹 text기반 resource 호출가능
     * - xml 유니코드 호출가능 (한글깨짐방지)
     * -이미지,플래쉬,동영상등 바이너리 데이타 처리하여 파일로 저장 가능
     * &#064;param serverUrl : 호출할 URL
     * &#064;param input : 파라미터를 넘길때 사용할 HashMap
     * &#064;param bBinary : true-&gt; 바이너리 모드로 호출 false-&gt;ascii 모드로 호출
     * &#064;return
     * &#064;throws MalformedURException
     * </PRE>
     *
     * @param urlStr
     * @param input
     * @param fileNm
     * @param bBinary
     * @return bufferResult
     * @throws Exception
     */
    public static byte[] callUrlExt(String urlStr, Map input, String fileNm, boolean bBinary) throws Exception{
        URL url = null;
        URLConnection httpConn = null;

        //binary
        OutputStream outStream = null;
        InputStream inStream = null;
        BufferedInputStream bufInStream = null;

        //text
        Writer writer = null;
        java.io.Reader reader = null;
        BufferedReader buf = null;

        String output = null;
        StringBuffer sb = new StringBuffer();

        byte[] bufferResult = null;

        try{
            url = new URL(urlStr);
            httpConn = url.openConnection();
            httpConn.setDoOutput(true); //post방식:true
            httpConn.setDoInput(true); //데이타 첨부되는 경우
            //헤더 셋팅- 웹서버마다 헤더정보가 부족하면 405에러를 반환하기도 한다.
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Accept-Charset", "euc-kr");
            //   connection.setRequestProperty("host",getHostIP());
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0; i-NavFourF; .NET CLR 1.1.4322)");
            outStream = httpConn.getOutputStream();
            writer = new OutputStreamWriter(outStream);
            //DataOutputStream doutput = new DataOutputStream(outStream);

            if(input != null && !input.isEmpty()){
                String param = "";
                Set dataKeySet = input.keySet();
                Iterator dataIterator = dataKeySet.iterator();
                while(dataIterator.hasNext()){
                    String dataKey = dataIterator.next().toString();
                    String str = input.get(dataKey).toString();
                    param += dataKey + "=" + str + "&";
                }
                writer.write(param);
            }
            writer.flush();
            inStream = httpConn.getInputStream();

            //text 읽을때
            if(bBinary == false){
                //스트링 버퍼로 변환하기 위해서는 아래처럼 한다.
                //euc-kr로 안하면 euc-kr로 인코딩 안된 문서는 utf-8로 읽어들이면서 sun.io.MalformedInputException을 발생
                //euc-kr하면 xml등을 호출시 유니코드 한글이 ???로 나온다.
                //reader = new InputStreamReader(inStream,"euc-kr");
                //cp949로 하면 euc-kr도 커버하면서 xml 유니코드 한글도 잘 나온다.
                reader = new InputStreamReader(inStream, "cp949");
                buf = new BufferedReader(reader);
                while((output = buf.readLine()) != null){
                    if(!output.equals("")){
                        output.trim();
                        sb.append(output + "\r\n");
                    }
                }
                bufferResult = sb.toString().getBytes();
            }else{
                //binary로 읽을 때
                bufInStream = new BufferedInputStream(inStream);
                int bufferOffset = 0;
                int nReadSize = 0;
                int bufferCapacity = bufInStream.available() + 1;

                bufferResult = new byte[bufferCapacity];
                while((nReadSize = bufInStream.read(bufferResult, bufferOffset, bufferCapacity - bufferOffset)) != -1){
                    bufferOffset += nReadSize;
                    if(bufferOffset >= bufferCapacity){
                        bufferCapacity *= 2;
                        byte[] newBuffer = new byte[bufferCapacity];
                        System.arraycopy(bufferResult, 0, newBuffer, 0, bufferOffset);
                        bufferResult = newBuffer;
                    }
                }
                if(fileNm != null){
                    BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(fileNm));
                    bout.write(bufferResult);
                    bout.flush();
                    bout.close();
                }
            }
        }catch(ConnectException e){
            logger.debug("error!!!" + e.toString());
        }catch(SocketException e){
            logger.debug("error!!!" + e.toString());
        }catch(java.io.IOException e){
            logger.debug("error!!!" + e.toString());
        }finally{
            try{
                writer.close();
                outStream.close();
                inStream.close();
                if(reader != null)
                    reader.close();
                if(buf != null)
                    buf.close();
                if(bufInStream != null)
                    bufInStream.close();
            }catch(java.io.IOException e1){
                logger.debug("error!!!" + e1.toString());
            }
        }
        return bufferResult;
    }

    /**
     * <pre>
     * url encode 시 characte set을 설정하는 메소드
     * </pre>
     * @param val
     * @param encode
     * @return val
     */
    public static String encodeCharset(String val, String encode){
        if(val != null && !encode.equals("")){
            try{
                val = new String(URLEncoder.encode(val, encode));

            }catch(UnsupportedEncodingException e){
                logger.error(e.getMessage());
            }
        }
        return val;
    }

    /**
     * <pre>
     * url decode 시 characte set을 설정하는 메소드
     * </pre>
     * @param val
     * @param encode
     * @return val
     */
    public static String decodeCharset(String val, String encode){
        if(val != null && !encode.equals("")){
            try{
                val = new String(URLDecoder.decode(val).getBytes(), encode);
            }catch(UnsupportedEncodingException e){
                logger.error(e.getMessage());
            }
        }
        return val;
    }

    /**
     * <pre>
     * post로 파일 업로드후 리턴값 받기
     * </pre>
     * @param url
     * @param fileList
     * @param fileInfo
     * @throws Exception
     */
    public static void uploadFile(URL url, List<File> fileList, List<String> fileInfo) throws Exception{
        HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setUseCaches(false);
        httpConn.setChunkedStreamingMode(1024);

        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Boundary);
        DataOutputStream httpOut = new DataOutputStream(httpConn.getOutputStream());

        for(int i = 0; i < fileList.size(); i++){
            File f = fileList.get(i);
            String str = "--" + Boundary + "\r\n" + "Content-Disposition: form-data;name=\"" + fileInfo.get(i)
                    + "\"; filename=\"" + f.getName() + "\"\r\n" + "Content-Type: image/png\r\n" + "\r\n";

            httpOut.write(str.getBytes());

            FileInputStream uploadFileReader = new FileInputStream(f);
            int numBytesToRead = 1024;
            int availableBytesToRead;
            while((availableBytesToRead = uploadFileReader.available()) > 0){
                byte[] bufferBytesRead;
                bufferBytesRead = availableBytesToRead >= numBytesToRead ? new byte[numBytesToRead]
                        : new byte[availableBytesToRead];
                uploadFileReader.read(bufferBytesRead);
                httpOut.write(bufferBytesRead);
                httpOut.flush();
            }
            httpOut.write(("--" + Boundary + "--\r\n").getBytes());
        }

        httpOut.write(("--" + Boundary + "--\r\n").getBytes());

        httpOut.flush();
        httpOut.close();

        // read & parse the response
        InputStream is = httpConn.getInputStream();
        StringBuilder response = new StringBuilder();
        byte[] respBuffer = new byte[4096];
        while(is.read(respBuffer) >= 0){
            response.append(new String(respBuffer).trim());
        }
        is.close();
        logger.debug(response.toString());
    }

    /**
     * <pre>
     * url로부터 파일을 생성 (이어받기)
     * </pre>
     * @param urlStr
     * @param fileNm
     * @throws Exception
     */
    public static void makeFileFromUrl(String urlStr, String fileNm) throws Exception{
        int nDone = 0;
        int nTimeout = 30000;
        long fileSize, remains, lenghtOfFile = 0;

        RandomAccessFile output = null;
        InputStream input = null;
        try{
            File file = new File(fileNm);
            if(file.exists() == false){
                file.createNewFile();
            }
            output = new RandomAccessFile(file.getAbsolutePath(), "rw");

            fileSize = output.length();
            output.seek(fileSize);

            URL url = new URL(urlStr);
            URLConnection httpConn = url.openConnection();
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            httpConn.setUseCaches(false);
            httpConn.setRequestProperty("Range", "bytes=" + String.valueOf(fileSize) + '-');
            httpConn.connect();
            httpConn.setConnectTimeout(nTimeout);
            httpConn.setReadTimeout(nTimeout);
            remains = httpConn.getContentLength();
            lenghtOfFile = remains + fileSize;

            if((remains <= nDone) || (remains == fileSize)){
                return;
            }
            input = httpConn.getInputStream();

            byte data[] = new byte[1024];
            int count = 0;

            if(fileSize < lenghtOfFile){
                while((count = input.read(data)) != -1){
                    output.write(data, 0, count);
                }
            }
        }catch(Exception e){
            logger.debug("error!!!" + e.toString());
        }finally{
            try{
                if(output != null)
                    output.close();
                if(input != null)
                    input.close();
            }catch(java.io.IOException e1){
                logger.debug("error!!!" + e1.toString());
            }
        }
    }

    /**
     * <pre>
     * content-type이 "multipart/form-data"인지를 검사한다.
     * request.getContentType() method를 사용하며 이때 구해지는 문자열을 적절한 크기로 잘라서 비교하는 logic을 사용한다.
     * (참고) html의 form에서 특별하게 enctype을 지정하지 않는경우 content-Type은 application/x-www-form-urlencoded이다.
     * </pre>
     * @param req servlet에서 전달받은 HttpServletRequest. 실제의 content type을 구하기 위해 사용한다.
     * @return boolean "multipart/form-data"인경우 true, 그렇지 않을경우 false.
     */
    public static boolean isMultipart(HttpServletRequest req){
        String contentType = null;
        String multipartContentType = "multipart/form-data";
        contentType = req.getContentType();

        return (contentType != null && contentType.length() > 19 && multipartContentType.equals(contentType.substring(
                0, 19))) ? true : false;
    }
    
    public static void infoHeader(HttpURLConnection con){
		System.out.println("############################################################");
		System.out.println("con : " + con.getURL());
		
		Map m = con.getHeaderFields();
		for (Iterator i = m.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			System.out.println("key : " + key + ", value : " + m.get(key));
		}
    }
    public static String getCookies(HttpURLConnection con){
		String cookies = "";

		Map m = con.getHeaderFields();
		if (m.containsKey("Set-Cookie")) {
			Collection c = (Collection) m.get("Set-Cookie");
			for (Iterator i = c.iterator(); i.hasNext();) {
				cookies += (String) i.next() + ", ";
			}
		}
		return cookies;
    }
    public static void setCookies(HttpURLConnection con, String  cookies){
		Map m = con.getHeaderFields();
    	m.put("Set-Cookie", cookies);
    }

    /**
     * <pre>
     * main(String[] args)
     * </pre>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        /*String url = "http://sheepsharedev.sheepshare.com/sheepshare/jsp/common/error/TZUI.zip";
        url = "http://localhost:7001/jsp/common/error/TZUI.zip";
        url = "http://localhost:7001/tz/common/file/downloadFileAtXpl.ajax?fileId=1&fileAtchId=405";
        makeFileFromUrl(url, "C:/temp/aaa.zip");*/
		URL url = new URL("http://dev.dwconst.co.kr/sheepshare.nsf?login&username=1202723&password=1202723");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false);
		connection.connect();
		HttpUtil.infoHeader(connection);
		String cookies = HttpUtil.getCookies(connection);
		System.out.println("cookies : " + cookies);
		
		url = new URL("http://dev.dwconst.co.kr/gw/tzs/comm.nsf/wFrmAprvCnt?readForm");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setInstanceFollowRedirects(false);
		//HttpUtil.setCookies(connection, cookies);
		connection.setRequestProperty("cookie", cookies);

		connection.connect();
		HttpUtil.infoHeader(connection);

		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "US-ASCII"));
		StringBuffer sb = new StringBuffer();

		String strData = "";
		while ((strData = br.readLine()) != null) {
			sb.append(strData);
		}
		String str = sb.toString();
		String totalCnt ="";
		str = str.substring(str.indexOf("<body"), (str.lastIndexOf("</body>") + 7));
		
		System.out.println("##################################### : " + str);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true); // never forget this!
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = (Document) builder.parse(new ByteArrayInputStream(str.getBytes("US-ASCII")));

        XPathFactory xmlFactory = XPathFactory.newInstance();
        XPath xpath = xmlFactory.newXPath();

        XPathExpression exprCode = xpath.compile("//body/form/text()");
        Object resultCode = exprCode.evaluate(doc, XPathConstants.NODESET);
        NodeList codeNodes = (NodeList) resultCode;
        if (codeNodes.getLength() > 0) {
        	Node codeList = codeNodes.item(0);
        	totalCnt = codeList.getNodeValue();
        }
        
        Map<String, Object> rslMap = new HashMap<String, Object>();
        JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(totalCnt);
        for(Object object : jsonObject.entrySet()){
        	Entry<String, Object> entry = (Entry) object;
        	rslMap.put(entry.getKey(), StringUtil.getText(entry.getValue()));
        }
    
        System.out.println("totalCnt 이퀄스 : " + totalCnt.indexOf("quot"));
        System.out.println("totalCnt : " + jsonObject.getString("total"));
        System.out.println("rslMap : " + rslMap);

//        callUrlExt(url, null, "C:/temp/aaa.zip", true);
    }
    
	public static Map<String, Object> getQueryMap(String query)
	{
	    String[] params = query.split("&");
	    Map<String, Object> map = new HashMap<String, Object>();
	    for (String param : params)
	    {
	        String name = param.split("=")[0];
	        String value = "";
	        if(param.split("=").length > 1)
	        	value = param.split("=")[1];
	        
	        map.put(name, value);
	    }
	    return map;
	}

}
