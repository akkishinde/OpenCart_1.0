package info.androidhive.Mahaveer;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;

/**
 * Created by Akshay on 3/24/2015.
 * After Splash screen this activity will be invoked.
 * After successful login you will be redirected to the Main Activity.
 */
public class LoginActivity extends Activity{
    public static AsyncHttpClient client = new AsyncHttpClient();
    private static final String TAG = "";
    EditText username,password;
    ProgressDialog prgDialog;
    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private final String DefaultUnameValue = "";
    private String UnameValue;
    private final String DefaultPasswordValue = "";
    private String PasswordValue;
    private void loadPreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        username.setText(UnameValue);
        password.setText(PasswordValue);
    }
    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        UnameValue = (username.getText()).toString();
        PasswordValue = (password.getText()).toString();
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.apply();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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
        username=(EditText)findViewById(R.id.username_text);
        password=(EditText)findViewById(R.id.password_text);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        loadPreferences();

    }
    public void login(View view)
    {   View v;
        Toast toast;
        TextView text;


        String nowEmail = username.getText().toString();
        String nowPass = password.getText().toString();
        if (Validation.isNotNull(nowEmail) && Validation.isNotNull(nowPass)) {
            if (Validation.validate(nowEmail)) {
                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                doLogin();


            } else {
                toast=Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_SHORT);
                v = toast.getView();
                text = (TextView) v.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.mWhite));
                text.setShadowLayer(0,0,0,0);
                v.setBackgroundResource(R.color.mTeal);
                //toast.setGravity(Gravity.TOP, 0, 950);
                toast.show();
            }

        } else {

            toast=Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_SHORT);
            v = toast.getView();
            text = (TextView) v.findViewById(android.R.id.message);
            text.setTextColor(getResources().getColor(R.color.mWhite));
            text.setShadowLayer(0,0,0,0);
            v.setBackgroundResource(R.color.mTeal);
            //toast.setGravity(Gravity.TOP, 0, 950);
            toast.show();
        }

    }

    private void doLogin() {

        StringEntity entity = null;
        JSONObject jsonParams = new JSONObject();
        String email=username.getText().toString();
        String pass=password.getText().toString();
        if (prgDialog == null || !prgDialog.isShowing()) {
            assert prgDialog != null;
            prgDialog.show();
        }
        try {
            jsonParams.put("email", email);
            jsonParams.put("password", pass);
            entity = new StringEntity(jsonParams.toString());
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.addHeader("X-Oc-Merchant-Id","123456");
        client.addHeader("X-Oc-Merchant-Language","en");
        client.post(getApplicationContext(), "http://mahaveersupermarket.com/api/rest/login", entity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                //public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                View v;
                Toast toast;
                TextView text;
                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("success").equals("true")) {
                        savePreferences();
                        prgDialog.hide();
                        JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
                        JSONObject json2 = json.getJSONObject("data");
                        String sessionID=json2.getString("session");
                        String first_name=json2.getString("firstname");
                        String username=json2.getString("email");
                        final Session session = (Session) getApplicationContext();
                        session.setSession_id(sessionID);
                        session.setFirstname(first_name);
                        session.setUsername(username);
                        session.setUser_id(json2.getString("customer_id"));
                        /*for(i=0;i<headers.length;i++)
                        {
                            //Log.i(TAG,"i="+i+" "+headers[i]+"\n");
                        }*/
                        toast=Toast.makeText(getApplicationContext(), "Welcome "+first_name, Toast.LENGTH_SHORT);
                        v = toast.getView();
                        text = (TextView) v.findViewById(android.R.id.message);
                        text.setTextColor(getResources().getColor(R.color.mWhite));
                        text.setShadowLayer(0,0,0,0);
                        v.setBackgroundResource(R.color.mGreen);
                        //toast.setGravity(Gravity.TOP, 0, 950);
                        toast.show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        //String error=obj.getString("error");
                        prgDialog.hide();
                        toast=Toast.makeText(getApplicationContext(), "Username & Password Mismatch", Toast.LENGTH_SHORT);
                        v = toast.getView();
                        text = (TextView) v.findViewById(android.R.id.message);
                        text.setTextColor(getResources().getColor(R.color.mWhite));
                        text.setShadowLayer(0,0,0,0);
                        v.setBackgroundResource(R.color.mRed);
                        //toast.setGravity(Gravity.TOP, 0, 950);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                prgDialog.hide();
                View v;
                Toast toast;
                TextView text;
                toast=Toast.makeText(getApplicationContext(), "Something went wrong at Server Side!", Toast.LENGTH_SHORT);
                v = toast.getView();
                text = (TextView) v.findViewById(android.R.id.message);
                text.setTextColor(getResources().getColor(R.color.mWhite));
                text.setShadowLayer(0,0,0,0);
                v.setBackgroundResource(R.color.mRed);
                //toast.setGravity(Gravity.TOP, 0, 950);
                toast.show();
            }

        });
    }

    public void register(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    public void skip(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {


            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_delete).setTitle("Exit")
                    .setMessage("Are you sure?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    System.runFinalizersOnExit(true);
                                    System.exit(0);
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                }
                            }

                    ).setNegativeButton("no", null).show();
        }

}