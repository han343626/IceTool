package com.zlsk.zTool.customControls.camera.base;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zlsk.zTool.utils.string.StringUtil;

import java.util.Hashtable;

/**
 * Created by IceWang on 2019/1/17.
 * zxing lib 生成二维码
 */

public class CodeBuilder {
    public interface BitmapCallback {
        void callback(Bitmap bitmap);
    }

    /**
     * 二维码/条形码内容
     */
    private String content;
    /**
     * 二维码/条形码背景色
     */
    private int backgroundColor = Color.WHITE;
    /**
     * 二维码/条形码颜色
     */
    private int codeColor = Color.BLACK;
    /**
     * 二维码/条形码宽度
     */
    private int width = 1000;
    /**
     * 二维码/条形码高度
     */
    private int height = 1000;

    public CodeBuilder content(String content) {
        this.content = content;
        return this;
    }

    public CodeBuilder backgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public CodeBuilder codeColor(int codeColor) {
        this.codeColor = codeColor;
        return this;
    }

    public CodeBuilder width(int width) {
        this.width = width;
        return this;
    }

    public CodeBuilder height(int height) {
        this.height = height;
        return this;
    }

    public void buildQrCode(BitmapCallback bitmapCallback) {
        if (StringUtil.isNullString(content)) {
            bitmapCallback.callback(null);
        }
        new Thread(() -> {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix;
            try {
                bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            } catch (Exception e) {
                bitmapCallback.callback(null);
                return;
            }

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixels[y * width + x] = bitMatrix.get(x, y) ? codeColor : backgroundColor;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            bitmapCallback.callback(bitmap);
        }).start();
    }

    public void buildBarCode(BitmapCallback bitmapCallback) {
        if (StringUtil.isNullString(content)) {
            bitmapCallback.callback(null);
        }
        new Thread(() -> {
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix result;
            try {
                result = writer.encode(content, BarcodeFormat.CODE_128, width, height == 1000 ? 300 : height, null);
            } catch (Exception e) {
                bitmapCallback.callback(null);
                return;
            }

            int width = result.getWidth();
            int height = result.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = result.get(x, y) ? codeColor : backgroundColor;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            bitmapCallback.callback(bitmap);
        }).start();
    }
}
