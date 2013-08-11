package tz.basis.integration.excel;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : ExcelWriter
 * 설    명 : 엑셀파일을 생성하는 Writer 클래스
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import tz.basis.data.GridData;
import tz.extend.util.FileUtil;
import tz.extend.util.ObjUtil;
import tz.extend.core.mvc.context.WebContext;

public class ExcelWriter {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(ExcelWriter.class);

    private HSSFWorkbook workbook = new HSSFWorkbook();;

    private static HSSFSheet sheet = null;

    private String sheetName;

    private HSSFCellStyle titleCellStyle;

    private HSSFCellStyle valueCellStyle;

    private HashMap titleCellStyles = new HashMap();

    private HashMap valueCellStyles = new HashMap();

    private String[] titles = null;

    private String encoding = POIConstants.ENCODING;

    // private int[] cellWidth = null;

    /**
     * <pre>
     * 엑셀 파일을 만드는 실질적인 역할을 하는 메소드
     * </pre>
     * @return
     * @throws Exception
     */
    public File execute() throws Exception {
        return execute(null, false);
    }

    /**
     * <pre>
     * 엑셀 파일을 만드는 실질적인 역할을 하는 메소드
     * </pre>
     * @param newExcelFilePath
     * @return
     * @throws Exception
     */
    public File execute(String newExcelFilePath) throws Exception {
        return execute(newExcelFilePath, false);
    }

    /**
     * <pre>
     * 엑셀 파일을 만드는 실질적인 역할을 하는 메소드
     * </pre>
     * @param inputData
     * @param outputFile
     * @param overwrite
     * @throws Exception
     */
    public File execute(String newExcelFilePath, boolean overwrite) throws Exception {
        // 만약 만들려는 파일이 xls로 끝나지 않으면 에러를 발생한다.
        if (ObjUtil.isNull(newExcelFilePath)) {
            // newExcelFilePath = POIConstants.NEWEXCELFILEPATH;
            newExcelFilePath = "";//POIConstants.DEFAULT_WRITE_FILE_PATH(POIConstants.EXTENSION_EXCEL_WITH_DOT);
        }
        File outputFile = new File(newExcelFilePath);
        if (!outputFile.getName().toLowerCase().endsWith(POIConstants.EXTENSION_EXCEL_WITH_DOT)) {
            throw new Exception("execute(File outputFile, boolean overwrite)");
        }

        File resultFile = null;
        if (!overwrite && outputFile.exists()) {
            resultFile = FileUtil.getUnOverwriteFile(outputFile, POIConstants.BackUpFileDateFormat);
        } else {
            resultFile = new File(outputFile.getAbsolutePath());
        }

        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(resultFile);
            workbook.write(fs);
        } catch (FileNotFoundException fnfe) {
            throw new Exception(fnfe);
        } catch (IOException ioe) {
            throw new Exception(ioe);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return resultFile;
    }

    public void toStream() throws Exception {
        toStream("");
    }

    /**
     * <pre>
     * toStream(String fileName)
     * </pre>
     * @param String fileName
     * @throws Exception
     */
    public void toStream(String fileName) throws Exception {

        if(ObjUtil.isNull(fileName)){
            fileName = POIConstants.DEFAULT_FILE_NAME + POIConstants.EXTENSION_EXCEL_WITH_DOT;
        }else if(!fileName.endsWith(POIConstants.EXTENSION_EXCEL_WITH_DOT)){
            fileName = fileName + POIConstants.EXTENSION_EXCEL_WITH_DOT;
        }
        try {
            HttpServletResponse response = WebContext.getResponse();
            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setContentType("text/file;charset=EUC-KR");

            HttpServletRequest request = WebContext.getRequest();
            String sUserAgent = request.getHeader("User-Agent");
            if (sUserAgent.indexOf("MSIE 5.5") != -1) {
                response.setHeader("Content-Disposition", "filename=\"" + this.encoding + "\"" + encodeURLEncoding(fileName) + "\";");
            } else {
                boolean isFirefox = (sUserAgent.toLowerCase().indexOf("firefox") != -1) ? true : false;
                if (isFirefox) {
                    response.setHeader("Content-Disposition", "attachment; filename=" + "\"=?" + this.encoding + "?Q?" + encodeQuotedPrintable(fileName) + "?=\";");
                } else {
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + encodeURLEncoding(fileName).replaceAll("\\+", " ") + "\"");
                }
            }
            response.setHeader("Content-Transfer-Encoding", "binary;");
            response.setHeader("Pragma", "no-cache;");
            response.setHeader("Expires", "-1;");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * <pre>
     * tencodeURLEncoding
     * </pre>
     * @param String p_sStr
     * @throws UnsupportedEncodingException
     */
    private String encodeURLEncoding(String p_sStr) throws UnsupportedEncodingException {
        String filename = p_sStr;
        try {
            filename = java.net.URLEncoder.encode(filename, POIConstants.ENCODING);// this.encoding
            filename = filename.replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            logger.debug("encodeURLEncoding(String p_sStr)" + e);
        }
        return filename;
    }

    /**
     * <pre>
     * encodeQuotedPrintable
     * </pre>
     * @param String p_sStr
     * @throws UnsupportedEncodingException
     */
    private String encodeQuotedPrintable(String p_sStr) throws UnsupportedEncodingException {
        String sUrlEncodingStr = URLEncoder.encode(p_sStr, this.encoding);
        sUrlEncodingStr = sUrlEncodingStr.replaceAll("\\+", "_");
        sUrlEncodingStr = sUrlEncodingStr.replaceAll("%", "=");
        return sUrlEncodingStr;
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @throws Exception
     */
    public void addSheet(HashMap inputData) throws Exception {
        createSheet();
        setCells(inputData);
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @throws Exception
     */
    public void addSheetForMap(List<Map<String, Object>> inputData) throws Exception {
        createSheet();
        setCellsForMap(inputData);
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @throws Exception
     */
    public void addSheet(Object[][] inputData) throws Exception {
        createSheet();
        setCells(inputData);
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @param sheetName
     * @throws Exception
     */
    public void addSheet(String pwd, HashMap inputData, String sheetName) throws Exception {
        setSheetName(sheetName);
        createSheet(pwd);
        setCells(inputData);
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @param sheetName
     * @throws Exception
     */
    public void addSheet(HashMap inputData, String sheetName) throws Exception {
        setSheetName(sheetName);
        createSheet();
        setCells(inputData);
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @param sheetName
     * @throws Exception
     */
    public void addSheetForMap(String pwd, List<Map<String, Object>> inputData, String sheetName) throws Exception {
        setSheetName(sheetName);
        createSheet(pwd);
        setCellsForMap(inputData);
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @param sheetName
     * @throws Exception
     */
    public void addSheetForMap(List<Map<String, Object>> inputData, String sheetName) throws Exception {
        setSheetName(sheetName);
        createSheet();
        setCellsForMap(inputData);
    }

    /**
     * <pre>
     * 각각의 cell에 타이틀과 내용을 채운다.
     * </pre>
     */
    public void addSheetForGrid(GridData<HashMap> inputData, String sheetName) {
        setSheetName(sheetName);
        createSheet();
        // title cell 내용 삽입
        String[] keyStrs = getTitles(inputData.get(0).keySet());
        String[] titleStrs = (ObjUtil.isNull(titles)) ? keyStrs : titles;
        createTitles(titleStrs);

        // cell 내용 삽입
        final int keysize = titleStrs.length;
        final int rowsize = inputData.size();
        for (int inx = 0; inx < rowsize; inx++) {
            HSSFRow valuerow = sheet.createRow(inx + 1);
            for (int iny = 0; iny < keysize; iny++) {
                HSSFCell cell = valuerow.createCell((short)iny);
                setCellValue(cell, inputData.get(inx).get(keyStrs[iny]).toString());
                if(valueCellStyles.get(keyStrs[iny]) != null ) {
                    cell.setCellStyle((HSSFCellStyle)valueCellStyles.get(keyStrs[iny]));
                } else {
                    cell.setCellStyle(createValueCellStyle());
                }
            }
        }
        autoCellSize(keysize);
    }

    /**
     * <pre>
     * Sheet를 만들어 cell에 데이터의 내용을 삽입한다.
     * </pre>
     * @param inputData
     * @param sheetName
     * @throws Exception
     */
    public void addSheet(Object[][] inputData, String sheetName) throws Exception {
        setSheetName(sheetName);
        createSheet();
        setCells(inputData);
    }

    /**
     * <pre>
     * 새로운 Sheet를 만든다.
     * </pre>
     * @param pwd
     */
    public void createSheet(String pwd) {
        sheet = (ObjUtil.isNull(sheetName)) ? workbook.createSheet(sheetName) : workbook.createSheet();
        if(!pwd.equals("")) sheet.protectSheet(pwd);
    }

    public void createSheet() {
        createSheet("");
    }

    /**
     * <pre>
     * 각각의 cell에 타이틀과 내용을 채운다.
     * </pre>
     */
    private void setCells(HashMap inputData) {
        // title cell 내용 삽입
        String[] keyStrs = getTitles(inputData.keySet());
        String[] titleStrs = (ObjUtil.isNull(titles)) ? keyStrs : titles;
        createTitles(titleStrs);
        // cell 내용 삽입
        final int rowsize = inputData.size();
        HSSFRow valuerow = sheet.createRow(1);
        for (int inx = 0; inx < rowsize; inx++) {
            HSSFCell cell = valuerow.createCell((short)inx);
            cell.setCellStyle(createValueCellStyle());
            setCellValue(cell, inputData.get(keyStrs[inx]));
        }
        autoCellSize(rowsize);
    }

    /**
     * <pre>
     * 각각의 cell에 타이틀과 내용을 채운다.
     * </pre>
     */
    private void setCellsForMap(List<Map<String, Object>> inputData) {
        // title cell 내용 삽입
        String[] keyStrs = getTitles(inputData.get(0).keySet());
        String[] titleStrs = (ObjUtil.isNull(titles)) ? keyStrs : titles;
        createTitles(titleStrs);

        // cell 내용 삽입
        final int keysize = titleStrs.length;
        final int rowsize = inputData.size();
        for (int inx = 0; inx < rowsize; inx++) {
            HSSFRow valuerow = sheet.createRow(inx + 1);
            for (int iny = 0; iny < keysize; iny++) {
                HSSFCell cell = valuerow.createCell((short)iny);
                setCellValue(cell, inputData.get(inx).get(keyStrs[iny]).toString());
                if(valueCellStyles.get(keyStrs[iny]) != null ) {
                    cell.setCellStyle((HSSFCellStyle)valueCellStyles.get(keyStrs[iny]));
                } else {
                    cell.setCellStyle(createValueCellStyle());
                }
            }
        }
        autoCellSize(keysize);
    }

    /**
     * <pre>
     * 각각의 cell에 타이틀과 내용을 채운다.
     * </pre>
     */
    private void setCells(Object[][] inputData) {
        // title cell 내용 삽입
        int cnt = 0;
        if (!ObjUtil.isNull(titles)) {
            createTitles(titles);
            cnt = 1;
        }
        // cell 내용 삽입
        final int rowsize = inputData.length;
        int keysize = inputData[0].length;
        for (int inx = 0; inx < rowsize; inx++) {
            HSSFRow row = sheet.createRow(inx + cnt);
            for (int iny = 0; iny < keysize; iny++) {
                HSSFCell cell = row.createCell((short)iny);
                cell.setCellStyle(createValueCellStyle());
                setCellValue(cell, inputData[inx][iny]);
            }
        }
        autoCellSize(keysize);
    }

    /**
     * <pre>
     * ResultSet의 내용을 Cell에 삽입한다.
     * </pre>
     */
    public void setCells(ResultSet rs) throws Exception {

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            final int keysize = rsmd.getColumnCount();
            String[] keyStrs = new String[keysize];
            int[] valueTypeStrs = new int[keysize];

            final HSSFCellStyle keyCellStyle = createTitleCellStyle();
            HSSFRow keyrow = sheet.createRow(0);
            for (int inx = 0; inx < keysize; inx++) {
                int index = inx + 1;
                keyStrs[inx] = JdbcUtils.convertUnderscoreNameToPropertyName(rsmd.getColumnLabel(index));
                valueTypeStrs[inx] = rsmd.getColumnType(index);
                HSSFCell cell = keyrow.createCell((short)inx);
                cell.setCellStyle(keyCellStyle);
                //cell.setCellValue((StringUtil.getText(titles)) ? keyStrs[inx] : titles[inx]);
                cell.setCellValue(new HSSFRichTextString((ObjUtil.isNull(titles)) ? keyStrs[inx] : titles[inx]));
            }

            final HSSFCellStyle valueCellStyle = createValueCellStyle();
            int inx = 0;
            while (rs.next()) {
                HSSFRow row = sheet.createRow(inx + 1);
                for (int iny = 0; iny < keysize; iny++) {
                    HSSFCell cell = row.createCell((short)iny);
                    cell.setCellStyle(valueCellStyle);
                    final int attributeType = valueTypeStrs[iny];
                    final int index = iny + 1;

                    if (attributeType == Types.VARCHAR || attributeType == Types.CHAR) {
                        String value = (String)rs.getString(index);
                        if (checkBlankCell(cell, value)) {
                            if (value.toString().startsWith("=")) {
                                cell.setCellFormula(value.toString().substring(1));
                                cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
                            } else {
                                cell.setCellValue(new HSSFRichTextString((String) value));
//                                cell.setCellValue((String) value);
                                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            }
                        }
                    } else if (attributeType == Types.NUMERIC || attributeType == Types.DECIMAL || attributeType == Types.INTEGER) {
                        BigDecimal value = (BigDecimal)rs.getBigDecimal(index);
                        if (checkBlankCell(cell, value)) {
                            if (value.scale() > 0) {
                                cell.setCellValue(value.intValue());
                            } else {
                                long intExpected = value.longValue();
                                if (intExpected < Integer.MAX_VALUE && intExpected > Integer.MIN_VALUE) {
                                    cell.setCellValue(value.intValue());
                                } else {
                                    cell.setCellValue(intExpected);
                                }
                            }
                            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }
                    } else if (attributeType == Types.CLOB) {
                        String value = (String)rs.getString(index);
                        if (checkBlankCell(cell, value)) {
                            cell.setCellValue(new HSSFRichTextString(value));
                            //cell.setCellValue(value);
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        }
                    } else if (attributeType == Types.BLOB) {
                        checkBlankCell(cell,"");
                    } else {
                        try {
                            //cell.setCellValue((String) rs.getObject(index));
                            cell.setCellValue(new HSSFRichTextString((String)rs.getObject(index)));
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        } catch (Exception e) {
                            checkBlankCell(cell,"");
                        }
                    }
                }
                inx++;
            }
            autoCellSize(keysize);
        } catch (Exception se) {
            throw new Exception(se);
        }
    }

    /**
     * <pre>
     * value 값이 null인지 체크하여 null이면 빈 스트링을 cell 의 값으로 한다.
     * </pre>
     * @param cell
     * @param value
     * @return
     */
    private boolean checkBlankCell(HSSFCell cell, Object value) {
        boolean isNotBlank = true;
        if (ObjUtil.isNull(value)) {
            //cell.setCellValue("");
            cell.setCellValue(new HSSFRichTextString((String) ""));
            cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
            isNotBlank = false;
        }
        return isNotBlank;
    }

    /**
     * <pre>
     * 타이틀 정보를 가져온다. 단 이경우는 HashMap, List<Map<String, Object>>일 경우이다.
     * </pre>
     * @param tempSet
     * @return
     */
    private String[] getTitles(Set tempSet) {
        Iterator iterator = tempSet.iterator();
        List keyList = new ArrayList();
        while (iterator.hasNext()) {
            keyList.add(iterator.next());
        }
        return (String[]) keyList.toArray(new String[keyList.size()]);
    }

    /**
     * <pre>
     * 엑셀의 최상위에 찍힐 타이틀을 생성한다.
     * </pre>
     * @param defaultTitle
     * @return
     */
    private void createTitles(String[] titles) {
        HSSFRow keyrow = sheet.createRow(0);
        final int keysize = titles.length;
        for (int inx = 0; inx < keysize; inx++) {
            HSSFCell cell = keyrow.createCell((short)inx);
            if(titleCellStyles.get(titles[inx]) != null ) {
                cell.setCellStyle((HSSFCellStyle)titleCellStyles.get(titles[inx]));
            } else {
                cell.setCellStyle(createTitleCellStyle());
            }
            cell.setCellValue(new HSSFRichTextString(titles[inx]));
            //cell.setCellValue(titles[inx]);
        }
    }

    /**
     * <pre>
     * sheet의 사이즈를 자동 조절한다.
     * </pre>
     * @param size
     */
    private void autoCellSize(int size) {
        for (int inx = 0; inx < size; inx++) {
            sheet.autoSizeColumn((short)inx);
        }
    }

    /**
     * <pre>
     * 엑셀의 타이틀의 스타일을 생성한다.
     * </pre>
     * @return
     */
    private HSSFCellStyle createTitleCellStyle() {
        if (ObjUtil.isNull(this.titleCellStyle)) {
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            HSSFFont f = workbook.createFont();
            f.setBoldweight(POIConstants.FONT_WEIGHT_BOLD);
            f.setFontHeightInPoints(POIConstants.DEFAULT_FONT_HEIGHT);
            // cellStyle.setFillForegroundColor(POIConstants.FONT_COLOR_BLACK);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            cellStyle.setFont(f);
            return cellStyle;
        }
        return this.titleCellStyle;
    }

    /**
     * <pre>
     * 엑셀의 각각의 cell의 데이터를 생성한다.
     * </pre>
     * @return
     */
    private HSSFCellStyle createValueCellStyle() {
        if (ObjUtil.isNull(valueCellStyle)) {
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            // HSSFFont f = workbook.createFont();
//           f.setFontHeightInPoints(POIConstants.DEFAULT_FONT_HEIGHT);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
            // cellStyle.setFont(f);
            return cellStyle;
        }
        return valueCellStyle;
    }

    /**
     * <pre>
     * cell의 데이터를 삽입한다.
     * </pre>
     * @param cell
     * @param data
     */
    private void setCellValue(HSSFCell cell, Object data) {
        if (data instanceof Integer) {
            Integer i = (Integer) data;
            cell.setCellValue(i.doubleValue());
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        } else if (data instanceof Double) {
            Double d = (Double) data;
            cell.setCellValue(d.doubleValue());
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        } else if (data instanceof String) {
            if (data.toString().startsWith("=")) {
                cell.setCellFormula(data.toString().substring(1));
                cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            } else {
                //cell.setCellValue((String) data);
                cell.setCellValue(new HSSFRichTextString((String) data));
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            }
        } else if (data instanceof Boolean) {
            Boolean bool = (Boolean) data;
            cell.setCellValue(bool.booleanValue());
            cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
        } else if (data == null) {
            //cell.setCellValue("");
            cell.setCellValue(new HSSFRichTextString(""));
            cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
        } else {
            cell.setCellType(HSSFCell.CELL_TYPE_ERROR);
            logger.debug("setCellValue(HSSFCell cell, Object data)");
        }
    }

    /**
     * <pre>
     * Cell에 Object[]를 삽입한다.
     * </pre>
     * @param cell
     * @param datas
     * @return
     */
    public HSSFCell setCellValue(HSSFCell cell, Object[] datas) {
        int size = datas.length;
        for (int inx = 0; inx < size; inx++) {
            setCellValue(cell, datas[inx]);
        }
        return cell;
    }

    /**
     * <pre>
     * POI의 workbook 객체를 가져온다.
     * </pre>
     * @return
     */
    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    /**
     * <pre>
     * 원하는 타이틀을 입력할 수 있다.
     * </pre>
     * @param titles
     *            타이틀 내용
     */
    public void setTitle(String[] aTitles) {
        this.titles = aTitles;
    }

    /**
     * <pre>
     * 타이틀 Cell의 스타일을 정한다.
     * </pre>
     * <예> HSSFCellStyle cellStyle = workbook.createCellStyle(); HSSFFont font =
     * workbook.createFont();
     * font.setBoldweight(POIConstants.FONT_WEIGHT_BOLD);
     * font.setColor(POIConstants.FONT_COLOR_BLACK);
     * font.setFontHeightInPoints(12);
     * cellStyle.setAlign)ment(HSSFCellStyle.ALIGN_LEFT);
     * cellStyle.setFont(font); </예>
     *
     * @param titleCellStyle
     */
    public void setTitleCellStyle(HSSFCellStyle aTitleCellStyle) {
        this.titleCellStyle = aTitleCellStyle;
    }

    /**
     * <pre>
     * 데이타 Cell의 스타일을 정한다.
     * </pre>
     * <예> HSSFCellStyle cellStyle = workbook.createCellStyle(); HSSFFont font =
     * workbook.createFont();
     * font.setBoldweight(POIConstants.FONT_WEIGHT_NORMAL);
     * font.setColor(POIConstants.FONT_COLOR_BLACK);
     * font.setFontHeightInPoints(10);
     * cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
     * cellStyle.setFont(font); </예>
     *
     * @param valueCellStyle
     */
    public void setValueCellStyle(HSSFCellStyle aValueCellStyle) {
        this.valueCellStyle = aValueCellStyle;
    }

    /**
     * <pre>
     * Sheet의 이름을 세팅한다.
     * </pre>
     * @param sheetName
     */
    public void setSheetName(String aSheetName) {
        this.sheetName = aSheetName;
    }

    /**
     * <pre>
     * 현재 작성중인 Sheet 이름을 가져온다.
     * </pre>
     * @return
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * <pre>
     * encoding type을 설정한다.
     * </pre>
     * @param encoding
     *            String
     */
    public void setEncoding(String aEncoding) {
        this.encoding = aEncoding;
    }

    /**
     * @deprecated
     */
    public void setWrite(HashMap inputData) throws Exception {
        addSheet(inputData);
        execute();
    }

    /**
     * @deprecated
     */
    public void setWrite(HashMap inputData, String newFilePath) throws Exception {
        addSheet(inputData);
        execute(newFilePath);
    }

    /**
     * @deprecated
     */
    public void setWrite(List<Map<String, Object>> inputData) throws Exception {
        addSheetForMap(inputData);
        execute();
    }

    /**
     * @deprecated
     */
    public void setWrite(List<Map<String, Object>> inputData, String newFilePath) throws Exception {
    	addSheetForMap(inputData);
        execute(newFilePath);
    }

    /**
     * @deprecated
     */
    public void setWrite(Object[][] inputData) throws Exception {
        addSheet(inputData);
        execute();
    }

    /**
     * @deprecated
     */
    public void setWrite(Object[][] inputData, String newFilePath) throws Exception {
        addSheet(inputData);
        execute(newFilePath);
    }

    /**
     * <pre>
     * 셀의 스타일 정보를 기록한다.
     * </pre>
     * @param newExcelFilePath
     * @return
     * @throws Exception
     */
    public void setTitleCellStyle(int CellIdx, HSSFCellStyle hSSFCellStyle) throws Exception {
        titleCellStyles.put(CellIdx, hSSFCellStyle);
    }

    /**
     * <pre>
     * 셀의 스타일 정보를 기록한다.
     * </pre>
     * @param newExcelFilePath
     * @return
     * @throws Exception
     */
    public void setTitleCellStyle(String TitleName, HSSFCellStyle hSSFCellStyle) throws Exception {
        titleCellStyles.put(TitleName, hSSFCellStyle);
    }

    public void setTitleCellStyles(String TitleNames, HSSFCellStyle hSSFCellStyle) throws Exception {
        if(TitleNames.indexOf(",") > -1) {
            String titleNameArry[] = TitleNames.split(",");
            for(int i=0;i<titleNameArry.length;i++) {
                titleCellStyles.put(titleNameArry[i], hSSFCellStyle);
            }
        } else {
            titleCellStyles.put(TitleNames, hSSFCellStyle);
        }
    }

    /**
     * <pre>
     * 셀의 스타일 정보를 기록한다.
     * </pre>
     * @param newExcelFilePath
     * @return
     * @throws Exception
     */
    public void setValueCellStyle(int CellIdx, HSSFCellStyle hSSFCellStyle) throws Exception {
        valueCellStyles.put(CellIdx, hSSFCellStyle);
    }

    /**
     * <pre>
     * 셀의 스타일 정보를 기록한다.
     * </pre>
     * @param newExcelFilePath
     * @return
     * @throws Exception
     */
    public void setValueCellStyle(String TitleName, HSSFCellStyle hSSFCellStyle) throws Exception {
        valueCellStyles.put(TitleName, hSSFCellStyle);
    }

    /**
     * <pre>
     * 셀의 스타일 정보를 기록한다.
     * </pre>
     * @param newExcelFilePath
     * @return
     * @throws Exception
     */
    public void setValueCellStyles(String TitleNames, HSSFCellStyle hSSFCellStyle) throws Exception {
        if(TitleNames.indexOf(",") > -1) {
            String titleNameArry[] = TitleNames.split(",");
            for(int i=0;i<titleNameArry.length;i++) {
                valueCellStyles.put(titleNameArry[i], hSSFCellStyle);
            }
        } else {
            valueCellStyles.put(TitleNames, hSSFCellStyle);
        }
    }

    /**
     * <pre>
     * 컬럼순서 재조합
     * </pre>
     * @param newExcelFilePath
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> changeColOrder(List<Map<String, Object>> mData, String aHeaders[]){
        List<Map<String, Object>> mData2 = new ArrayList<Map<String, Object>>();
        for(int i=0;i<mData.size();i++) {
            HashMap input = (HashMap)mData.get(i);
            HashMap newInput = new HashMap();
            for(int j=0;j<aHeaders.length;j++) {
                if(newInput.get(aHeaders[j]) == null) {
                    newInput.put(aHeaders[j], input.get(aHeaders[j]));
                } else {
                    newInput.put(aHeaders[j] + "2", input.get(aHeaders[j]));
                }
            }
            mData2.add(mData2.size(), newInput);
        }
        return mData2;
    }
}
