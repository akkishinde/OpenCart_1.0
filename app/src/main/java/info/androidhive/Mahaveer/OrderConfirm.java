package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Splash New on 4/6/2015.
 */
public class OrderConfirm extends Activity {
    static int flag=1;
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
        agree=(CheckBox)findViewById(R.id.checkBox);
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

        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=0;
                address1.setVisibility(View.VISIBLE);
                address2.setVisibility(View.VISIBLE);
                pincode.setVisibility(View.VISIBLE);
                city.setVisibility(View.VISIBLE);
            }
        });
    }

    public void confirm(View view) {
        View v;
        Toast toast;
        TextView text;
        if(flag==0) {
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
        if(flag==1){
            Toast.makeText(getApplicationContext(), "Existing Address", Toast.LENGTH_SHORT).show();
        }
    }

    private void invokeWS() {

            Toast.makeText(getApplicationContext(), "New Address", Toast.LENGTH_SHORT).show();
        }
}
