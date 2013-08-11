package tz.basis.integration.excel;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : ExcelTemplateManager
 * 설    명 : 엑셀 템플릿 파일을 파싱하거나 생성한다.
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
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jxl.BooleanCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.StringFormulaCell;
import jxl.Workbook;

import org.apache.commons.lang.StringUtils;

import tz.extend.util.DateUtil;

public class ExcelTemplateManager {

    public static String[] headerLabelCodesForMessageTemplate = { "code", "message" };

    public static String[] columnDataKeysForMessageTemplate = { "CODE", "MESSAGE" };

    /**
     * 템플릿 파일의 데이터를 List<Map<String, Object>>로 전환한다.
     * 엑셀파일의 첫번째 Sheet만 변환작업에 포함된다.
     * List<Map<String, Object>>는 Sheet 명으로 생성되며 모두 String 타입으로 변환된다.
     *
     * @param templateFile 전환할 엑셀파일
     * @return 전환된 List<Map<String, Object>>
     * @exception Exception 파일 파싱중에 에러 발생시
     */
    private static Map<String, Object> parseTemplate(File templateFile, String[] columnDataKeys) throws Exception{
        Workbook w = null;

        try {
            w = Workbook.getWorkbook(templateFile);
        } catch (Exception e) {
            throw new Exception("tz.err.excel.data");
        }

        Sheet sheet = w.getSheet(0); // 첫번째 sheet만 작업대상에 포함됨.
        Map<String, Object> sheetData = new HashMap<String, Object>();
        int rowSize = sheet.getRows();

        for (int columnIndex = 0; columnIndex < columnDataKeys.length; columnIndex++) {
            for (int rowIndex = 1; rowIndex < rowSize; rowIndex++) { // 첫번째 열은 헤더임.
                sheetData.put(columnDataKeys[columnIndex].toString(), getCellValueAsString(sheet.getCell(columnIndex, rowIndex)));
            }
        }

        return sheetData;
    }

    /**
     * 메시지 등록용 템플릿 파일의 데이터를 List<Map<String, Object>>로 전환한다.
     * 엑셀파일의 첫번째 Sheet만 변환작업에 포함된다.
     * List<Map<String, Object>>는 Sheet 명으로 생성되며 모두 String 타입으로 변환된다.
     *
     * @param templateFile 전환할 엑셀파일
     * @return 전환된 List<Map<String, Object>>
     * @exception Exception 파일 파싱중에 에러 발생시
     */
    public static Map<String, Object> parseMessageTemplate(File templateFile) throws Exception{
        return parseTemplate(templateFile, columnDataKeysForMessageTemplate);
    }

    /**
     * 셀의 내용을 텍스트 형식으로 리턴한다.
     * 날짜 셀은 yyyy-MM-dd 형식으로 리턴.
     *
     * 비어있는 셀은 null을 리턴.
     *
     * @param cell
     * @return
     */
    private static String getCellValueAsString(Cell cell){
        String result = null;

        CellType type = cell.getType();

        if ((type == CellType.DATE) || (type == CellType.DATE_FORMULA)) {
            Date date = ((DateCell)cell).getDate();
            //DateFormat dateFormat = (DateFormat)((DateCell)cell).getDateFormat();
            result = DateUtil.formatDate(date, "yyyy-MM-dd");
        } else if ((type == CellType.NUMBER) || (type == CellType.NUMBER_FORMULA)) {
            double dValue = ((NumberCell)cell).getValue();
            NumberFormat numberFormat = ((NumberCell)cell).getNumberFormat();
            result = numberFormat.format(dValue);
        } else if (type == CellType.STRING_FORMULA) {
            result = ((StringFormulaCell)cell).getString();
        } else if (type == CellType.LABEL) {
            result = ((LabelCell)cell).getString();
        } else if ((type == CellType.BOOLEAN) || (type == CellType.BOOLEAN_FORMULA)) {
            result = ((BooleanCell)cell).getValue() ? "Y" : "N";
        } else {
            result = cell.getContents();
        }

        if (StringUtils.isEmpty(result)) {
            return null;
        }

        return result;
    }
}


