package tz.basis.integration.excel;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : POIConstants
 * 설    명 : POI 컴포넌트에 필요한 공통 코드를 관리한다.
 * 작 성 자 :
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

public class POIConstants {

	public final static String EXCEL = "EXCEL";

	public final static String CSV = "CSV";

	/**
	 * @deprecated
	 */
	public final static String ALLFILE = "ALL";

	public final static int TYPE_EXCEL = 0;

	public final static int TYPE_CSV = 1;

	public final static String EXTENSION_EXCEL = "xls";

	public final static String EXTENSION_CSV = "csv";

	public final static String EXTENSION_EXCEL_WITH_DOT = "." + EXTENSION_EXCEL;

	public final static String EXTENSION_CSV_WITH_DOT = "." + EXTENSION_CSV;

//	public final static String DEFAULT_DERECTORY = FileUtil.UPLOAD_PATH + "/";

	public final static String DEFAULT_FILE_NAME = "newFile";

	public final static String BackUpFileDateFormat = "yyyyMMddHHmmssSSS";

	public final static short FONT_WEIGHT_BOLD = HSSFFont.BOLDWEIGHT_BOLD;

	public final static short FONT_WEIGHT_NORMAL = HSSFFont.BOLDWEIGHT_NORMAL;

	public final static short FONT_COLOR_RED = HSSFColor.RED.index;

	public final static short FONT_COLOR_BLUE = HSSFColor.BLUE.index;

	public final static short FONT_COLOR_BLACK = HSSFColor.BLACK.index;

	public final static short FONT_COLOR_LIGHT_TURQUOISE = HSSFColor.LIGHT_TURQUOISE.index;

	public final static String DEFAULT_FONT_NAME = "Courier New";

	public static final short DEFAULT_FONT_HEIGHT = 12;

	public static final String ENCODING = "UTF-8";

	public static final String READ_CONFIG_PREFIX = "/tz/framework/excel/read/";

	public static final String WRITE_CONFIG_PREFIX = "/tz/framework/excel/write/";
//
//	/**
//	 * <pre>
//	 * default batch size를 가져오는 메소드
//	 * </pre>
//	 * @param extension
//	 * @return
//	 */
//	public static final int DEFAULT_BATCHSIZE() throws Exception {
//		int defulat_batchSize = 1000;
//		return Configuration.getGroup().getInt(READ_CONFIG_PREFIX + "batch-size", defulat_batchSize);
//	}
//
//	/**
//	 * <pre>
//	 * default write File의 Path를 가져오는 메소드
//	 * </pre>
//	 * @param extension
//	 * @return
//	 */
//	public static final String DEFAULT_WRITE_FILE_PATH(String extension) throws Exception {
//		Configuration conf = Configuration.getGroup();
//		String write_directory = FileUtil.getCompleteLeadingSeperator(conf.getString(WRITE_CONFIG_PREFIX + "directory", Constants.uploadDefault + "/"));
//		String fileName = conf.getString(WRITE_CONFIG_PREFIX + "file-name", DEFAULT_FILE_NAME);
//		return write_directory + fileName + extension;
//	}
//
//	/**
//	 * <pre>
//	 * default read File의 Path를 가져오는 메소드
//	 * </pre>
//	 * @param extension
//	 * @return
//	 */
//	public static final String DEFAULT_READ_FILE_PATH(String extension) throws Exception {
//		Configuration conf = Configuration.getGroup();
//		String read_directory = FileUtil.getCompleteLeadingSeperator(conf.getString(READ_CONFIG_PREFIX + "directory", Constants.uploadDefault + "/"));
//		String fileName = conf.getString(READ_CONFIG_PREFIX + "file-name", DEFAULT_FILE_NAME);
//		return read_directory + fileName + extension;
//	}
//
//	/**
//	 * @deprecated
//	 */
//	public final static String NEWEXCELFILEPATH = Configuration.gettzHome() + "/poi/newExcelFile.xls";
//
//	/**
//	 * @deprecated
//	 */
//	public final static String NEWCSVFILEPATH = Configuration.gettzHome() + "/poi/newCSVFile.csv";

	/**
	 * @deprecated
	 */
	public final static int TypeLData = 0;

	/**
	 * @deprecated
	 */
	public final static int TypeLMultiData = 1;

	/**
	 * @deprecated
	 */
	public final static int TypeList = 2;



}



