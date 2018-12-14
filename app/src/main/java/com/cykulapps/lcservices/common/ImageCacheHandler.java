package com.cykulapps.lcservices.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.cykulapps.lcservices.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCacheHandler {

    public static ImageCacheHandler instance;

    private Context mContext;
    private ContextWrapper contextWrapper;
    private File directory;

    private Bitmap bitmap = null;

    private ImageCacheHandler(Context context) {

        mContext = context;
        contextWrapper = new ContextWrapper(mContext);
        directory = contextWrapper.getDir("image_cache", Context.MODE_PRIVATE);

    }

    public static ImageCacheHandler getInstance(Context context) {
        if (instance == null)
            instance = new ImageCacheHandler(context);
        return instance;

    }

    public void setImage(final ImageView imageView, final String userId, String imageURL) {
        bitmap = findImageFromMemory(userId);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            Picasso.get()
                    .load(imageURL)
                    .placeholder(R.drawable.loading)   // optional
                    .error(R.drawable.loading)
                    .fit()
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            /*try {
                                //saveImageToMemory(userId, ((BitmapDrawable) imageView.getDrawable()).getBitmap());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // bitmap = findImageFromMemory(userId);*/

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }
    }


    /**
     * Method to save image to the memory
     * @param name name with which the image should be saved
     * @param bitmap bitmap of the image
     * @return returns true if the operation is successful
     * @throws IOException
     */
    private boolean saveImageToMemory(String name, Bitmap bitmap) throws IOException {

        FileOutputStream fileOutputStream = null;

        try {
            File image = new File(directory, name + ".jpeg");
            fileOutputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fileOutputStream != null)
                fileOutputStream.close();
        }
    }


    private Bitmap findImageFromMemory(String userId) {

        if (userId != null) {
            try {
                File file = new File(directory, userId + ".jpeg");
                Log.d("ImageID", file + "");
                if (file.exists()) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    return BitmapFactory.decodeStream(new FileInputStream(file), null, options);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


}
