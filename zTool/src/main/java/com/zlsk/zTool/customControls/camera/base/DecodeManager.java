package com.zlsk.zTool.customControls.camera.base;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zlsk.zTool.utils.bitmap.BitmapUtil;

import java.util.Hashtable;
import java.util.Vector;

public class DecodeManager {
    private static DecodeManager instance;
    private MultiFormatReader multiFormatReader;

    private DecodeManager() {
        initDecodeReader();
    }

    public static synchronized DecodeManager getInstance() {
        if (instance == null) {
            instance = new DecodeManager();
        }
        return instance;
    }

    private void initDecodeReader() {
        multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<>();

            Vector<BarcodeFormat> PRODUCT_FORMATS = new Vector<BarcodeFormat>(5);
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_A);
            PRODUCT_FORMATS.add(BarcodeFormat.UPC_E);
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_13);
            PRODUCT_FORMATS.add(BarcodeFormat.EAN_8);
            // PRODUCT_FORMATS.add(BarcodeFormat.RSS14);
            Vector<BarcodeFormat> ONE_D_FORMATS = new Vector<BarcodeFormat>(PRODUCT_FORMATS.size() + 4);
            ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_39);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_93);
            ONE_D_FORMATS.add(BarcodeFormat.CODE_128);
            ONE_D_FORMATS.add(BarcodeFormat.ITF);
            Vector<BarcodeFormat> QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
            QR_CODE_FORMATS.add(BarcodeFormat.QR_CODE);
            Vector<BarcodeFormat> DATA_MATRIX_FORMATS = new Vector<BarcodeFormat>(1);
            DATA_MATRIX_FORMATS.add(BarcodeFormat.DATA_MATRIX);

            decodeFormats.addAll(ONE_D_FORMATS);
            decodeFormats.addAll(QR_CODE_FORMATS);
            decodeFormats.addAll(DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        multiFormatReader.setHints(hints);
    }

    public Result decode(String picPath){
        Result rawResult = null;

        Bitmap bitmap = BitmapUtil.getBitmapFromUrl(picPath);
        bitmap = BitmapUtil.zoomBitmap(bitmap,bitmap.getWidth() / 2,bitmap.getHeight() / 2);

        int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(data, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        RGBLuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(),bitmap.getHeight(),data);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            rawResult =  multiFormatReader.decodeWithState(binaryBitmap);
        } catch (ReaderException | IllegalArgumentException ignored) {

        } finally {
            multiFormatReader.reset();
        }

        return rawResult;
    }

    public Result decode(byte[] data, int width, int height) {
        Result rawResult = null;

        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }

        PlanarYUVLuminanceSource source = CameraManager.getInstance().buildLuminanceSource(rotatedData, height, width);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            rawResult = multiFormatReader.decodeWithState(bitmap);
        } catch (ReaderException | IllegalArgumentException ignored) {
            // continue
        } finally {
            multiFormatReader.reset();
        }
        return rawResult;
    }
}