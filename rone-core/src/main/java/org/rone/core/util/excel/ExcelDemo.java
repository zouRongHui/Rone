package org.rone.core.util.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * excel处理示例
 * @author rone
 */
public class ExcelDemo {

    public static void main(String[] args) throws Exception {
        ExcelDemo.createExcel();
        ExcelDemo.parseExcel();
    }

    /**
     * 解析excel文件
     * @throws Exception
     */
    public static void parseExcel() throws Exception {
        /*
         * 工作簿
         * 若处理大量数据可使用SXSSFWorkbook来操作，但下下面获取sheet时需要注意
         *   workbook = new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(new File("/excel.xlsx"))));
         * web项目中从http流中加载
         *   String originalFileName = org.springframework.web.multipart.MultiparFile.getOriginalFilename();
         *   if (originalFileName == null || !(originalFileName.endsWith(".xlsx")) || !(originalFileName.endsWith(".xls"))) {
         *       throw new IllegalArgumentException("文件格式不正确，请上传正确的excel文件");
         *   }
         *   if (originalFileName.endsWith(".xls")) {
         *       workbook = new HSSFWorkbook(MultipartFile.getInputStream());
         *   } else if (originalFileName.endsWith(".xlsx")) {
         *       workbook = new XSSFWorkbook(MultipartFile.getInputStream());
         *   }
         */
        Workbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:\\ExcelDemo.xlsx")));
        /*
         * 数据表
         * 根据sheet的名称来获取
         *   sheet = workbook.getSheet("sheet1");
         * 根据sheet的索引来获取
         *   sheet = workbook.getSheetAt(0);
         */
        Sheet sheet;
        // 数据行
        Row row;
        // 单元格
        Cell cell;
        if (workbook != null) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                /*
                 * SXSSFWorkbook对象获取sheet时需要先获取XSSFWorkbook，否则后面的Row数据为null(SXSSFWorkbook的特性)
                 *   sheet = ((SXSSFWorkbook) workbook).getXSSFWorkbook().getSheetAt(i);
                 */
                sheet = workbook.getSheetAt(i);
                if (sheet != null) {
                    for (int j = 0; j < sheet.getLastRowNum(); j++) {
                        row = sheet.getRow(j);
                        if (row != null) {
                            for (int k = 0; k < row.getLastCellNum(); k++) {
                                cell = row.getCell(k);
                                if (cell != null) {
                                    System.out.println("sheet:" + i + ",row: " + j + ",cell: " + k + " " + ExcelUtils.getCellValue(cell));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 生成excel文件
     * @throws Exception
     */
    public static void createExcel() throws Exception {
        /*
         * 工作簿，就是一个excel对象
         * .xls的Workbook实现类,65535行、256列
         *   workbook = new HSSFWorkbook();
         * .xlsx的Workbook实现类,1048576行,16384列
         *   workbook = new XSSFWorkbook();
         * 大数据的excel导出，未避免OOM请使用SXSSFWorkbook，1000:内存中保留的数据量
         *   workbook = new SXSSFWorkbook(1000);
         *   // 设置是否压缩该临时文件
         *   ((SXSSFWorkbook) workbook).setCompressTempFiles(true);
         */
        Workbook workbook = new XSSFWorkbook();
        // 因为当个sheet有最大行数限制，所以这里配置一下支持每个sheet中实际数据的最大行数
        int sheetDataMaxRowNum = ExcelUtils.XSSF_WORK_BOOK_SHEET_MAX_ROW_NUM - 1;

        List<SheetHead> sheetHeadList = new ArrayList<>();
        sheetHeadList.add(new SheetHead("姓名", 6));
        sheetHeadList.add(new SheetHead("地区", 10));
        sheetHeadList.add(new SheetHead("性别", 6));
        sheetHeadList.add(new SheetHead("手机号码", 13));
        sheetHeadList.add(new SheetHead("现居省", 13));
        sheetHeadList.add(new SheetHead("现居市", 13));
        List<String[]> dataList = new ArrayList<>();
        dataList.add(new String[]{"李白", "甘肃天水", "男", "110"});
        dataList.add(new String[]{"李清照", "山东济南", "女", "120"});
        dataList.add(new String[]{"杜甫", "河南巩县", "男生", "119"});
        dataList.add(new String[]{"白居易", "山西太原", "男", "114"});
        dataList.add(new String[]{"苏轼", "四川眉山", "男", "12315"});

        // 文本单元格样式
        CellStyle textCellStyle = ExcelUtils.getTextFormatStyle(workbook);
        int sheetNum = dataList.size() / sheetDataMaxRowNum;
        for (int i = 0; i <= sheetNum; i++) {
            // 工作表
            Sheet sheet = workbook.createSheet();
            Row row = sheet.createRow(0);
            // 设置行高，excel中高度值 * 20 = 此处设置的高度值
            row.setHeight(Short.valueOf("500"));
            ExcelUtils.writeTextCell(row, 0, "合并后的标题单元格", textCellStyle);
            // 设置单元格合并
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            row = sheet.createRow(1);
            for (int j = 0; j < sheetHeadList.size(); j++) {
                SheetHead sheetHead = sheetHeadList.get(j);
                ExcelUtils.writeTextCell(row, j, sheetHead.getName(), textCellStyle);
                if (sheetHead.getWidth() != null) {
                    // 设置列宽
                    sheet.setColumnWidth(j, sheetHead.getWidth() * ExcelUtils.SINGLE_CHAR_WIDTH);
                }
            }
            for (int j = 0; j < sheetDataMaxRowNum && j < (dataList.size() - (sheetDataMaxRowNum * i)); j++) {
                String[] data = dataList.get(j + (i * sheetDataMaxRowNum));
                row = sheet.createRow(j + 2);
                ExcelUtils.writeTextCell(row, 0, data[0], textCellStyle);
                ExcelUtils.writeTextCell(row, 1, data[1], textCellStyle);
                ExcelUtils.writeTextCell(row, 2, data[2], textCellStyle);
                ExcelUtils.writeTextCell(row, 3, data[3], textCellStyle);
            }
        }

        // 直接通过数组数据配置数据有效性
        Sheet sheet = workbook.getSheetAt(0);
        String validationData = "男,女,未知";
        ExcelUtils.addValidationData(sheet, ExcelUtils.DataValidationConstraintType.EXPLICIT_LIST, validationData, 1, ExcelUtils.XSSF_WORK_BOOK_SHEET_MAX_ROW_NUM - 1, 2, 2);

        Sheet hiddenSheet = workbook.createSheet("hidden");
        // 设置sheet隐藏
        workbook.setSheetHidden(workbook.getSheetIndex("hidden"), true);
        ExcelUtils.writeTextCell(hiddenSheet.createRow(0), 0, "江苏省", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.createRow(1), 0, "南京市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.createRow(2), 0, "苏州市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.createRow(3), 0, "无锡市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.createRow(4), 0, "常州市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.getRow(0), 1, "浙江省", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.getRow(1), 1, "杭州市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.getRow(2), 1, "嘉兴市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.getRow(3), 1, "湖州市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.getRow(4), 1, "宁波市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.getRow(0), 2, "上海市", textCellStyle);
        ExcelUtils.writeTextCell(hiddenSheet.getRow(1), 2, "上海市", textCellStyle);

        // 通过引用公式来配置
        ExcelUtils.addValidationData(sheet, ExcelUtils.DataValidationConstraintType.FORMULA_LIST, "'hidden'!$A$1:$C$1", 1, ExcelUtils.XSSF_WORK_BOOK_SHEET_MAX_ROW_NUM - 1, 4, 4);

        // 通过引用公式来配置联动数据有效性限制
        Name jiangsuProvince = workbook.createName();
        jiangsuProvince.setNameName("江苏省");
        jiangsuProvince.setRefersToFormula("'hidden'!$A$2:$A$5");
        Name zhejiangProvince = workbook.createName();
        zhejiangProvince.setNameName("浙江省");
        zhejiangProvince.setRefersToFormula("'hidden'!$B$2:$B$5");
        Name shanghaiCity = workbook.createName();
        shanghaiCity.setNameName("上海市");
        shanghaiCity.setRefersToFormula("'hidden'!$C$2:$C$2");
        ExcelUtils.addValidationData(sheet, ExcelUtils.DataValidationConstraintType.FORMULA_LIST, "=INDIRECT($E2)", 1, ExcelUtils.XSSF_WORK_BOOK_SHEET_MAX_ROW_NUM - 1, 5, 5);

        //导出到本地文件
        File file = new File("E:\\ExcelDemo.xlsx");
        if (!file.exists()) {
            file.createNewFile();
        }
        workbook.write(new FileOutputStream(file));
        workbook.close();
        /*
         * web项目中已流的形式返回给前端
         *   workbook.write(javax.servlet.http.HttpServletResponse.getOutPutStream());        *
         */

    }

    /**
     * Excel表头信息
     * @author Rone
     */
    public static class SheetHead {
        private String name;
        private Integer width;

        public SheetHead(String name, Integer width) {
            this.name = name;
            this.width = width;
        }

        public SheetHead() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }
    }
}
