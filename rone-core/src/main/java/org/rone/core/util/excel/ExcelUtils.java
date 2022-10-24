package org.rone.core.util.excel;

import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.rone.core.jdk.exception.RoneException;

/**
 * excel工具
 * @author rone
 */
public class ExcelUtils {

    private ExcelUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * .xls的Workbook实现类，单个sheet最大65535行
     */
    public static final int HSSF_WORK_BOOK_SHEET_MAX_ROW_NUM = 65535;
    /**
     * .xls的Workbook实现类，单个sheet最大256列
     */
    public static final int HSSF_WORK_BOOK_SHEET_MAX_COL_NUM = 256;
    /**
     * .xlsx的Workbook实现类，单个sheet最大1048576行
     */
    public static final int XSSF_WORK_BOOK_SHEET_MAX_ROW_NUM = 1048576;
    /**
     * .xlsx的Workbook实现类，单个sheet最大16384列
     */
    public static final int XSSF_WORK_BOOK_SHEET_MAX_COL_NUM = 16384;
    /**
     * 单元格中单个字符的宽度
     */
    public static final int SINGLE_CHAR_WIDTH = 256;
    /**
     * 通过数据直接去配置数据有效性所支持的最大的数据长度
     */
    public static final int VALIDATION_DATA_EXPLICIT_LIST_CONSTRAINT = 256;
    /**
     * excel中列的中字母循环阈值
     */
    private static final int COLUMN_Z_INT = 26;

    /**
     * 根据单元格数据类型获取数据
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        Object result = null;
        switch (cell.getCellType()) {
            //文本
            case STRING:
                result = cell.getStringCellValue();
                break;
            //数字(整数、小数、日期)
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    //日期
                    result = cell.getDateCellValue();
                } else {
                    //若数值过大会有精度损失，可使用 NumberToTextConverter.toText(cell.getNumericCellValue()) 获取未有精度损失的字符类型数据
                    result = cell.getNumericCellValue();
                }
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue();
                break;
            //公式
            case FORMULA:
                //实际中未遇到过，不清楚咋解析处理
                result = cell.getCellFormula();
                break;
            //空
            case BLANK:
                System.out.println("空的单元格类型: " + cell.getAddress().toString());
                break;
            //未知类型
            case _NONE:
                System.out.println("未知的单元格类型: " + cell.getAddress().toString());
                break;
            //错误
            case ERROR:
                System.out.println("错误的单元格类型: " + cell.getAddress().toString());
                break;
            default:
        }
        return result;
    }

    /**
     * 生成数字单元格
     * @param row
     * @param cellIndex
     * @param value
     * @param style
     */
    public static void writeNumberCell(Row row, Integer cellIndex, Double value, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        // 配置单元格数据类型
        cell.setCellType(CellType.NUMERIC);
        // 给单元格赋值
        cell.setCellValue(value);
        // 设置单元格样式
        cell.setCellStyle(style);
    }

    /**
     * 生成文本的单元格
     * @param row
     * @param cellIndex
     * @param value
     * @param style
     */
    public static void writeTextCell(Row row, Integer cellIndex, String value, CellStyle style) {
        Cell cell = row.createCell(cellIndex);
        // 配置单元格数据类型
        cell.setCellType(CellType.STRING);
        // 给单元格赋值
        cell.setCellValue(value);
        // 设置单元格样式
        cell.setCellStyle(style);
    }

    /**
     * 获取文本类型的单元格样式
     * @param workbook
     * @return
     */
    public static CellStyle getTextFormatStyle(Workbook workbook) {
        return getFormatStyle(workbook, "@");
    }

    /**
     * 获取带有小数的数字类型的单元格样式
     * @param workbook
     * @return
     */
    public static CellStyle getDoubleFormatStyle(Workbook workbook) {
        return getFormatStyle(workbook, "0.00");
    }

    /**
     * 获取整型数字类型的单元格样式
     * @param workbook
     * @return
     */
    public static CellStyle getIntegerFormatStyle(Workbook workbook) {
        return getFormatStyle(workbook, "0");
    }

    /**
     * 按照格式获取单元格样式
     * @param workbook
     * @param format
     * @return
     */
    public static CellStyle getFormatStyle(Workbook workbook, String format) {
        CellStyle contextStyle = workbook.createCellStyle();
        DataFormat df = workbook.createDataFormat();
        contextStyle.setDataFormat(df.getFormat(format));
        contextStyle.setBorderTop(BorderStyle.THIN);
        contextStyle.setBorderBottom(BorderStyle.THIN);
        contextStyle.setBorderLeft(BorderStyle.THIN);
        contextStyle.setBorderRight(BorderStyle.THIN);
        contextStyle.setAlignment(HorizontalAlignment.CENTER);
        contextStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return contextStyle;
    }

    /**
     * 配置单元格的数据有效性
     * @param sheet
     * @param dataValidationConstraintType  数据有效性的类型
     * @param validationData    数据有效性的配置内容
     * @param firstRow  起始行
     * @param lastRow   终止行
     * @param firstCol  起始列
     * @param lastCol   终止列
     * @throws RoneException
     */
    public static void addValidationData(Sheet sheet, DataValidationConstraintType dataValidationConstraintType, String validationData, int firstRow, int lastRow, int firstCol, int lastCol) throws RoneException {
        DataValidationHelper dataValidationHelper;
        if (sheet instanceof XSSFSheet) {
            dataValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
        } else if (sheet instanceof HSSFSheet) {
            dataValidationHelper = new HSSFDataValidationHelper((HSSFSheet) sheet);
        } else {
            return;
        }
        DataValidationConstraint dataValidationConstraint;
        switch (dataValidationConstraintType) {
            case EXPLICIT_LIST:
                if (validationData.length() > VALIDATION_DATA_EXPLICIT_LIST_CONSTRAINT) {
                    throw new RoneException("通过数据直接去配置数据有效性时，所有可选数据长度不能超过256个字符(包含逗号)");
                }
                dataValidationConstraint = dataValidationHelper.createExplicitListConstraint(validationData.split(","));
                break;
            case FORMULA_LIST:
                dataValidationConstraint = dataValidationHelper.createFormulaListConstraint(validationData);
                break;
            default:
                return;

        }
        // 配置生效的范围区域，起始行、终止行、起始列、终止列
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DataValidation dataValidation = dataValidationHelper.createValidation(dataValidationConstraint, cellRangeAddressList);
        // 是否展示下拉箭头
        dataValidation.setSuppressDropDownArrow(true);
        // 是否展示错误提示框
        dataValidation.setShowErrorBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * excel配置数据有效性的方式
     */
    public enum DataValidationConstraintType{
        // 直接根据数据配置，但数长度不能大于256个字符
        EXPLICIT_LIST,
        // 根据引用公式来配置
        FORMULA_LIST
    }

    /**
     * 将列数转换成excel中字母表达式
     * 小于27的列数，直接转换
     * 大于27的列数，可将算法理解成将10进制转换成26进制(A：1,...Z:0)
     * @param columnNumber
     * @return
     */
    public static String excelColumnInt2String(int columnNumber) {
        if (columnNumber <= COLUMN_Z_INT) {
            return String.valueOf((char)(columnNumber % 27 + 64));
        } else {
            return excelColumnInt2StringRecursion(columnNumber);
        }
    }

    /**
     * 大于27的列数，通过类26进制的转换算法转换
     * @param columnNumber
     * @return
     */
    private static String excelColumnInt2StringRecursion(int columnNumber) {
        StringBuilder result = new StringBuilder();
        int quotient = columnNumber / COLUMN_Z_INT;
        int remainder = columnNumber % COLUMN_Z_INT;
        if (remainder == 0) {
            quotient--;
            remainder = COLUMN_Z_INT;
        }
        if (quotient > 0) {
            result.append(excelColumnInt2StringRecursion(quotient));
        }
        remainder += 64;
        result.append((char) remainder);
        return result.toString();
    }
}
