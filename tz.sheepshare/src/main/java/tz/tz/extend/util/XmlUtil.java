package tz.extend.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import tz.extend.util.StringUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : XmlUtil
 * 설      명 : XML 처리를 위한 Utility Class
 * 작 성 자 : TZ
 * 작성일자 : 2013-12-05
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-12-07             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class XmlUtil {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    public static HashMap xFirmExec = new HashMap();

    /**
     * <pre>
     * 하위 노드의 목록 가져오기
     * </pre>
     * @param xmlFilePath
     * @param tagNm
     * @param attri
     * @return
     */
    public static HashMap getSubItemByAttr(String xmlFilePath, String tagNm, String attri){
        HashMap result = new HashMap();
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFilePath));
            Element root = doc.getDocumentElement();
            NodeList children = root.getElementsByTagName(tagNm);
            int nCnt = 0;
            for(int i = 0; i < children.getLength(); i++){
                String id = "";
                Element statement = (Element)children.item(i);
                Attr attrib = statement.getAttributeNode(attri);
                if(attrib != null){
                    id = attrib.getValue();
                }
                if(!id.equals("")){
                    result.put(Integer.toString(nCnt++), id);
                }
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * <pre>
     * search( String strTarget, String strSearch )
     * </pre>
     * @param strTarget
     * @param strSearch
     * @return
     * @throws Exception
     */
    public static int search(String strTarget, String strSearch) throws Exception{
        int result = 0;
        try{
            String strCheck = new String(strTarget);
            int nSize = strTarget.length();
            for(int i = 0; i < nSize;){
                int loc = strCheck.indexOf(strSearch);
                if(loc == -1){
                    break;
                }else{
                    result++;
                    i = loc + strSearch.length();
                    strCheck = strCheck.substring(i);
                }
            }
        }catch(Exception e){
            throw new Exception("[StringUtil][search]" + e.getMessage(), e);
        }
        return result;
    }

    /**
     * <pre>
     * JDK 1.4의 JAXP사용시.. (indenting은 지원하지 않는다.)
     * </pre>
     * @param doc
     * @param fname
     * @param encoding
     */
    public static void saveDocAsFile(Document doc, String fname, String encoding){
        try{
            TransformerFactory tfFac = TransformerFactory.newInstance();
            // use null trandformation
            Transformer tf = tfFac.newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, encoding);
            tf.transform(new DOMSource(doc), new StreamResult(new FileWriter(fname)));
        }catch(IOException e){
            logger.error(e.getMessage());
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
    }

    /**
     * <pre>
     * </pre>
     * @param inputData
     * @param encoding
     * @return
     */
    public static String MultiToXml(List<Map<String, Object>> inputData, String encoding){
        String xmlString = "";
        try{
            DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            String strRoot = "ListMap";
            db = fac.newDocumentBuilder();
            Document doc = db.newDocument();
            Element com = doc.createElement(strRoot);
            doc.appendChild(com);

            // 컬럼정보 추가
            Element dep = doc.createElement("SCHEMA");
            com.appendChild(dep);

            HashMap data = (HashMap)inputData.get(0);
            Set dataKeySet = data.keySet();
            Iterator dataIterator = dataKeySet.iterator();
            while(dataIterator.hasNext()){
                String dataKey = dataIterator.next().toString();
                Element mem = doc.createElement("COLUNM");
                mem.setAttribute("NAME", dataKey);
                mem.setAttribute("DESC", "");
                mem.setAttribute("TYPE", "");
                mem.setAttribute("SIZE", "");
                dep.appendChild(mem);
            }

            // DATASET 추가
            dep = doc.createElement("DATASET");
            com.appendChild(dep);
            int rowsize = inputData.size();
            for(int i = 0; i < rowsize; i++){
                data = (HashMap)inputData.get(i);
                Element mem = doc.createElement("DATA");
                mem.setAttribute("ROWNUM", Integer.toString(i + 1));

                Set dataKeySet2 = data.keySet();
                Iterator dataIterator2 = dataKeySet2.iterator();
                while(dataIterator2.hasNext()){
                    String dataKey = dataIterator2.next().toString();
                    String value = "";
                    if(data.get(dataKey) != null)
                        value = data.get(dataKey).toString();
                    mem.setAttribute(dataKey, value);
                }
                dep.appendChild(mem);
            }

            // ROWSIZE 추가
            dep = doc.createElement("ROWSIZE");
            dep.appendChild(doc.createTextNode(Integer.toString(rowsize)));
            com.appendChild(dep);

            TransformerFactory transfactory = TransformerFactory.newInstance();
            Transformer transformer = transfactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source source = new DOMSource(doc);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            xmlString = sw.toString();
        }catch(ParserConfigurationException e){
            logger.error(e.getMessage());
        }catch(TransformerConfigurationException e){
            logger.error(e.getMessage());
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
        return xmlString;
    }

    /**
     * <pre>
     * HashMapToXml( HashMap data, String encoding )
     * </pre>
     * @param data
     * @param encoding
     * @return
     */
    public static String HashMapToXml(Map<String, Object> data, String encoding){
        List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
        mData.add(mData.size(), data);
        return MultiToXml(mData, encoding);
    }

    /**
     * <pre>
     * MultiToXml < - > XmlToMulti 상호 변환 가능
     * </pre>
     * @param input
     * @param encoding
     * @return
     */
    public static Map<String, ?> XmlToMulti(Map<String, Object> input, String encoding){
        String aXmlStr = StringUtil.getText(input.get("xmlStr"));
        String aSchemaPath = StringUtil.getText(input.get("schemaPath"));
        String aDataPath = StringUtil.getText(input.get("dataPath"));
        Document doc = XmlUtil.parsing(aXmlStr, encoding, false);
        doc.getDocumentElement().normalize();
        Map<String, Object> inputData = new HashMap<String, Object>();
        inputData.put("result", aXmlStr);
        List<Map<String, Object>> column = null;
        if(!aSchemaPath.equals("")) {
            inputData.put("xPath", aSchemaPath);
            column = XmlToMultiByAttri(inputData, encoding);
        }
        inputData.put("xPath", aDataPath);
        List<Map<String, Object>> data = XmlToMultiByAttri(inputData, encoding);

        Map<String, Object> lHolder = new HashMap<String, Object>();
        Map<String, Object> header = new HashMap<String, Object>();
        if(data.get(0).containsKey("qRowCnt")){
            header.put("qRowCnt", data.get(0).get("qRowCnt"));
        }
        if(data.get(0).containsKey("qType")){
            header.put("qType", data.get(0).get("qType"));
        }
        if(data.get(0).containsKey("qMessage")){
            header.put("qMessage", data.get(0).get("qMessage"));
        }

        lHolder.put("HEADER", header);
        lHolder.put("COLUMN", column);
        lHolder.put("BODY", data);
        return lHolder;
    }

    /**
     * <pre>
     * MultiToXml < - > XmlToMulti 상호 변환 가능
     * </pre>
     * @param aXmlStr
     * @param encoding
     * @return
     */
    public static Map<String, ?> XmlToMulti(String aXmlStr, String encoding){
        Map<String, Object> input = new HashMap<String, Object>();
        input.put("xmlStr", aXmlStr);
        input.put("schemaPath", "/List<Map<String, Object>>/SCHEMA/COLUNM");
        input.put("dataPath", "/List<Map<String, Object>>/DATASET/DATA");
        return XmlToMulti(input, encoding);
    }

    /**
     * <pre>
     * List<Map<String, Object>> XmlToMultiByValue( HashMap inputData )
     * </pre>
     * @param inputData
     * @return
     */
    public static List<Map<String, Object>> XmlToMultiByValue(Map<String, Object> inputData){
        return XmlToMultiByValue(inputData, "utf-8");
    }

    /**
     * <pre>
     * List<Map<String, Object>> XmlToMultiByValue( HashMap inputData, String encoding )
     * </pre>
     * @param inputData
     * @param encoding
     * @return
     */
    public static List<Map<String, Object>> XmlToMultiByValue(Map<String, Object> inputData, String encoding){
        String strResult = inputData.get("result").toString();
        String xPath = inputData.get("xPath").toString();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try{
            Document doc = XmlUtil.parsing(strResult, encoding, false);
            doc.getDocumentElement().normalize();
            result = XmlUtil.getNodeListByValue(doc, xPath);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * <pre>
     * List<Map<String, Object>> XmlToMultiByAttri( HashMap inputData )
     * </pre>
     * @param inputData
     * @return
     */
    public static List<Map<String, Object>> XmlToMultiByAttri(Map<String, Object> inputData){
        return XmlToMultiByAttri(inputData, "utf-8");
    }

    /**
     * <pre>
     * List<Map<String, Object>> XmlToMultiByAttri( HashMap inputData, String encoding )
     * </pre>
     * @param inputData
     * @param encoding
     * @return
     */
    public static List<Map<String, Object>> XmlToMultiByAttri(Map<String, Object> inputData, String encoding){
        String strResult = inputData.get("result").toString();
        String xPath = inputData.get("xPath").toString();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try{
            Document doc = XmlUtil.parsing(strResult, encoding, false);
            doc.getDocumentElement().normalize();
            result = XmlUtil.getNodeListByAttri(doc, xPath);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * <pre>
     * List<Map<String, Object>> XmlToMultiByAttValue( HashMap inputData )
     * </pre>
     * @param inputData
     * @return
     */
    public static List<Map<String, Object>> XmlToMultiByAttValue(HashMap<String, Object> inputData){
        return XmlToMultiByAttValue(inputData, "utf-8");
    }

    /**
     * <pre>
     * List<Map<String, Object>> XmlToMultiByAttValue( HashMap inputData, String encoding )
     * </pre>
     * @param inputData
     * @param encoding
     * @return
     */
    public static List<Map<String, Object>> XmlToMultiByAttValue(HashMap<String, Object> inputData, String encoding){
        String strResult = inputData.get("result").toString();
        String xPath = inputData.get("xPath").toString();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try{
            Document doc = XmlUtil.parsing(strResult, encoding, false);
            doc.getDocumentElement().normalize();
            result = XmlUtil.getNodeListByAttValue(doc, xPath);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * <pre>
     * 해당 file을 parsing하여 dom tree를 생성한다.
     * </pre>
     * @param parsing할 파일 name
     * @param DTD check 여부
     * @return 생성된 Document
     */
    public static Document parsing(String xmlStr, String encoding, boolean validating){
        Document temp = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        factory.setCoalescing(true); // Convert CDATA to Text nodes
                factory.setIgnoringComments( true );
                factory.setIgnoringElementContentWhitespace( true );
        DocumentBuilder builder = null;
        try{
            builder = factory.newDocumentBuilder();
            byte[] byteArray = null;
            if(encoding != null && !encoding.equals("")) {
                byteArray = xmlStr.getBytes(encoding);
            } else {
                byteArray = xmlStr.getBytes();
            }
            builder.parse(new InputSource(new StringReader(xmlStr.toString())));

            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            temp = builder.parse(bis);
        }catch(ParserConfigurationException e){
            logger.error(e.toString());
        }catch(SAXException e){
            logger.error(e.toString());
        }catch(IOException e){
            logger.error(e.toString());
        }catch(Exception e){
            logger.error(e.toString());
        }
        return temp;
    }

    /**
     * <pre>
     * getNodeListByValue( Document doc, String path )
     * </pre>
     * @param doc
     * @param path
     * @return
     */
    // xpath를 이용하여 하위 노드의 Value set 가져오기
    //    <feature>
    //        <code>APP1126</code>
    //        <code>APP1127</code>
    //        <code>APP1128</code>
    //    </feature>
    public static List<Map<String, Object>> getNodeListByValue(Document doc, String path){
        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        try{
            NodeIterator nl = XPathAPI.selectNodeIterator(doc, path);
            Node childNode;
            String firstNode = "";
            while((childNode = nl.nextNode()) != null){
                NodeList children = childNode.getChildNodes();
                HashMap input = new HashMap();
                int nSize = children.getLength();
                for(int i = 0; i < nSize; i++){
                    Node childNode2 = children.item(i);
                    if(childNode2.getFirstChild() != null){
                        if(!firstNode.equals("") && firstNode.equals(childNode2.getNodeName())){
                            if(input.size() > 0)
                                map.add(map.size(), input);
                            input = new HashMap();
                        }
                        if(firstNode.equals("")){
                            firstNode = childNode2.getNodeName();
                        }
                        input.put(childNode2.getNodeName(), childNode2.getFirstChild().getNodeValue());
                    }else{
                        if(!childNode2.getNodeName().equals("#text")){
                            input.put(childNode2.getNodeName(), "");
                        }else if(!childNode2.getNodeName().startsWith("[#text:")){
                            input.put(childNode.getNodeName(), StringUtil.LRTrim(childNode2.getNodeValue()));
                        }
                    }
                }
                map.add(map.size(), input);
            }
        }catch(javax.xml.transform.TransformerException e){
            logger.debug("**** getNodeListByValue: " + e.getMessage());
        }
        return map;
    }

    /**
     * <pre>
     * getNodeListByAttri( Document doc, String path )
     * </pre>
     * @param doc
     * @param path
     * @return
     */
    // xpath를 이용하여 하위 노드의 Attribute set 가져오기
    //    <feature>
    //        <code name="APP1126">
    //        <code name="APP1126">
    //        <code name="APP1126">
    //        <code name="APP1126">
    //    </feature>
    public static List<Map<String, Object>> getNodeListByAttri(Document doc, String path){
        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        try{
            NodeList nodeList = XPathAPI.selectNodeList(doc, path);
            if(nodeList != null){
                int nSize = nodeList.getLength();
                for(int i = 0; i < nSize; i++){
                    HashMap input = new HashMap();
                    NamedNodeMap nnm = nodeList.item(i).getAttributes();
                    int nSize2 = nnm.getLength();
                    for(int j = 0; j < nSize2; j++){
                        Node node = nnm.item(j);
                        if(node != null){
                            input.put(node.getNodeName(), node.getNodeValue());
                        }
                    }
                    map.add(map.size(), input);
                }
            }
        }catch(javax.xml.transform.TransformerException tr){
        }
        return map;
    }

    /**
     * <pre>
     * getNodeListByMValue( Document doc, String path )
     * </pre>
     * @param doc
     * @param path
     * @return
     */
    // xpath를 이용하여 하위 노드의 Attribute 및 value 가져오기
    //    <feature>
    //        <key>
    //            <name>3 Cooling Speeds</name>
    //            <content>Control how fast a room is cooled by adjusting the air temperature.</content>
    //        </key>
    //        <key>
    //            <name>3 Fan Speeds</name>
    //            <content>Maintaining a comfortable room is easy thanks to variable fan speeds.</content>
    //        </key>
    //    </feature>
    public static List<Map<String, Object>> getNodeListByMValue(Document doc, String path){
        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        try{
            NodeList children = XPathAPI.selectNodeList(doc, path);
            for(int i = 0; i < children.getLength(); i++){
                HashMap input = new HashMap();
                Element statement = (Element)children.item(i);
                NodeList cList = statement.getChildNodes();
                for(int j = 0; j < cList.getLength(); j++){
                    String code = cList.item(j).getNodeName();
                    if(code.equals("#text"))
                        continue;
                    String value = "";
                    if(cList.item(j).getFirstChild() != null){
                        value = cList.item(j).getFirstChild().getNodeValue();
                    }else{
                        value = "";
                    }
                    value = StringUtil.htmlToString(value);
                    input.put(code, value);
                }
                map.add(map.size(), input);
            }
        }catch(FactoryConfigurationError e){
            logger.error(e.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * <pre>
     * getNodeListByMValue( Map inputData, String path )
     * </pre>
     * @param inputData
     * @param path
     * @return
     */
    public static List<Map<String, Object>> getNodeListByMValue(Map<String, Object> inputData, String path){
        return getNodeListByMValue(inputData, path, "utf-8");
    }

    /**
     * <pre>
     * getNodeListByMValue( Map inputData, String path, String encoding )
     * </pre>
     * @param inputData
     * @param path
     * @param encoding
     * @return
     */
    public static List<Map<String, Object>> getNodeListByMValue(Map<String, Object> inputData, String path, String encoding){
        String strResult = inputData.get("result").toString();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try{
            Document doc = XmlUtil.parsing(strResult, encoding, false);
            doc.getDocumentElement().normalize();
            result = getNodeListByMValue(doc, path);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * <pre>
     * getNodeListByAttValue( Document doc, String path )
     * </pre>
     * @param doc
     * @param path
     * @return
     */
    // xpath를 이용하여 하위 노드의 Attribute 및 value 가져오기
    //    <feature>
    //        <key id="APP1126">
    //            <name>3 Cooling Speeds</name>
    //            <content>Control how fast a room is cooled by adjusting the air temperature.</content>
    //        </key>
    //        <key id="APP1127">
    //            <name>3 Fan Speeds</name>
    //            <content>Maintaining a comfortable room is easy thanks to variable fan speeds.</content>
    //        </key>
    //    </feature>
    public static List<Map<String, Object>> getNodeListByAttValue(Document doc, String path){
        List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
        try{
            Element root = doc.getDocumentElement();
            NodeList children = root.getElementsByTagName(path);
            int row = 0;
            for(int i = 0; i < children.getLength(); i++){
                HashMap input = new HashMap();
                String attr = "";
                Element statement = (Element)children.item(i);
                Attr attrib = statement.getAttributeNode("id");
                if(attrib != null){
                    attr = attrib.getValue();
                }
                if(attr == null){
                    logger.debug("there isn't attribute " + "id");
                    return new ArrayList<Map<String, Object>>();
                }
                input.put(path, attr);
                NodeList cList = statement.getChildNodes();
                for(int j = 0; j < cList.getLength(); j++){
                    if(cList.item(j).getNextSibling() == null)
                        continue;
                    String code = cList.item(j).getNextSibling().getNodeName();
                    if(code.equals("#text"))
                        continue;
                    String value = "";
                    if(cList.item(j).getNextSibling().getFirstChild() != null){
                        value = cList.item(j).getNextSibling().getFirstChild().getNodeValue();
                    }else{
                        value = "";
                    }
                    value = StringUtil.htmlToString(value);
                    input.put(code, value);
                }
                map.add(map.size(), input);
            }
        }catch(FactoryConfigurationError e){
            logger.error(e.getMessage());
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return map;
    }

    /**
     * <pre>
     * parseFile( String fileName )
     * </pre>
     * @param fileName
     * @return
     */
    public static Document parseFile(String fileName){
        Document doc = null;
        File sourceFile = new File(fileName);
        try{
            if(sourceFile.exists()){
                DocumentBuilder docBuilder;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setIgnoringElementContentWhitespace(true);
                try{
                    docBuilder = factory.newDocumentBuilder();
                }catch(ParserConfigurationException e){
                    logger.debug("**** Wrong parser configuration: " + e.getMessage());
                    return null;
                }
                doc = docBuilder.parse(sourceFile);
            }else{
                logger.debug("**** 파일 없음 : " + fileName);
            }
        }catch(SAXException e){
            logger.debug("**** Wrong XML file structure: " + e.getMessage());
            return null;
        }catch(IOException e){
            logger.debug("**** Could not read source file: " + e.getMessage());
        }
        return doc;
    }

    /**
     * <pre>
     * getNodeValue( Document doc, String strTag, String colNm, int nRow )
     * </pre>
     * @param doc
     * @param strTag
     * @param colNm
     * @param nRow
     * @return
     */
    // String tmp = getNodeValue(doc, "<target name=az01.ExcuteBatch>", "pgmTypeCd");
    public static String getNodeValue(Document doc, String strTag, String colNm, int nRow){
        String tagNm = "";
        String tagId = "";

        String strTmp = strTag.substring(strTag.indexOf("<") + 1, strTag.indexOf(">"));
        String strTmpArry[] = strTmp.split(" ");
        if(strTmpArry.length > 1){
            strTag = strTmpArry[0].trim();
            strTmp = strTmpArry[1].trim();
        }else{
            strTag = strTmpArry[0].trim();
        }
        if(strTmp.indexOf("=") > 0){
            strTmpArry = strTmp.split("=");
        }
        if(strTmpArry.length > 1){
            tagNm = strTmpArry[0].trim();
            tagId = strTmpArry[1].trim();
        }else{
            tagId = strTmpArry[0].trim();
        }

        String NodeValue = "";
        Element root = doc.getDocumentElement();
        NodeList children = root.getElementsByTagName(strTag);
        for(int i = 0; i < children.getLength(); i++){
            String id = "";
            Element statement = (Element)children.item(i);
            Attr attrib = statement.getAttributeNode(tagNm);
            if(attrib != null){
                id = attrib.getValue();
            }
            if(id == null){
                logger.debug("XmlUtil id is null");
                return "";
            }
            if(id.equals(tagId)){
                NodeList nNodeList = root.getElementsByTagName(colNm);
                if(nNodeList.item(nRow) == null){
                    return null;
                }
                Element element = (Element)nNodeList.item(nRow);
                NodeValue = element.getFirstChild().getNodeValue();
                return NodeValue;
            }
        }
        return NodeValue;
    }

    /**
     * <pre>
     * xml 파일을 읽어서 원하는 xml String을 순차적으로 HashMap에 넣는다.
     * </pre>
     * @param xmlFilePath
     * @param qSrvId
     * @return
     */
    public static HashMap getNodeValueToHashMap(String xmlFilePath, String qSrvId){
        HashMap lData = new HashMap();
        String footer = "";
        try{
            File f = new File(xmlFilePath);
            int nCnt = 0;
            if(f.exists()){
                //            logger.debug("================== fileName =>" + xmlFilePath + " exist!");
                if(f.canRead()){
                    BufferedReader in = new BufferedReader(new FileReader(xmlFilePath));
                    String s = "";
                    while((s = in.readLine()) != null){
                        if(s.indexOf(qSrvId) > 0 || !footer.equals("")){
                            if(footer.equals("")){
                                String strTmp = s.substring(s.indexOf("<"), s.length());
                                footer = strTmp.substring(strTmp.indexOf("<") + 1, strTmp.indexOf(" ")) + ">";
                            }
                            if(!footer.equals("") && s.indexOf(footer) < 0 && !footer.equals("finished")){
                                lData.put(Integer.toString(nCnt++), s.trim());
                            }else if(!footer.equals("") && s.indexOf(footer) > 0){
                                footer = "finished";
                            }
                        }
                    }
                }
            }else{
                logger.debug("================== fileName =>" + xmlFilePath + " not exist!");
            }

        }catch(FileNotFoundException e){
            logger.error(e.getMessage());
        }catch(IOException e){
            logger.error(e.getMessage());
        }
        return lData;
    }

    /**
     * <pre>
     * root에서 tagName을 가지고 null이 아닌 첫번째 node를 찾고<br>
     * 그것의 children중에서 subTagName을 가지는 null이 아닌 첫번째 Node의 Value를 return
     * </pre>
     * @return java.lang.String
     * @param root org.w3c.dom.Element
     * @param tagName java.lang.String
     * @param subTagName java.lang.String
     */
    public static String getSubTagValue(Element root, String tagName, String subTagName){
        Node rnode = getSubNode(root, tagName, subTagName);
        if(rnode == null)
            return "";
        return rnode.getNodeValue();
    }

    /**
     * <pre>
     * root에서 tagName을 가지고 null이 아닌 첫번째 node를 찾고<br>
     * 그것의 children중에서 subTagName을 가지고 null이 아닌 첫번째 노드를 return
     * </pre>
     * @return org.w3c.dom.Node
     * @param root org.w3c.dom.Element
     * @param tagName java.lang.String
     * @param subTagName java.lang.String
     */
    public static Node getSubNode(Element root, String tagName, String subTagName){
        NodeList list = root.getElementsByTagName(tagName);
        for(int loop = 0; loop < list.getLength(); loop++){
            Node node = list.item(loop);
            if(node == null)
                continue;

            Node child = getSubNode(node, subTagName);
            if(child != null)
                return child;
        }
        return null;
    }

    /**
     * <pre>
     * node의 children중에서 tagName을 가지고 null이 아닌 첫번째 Node객체 return
     * </pre>
     * @return org.w3c.dom.Node
     * @param node org.w3c.dom.Node
     * @param tagName java.lang.String
     */
    public static Node getSubNode(Node node, String tagName){
        if(node == null)
            return null;

        NodeList children = node.getChildNodes();
        for(int innerLoop = 0; innerLoop < children.getLength(); innerLoop++){
            Node child = children.item(innerLoop);
            if((child != null) && (child.getNodeName() != null) && child.getNodeName().equals(tagName)){
                Node grandChild = child.getFirstChild();
                if(grandChild != null && grandChild.getNodeValue() != null)
                    return grandChild;
            }
        } // end inner loop
        return null;
    }

    /**
     * <pre>
     * node의 children중에서 subTagName을 가지고 null이 아닌 첫번째 Node의 Value를 return
     * </pre>
     * @return java.lang.String
     * @param node org.w3c.dom.Node
     * @param subTagName java.lang.String
     */
    public static String getSubTagValue(Node node, String subTagName){
        Node rnode = getSubNode(node, subTagName);
        if(rnode == null)
            return "";
        return rnode.getNodeValue();
    }

    /**
     * <pre>
     * doc, "/press-release/head/byline", "author"
     * </pre>
     * @param node
     * @param xpath
     * @param attr
     * @return
     */
    public static String getAttrValue(Node node, String xpath, String attr){
        try{
            NodeList nodelist = XPathAPI.selectNodeList(node, xpath);
            Element statement = (Element)nodelist.item(0);
            Attr attrib1 = statement.getAttributeNode(attr);
            if(attrib1 != null){
                return attrib1.getValue();
            }
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
        return "";
    }

    /**
     * <pre>
     * doc, "/press-release/body/section/subheading", 2
     * </pre>
     * @param node
     * @param xpath
     * @param cnt
     * @return
     */
    public static String getNodeValue(Node node, String xpath, int cnt){
        try{
            NodeList nodeList = XPathAPI.selectNodeList(node, xpath);
            if(nodeList != null){
                for(int i = 0; i < nodeList.getLength(); i++){
                    if(i == cnt){
                        return nodeList.item(i).getFirstChild().getNodeValue();
                    }
                }
            }
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
        return "";
    }

    /**
     * <pre>
     * doc, "/press-release/body/section/subheading", 2
     * </pre>
     * @param doc
     * @param xpath
     * @return
     */
    public static String getNodeValue(Document doc, String xpath){
        try{
            Node root = doc.getDocumentElement();
            return XPathAPI.eval(root, xpath).str();
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
        return "";
    }

    /**
     * <pre>
     * doc, "/press-release/body/section/subheading", 2
     * </pre>
     * @param doc
     * @param xpath
     * @param k
     * @return
     */
    public static String getNodeValue(Document doc, String xpath, int k){
        try{
            NodeIterator nl = XPathAPI.selectNodeIterator(doc, xpath);
            Node childNode;
            int i = 0;
            while((childNode = nl.nextNode()) != null){
                NodeList children = childNode.getChildNodes();
                if(i == k){
                    if(children.item(0) != null){
                        return children.item(0).getNodeValue(); //.getTextContent()
                    }
                }
                i++;
            }
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
        return "";
    }

    /**
     * <pre>
     * MultiToXml( Map<String, HashMap> lHolder )
     * </pre>
     * @param lHolder
     */
    // List<Map<String, Object>>
    public static void MultiToXml(Map<String, HashMap> lHolder){
        //        try {
        //            HashMap input = (HashMap)lHolder.get("INFO");
        //            List<Map<String, Object>> mData = (List<Map<String, Object>>)lHolder.get("DATA");
        //
        //            String strRoot = input.get( "ROOT" ).toString();
        //            String strCol = input.get( "COL" ).toString();
        //            String strId = input.get( "ID" ).toString();
        //            String strCharset = input.get( "CHARSET" ).toString();
        //            String strFileNm = input.get( "FILENAME" ).toString();
        //            String strDtdNm = input.get( "DTDNAME" ).toString();
        //
        //            File filename = new File( strFileNm );
        //            FileOutputStream fos = new FileOutputStream( filename );
        //            OutputFormat of = new OutputFormat( "XML", strCharset, true );
        //            of.setIndent( 1 );
        //            of.setIndenting( true );
        //            of.setDoctype( null, strDtdNm );
        //            XMLSerializer serializer = new XMLSerializer( fos, of );
        //            ContentHandler hd = serializer.asContentHandler();
        //            hd.startDocument();
        //            //hd.processingInstruction("xml-stylesheet","type=\"text/xsl\" href=\"users.xsl\"");
        //            AttributesImpl atts = new AttributesImpl();
        //            hd.startElement( "", "", strRoot, atts );
        //
        //            for ( int i = 0; i < mData.size(); i++ ) {
        //                HashMap data = (HashMap)mData.get( i );
        //                String[] id = new String[data.size()];
        //                String[] desc = new String[data.size()];
        //                Set dataKeySet = data.keySet();
        //                Iterator dataIterator = dataKeySet.iterator();
        //                int p = 0;
        //                while ( dataIterator.hasNext() ) {
        //                    String dataKey = dataIterator.next().toString();
        //                    String str = data.get( dataKey ).toString();
        //                    id[p] = dataKey;
        //                    desc[p] = str;
        //                    p++;
        //                }
        //
        //                for ( int k = 0; k < id.length; k++ ) {
        //                    atts.clear();
        //                    atts.addAttribute( "", "", strId, "CDATA", id[k] );
        //                    hd.startElement( "", "", strCol, atts );
        //                    hd.characters( desc[k].toCharArray(), 0, desc[k].length() );
        //                    hd.endElement( "", "", strCol );
        //                }
        //            }
        //            hd.endElement( "", "", strRoot );
        //            hd.endDocument();
        //            fos.close();
        //        } catch ( Exception e ) {
        //        }
    }

    // MultiToXml : 표준 xml 생성
    //    inputData.put( "fileNm", "aaaa.xml" );
    //    inputData.put( "root", "spec" );
    //    <spec>
    //    <modelCode id="1000000012">
    //        <modelCodeDisplay>WD3274RHD</modelCodeDisplay>
    //        <specId>SP001583</specId>
    //        <specName>eeee</specName>
    //    </modelCode>
    //    public static OutputStream MultiToXmlStr( HashMap inputData, List<Map<String, Object>> mData ) {
    //        ByteArrayOutputStream os = new ByteArrayOutputStream();
    //        try {
    //            OutputFormat of = new OutputFormat( "XML", "utf-8", true );
    //            of.setIndent( 1 );
    //            of.setIndenting( true );
    //            XMLSerializer serializer = new XMLSerializer( os, of );
    //            ContentHandler hd = serializer.asContentHandler();
    //            hd.startDocument();
    //
    //            String strRoot = inputData.get( "root" ).toString();
    //            String strGroup = "";
    //            hd.startElement( "", "", strRoot, null );
    //            for ( int i = 0; i < mData.size(); i++ ) {
    //                HashMap data = (HashMap)mData.get( i );
    //
    //                int nCnt = 0;
    //                Set dataKeySet = data.keySet();
    //                Iterator dataIterator = dataKeySet.iterator();
    //                while ( dataIterator.hasNext() ) {
    //                    String dataKey = dataIterator.next().toString();
    //                    String dataValue = data.get( dataKey ).toString();
    //                    dataValue = StringUtil.java2html(dataValue);    // html2java
    //                    if(nCnt == 0) {
    //                        strGroup = dataKey;
    //                        AttributesImpl atts = new AttributesImpl();
    //                        atts.addAttribute( "", "", "id", "CDATA", dataValue );
    //                        hd.startElement( "", "", strGroup, atts );
    //                    } else {
    //                        hd.startElement( "", "", dataKey, null );
    //                        hd.characters( dataValue.toCharArray(), 0, dataValue.length() );
    //                        hd.endElement( "", "", dataKey );
    //                    }
    //                    nCnt++;
    //                }
    //                hd.endElement( "", "", strGroup );
    //            }
    //            hd.endElement( "", "", strRoot );
    //            hd.endDocument();
    //            os.close();
    //        } catch ( FileNotFoundException e ) {
    //        } catch ( IOException e ) {
    //        } catch ( SAXException e ) {
    //        }
    //        return os;
    //    }

    // MultiToXml : 표준 xml 생성
    //    inputData.put( "fileNm", "aaaa.xml" );
    //    inputData.put( "root", "spec" );
    //    <spec>
    //    <modelCode id="1000000012">
    //        <modelCodeDisplay>WD3274RHD</modelCodeDisplay>
    //        <specId>SP001583</specId>
    //        <specName>eeee</specName>
    //    </modelCode>
    /*
    <News>
       <Article>
           <Key>10<Key>
           <Source>epr</Source>
           <SourceURL>www.naver.com</SourceURL>
           <Filename>1111</Filename>
           <Display>11/25/2008</Display>
           <Title>LG release New PRADA 2 phone in europe 6</Title>
           <Content>&lt;p&gt;Seoul, Korea, November 3rd, 2008- &lt;a href=""http://www.lge.com/""&gt;LG Electronics, Inc. (LG)&lt;/a&gt;, a worldwide technology and design leader in mobile communications and Microsoft Corp. (Microsoft), the worldwide leader in software, services and solutions, today announced the signing of Memorandum of Understanding (MoU) to form strategic collaboration in Mobile Convergence.&lt;/p&gt;</Content>
       </Article>
       <Article>
           <Key>20<Key>
           <Source>epr2</Source>
           <SourceURL>www.naver.com</SourceURL>
           <Filename>22222</Filename>
           <Display>12/31/2008</Display>
           <Title>LG release New PRADA 2 phone in europe 6</Title>
           <Content>&lt;p&gt;Seoul, Korea, November 3rd, 2008- &lt;a href=""http://www.lge.com/""&gt;LG Electronics, Inc. (LG)&lt;/a&gt;, a worldwide technology and design leader in mobile communications and Microsoft Corp. (Microsoft), the worldwide leader in software, services and solutions, today announced the signing of Memorandum of Understanding (MoU) to form strategic collaboration in Mobile Convergence.&lt;/p&gt;</Content>
       </Article>
    </News>

     */

    /**
     * <pre>
     * main( String[] args )
     * </pre>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

        HashMap tmp = new HashMap();
        tmp.put("Key", "10");
        tmp.put("Source", "epr");
        tmp.put("SourceURL", "1-specIdaaaa");
        tmp.put("Filename", "1-specName&tra;");
        tmp.put("Display", "1-specName&trade;");
        tmp.put("Title", "1-specName&trade;");
        tmp.put("Content", "1-specName&trade;");
        mData.add(mData.size(), tmp);

        tmp.put("Key", "20");
        tmp.put("Source", "erp");
        tmp.put("SourceURL", "1-specId");
        tmp.put("Filename", "1-specName2&trade;");
        tmp.put("Display", "1-specName2&trade;");
        tmp.put("Title", "1-specName2&trade;");
        tmp.put("Content", "1-specName2&trade;");
        mData.add(mData.size(), tmp);

        HashMap input = new HashMap();

        input.put("CHARSET", "utf-8");
        input.put("fileNm", "C:/TZ_WEB/workspace/aaa.xml");
        input.put("DTDNAME", "users.dtd");

        input.put("root", "News");
        input.put("level1", "Article");
        input.put("userId", "test userId");
        XmlUtil.MultiToXml2(input, mData);
    }

    /**
     * <PRE>
     * xml파일을 dom방식으로 읽는 모듈
     * </PRE>
     * @param filename
     * @param validating
     * @return
     */
    public static Document parseXmlFile(String filename, boolean validating){
        try{
            // Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validating);

            // Create the builder and parse the file
            Document doc = factory.newDocumentBuilder().parse(new File(filename));
            return doc;
        }catch(SAXException e){
            logger.error(e.getMessage());
            // A parsing error occurred; the xml input is not valid
        }catch(ParserConfigurationException e){
            logger.error(e.getMessage());
        }catch(IOException e){
            logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * <PRE>
     * doc를 새로운 xml에 적용
     * </PRE>
     * @param doc
     * @param filename
     */
    public static void writeXmlFile(Document doc, String filename){
        try{
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);

            // Prepare the output file
            File file = new File(filename);
            Result result = new StreamResult(file);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        }catch(TransformerConfigurationException e){
            logger.error(e.getMessage());
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
    }

    /**
     * <PRE>
     * xml Document생성
     * </PRE>
     * @return
     */
    public static Document createDomDocument(){
        try{
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //builder.setNamespaceAware(true);
            Document doc = builder.newDocument();
            return doc;
        }catch(ParserConfigurationException e){
        }
        return null;
    }

    /**
     * <PRE>
     * List<Map<String, Object>>를 xml파일로 변환
     * </PRE>
     * @param inputData
     * @param mData
     */
    public static void MultiToXml2(Map<String, Object> inputData, List<Map<String, Object>> mData){
        try{
            String strRoot = inputData.get("root").toString();
            String level1 = inputData.get("level1").toString(); //root아래의 element명칭
            Document doc = createDomDocument();
            Element element = doc.createElement(strRoot);
            doc.appendChild(element);

            // root node 아래에 Creator추가...
            if(inputData.get("userId") != null){
                Element element2 = doc.createElement("Creator");
                element.insertBefore(element2, element.getNextSibling());
                Text text2 = doc.createTextNode(inputData.get("userId").toString());
                element2.appendChild(text2);
            }

            for(int i = 0; i < mData.size(); i++){
                HashMap data = (HashMap)mData.get(i);
                int nCnt = 0;
                Set dataKeySet = data.keySet();
                Iterator dataIterator = dataKeySet.iterator();

                Element element0 = doc.createElement(level1);
                element.insertBefore(element0, element.getNextSibling());
                while(dataIterator.hasNext()){
                    String dataKey = dataIterator.next().toString(); //LMdata의 key
                    String eleName = dataKey.substring(0, 1).toUpperCase() + dataKey.substring(1, dataKey.length());
                    String dataValue = data.get(dataKey).toString(); //LMData의 value
                    //logger.debug("dataKey="+dataKey+"    "+"dataValue="+dataKey);
                    //생성중인 xml doc에서 root element의 마지막 child 추출....
                    Element element1 = doc.createElement(eleName);
                    element0.insertBefore(element1, element0.getNextSibling());
                    //Text text0 = doc.createTextNode( dataValue );
                    CDATASection cdata3 = doc.createCDATASection(dataValue);
                    element1.appendChild(cdata3);
                }
            }

            writeXmlFile(doc, inputData.get("fileNm").toString());
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        //return os;
    }

    /**
     * <pre>
     * MultiToXmlFile( HashMap inputData, List<Map<String, Object>> mData )
     * </pre>
     * @param inputData
     * @param mData
     */
    public static void MultiToXmlFile(Map<String, Object> inputData, List<Map<String, Object>> mData){
        Document doc = MultiToXml(inputData, mData);
        writeXmlFile(doc, inputData.get("fileNm").toString());
    }

    /**
     * <pre>
     * MultiToXmlStr( HashMap inputData, List<Map<String, Object>> mData )
     * </pre>
     * @param inputData
     * @param mData
     */
    public static String MultiToXmlStr(Map<String, Object> inputData, List<Map<String, Object>> mData){
        String xmlString = "";
        try{
            Document doc = MultiToXml(inputData, mData);
            TransformerFactory transfactory = TransformerFactory.newInstance();
            Transformer transformer = transfactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Source source = new DOMSource(doc);
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            transformer.transform(source, result);
            xmlString = sw.toString();
        }catch(TransformerConfigurationException e){
            logger.error(e.getMessage());
        }catch(TransformerException e){
            logger.error(e.getMessage());
        }
        return xmlString;
    }

    /**
     * <pre>
     * MultiToXml( HashMap inputData, List<Map<String, Object>> mData )
     * </pre>
     * @param inputData
     * @param mData
     * @return
     */
    public static Document MultiToXml(Map<String, Object> inputData, List<Map<String, Object>> mData){
        Document doc = createDomDocument();
        try{
            String strRoot = StringUtil.getText(inputData.get("root"));
            String strNodeId = StringUtil.getText(inputData.get("nodeId"));
            if(strNodeId.equals("")) strNodeId = "node";
            String strGroup = "";
            Element element = doc.createElement(strRoot);
            doc.appendChild(element);

            Element element0 = null;
            for(int i = 0; i < mData.size(); i++){
                HashMap data = (HashMap)mData.get(i);

                int nCnt = 0;
                Set dataKeySet = data.keySet();
                Iterator dataIterator = dataKeySet.iterator();

                while(dataIterator.hasNext()){
                    String dataKey = dataIterator.next().toString();
                    String dataValue = data.get(dataKey).toString();
                    //dataValue = StringUtil.java2html(dataValue);    // html2java
                    strGroup = dataKey;
                    if(nCnt == 0){
                        //Root 다음 노드...
                        element0 = doc.createElement(strNodeId + Integer.toString(nCnt));
//                        element0.setAttribute("id", Integer.toString(nCnt)); //id생성
                        element.insertBefore(element0, element.getNextSibling());
                        
                        element0 = (Element)element.getLastChild();
                        //logger.debug(element0.getAttribute("id"));
                        Element element1 = doc.createElement(strGroup);
                        element0.insertBefore(element1, element0.getNextSibling());
                        CDATASection cdata3 = doc.createCDATASection(dataValue);
                        element1.appendChild(cdata3);
                    }else{
                        //생성중인 xml doc에서 root element의 마지막 child 추출....
                        element0 = (Element)element.getLastChild();
                        //logger.debug(element0.getAttribute("id"));
                        Element element1 = doc.createElement(strGroup);
                        element0.insertBefore(element1, element0.getNextSibling());
                        CDATASection cdata3 = doc.createCDATASection(dataValue);
                        element1.appendChild(cdata3);
                    }
                    nCnt++;
                }
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return doc;
    }

    /**
     * <PRE>
     * 파일로 만들지 않고 Stream으로 생성
     * xml을 파일로 만들지 않고 Stream으로 생성해서 servlet에서 write
     * </PRE>
     * @param doc
     * @param w
     * @param multiLine
     * @throws IOException
     */
    public static void writeXml(Document doc, OutputStream w, boolean multiLine) throws IOException{
        try{
            TransformerFactory tFac = TransformerFactory.newInstance();
            Transformer transformer = tFac.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(w));
        }catch(Exception e){

        }
    }

    // * JAXP INITIALIZATION
    protected static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    protected static final DocumentBuilderFactory vfactory = DocumentBuilderFactory.newInstance();
    static{
        factory.setValidating(false);
        vfactory.setValidating(true);
    }

    /**
     * document builder를 생성한다.
     *
     * @param validating XML validation을 위한 validating flag
     * @return DocumentBuilder object
     * @throws Exception ParserConfigurationException 발생시
     */
    public static DocumentBuilder createBuilder(boolean validating) throws Exception{
        try{
            DocumentBuilder builder = null;

            if(validating)
                builder = vfactory.newDocumentBuilder();
            else
                builder = factory.newDocumentBuilder();

            builder.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicID, String systemID){
                    String home = "C:/tz-dev/workspace/com.tz.tz.framework/src/main/resources/common";

                    publicID = home + "/conf/" + publicID;

                    if(publicID != null && publicID.endsWith(".dtd")){
                        try{
                            FileInputStream in = new FileInputStream(publicID);
                            if(in != null){
                                return new InputSource(in);
                            }
                        }catch(FileNotFoundException e){
                            throw new InternalError(e.getMessage());
                        }
                    }
                    return null;
                }
            });

            return builder;
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return null; // unreachable code. compile error 방지용.
    }

    /**
     * empty document를 생성한다.
     *
     * @return Document -생성된 document
     * @throws Exception
     */
    public static Document createEmptyDocument() throws Exception{
        try{
            return createBuilder(false).newDocument();
        }catch(Exception e){
            throw e;
        }

    }

    /**
     * XML 파일을 parse하고 org.w3c.dom.Document 그룹를 반환한다.
     *
     * @param file XML file
     * @param validating XML validation을 위한 flag
     * @return Document object
     * @throws Exception
     */
    public static Document parse(File file, boolean validating) throws Exception{
        try{
            return createBuilder(validating).parse(file);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return null; // unreachable code. compile error 방지용.
    }

    /**
     * XML input stream을 parse하고 org.w3c.dom.Document 그룹를 반환한다.
     *
     * @param in XML input stream
     * @param validating XML validation을 위한 flag
     * @return Document object
     * @throws Exception
     */
    public static Document parse(InputStream in, boolean validating) throws Exception{
        try{
            return createBuilder(validating).parse(in);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return null; // unreachable code. compile error 방지용.
    }
}
