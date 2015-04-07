package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;

/**
 * Created by Akshay on 4/6/2015.
 */
public class OrderConfirm extends Activity {
    String TAG="";
    static int flag = 1;
    String str_fname, str_lname, str_add1, str_add2, str_pincode, str_city, str_state;
    static String address_id;
    TextView fname, lname, xadd1, xadd2, xcity, xpincode, xstate;
    static EditText address1, address2, pincode, city;
    Button switch1;
    CheckBox agree;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_confirm);
        switch1 = (Button) findViewById(R.id.existing_address);
        address1 = (EditText) findViewById(R.id.address1_text);
        address2 = (EditText) findViewById(R.id.address2_text);
        pincode = (EditText) findViewById(R.id.postal_text);
        city = (EditText) findViewById(R.id.city_text);
        agree = (CheckBox) findViewById(R.id.checkBox);

        fname = (TextView) findViewById(R.id.fname);
        lname = (TextView) findViewById(R.id.lname);
        xadd1 = (TextView) findViewById(R.id.address1);
        xadd2 = (TextView) findViewById(R.id.address2);
        xcity = (TextView) findViewById(R.id.city);
        xpincode = (TextView) findViewById(R.id.pin);
        xstate = (TextView) findViewById(R.id.state);
       /* address1.setVisibility(View.INVISIBLE);
        address2.setVisibility(View.INVISIBLE);
        pincode.setVisibility(View.INVISIBLE);
        city.setVisibility(View.INVISIBLE);*/
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
        GetExistingAddress();
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 0;
                address1.setVisibility(View.VISIBLE);
                address2.setVisibility(View.VISIBLE);
                pincode.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
            }
        });
    }

    //This Method is called on the creation of the activity
    private void GetExistingAddress() {
        LoginActivity.client.get(getApplicationContext(), "http://webshop.opencart-api.com/api/rest/paymentaddress", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("success").equals("true")) {
                        JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
                        JSONObject json2 = json.getJSONObject("data");
                        JSONObject json3 = (JSONObject) new JSONTokener(json2.toString()).nextValue();
                        JSONObject json4 = json3.getJSONObject("addresses");
                        JSONObject json5 = (JSONObject) new JSONTokener(json4.toString()).nextValue();
                        address_id = json2.getString("address_id");
                        JSONObject json6 = json5.getJSONObject(address_id);
                        str_fname = json6.getString("firstname");
                        str_lname = json6.getString("lastname");
                        str_add1 = json6.getString("address_1");
                        str_add2 = json6.getString("address_2");
                        str_pincode = json6.getString("postcode");
                        str_city = json6.getString("city");
                        str_state = json6.getString("zone");

                        fname.setText("First Name: " + str_fname);
                        lname.setText("Last Name: " + str_lname);
                        xadd1.setText("Address1: " + str_add1);
                        xadd2.setText("Address2: " + str_add2);
                        xcity.setText("City: " + str_city);
                        xpincode.setText("Pincode: " + str_pincode);
                        xstate.setText("State: " + str_state);
                        Toast.makeText(getApplicationContext(), "Name:" + str_fname, Toast.LENGTH_SHORT).show();

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

    public void confirm(View view) {
        View v;
        Toast toast;
        TextView text;
        if (flag == 0) {
            if (Validation.isNotNull(address1.getText().toString()) && Validation.isNotNull(address2.getText().toString())
                    && Validation.isNotNull(pincode.getText().toString())) {
                if (agree.isChecked()) {
                    invokeWS();
                } else {
                    toast = Toast.makeText(getApplicationContext(), "Please Accept the payment method!", Toast.LENGTH_SHORT);
                    v = toast.getView();
                    text = (TextView) v.findViewById(android.R.id.message);
                    text.setTextColor(getResources().getColor(R.color.mWhite));
                    text.setShadowLayer(0, 0, 0, 0);
                    v.setBackgroundResource(R.color.mTeal);
                    //toast.setGravity(Gravity.TOP, 0, 950);
                    toast.show();
                }
            } else {
                toast = Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_SHORT);
                v = toast.getView();
                text = (TextView) v.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.mWhite));
                text.setShadowLayer(0, 0, 0, 0);
                v.setBackgroundResource(R.color.mTeal);
                //toast.setGravity(Gravity.TOP, 0, 950);
                toast.show();
            }
        }
        if (flag == 1) {
            // Toast.makeText(getApplicationContext(), "Existing Address", Toast.LENGTH_SHORT).show();
            AddPaymentAddress();
        }
    }


    private void AddPaymentAddress() {
        StringEntity entity = null;

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("payment_address", "existing");
            jsonParams.put("address_id", address_id);
            entity = new StringEntity(jsonParams.toString());
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        LoginActivity.client.post(getApplicationContext(), "http://webshop.opencart-api.com/api/rest/paymentaddress", entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.i(TAG, response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("success").equals("true")) {
                        AddShipmentAddress();
                        //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void AddShipmentAddress() {
                StringEntity entity = null;

                try {
                    JSONObject jsonParams = new JSONObject();
                    jsonParams.put("shipping_address", "existing");
                    jsonParams.put("address_id", address_id);
                    entity = new StringEntity(jsonParams.toString());

                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                LoginActivity.client.post(getApplicationContext(), "http://webshop.opencart-api.com/api/rest/shippingaddress", entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i(TAG, response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("success").equals("true")) {

                                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                GetShippingMethod();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void GetShippingMethod() {
                        StringEntity entity = null;

                        try {
                            JSONObject jsonParams = new JSONObject();
                            jsonParams.put("shipping_method", "flat.flat");
                            jsonParams.put("comment", "test comment");
                            entity = new StringEntity(jsonParams.toString());

                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        LoginActivity.client.post(getApplicationContext(), "http://webshop.opencart-api.com/api/rest/shippingaddress", entity, "application/json", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                Log.i(TAG, response);
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getString("success").equals("true")) {

                                        //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                        GetPaymentMethod();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            private void GetPaymentMethod() {


                            }

                        });
                    }
                });
            }
        });
    }

    private void invokeWS() {

        Toast.makeText(getApplicationContext(), "New Address", Toast.LENGTH_SHORT).show();
    }
}
