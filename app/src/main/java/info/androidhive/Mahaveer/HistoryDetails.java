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

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
