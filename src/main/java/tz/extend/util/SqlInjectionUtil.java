package tz.extend.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import tz.extend.util.StringUtil;
import tz.extend.core.exception.BizException;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : SqlInjectionUtil
 * 설    명 : 데이터베이스 조회시 입력 파라미터의 sql injection 을 막기위해
 *           파라미터 값 필터 하여 exception 리턴
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
public class SqlInjectionUtil {

    public static void checkInjectionString(Object parameter){
        //final String regex = ".*(--|%[a-zA-z_0-9\\w\\W]|[a-zA-z_0-9\\w\\W]%|^%|%$).*";
    	final String regex = ".*(--).*";
    	return;
/*        if(parameter == null)
            return;
        try{
            if(parameter instanceof java.util.HashMap){
                String paramVal = "";
                Set<?> dataKeySet = ((HashMap<?, ?>)parameter).keySet();
                Iterator<?> dataIterator = dataKeySet.iterator();
                while(dataIterator.hasNext()){
                    String dataKey = dataIterator.next().toString();
                    try{
                        paramVal = StringUtil.getText((((HashMap<?, ?>)parameter)).get(dataKey));
                    }catch(Exception castEx){
                        continue;
                    }
                    if(paramVal.matches(regex)){
                        throw new BizException("ad.err.illegalInputString");
                    }
                }
            }else if(parameter instanceof String){
                if(parameter.toString().matches(regex)){
                    throw new BizException("ad.err.illegalInputString");
                }
            }else{
                BeanInfo beanInfo = Introspector.getBeanInfo(parameter.getClass());
                PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
                String objVal = "";
                for(PropertyDescriptor prop : props){
                    if(prop.getName().equals("class")){
                        continue;
                    }
                    Object obj = prop.getReadMethod().invoke(parameter, (Object[])null);
                    if(obj instanceof String){
                        objVal = (String)prop.getReadMethod().invoke(parameter, (Object[])null);
                        if(objVal.matches(regex)){
                            throw new BizException("ad.err.illegalInputString");
                        }
                    }
                }
            }
        }catch(Exception e){
            throw new BizException(e.getMessage());
        }*/
    }
}
