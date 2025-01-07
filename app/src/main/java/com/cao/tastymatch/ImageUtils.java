package com.cao.tastymatch;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageUtils {
    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream); // Сжатие изображения
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT); // Преобразование в строку Base64
    }

    public static boolean isBase64Encoded(String string) {
        try {
            byte[] decodedBytes = android.util.Base64.decode(string, android.util.Base64.DEFAULT);
            return decodedBytes.length > 0;
        } catch (IllegalArgumentException e) {
            return false; // Если строка не декодируется, значит это не Base64
        }
    }

    public static Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
        return android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
