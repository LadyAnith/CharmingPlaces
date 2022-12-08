package com.example.charmingplaces.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

/**
 * Utilidades para transformar imágenes
 */
public class ImageUtils {
    private ImageUtils(){
        super();
    }

    /**
     * Este método se encarga de transformar una imagen codificada en Base64 a Bitmap y lo setea a un ImagenView pasado como parámetro
     *
     * @param imageView ImagenView a settear
     * @param encodedImage imagen codificada
     */
    public static void setImage(ImageView imageView, String encodedImage) {

        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }
}
