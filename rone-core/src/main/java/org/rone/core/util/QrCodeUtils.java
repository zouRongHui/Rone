package org.rone.core.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 二维码工具类
 * @author rone
 */
public class QrCodeUtils {

    private QrCodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void main(String[] args) throws IOException, NotFoundException {
        byte[] imageBytes = QrCodeUtils.generateQRcodeByte("fuck the world", 320, "png");
        // 将字节数组输出到图片文件
        File file = new File("E:\\text.png");
        OutputStream outputStream = new FileOutputStream(file);
        outputStream.write(imageBytes);
        outputStream.flush();
        outputStream.close();

        System.out.println(QrCodeUtils.readQRCode(new FileInputStream(new File("E:\\text.png"))));
    }

    /**
     * 获取生成的二维码图片的字节数组
     * @param content   内容
     * @param widthAndHeight     宽高
     * @param picFormat
     * @return
     */
    public static byte[] generateQRcodeByte(String content, int widthAndHeight, String picFormat) {
        byte[] codeBytes = null;
        try {
            // 构造二维字节矩阵，将二位字节矩阵渲染为二维缓存图片
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // 定义输出流，将二维缓存图片写到指定输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, picFormat, out);

            // 将输出流转换为字节数组
            codeBytes = out.toByteArray();
        } catch (WriterException | IOException e) {
            String.format("二维码生成失败，错误：%s", e);
            e.printStackTrace();
        }
        return codeBytes;
    }

    /**
     * 根据输入流解析二维码
     * @param inputStream
     * @return
     * @throws NotFoundException
     * @throws IOException
     */
    public static Result readQRCode(InputStream inputStream) throws NotFoundException, IOException {
        try {
            // 图片缓冲
            BufferedImage image = ImageIO.read(inputStream);
            return readQRCode(image);
        } catch (IOException e) {
            String.format("二维码解析失败，错误：%s", e);
            throw e;
        }
    }

    /**
     * 根据图片缓冲对象解析二维码
     * @param image
     * @return
     * @throws NotFoundException
     */
    public static Result readQRCode(BufferedImage image) throws NotFoundException {
        try {
            // 二进制比特图
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            // 二维码结果
            Result result = (new MultiFormatReader()).decode(binaryBitmap);
            return result;
        } catch (NotFoundException e) {
            String.format("二维码解析失败，错误：%s", e);
            throw e;
        }
    }
}
