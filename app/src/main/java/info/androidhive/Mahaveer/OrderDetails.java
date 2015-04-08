package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Akshay New on 4/8/2015.
 */
public class OrderDetails extends Activity {
    TextView invoice, fname, lname, payment, total;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        invoice = (TextView) findViewById(R.id.invoice);
        fname = (TextView) findViewById(R.id.fname);
        lname = (TextView) findViewById(R.id.lname);
        payment = (TextView) findViewById(R.id.payment);
        total = (TextView) findViewById(R.id.total);
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
                LoginActivity.client.post("http://webshop.opencart-api.com/api/rest/confirm", new AsyncHttpResponseHandler() {
                    public static final String TAG = "";

                    @Override
                    public void onSuccess(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("success").equals("true")) {
                                JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
                                JSONObject json2 = json.getJSONObject("data");
                                invoice.setText("Invoice Prefix: "+json2.getString("invoice_prefix"));
                                fname.setText("First Name: "+json2.getString("firstname"));
                                lname.setText("Last Name: "+json2.getString("lastname"));
                                payment.setText("Payment Method: "+json2.getString("payment_method"));
                                JSONArray jr = json2.getJSONArray("totals");
                                JSONObject totals = jr.getJSONObject(jr.length()-1);
                                total.setText("Total: "+totals.getString("text"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }

    public void finish(View view) {

        LoginActivity.client.put("http://webshop.opencart-api.com/api/rest/confirm", new AsyncHttpResponseHandler() {
            public static final String TAG = "";

            @Override
            public void onSuccess(String response) {
                Log.i(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("success").equals("true")) {
                        Toast.makeText(getApplicationContext(), "Order ID:"+obj.getString("order_id"), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(OrderDetails.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
