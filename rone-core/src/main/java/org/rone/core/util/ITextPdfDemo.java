package org.rone.core.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;

/**
 * java生成PDF文件
 * @author rone
 */
public class ITextPdfDemo {

    public static void main(String[] args) throws Exception {
        // 1.新建document对象
        Document pdfDoc = new Document();

        // 2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中，也可以关联其他的输出流输出到指定位置，例如response的流返回前端。
        // 创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(pdfDoc, new FileOutputStream("C:/localTest.pdf"));

        //设置页眉和页脚
        PdfPageEvent pdfPageEvent = new PdfPageEventHelper(){
            private PdfTemplate template;
            private BaseFont baseFont = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            private int fontSize = 8;

            //文档打开时触发
            @Override
            public void onOpenDocument(PdfWriter writer, Document document) {
                super.onOpenDocument(writer, document);
                template = writer.getDirectContent().createTemplate(50, 50);
            }

            //单个页面结束时触发
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                super.onEndPage(writer, document);
                //页眉
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("页眉", new Font(baseFont, fontSize)),
                        (document.rightMargin() + document.right() + document.leftMargin() - document.left()) / 2.0F,
                        document.top() + 20F, 0);
                //页脚,格式为 第X页 /共Y页
                String foot = "第 " + writer.getPageNumber() + " 页 /共";
                //获取页脚第一部分文本长度， 用于调节第二部分的位置
                Float length = baseFont.getWidthPoint(foot, fontSize);
                PdfContentByte cb = writer.getDirectContent();
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, new Paragraph(foot, new Font(baseFont, fontSize)),
                        (document.rightMargin() + document.right() + document.leftMargin() - document.left() - length) / 2.0F,
                        document.bottom() - 20F, 0);
                //通过一个模板来实现显示总页数
                cb.addTemplate(template, (document.rightMargin() + document.right() + document.leftMargin() - document.left()) / 2.0F, document.bottom() - 20F);
            }

            //文档关闭时触发
            @Override
            public void onCloseDocument(PdfWriter writer, Document document) {
                //在文档关闭时，才知道该文档的总页数
                template.beginText();
                template.setFontAndSize(baseFont, fontSize);
                template.showText(" " + writer.getPageNumber() + " 页");
                template.endText();
                template.closePath();
            }
        };
        writer.setPageEvent(pdfPageEvent);

        // 3.打开文档
        pdfDoc.open();

        //中文字体
        BaseFont chineseBaseFont = BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
        Font chineseFont = new Font(chineseBaseFont);
        //文本样式
        Font titleFont = new Font(chineseBaseFont);
        titleFont.setSize(20);
        titleFont.setStyle(Font.BOLD);
        Font subtitleFont = new Font(chineseBaseFont);
        subtitleFont.setSize(15);
        subtitleFont.setStyle(Font.BOLD);

        // 4.添加内容
        //标题
        Paragraph titleParagraph = new Paragraph("PDF文件", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        pdfDoc.add(titleParagraph);

        pdfDoc.add(new Paragraph(" "));

        //添加一个表格
        Paragraph reportInstruction = new Paragraph();
        reportInstruction.setFont(chineseFont);
        reportInstruction.add(new Chunk("报告说明", subtitleFont));
        reportInstruction.add(new Chunk("\n1. 本报告xxxxxxxxx", new Font()));
        reportInstruction.add(new Chunk("\n2. 版权所有 rone", new Font()));
        reportInstruction.add(new Chunk("\n3. 本文档为xxxxx", new Font()));
        reportInstruction.add(new Chunk("\n4. XXXXXXXXXXXXXXXXXXXXXXXXXXXXX", new Font()));
        PdfPTable reportInstructionTable = new PdfPTable(1);
        PdfPCell reportInstructionCell = new PdfPCell();
        reportInstructionCell.setPhrase(reportInstruction);
        reportInstructionTable.addCell(reportInstructionCell);
        pdfDoc.add(reportInstructionTable);

        pdfDoc.add(new Paragraph(" "));

        //设置属性
        //标题
        pdfDoc.addTitle("rone");
        //作者
        pdfDoc.addAuthor("rone");
        //主题
        pdfDoc.addSubject("报告");
        //关键字
        pdfDoc.addKeywords("报告");
        //创建时间
        pdfDoc.addCreationDate();
        //应用程序
        pdfDoc.addCreator("rone");

        //关闭文档
        pdfDoc.close();
        //关闭书写器
        writer.close();
    }
}
