package com.mitranetpars.sportmagazine.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.MemoryFile;
import android.util.Base64;

import com.mitranetpars.sportmagazine.SportMagazineApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Hamed on 11/30/2016.
 */

public class ImageUtils {
    public static int SIZE_LIMIT = 512 * 1024;

    public static Bitmap compressBitmap(Bitmap selectedBitmap, int sampleSize, int quality) throws Exception {
        File tempFile =
                File.createTempFile("tempbmp", "bmp",
                        SportMagazineApplication.getContext().getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        selectedBitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
        outputStream.close();

        long lengthInKb = tempFile.length(); //in kb
        if (lengthInKb > SIZE_LIMIT) {
            tempFile.delete();
            return compressBitmap(selectedBitmap, (sampleSize * 2), (quality / 4));
        }

        selectedBitmap.recycle();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        FileInputStream inputStream = new FileInputStream(tempFile);
        Bitmap newBitmap = BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();
        tempFile.delete();
        return newBitmap;
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeFromBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
