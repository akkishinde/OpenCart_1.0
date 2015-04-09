package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.Mahaveer.adapter.CartAdapter;
import info.androidhive.Mahaveer.adapter.HistDetailAdapter;
import info.androidhive.Mahaveer.model.HistDetailList;

/**
 * Created by Splash New on 4/9/2015.
 */
public class HistoryDetails extends Activity {
    TextView orderid,invoiceid,date,payment,total,status,count;
    private static final String TAG = "";
    static String url = "";
    private ProgressDialog pDialog;
    private List<HistDetailList> movieList = new ArrayList<HistDetailList>();
    ListView listView;
    private HistDetailAdapter adapter;
    private static String pid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_details);
        orderid=(TextView)findViewById(R.id.order_id);
        invoiceid=(TextView)findViewById(R.id.invoice_no);
        date=(TextView)findViewById(R.id.date);
        payment=(TextView)findViewById(R.id.payment);
        total=(TextView)findViewById(R.id.total);
        status=(TextView)findViewById(R.id.status);
        count=(TextView)findViewById(R.id.count);
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
        url="http://webshop.opencart-api.com/api/rest/orders/";
        listView = (ListView) findViewById(R.id.list);
        adapter = new HistDetailAdapter(this, movieList);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        Intent intent = getIntent();
        pid = intent.getStringExtra("id");
        LoginActivity.client.get(getApplicationContext(),"http://webshop.opencart-api.com/api/rest/orders/"+pid, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                pDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);
                    Log.d(TAG, "Order Id : " + response);
                    if (obj.getString("success").equals("true")) {
                        JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
                        JSONObject json2=json.getJSONObject("data");
                        orderid.setText("Order ID: "+json2.getString("order_id"));
                        invoiceid.setText("Invoice Number: "+json2.getString("invoice_no"));
                        date.setText("Date Added: "+json2.getString("date_added"));
                        payment.setText("Payment Method: "+json2.getString("payment_method"));
                        total.setText("Total Amount: "+json2.getString("total").replace("000",""));
                        status.setText("Order Status: "+json2.getString("order_status"));
                        JSONArray products=json2.getJSONArray("products");
                        count.setText("Item Count: "+products.length());
                        for (int i = 0; i < products.length(); i++){
                            JSONObject prod_data = products.getJSONObject(i);
                            HistDetailList movie=new HistDetailList();
                            movie.setTitle(prod_data.getString("name"));
                            //movie.setThumbnailUrl(prod_data.getString("thumb"));
                            movie.setRating((prod_data.getString("price")));
                            movie.setYear(prod_data.getString("total"));
                            movie.setProduct_id(prod_data.getString("product_id"));
                            movieList.add(movie);
                            adapter.notifyDataSetChanged();
                            Session.getInstance().getRequestQueue();
                            ListView lv=(ListView) findViewById(R.id.list);
                            lv.setAdapter(adapter);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
