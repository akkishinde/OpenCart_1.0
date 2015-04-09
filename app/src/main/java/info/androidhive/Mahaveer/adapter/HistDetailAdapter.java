package info.androidhive.Mahaveer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import info.androidhive.Mahaveer.ItemsDetails;
import info.androidhive.Mahaveer.R;
import info.androidhive.Mahaveer.Session;
import info.androidhive.Mahaveer.model.CartList;
import info.androidhive.Mahaveer.model.HistDetailList;

/**
 * Created by Splash New on 4/9/2015.
 */
public class HistDetailAdapter extends BaseAdapter{


    private static final String TAG = "";
    private Activity activity;
    private LayoutInflater inflater;
    private List<HistDetailList> movieItems;
    ImageLoader imageLoader = Session.getInstance().getImageLoader();


    public HistDetailAdapter(Activity activity, List<HistDetailList> movieItems) {
        super();
        this.activity = activity;
        this.movieItems = movieItems;
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location) {
        return movieItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.hist_list_row, null);

        if (imageLoader == null)
            imageLoader = Session.getInstance().getImageLoader();
       /* NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);*/
        TextView title = (TextView) convertView.findViewById(R.id.title);
        Log.i(TAG,"title"+title.toString());
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
         HistDetailList m = movieItems.get(position);

        // thumbnail image
//        thumbNail.setImageUrl(m.getThumbnailUrl(), imageLoader);

        // title
        title.setText(m.getTitle());

        // rating
        rating.setText("Price: " + String.valueOf(m.getRating()));

        // genre
       /* String genreStr = "";
        for (String str : m.getGenre()) {
            genreStr += str + ", ";
        }
        genreStr = genreStr.length() > 0 ? genreStr.substring(0,
                genreStr.length() - 2) : genreStr;
        genre.setText(genreStr);
*/
        // release year
        year.setText(String.valueOf(m.getYear()));
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HistDetailList m = movieItems.get(position);
                Intent intent = new Intent(activity, ItemsDetails.class);
                intent.putExtra("id", m.getProduct_id());

                activity.startActivity(intent);
            }
        });

        return convertView;
    }


}
