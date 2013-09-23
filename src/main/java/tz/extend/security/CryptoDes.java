package tz.extend.security;

import java.security.Key;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : CryptoDes
 * 설      명 : DES 알고리즘 처리를 위한 Utility Class
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
public class CryptoDes {

    public static void main(String[] args) throws Exception {
        String text = "0400490";

        String enen = encrypt(text);
        String dede = decrypt(enen);

        System.out.println("\nOrigin key: " + key());
        System.out.println("\nOrigin text: " + text);
        System.out.println("\nEncrypted text: " + enen);
        System.out.println("\nDecrypted text: " + dede);
    }

    /**
     * 고정키 정보
     * @return
     */
    public static String key() {
        return "4C49314757423EF4B9E28865";
    }

    /**
     * 키값
     * 24바이트인 경우 TripleDES 아니면 DES
     * @return
     * @throws Exception
     */
    public static Key getKey() throws Exception {
        return (key().length() == 24) ? getKey2(key()) : getKey1(key());
    }

    /**
     * 지정된 비밀키를 가지고 오는 메서드 (DES)
     * require Key Size : 16 bytes
     *
     * @return Key 비밀키 클래스
     * @exception Exception
     */
    public static Key getKey1(String keyValue) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(keyValue.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }

    /**
     * 지정된 비밀키를 가지고 오는 메서드 (TripleDES)
     * require Key Size : 24 bytes
     * @return
     * @throws Exception
     */
    public static Key getKey2(String keyValue) throws Exception {
        DESedeKeySpec desKeySpec = new DESedeKeySpec(keyValue.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }

    /**
     * 문자열 대칭 암호화
     *
     * @param ID
     *            비밀키 암호화를 희망하는 문자열
     * @return String 암호화된 ID
     * @exception Exception
     */
    public static String encrypt(String ID) throws Exception {
        if (ID == null || ID.length() == 0)
            return "";

        String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getKey());
        String amalgam = ID;

        byte[] inputBytes1 = amalgam.getBytes("UTF8");
        byte[] outputBytes1 = cipher.doFinal(inputBytes1);
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String outputStr1 = encoder.encode(outputBytes1);
        return outputStr1;
    }

    /**
     * 문자열 대칭 복호화
     *
     * @param codedID
     *            비밀키 복호화를 희망하는 문자열
     * @return String 복호화된 ID
     * @exception Exception
     */
    public static String decrypt(String codedID) throws Exception {
        if (codedID == null || codedID.length() == 0)
            return "";

        String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getKey());
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

        byte[] inputBytes1 = decoder.decodeBuffer(codedID);
        byte[] outputBytes2 = cipher.doFinal(inputBytes1);

        String strResult = new String(outputBytes2, "UTF8");
        return strResult;
    }

}