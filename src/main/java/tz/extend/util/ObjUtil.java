package tz.extend.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tz.extend.iam.UserInfo;

/**
 *
 */
public class ObjUtil {

	/**
	 * <pre>
	 * Object data가 null인지 여부 확인
	 * </pre>
	 *
	 * @param data
	 * @return
	 */
	public static boolean isNull(Object data) {
		if (data == null)
			return true;
		else {
			if (data.toString().equals("")) {
				return true;
			}
		}
		return false;
	}

    /**
     * Deep Copy된 객체를 반환한다.
     *
     * @param oldObj Object 카피할 Object
     * @return Object DeepCopy된 Object
     * @throws Exception
     */
    public static Object deepClone(Object oldObj) {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);

            oos.writeObject(oldObj);
            oos.flush();
            ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);

            // 새로운 Object를 반환한다.
            return ois.readObject();
        }catch(Exception e){
            System.out.println(e.getMessage());
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return null;
    }

	/**
	 * <pre>
	 * 일식 여부 리턴
	 * </pre>
	 *
	 * @param pUnt
	 * @return
	 */
	public static String isSik(String pUnt) {
		if (ObjUtil.isNull(pUnt)) {
			return "N";
		}
		if (pUnt.equals("L/S") || pUnt.equals("식")) {
			return "Y";
		} else {
			return "N";
		}
	}

    /**
     * <pre>
     * Site SITE SQL 리턴
     * </pre>
     *
     * @param
     * @return SQL
     */
    public static String getSiteSql() {
        String qrylang = StringUtil.getText(UserInfo.getUserInfo().getSiteWhere());
        qrylang = qrylang.replace("[", "").replace("]", "");
        return qrylang;
    }
    /**
     * <pre>
     * dept DEPT SQL 리턴
     * </pre>
     *
     * @param
     * @return SQL
     */
    public static String getDeptSql() {
        String qrylang = StringUtil.getText(UserInfo.getUserInfo().getDeptWhere());
        qrylang = qrylang.replace("[", "").replace("]", "");
        return qrylang;
    }
}
