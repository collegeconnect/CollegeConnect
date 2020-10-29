package com.college.collegeconnect.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.io.IOException;
import java.util.List;

public class ImageCreateAdapter extends PagerAdapter {
    List<Uri> eventImages;
    Context context;
    Uri filePath;
    Bitmap bitmap;

    public ImageCreateAdapter(List<Uri> eventImages, Context context) {
        this.eventImages = eventImages;
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        filePath = eventImages.get(position);
        try {
            if (Build.VERSION.SDK_INT < 28)
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
            else
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.getContentResolver(), filePath));

            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return eventImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }
}
