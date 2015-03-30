package info.androidhive.Mahaveer;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import info.androidhive.Mahaveer.util.LruBitmapCache;

/**
 * Created by Splash New on 3/25/2015.
 */
public class Session extends Application {
    private String session_id;
    private String username;
    private String firstname;
    public static final String TAG = Session.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static Session mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized Session getInstance() {
        return mInstance;
    }
    public String getSession_id() {

        return session_id;
    }
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    //My own Global Data
    public void setSession_id(String sSession_id) {

        session_id = sSession_id;

    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String sUsername) {

        username = sUsername;
    }

    public String getFirstname() {

        return firstname;
    }

    public void setFirstname(String fn) {

        firstname = fn;

    }

}
