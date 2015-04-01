package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Splash New on 3/31/2015.
 */
public class ItemsDetails extends Activity{
    NetworkImageView main,im1,im2,im3;
    TextView title,manufacturer;
    ImageLoader imageLoader = Session.getInstance().getImageLoader();
    String url="http://webshop.opencart-api.com:80/api/rest/products/47";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        title= (TextView) findViewById(R.id.name);
        manufacturer=(TextView) findViewById(R.id.manufacturer);
        main= (NetworkImageView) findViewById(R.id.thumbnail);

        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.mOrange)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources()
                    .getColor(R.color.mOrangeDark));
        }
        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        if (actionBarTitleId > 0) {
            TextView title = (TextView) findViewById(actionBarTitleId);
            if (title != null) {
                title.setTextColor(getResources()
                        .getColor(R.color.mWhite));
            }
        }
       // Intent intent = getIntent();
        //String title = intent.getStringExtra("title");
       // Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
        LoadData();

    }

    private void LoadData() {
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("X-Oc-Merchant-Id","123");
        client.addHeader("X-Oc-Merchant-Language","en");
        client.get(getApplicationContext(), "http://webshop.opencart-api.com/api/rest/products/47", new AsyncHttpResponseHandler() {
            public static final String TAG = "";

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getString("success").equals("true")) {
                        JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
                        JSONObject json2 = json.getJSONObject("data");
                        //Toast.makeText(getApplicationContext(), "title:", Toast.LENGTH_SHORT).show();
                        title.setText(json2.getString("name"));
                        manufacturer.setText(json2.getString("manufacturer"));
                        main.setImageUrl(json2.getString("image"), imageLoader);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

            }

        });
    }


}
