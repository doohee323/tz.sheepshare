package tz.extend.util;

/*
 */
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * String Handling 을 위한  Utility Class
 * </pre>
 */
public class MultiUtil {

    public MultiUtil() {}

    /**
     * List<Map<String, Object>>를 가우스데이터셋으로 변경하기 위한 header 정보와 raw데이터인 data를 병합하여 리턴한다.
     * @param header Header 정보
     * @return List<Map<String, Object>> Raw 정
     */
    public static List<Map<String, Object>> toConvertableData(Map<String, Object> header, List<Map<String, Object>> data){
        List<Map<String, Object>> gdsData = new ArrayList<Map<String, Object>>();
        gdsData = data;
        gdsData.add(header);
        return gdsData;
    }

    public static void fillColValue(List<Map<String, Object>> mData, String column, Object value){
        int getDataCount = mData.size();
        for(int i = 0; i < getDataCount; i++){
            mData.get(i).put(column, value);
        }
    }

//    public static void fillColValue(List<Map<String, Object>> mData, String column, int value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.addInt(column, value);
//        }
//    }
//
//    public static void fillColValue(List<Map<String, Object>> mData, String column, double value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.addDouble(column, value);
//        }
//    }
//
//    public static void fillColValue(List<Map<String, Object>> mData, String column, float value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.addFloat(column, value);
//        }
//    }
//
//    public static void fillColValue(List<Map<String, Object>> mData, String column, short value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.addShort(column, value);
//        }
//    }
//
//    public static void fillColValue(List<Map<String, Object>> mData, String column, long value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.addLong(column, value);
//        }
//    }
//
//    public static void fillColValue(List<Map<String, Object>> mData, String column, BigDecimal value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.addBigDecimal(column, value);
//        }
//    }
//
//    public static void fillColValue(List<Map<String, Object>> mData, String column, boolean value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.addBoolean(column, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, String value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyString(column, i, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, int value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyInt(column, i, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, double value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyDouble(column, i, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, float value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyFloat(column, i, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, short value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyShort(column, i, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, long value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyLong(column, i, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, BigDecimal value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyBigDecimal(column, i, value);
//        }
//    }
//
//    public static void modiColValue(List<Map<String, Object>> mData, String column, boolean value){
//        int getDataCount = mData.size();
//        for(int i = 0; i < getDataCount; i++){
//            mData.modifyBoolean(column, i, value);
//        }
//    }

//    public static GauceOutputStream setFragment(GauceDS gds, HttpServletResponse res){
//        String useYn = "";
//        try{
//            useYn = LConfiguration.getGroup().getString("/devon/project<tz>/gauceFragment/useYn", "");
//        }catch(LConfigurationException e){
//            e.printStackTrace();
//        }
//        GauceOutputStream os = null;
//        if(useYn.equals("true")){
//            try{
//                os = ((HttpGauceResponse)res).getGauceOutputStream();
//                os.fragment(gds, 10);
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }
//        return os;
//    }
//
//    public static List<Map<String, Object>> mappingHeader(Map<String, Object> header, List<Map<String, Object>> mData){
////        Map<String, Object> output = new HashMap<String, Object>();
////        Map<String, Object> input = mData.getMap<String, Object>(0);
////        Iterator keys = input.keySet().iterator();
////        while(keys.hasNext()){
////            String key = (String)keys.next();
////            output.setString(key, header.getString(key));
////        }
//        mData = LGauceConverter.prepareToParam(header, mData);
//        return mData;
//    }
//
//    public static List<Map<String, Object>> mappingHeader(Map<String, Object> header, List<Map<String, Object>> mData, DefaultNaming naming){
//        Map<String, Object> output = new HashMap<String, Object>();
//        Map<String, Object> input = mData.get(0);
//        Iterator keys = input.keySet().iterator();
//        while(keys.hasNext()){
//            String key = (String)keys.next();
//            key = key.toLowerCase();
//            key = naming.getAttributeName(key);
//            output.put(key, header.get(key));
//        }
//        mData = LGauceConverter.prepareToParam(output, mData);
//        return mData;
//    }
//
//    public static void flush(GauceDS gds, GauceOutputStream os){
//        String useYn = "";
//        try{
//            useYn = LConfiguration.getGroup().getString("/devon/project<tz>/gauceFragment/useYn", "");
//        }catch(LConfigurationException e){
//            e.printStackTrace();
//        }
//        if(useYn.equals("true")){
//            try{
//                os.write(gds);
//                os.close();
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }else{
//            gds.flush();
//        }
//    }

    public static Map<String, Object> getHeaderFromStr(String headerStr){
        Map<String, Object> inputData = new HashMap<String, Object>();
        inputData.put("header", headerStr);
        return getHeaderFromStr(inputData, "header");
    }
    
    // List<Map<String, Object>>의 header 정보를 생성
    public static Map<String, Object> getHeaderFromStr(Map<String, Object> inputData, String inputHeaderCol){
        //      String v_header = "num:STRING(5)," + 
        //      "yyyy:STRING(4)," + 
        //      "mm:STRING(2)," + 
        //      "value:DECIMAL(20,2)," + 
        //      "etc:STRING(100)";
        String headerStr = StringUtil.getText(inputData.get(inputHeaderCol));

        Map<String, Object> header = new HashMap<String, Object>();
        if(headerStr.indexOf(",") < 0) return header;
        
        String arryHeader[] = headerStr.split(",");
        for(int i = 0; i < arryHeader.length; i++){
            String col = arryHeader[i];
            String colNm = col.substring(0, col.indexOf(":"));
            String colType = col.substring(col.indexOf(":") + 1, col.indexOf("("));

            int ncolType = 0;
            if(colType.equals("STRING")){
                ncolType = Types.VARCHAR;
            }else if(colType.equals("INT")){
                ncolType = Types.INTEGER;
            }else if(colType.equals("DECIMAL")){
                ncolType = Types.DECIMAL;
            }

            String colSize = col.substring(col.indexOf("(") + 1, col.indexOf(")"));
            if(colSize.indexOf(".") > 0) {
                colSize = colSize.substring(0, colSize.indexOf(".")) + "|" + colSize.substring(colSize.indexOf(".") + 1, colSize.length());
            } else {
                colSize = colSize + "|0";
            }
            colSize = String.valueOf(ncolType) + "|" + colSize;
            header.put(colNm, colSize);
        }
        return header;
    }
    
//    // List<Map<String, Object>>의 header 정보를 변경
//    public static List<Map<String, Object>> changeLMultiHeader(List<Map<String, Object>> mData, Map<String, Object> inputData){
//        
//    	Map<String, Object> header = LGauceConverter.extractHeaderParam(mData);
//        
//        Set dataKeySet = inputData.keySet();
//        Iterator dataIterator = dataKeySet.iterator();
//        while(dataIterator.hasNext()){
//            String dataKey = dataIterator.next().toString();
//            String colType = inputData.getString(dataKey);
//            String colSize = colType.substring(colType.indexOf("(") + 1, colType.indexOf(")"));
//            if(colSize.indexOf(".") > 0) {
//                colSize = colSize.substring(0, colSize.indexOf(".")) + "|" + colSize.substring(colSize.indexOf(".") + 1, colSize.length());
//            } else {
//                colSize = colSize + "|0";
//            }
//            colType = colType.substring(0, colType.indexOf("("));
//            
//            int ncolType = 0;
//            if(colType.equals("STRING")){
//                ncolType = Types.VARCHAR;
//            }else if(colType.equals("INT")){
//                ncolType = Types.INTEGER;
//            }else if(colType.equals("DECIMAL")){
//                ncolType = Types.DECIMAL;
//            }
//            header.setMetaString(dataKey, String.valueOf(ncolType) + "|" + colSize);
//        }
//        
//        return (List<Map<String, Object>>)LGauceConverter.prepareToParam(header, mData);
//    }
//
//    // List<Map<String, Object>>의 header 정보를 변경
//    public static List<Map<String, Object>> changeLMultiHeader(List<Map<String, Object>> mData, String v_header){
//        Map<String, Object> inputData = new HashMap<String, Object>();
//        
//        String cols[] = v_header.split(",");
//        for(int i = 0; i < cols.length; i++){
//            String col[] = cols[i].split(":");
//            inputData.setString(col[0], col[1]);
//        }
//        return changeLMultiHeader(mData, inputData);
//    }
    
}


