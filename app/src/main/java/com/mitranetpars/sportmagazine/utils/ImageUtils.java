package com.mitranetpars.sportmagazine.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import com.mitranetpars.sportmagazine.SportMagazineApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import id.zelory.compressor.Compressor;

/**
 * Created by Hamed on 11/30/2016.
 */

public class ImageUtils {

    public static Bitmap compressBitmap(Bitmap selectedBitmap) throws Exception {
        File tempFile =
                File.createTempFile(UUID.randomUUID().toString(), ".bmp",
                        SportMagazineApplication.getContext().getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        selectedBitmap.compress(Bitmap.CompressFormat.WEBP, 90, outputStream);
        outputStream.close();
        selectedBitmap.recycle();

        Bitmap newBitmap =
                new Compressor.Builder(SportMagazineApplication.getContext())
                .setMaxWidth(1280)
                .setMaxHeight(960)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToBitmap(tempFile);

        tempFile.delete();
        return newBitmap;
    }

    public static Bitmap compressLogo(Bitmap selectedBitmap) throws Exception {
        File tempFile =
                File.createTempFile(UUID.randomUUID().toString(), ".bmp",
                        SportMagazineApplication.getContext().getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        selectedBitmap.compress(Bitmap.CompressFormat.WEBP, 90, outputStream);
        outputStream.close();
        selectedBitmap.recycle();

        Bitmap newBitmap =
                new Compressor.Builder(SportMagazineApplication.getContext())
                        .setMaxWidth(960)
                        .setMaxHeight(720)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .build()
                        .compressToBitmap(tempFile);

        tempFile.delete();
        return newBitmap;
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.WEBP, 90, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap decodeFromBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public static File decodeAndSaveFromBase64(String input) throws IOException {
        byte[] decodedByte = Base64.decode(input, 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);

        File tempFile =
                File.createTempFile(UUID.randomUUID().toString(), ".bmp",
                        SportMagazineApplication.getContext().getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);
        bitmap.compress(Bitmap.CompressFormat.WEBP, 90, outputStream);
        outputStream.close();
        bitmap.recycle();

        return tempFile;
    }
}
