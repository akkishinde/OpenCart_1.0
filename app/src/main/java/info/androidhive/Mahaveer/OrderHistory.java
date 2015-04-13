package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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

import java.util.ArrayList;
import java.util.List;


import info.androidhive.Mahaveer.adapter.OrderHistAdapter;
import info.androidhive.Mahaveer.model.OrderHistList;

/**
 * Created by Aksahy on 4/8/2015.
 * This activity will give the list of orders in Order History MENU.
 */
public class OrderHistory extends Activity{
    private static final String TAG = "";
    static String url = "";
    ListView listView;
    private ProgressDialog pDialog;
    private List<OrderHistList> movieList = new ArrayList<OrderHistList>();
    private OrderHistAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);
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
        final Session session = (Session) getApplicationContext();
        final String uid=session.getUser_id();
        url="http://webshop.opencart-api.com/api/rest/orders/user/";
        listView = (ListView) findViewById(R.id.list);
        adapter = new OrderHistAdapter(this,movieList);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        LoginActivity.client.addHeader("X-Oc-Session",Session.getInstance().getSession_id());
        LoginActivity.client.get(getApplicationContext(),url+uid, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "View Cart : " + response);
                pDialog.hide();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("success").equals("true")) {
                        JSONArray orders=obj.getJSONArray("data");
                        for (int i = 0; i < orders.length(); i++){
                            JSONObject prod_data = orders.getJSONObject(i);
                            OrderHistList movie=new OrderHistList();
                            movie.setTitle(prod_data.getString("date_added"));
                            movie.setRating((prod_data.getString("status")));
                            movie.setYear(prod_data.getString("total"));
                            movie.setOrder_id(prod_data.getString("order_id"));
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
