package tz.extend.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : FormatterCache
 * 설      명 : Formatter 처리를 위한 Uiility Class
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
public class FormatterCache {

    static final int MAXFORMATCOUNT = 100;
    static final int REMOVECOUNTONFULL = 10;

    static Hashtable htDecimalFormat;
    static ArrayList alDecimalFormat;
    static int decimalFormatCount;

    static Hashtable htDateFormat;
    static ArrayList alDateFormat;
    static int dateFormatCount;

    static {
        htDecimalFormat = new Hashtable();
        alDecimalFormat = new ArrayList();
        htDateFormat = new Hashtable();
        alDateFormat = new ArrayList();

        decimalFormatCount = dateFormatCount=0;
    }

    /**
     * ���信 ���� DecimalFormat ��ȯ
     * @param format ����
     * @return DecimalFormat
     */
    public static DecimalFormat getDecimalFormat( String format )
    {
        DecimalFormat fmt = (DecimalFormat)htDecimalFormat.get(format);
        if(fmt==null) {
            fmt = new DecimalFormat(format);
            htDecimalFormat.put(format, fmt);
            alDecimalFormat.add(format);
            decimalFormatCount++;
            if(decimalFormatCount>=MAXFORMATCOUNT) {
                for(int i=0;i<REMOVECOUNTONFULL;i++) {
                    htDecimalFormat.remove(alDecimalFormat.remove(0));
                    decimalFormatCount--;
                }
            }
        }
        return fmt;
    }

    /**
     * ���˿� ���� SimpleDateFormat ��ȯ
     * @param format ����
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getDateFormat( String format )
    {
        SimpleDateFormat fmt = (SimpleDateFormat)htDateFormat.get(format);
        if(fmt==null) {
            fmt = new SimpleDateFormat(format);
            htDateFormat.put(format, fmt);
            alDateFormat.add(format);
            dateFormatCount++;
            if(dateFormatCount>=MAXFORMATCOUNT) {
                for(int i=0;i<REMOVECOUNTONFULL;i++) {
                    htDateFormat.remove(alDateFormat.remove(0));
                    dateFormatCount--;
                }
            }
        }
        return fmt;
    }
}
