package tz.extend.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : GoogleUtil
 * 설      명 : Google 날씨 정보 연동을 위한 Utility Class
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
public class GoogleUtil {

    public static void main(String[] args){
        Map<String, Object> map = getWeather("Seoul");

        String city = ((Map<String, Object>)map.get("forecast_information")).get("city").toString();
        System.out.println("city : " + city);
        String forecast_date = ((Map<String, Object>)map.get("forecast_information")).get("forecast_date").toString();
        System.out.println("forecast_date : " + forecast_date);

        String condition = ((Map<String, Object>)map.get("current_conditions")).get("condition").toString();
        System.out.println("condition : " + condition);
        String temp_c = ((Map<String, Object>)map.get("current_conditions")).get("temp_c").toString();
        System.out.println("temp_c : " + temp_c);

        List<Map<String, Object>> foreList = (List<Map<String, Object>>)map.get("forecast_conditions");
        for(int i = 0; i < foreList.size(); i++){
            Map<String, Object> forecast = (Map<String, Object>)foreList.get(i);
            System.out.println("day_of_week : " + forecast.get("day_of_week"));
            System.out.println("day_of_week : " + forecast.get("high"));
            System.out.println("-----------------------");
        }
    }

    /**
     * Google 날씨 정보를 얻어온다.
     * Google 날씨 정보 Webservice 호출
     * @param city
     * @return
     */
    public static Map<String, Object> getWeather(String city){
        Map<String, Object> map = new HashMap<String, Object>();
        try{
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder parser = f.newDocumentBuilder();
            String url = "http://www.google.co.kr/ig/api?weather=" + city;
            URL u = new URL(url);
            InputStreamReader is = new InputStreamReader(u.openStream(), "euc-kr");
            Document xmlDoc = null;
            //DOM 파서로부터 입력받은 파일을 파싱하도록 요청
            xmlDoc = parser.parse(new InputSource(is));
            NodeList children = XPathAPI.selectNodeList(xmlDoc, "xml_api_reply/weather");
            for(int i = 0; i < children.getLength(); i++){
                Element statement = (Element)children.item(i);
                NodeList cList = statement.getChildNodes();
                List<Map<String, Object>> foreList = new ArrayList<Map<String, Object>>();
                for(int j = 0; j < cList.getLength(); j++){
                    String group = cList.item(j).getNodeName();
                    if(cList.item(j).getFirstChild() != null){
                        Map<String, Object> input = new HashMap<String, Object>();
                        String code = "";
                        String value = "";
                        NodeList cList2 = cList.item(j).getChildNodes();
                        for(int k = 0; k < cList2.getLength(); k++){
                            code = cList2.item(k).getNodeName();
                            value = cList2.item(k).getAttributes().item(0).getNodeValue();
                            input.put(code, value);
                        }
                        if(group.equals("forecast_conditions")){
                            foreList.add(foreList.size(), input);
                        }else{
                            map.put(group, input);
                        }
                    }
                }
                map.put("forecast_conditions", foreList);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }

}