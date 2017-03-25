package com.github.aleksandermielczarek.contactpicker.ui.bindingadapter;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.github.aleksandermielczarek.contactpicker.domain.data.Contact;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aleksander Mielczarek on 03.12.2016.
 */

public final class ImageViewBindingAdapter {

    private static final ColorGenerator generator = ColorGenerator.MATERIAL;
    private static final TextDrawable.IShapeBuilder shapeBuilder = TextDrawable.builder();

    private ImageViewBindingAdapter() {

    }

    @BindingAdapter("contactPhoto")
    public static void setImage(ImageView imageView, Contact contact) {
        if (contact != null && contact.isPrimaryNumber() && TextUtils.isEmpty(contact.getPhoto())) {
            imageView.setVisibility(View.VISIBLE);
            String name = contact.getName();
            int color = generator.getColor(name);
            TextDrawable textDrawable = shapeBuilder.buildRound(name.substring(0, 1), color);
            imageView.setImageDrawable(textDrawable);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @BindingAdapter("contactPhoto")
    public static void setImage(CircleImageView imageView, Contact contact) {
        if (contact != null && contact.isPrimaryNumber() && !TextUtils.isEmpty(contact.getPhoto())) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(imageView.getContext())
                    .load(contact.getPhoto())
                    .into(imageView);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

}
