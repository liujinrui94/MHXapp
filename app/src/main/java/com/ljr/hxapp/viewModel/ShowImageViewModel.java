package com.ljr.hxapp.viewModel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ljr.hxapp.databinding.ActShowPicBinding;

/**
 * @author:liujinrui
 * @Date:2019/1/30
 * @Description:
 */
public class ShowImageViewModel {
    private ActShowPicBinding binding;
    private String image;

    public ShowImageViewModel(ActShowPicBinding binding, String image) {
        this.binding = binding;
        this.image = image;

    }


    public String getImageUrl() {
        // The URL will usually come fromI a model (i.e Profile)
        return image;
    }

    @BindingAdapter({"bind:ImageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).into(view);
    }



}
