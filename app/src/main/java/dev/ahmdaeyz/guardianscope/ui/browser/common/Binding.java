package dev.ahmdaeyz.guardianscope.ui.browser.common;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dev.ahmdaeyz.guardianscope.R;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Binding {

    public static String formatDate(String nonFormattedDate) {
        SimpleDateFormat adaptFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat myFormatter = new SimpleDateFormat("MMM dd,yyyy");
        String formattedDate = "";
        try {
            Date date = adaptFormat.parse(nonFormattedDate.replace(nonFormattedDate.substring(nonFormattedDate.indexOf("T")), ""));
            if (date != null) {
                formattedDate = myFormatter.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static void bindImageViewWithRoundCorners(ImageView imageView, String url) {
        Glide.with(imageView)
                .load(url)
                .placeholder(R.drawable.the_g_letter)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(30, 0)))
                .into(imageView);
    }
}
