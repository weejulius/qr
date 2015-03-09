package demo.m.qr1688;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import demo.m.qr1688.model.Spu;

/**
 * Created by jyu on 15-3-5.
 */
public class SpuArrayAdapter extends ArrayAdapter<Spu> {


    public SpuArrayAdapter(Context context, List<Spu> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Get the data item for this position
       final  Spu spu = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.lo_spu, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        final ImageView ivImg = (ImageView) view.findViewById(R.id.ivSpuPic);

        // Populate the data into the template view using the data object
        tvTitle.setText(spu.subject);

        String moneySymbol = Currency.getInstance(Locale.CHINA).getSymbol();

        SpannableStringBuilder priceSb = new SpannableStringBuilder();
        priceSb.append(moneySymbol).append(" ").append(spu.discountPrice);

        priceSb.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, moneySymbol.length() + spu.discountPrice.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        priceSb.append("      ");
        priceSb.append(spu.price);

        priceSb.setSpan(new StrikethroughSpan(), priceSb.length() - spu.price.length(), priceSb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        priceSb.setSpan(new RelativeSizeSpan(0.8f), priceSb.length() - spu.price.length(), priceSb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvPrice.setText(priceSb);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("ass", spu.img + "=================================");

                try {
                    new ImageAsyncTask(ivImg).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,spu.img).get(6, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("saa",spu.img+" ??????????expired???????????????");

            }
        }).start();
        // Return the completed view to render on screen
        return view;
    }


    public static class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public ImageAsyncTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            try {
                Log.d("saa", url + " : >>>>>>>>>>>>>>>>>>");

                Bitmap bm = BitmapFactory.decodeStream(new URL(url).openStream());
                Log.d("saa", url + " : <<<<<<<<<<<<<<<<<<");
                return bm;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
