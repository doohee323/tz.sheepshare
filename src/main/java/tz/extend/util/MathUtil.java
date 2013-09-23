package tz.extend.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;


/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MathUtil
 * 설    명 : Number의 조작을 위한  Utility Class
 *       1. double 값 처리
 *          double d = 123.12356;
 *         - 반올림 처리
 *         - 소수점 3째 자리 이하 사사오입
 *         logger.debug(JFMath.roundHalfUp(d,3));
 *         - 소수점 3째 자리 이하 절사
 *         logger.debug(JFMath.roundDown(d,3));
 *         - 소수점 이하 버림
 *         logger.debug(JFMath.roundDown(d,0));
 *         - 실행 결과
 *           123.124
 *           123.123
 *           123.0
 * 작 성 자 : TZ
 * 작성일자 : 2013. 10. 27.
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class MathUtil {


    /**
     * <pre>
     * value 값을 scale에 맞게 내림 적용한다.
     * </pre>
     * @param value  대상값
     * @param scale  내림 기준 scale
     */
    static public double roundDown(double value, int scale) {
        return roundDown(new Double(value), scale).doubleValue();
    }

    /**
     * <pre>
     * value 값을 scale에 맞게 내림 적용한다.
     * </pre>
     * @param value  대상값
     * @param scale  내림 기준 scale
     */
    static public Double roundDown(Double value, int scale) {
    	/*
    	Double은 바로 BigDecimal의 생성자의 파라미터로 사용하면 소수점이하의 변형이 발생하여
    	String으로 변환한 후에 생성자의 파라미터로 사용한다.
    	*/
        BigDecimal b = new BigDecimal(toString(value));
        return new Double(b.setScale(scale, BigDecimal.ROUND_DOWN).toString());
    }

    /**
     * <pre>
     * value 값을 scale에 맞게 반올림 적용한다.
     * </pre>
     * @param value  대상값
     * @param scale  반올림 기준 scale
     */
    static public double roundHalfUp(double value, int scale) {
        return roundHalfUp(new Double(value), scale).doubleValue();
    }

    /**
     * <pre>
     * value 값을 scale에 맞게 반올림 적용한다.
     * </pre>
     * @param value  대상값
     * @param scale  반올림 기준 scale
     */
    static public Double roundHalfUp(Double value, int scale) {
        BigDecimal b = new BigDecimal(toString(value));
        return new Double(b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString());
    }

    /**
     * <pre>
     * value 값을 scale에 맞게 올림 적용한다.
     * </pre>
     * @param value  대상값
     * @param scale  올림 기준 scale
     */
    static public double roundUp(double value, int scale) {
        return roundUp(new Double(value), scale).doubleValue();
    }

    /**
     * <pre>
     * value 값을 scale에 맞게 올림 적용한다.
     * </pre>
     * @param value  대상값
     * @param scale  올림 기준 scale
     */
    static public Double roundUp(Double value, int scale) {
        BigDecimal b = new BigDecimal(toString(value));
        return new Double(b.setScale(scale, BigDecimal.ROUND_UP).toString());
    }


    /**
     * <pre>
     * double d 값을 comma Format의 String으로 리턴한다.
     * </pre>
     * @param d  double value
     */
    public static String toString(Double d) {
        DecimalFormat df = new DecimalFormat("#0.0#################");
        return df.format(d.doubleValue());

    }
    /**
     * <pre>
     * 소스금액(value), 환율(rate), 자리수(scale)를 받아서
     * 타겟금액 Return
     * </pre>
     * @param longSrcamt 소스금액
     * @param intRate 환율
     * @param intDigit 자리수
     * @exception   Exception
     * @return 타겟금액
     */
    public static BigDecimal getTrgAmt(BigDecimal value, BigDecimal rate, int scale) {
    	BigDecimal currAmt = value.multiply(rate);

    	return currAmt.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * <pre>
     * GEOLOCATION 거리 계산
     * </pre>
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return Float
     */
    public static float getDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        int meterConversion = 1609;
        return new Float(dist * meterConversion).floatValue();
    }

}
