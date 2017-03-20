package com.github.aleksandermielczarek.contactpicker.ui.bindingadapter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public final class ImageViewBindingAdapter {

    private static final ColorGenerator generator = ColorGenerator.MATERIAL;
    private static final TextDrawable.IShapeBuilder shapeBuilder = TextDrawable.builder();

    private ImageViewBindingAdapter() {

    }

    @BindingAdapter("contactNamePhoto")
    public static void setImage(ImageView imageView, String name) {
        if (!TextUtils.isEmpty(name)) {
            int color = generator.getColor(name);
            TextDrawable textDrawable = shapeBuilder.buildRound(name.substring(0, 1), color);
            imageView.setImageDrawable(textDrawable);
        }
    }

    @BindingAdapter("contactPhoto")
    public static void setImage(CircleImageView imageView, String photo) {
        Glide.with(imageView.getContext())
                .load(photo)
                .into(imageView);
    }

}
