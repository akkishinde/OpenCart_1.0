package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

/**
 * Created by Splash New on 3/31/2015.
 */
public class ItemsDetails extends Activity{
    private ProgressDialog pDialog;
    NetworkImageView item_header,im1,im2,im3,im4;
    TextView title,manufacturer,item_name,brand,stock,price;
    ArrayList<String> imageadapter = new <String>ArrayList<String>();
    private static String pid;
    ImageLoader imageLoader = Session.getInstance().getImageLoader();
    String url="http://webshop.opencart-api.com/api/rest/products/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        title= (TextView) findViewById(R.id.name);
        manufacturer=(TextView) findViewById(R.id.manufacturer);
        item_header= (NetworkImageView) findViewById(R.id.thumbnail);
        im1=(NetworkImageView) findViewById(R.id.imageView2);
        im2=(NetworkImageView) findViewById(R.id.imageView3);
        im3=(NetworkImageView) findViewById(R.id.imageView4);
        im4=(NetworkImageView) findViewById(R.id.imageView5);
        item_name=(TextView)findViewById(R.id.item_name);
        brand=(TextView)findViewById(R.id.brand_name);
        stock=(TextView)findViewById(R.id.stock_status);
        price=(TextView)findViewById(R.id.price);

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
        Intent intent = getIntent();
        pid = intent.getStringExtra("id");
        //Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
        LoadData();
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    private void LoadData() {
        url="http://webshop.opencart-api.com/api/rest/products/"+pid;
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("X-Oc-Merchant-Id","123");
        client.addHeader("X-Oc-Merchant-Language","en");
        client.get(getApplicationContext(),url, new AsyncHttpResponseHandler() {
            public static final String TAG = "";

            @Override
            public void onSuccess(String response) {
                pDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getString("success").equals("true")) {
                        JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
                        JSONObject json2 = json.getJSONObject("data");
                        //Toast.makeText(getApplicationContext(), "title:", Toast.LENGTH_SHORT).show();
                        title.setText(json2.getString("name"));
                        manufacturer.setText(json2.getString("manufacturer"));
                        item_header.setImageUrl(json2.getString("image"), imageLoader);
                        im1.setImageUrl(json2.getString("image"),imageLoader);
                        JSONArray jsonArray=json2.getJSONArray("images");
                        for(int i=0;i<jsonArray.length();i++) {
                          imageadapter.add(jsonArray.get(i).toString());
                        }
                        if(imageadapter.size()>0)
                        im2.setImageUrl(imageadapter.get(0), imageLoader);
                        if(imageadapter.size()>1)
                        im3.setImageUrl(imageadapter.get(1), imageLoader);
                        if(imageadapter.size()>2)
                        im4.setImageUrl(imageadapter.get(2), imageLoader);

                        item_name.setText(json2.getString("name"));
                        brand.setText("Manufacturer: "+json2.getString("manufacturer"));
                        stock.setText("Stock: " + json2.getString("stock_status"));
                        price.setText("Price: "+json2.getString("price"));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                pDialog.hide();
                Toast.makeText(getApplicationContext(),"Something went wrong at server side!",Toast.LENGTH_SHORT).show();
            }

        });
    }


}
