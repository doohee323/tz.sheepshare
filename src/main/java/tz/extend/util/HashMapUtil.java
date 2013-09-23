package tz.extend.util;


/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 :
 * 설    명 : HashMap Handling 을 위한  Utility Class
 * 작 성 자 : TZ
 * 작성일자 : 2013. 11. 20
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013. 11. 20
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class HashMapUtil {

    /**
     * log 처리를 위한 변수 선언
     */
  //  private static final Logger logger = LoggerFactory.getLogger(HashMapUtil.class);

    /**
     * <pre>
     * HashMap 2개를  병합하여 리턴한다.
     * </pre>
     * @param header Header 정보
     * @return HashMap Raw 정
     */
//    public static Map mergeValue(Map source, Map target) throws Exception{
//        try{
//            Set dataKeySet = source.keySet();
//            Iterator dataIterator = dataKeySet.iterator();
//            while(dataIterator.hasNext()){
//                String dataKey = dataIterator.next().toString();
//                Object str = target.get(dataKey);
//                if(str == null || str.toString().equals("")){
//                    if(source.get(dataKey) != null && !source.get(dataKey).toString().equals("")){
//                        target.put(dataKey, source.get(dataKey));
//                    }
//                }
//            }
//        }catch(Exception e){
//            //logger.debug("Error:" + e);
//        }
//        return target;
//    }

    /**
     * <pre>
     * Null 값이 있는 컬럼에 임의 값을 넣어 준다.
     * </pre>
     * @param input
     * @param dummyStr
     * @return input
     * @throws Exception
     */
//    public static Map setDumyValue(Map input, String dummyStr) throws Exception{
//        try{
//            Set dataKeySet = input.keySet();
//            Iterator dataIterator = dataKeySet.iterator();
//            while(dataIterator.hasNext()){
//                String dataKey = dataIterator.next().toString();
//                Object str = input.get(dataKey);
//                if(str == null || str.toString().equals("")){
//                    input.put(dataKey, dummyStr);
//                }
//            }
//        }catch(Exception e){
//            logger.debug("Error:" + e);
//        }
//        return input;
//    }

    /**
     * <pre>
     * HashMap Sorting
     * </pre>
     * @param mData
     * @param aCol
     * @return data
     */
//    public static HashMap getVHashMap(HashMap mData, String aCol){
//        HashMap data = new HashMap();
//        try{
//            ArrayList list = (ArrayList)mData.get(aCol);
//            for(int i = 0; i < list.size(); i++){
//                data.put(list.get(i), null);
//            }
//        }catch(Exception e){
//            logger.debug("Error:" + e);
//        }
//        return data;
//    }
}
