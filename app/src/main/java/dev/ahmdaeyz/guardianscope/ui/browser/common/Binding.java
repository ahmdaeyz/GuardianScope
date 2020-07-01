package dev.ahmdaeyz.guardianscope.ui.browser.common;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import dev.ahmdaeyz.guardianscope.R;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Binding {

    public static String formatDate(long fromEpoch) {
//        SimpleDateFormat adaptFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("MMM dd,yyyy");
        String formattedDate;
        LocalDate date = LocalDate.ofEpochDay(fromEpoch);
        formattedDate = myFormatter.format(date);

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
