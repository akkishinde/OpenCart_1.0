package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Aksahy on 3/31/2015.
 * This activity will display the details of the items.
 * You can buy the items or add to wishlist from this activity.
 */
public class ItemsDetails extends Activity {
    private ProgressDialog pDialog;
    NetworkImageView item_header, im1, im2, im3, im4;
    ImageButton addcart, addwish;
    TextView title, manufacturer, item_name, brand, stock, price, description;
    EditText qty;
    ArrayList<String> imageadapter = new <String>ArrayList<String>();
    private static String pid;
    ImageLoader imageLoader = Session.getInstance().getImageLoader();
    String url = "http://webshop.opencart-api.com/api/rest/products/";
    static String headurl = "";
    static String item_id = "";
    static String item_qty = "";
     int empty_flag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        title = (TextView) findViewById(R.id.name);
        manufacturer = (TextView) findViewById(R.id.manufacturer);
        item_header = (NetworkImageView) findViewById(R.id.thumbnail);
        im1 = (NetworkImageView) findViewById(R.id.imageView2);
        im2 = (NetworkImageView) findViewById(R.id.imageView3);
        im3 = (NetworkImageView) findViewById(R.id.imageView4);
        im4 = (NetworkImageView) findViewById(R.id.imageView5);
        item_name = (TextView) findViewById(R.id.item_name);
        brand = (TextView) findViewById(R.id.brand_name);
        stock = (TextView) findViewById(R.id.stock_status);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        addcart = (ImageButton) findViewById(R.id.addCartButton);
        addwish = (ImageButton) findViewById(R.id.addWishButton);
        qty = (EditText) findViewById(R.id.editText);
        ActionBar mActionBar = getActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayHomeAsUpEnabled(true);
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
        if (pDialog == null || !pDialog.isShowing()) {
            pDialog.show();
        }
        im2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item_header.setImageUrl(imageadapter.get(0), imageLoader);
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item_header.setImageUrl(imageadapter.get(1), imageLoader);
            }
        });
        im4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item_header.setImageUrl(imageadapter.get(2), imageLoader);
            }
        });
        im1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item_header.setImageUrl(headurl, imageLoader);
            }
        });

        addcart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (empty_flag == 0) {
                    item_qty = qty.getText().toString();
                    StringEntity entity = null;
                    JSONObject innerObj = new JSONObject();
                    JSONObject outerObj = new JSONObject();
                    try {
                        innerObj.put(item_id, item_qty);
                        outerObj.put("quantity", innerObj);
                        entity = new StringEntity(outerObj.toString());

                    } catch (JSONException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    LoginActivity.client.put(getApplicationContext(), "http://webshop.opencart-api.com/api/rest/cart", entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            View v;
                            Toast toast;
                            TextView text;
                            toast = Toast.makeText(getApplicationContext(), "Item added to Cart!", Toast.LENGTH_SHORT);
                            v = toast.getView();
                            text = (TextView) v.findViewById(android.R.id.message);
                            text.setTextColor(getResources().getColor(R.color.mWhite));
                            text.setShadowLayer(0, 0, 0, 0);
                            v.setBackgroundResource(R.color.mTeal);
                            toast.show();
                            title.setFocusable(true);
                            title.setFocusableInTouchMode(true);
                            qty.setText("");


                        }

                        @Override
                        public void onFailure(int statusCode, Throwable error,
                                              String content) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at Server Side! " + statusCode, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {

                    Toast toast;
                    TextView text;
                    toast = Toast.makeText(getApplicationContext(), "Out Of Stock", Toast.LENGTH_SHORT);
                    v = toast.getView();
                    text = (TextView) v.findViewById(android.R.id.message);
                    text.setTextColor(getResources().getColor(R.color.mWhite));
                    text.setShadowLayer(0, 0, 0, 0);
                    v.setBackgroundResource(R.color.mRed);
                    toast.show();
                    title.setFocusable(true);
                    title.setFocusableInTouchMode(true);
                    qty.setText("");

                }
            }
        });

        addwish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LoginActivity.client.post(getApplicationContext(), "http://webshop.opencart-api.com/api/rest/wishlist/" + item_id, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        View v;
                        Toast toast;
                        TextView text;
                        toast = Toast.makeText(getApplicationContext(), "Item added to WishList!", Toast.LENGTH_SHORT);
                        v = toast.getView();
                        text = (TextView) v.findViewById(android.R.id.message);
                        text.setTextColor(getResources().getColor(R.color.mWhite));
                        text.setShadowLayer(0, 0, 0, 0);
                        v.setBackgroundResource(R.color.mTeal);
                        toast.show();
                        title.setFocusable(true);
                        title.setFocusableInTouchMode(true);


                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        Toast.makeText(getApplicationContext(), "Something went wrong at Server Side! " + statusCode, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void LoadData() {
        url = "http://webshop.opencart-api.com/api/rest/products/" + pid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("X-Oc-Merchant-Id", "123");
        client.addHeader("X-Oc-Merchant-Language", "en");
        client.get(getApplicationContext(), url, new AsyncHttpResponseHandler() {
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
                        item_id = json2.getString("id");
                        manufacturer.setText(json2.getString("manufacturer"));
                        item_header.setImageUrl(json2.getString("image"), imageLoader);
                        headurl = json2.getString("image");
                        im1.setImageUrl(json2.getString("image"), imageLoader);
                        JSONArray jsonArray = json2.getJSONArray("images");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            imageadapter.add(jsonArray.get(i).toString());
                        }
                        if (imageadapter.size() > 0) {
                            im2.setEnabled(true);
                            im2.setVisibility(View.VISIBLE);
                            im2.setImageUrl(imageadapter.get(0), imageLoader);
                        }
                        if (imageadapter.size() > 1) {
                            im3.setEnabled(true);
                            im3.setVisibility(View.VISIBLE);
                            im3.setImageUrl(imageadapter.get(1), imageLoader);
                        }
                        if (imageadapter.size() > 2) {
                            im4.setEnabled(true);
                            im4.setVisibility(View.VISIBLE);
                            im4.setImageUrl(imageadapter.get(2), imageLoader);
                        }
                        item_name.setText(json2.getString("name"));
                        brand.setText("Manufacturer: " + json2.getString("manufacturer"));
                        stock.setText("Stock: " + json2.getString("stock_status"));
                        if (json2.getString("stock_status").equals("Out Of Stock")) {
                            empty_flag = 1;
                        }
                        price.setText("Price: " + json2.getString("price"));
                        description.setText(json2.getString("description").replaceAll("<(.*?)\\>", " ")
                                .replaceAll("<(.*?)\\\n", " ")
                                .replaceFirst("(.*?)\\>", " ")
                                .replaceAll("&nbsp;", " ")
                                .replaceAll("&amp;", " ")
                                .replaceAll("&#39;", "'")
                                .replaceAll("&quot;", "\""));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                pDialog.hide();
                Toast.makeText(getApplicationContext(), "Something went wrong at server side!", Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
       /* SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
*/
        return super.onCreateOptionsMenu(menu);
        // getMenuInflater().inflate(R.menu.main, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title


        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                CallViewCart();
                return true;
          /*  case R.id.action_search:
                return true;*/
            case R.id.action_order_hist:
                CallViewHistory();
                return true;
            case R.id.action_wishlist:
                CallWishList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CallWishList() {
        Intent intent = new Intent(this, ViewWish.class);
        startActivity(intent);
    }

    private void CallViewCart() {
        Intent intent = new Intent(this, ViewCart.class);
        startActivity(intent);
    }

    private void CallViewHistory() {
        Intent intent = new Intent(this, OrderHistory.class);
        startActivity(intent);
    }

}
