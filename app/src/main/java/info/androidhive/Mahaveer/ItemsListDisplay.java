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
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.Mahaveer.adapter.CustomListAdapter;

import info.androidhive.Mahaveer.model.ItemList;


/**
 * Created by Splash New on 3/31/2015.
 */
public class ItemsListDisplay extends Activity{
    private static String url = "";
    private ProgressDialog pDialog;
    private List<ItemList> movieList = new ArrayList<ItemList>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items);
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
        String category_id = intent.getStringExtra("category_id");
        url="http://webshop.opencart-api.com/api/rest/products/category/"+category_id;
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, movieList);
        listView.setAdapter(adapter);
        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        if (pDialog == null || !pDialog.isShowing()) {
            pDialog.show();
        }
        JsonObjectRequest movieReq = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    public static final String TAG = "";

                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d(TAG, response.toString());

                        try {
                            pDialog.hide();
                            //String data=response.getString("Data");
                            JSONArray jr=response.getJSONArray("data");
                            for (int i = 0; i < jr.length(); i++) {
                                JSONObject obj = jr.getJSONObject(i);
                                ItemList movie = new ItemList();
                                movie.setTitle(obj.getString("name"));
                                movie.setThumbnailUrl(obj.getString("image"));
                                movie.setRating((obj.getString("price")));
                                movie.setYear(obj.getString("stock_status"));
                                movie.setProduct_id(obj.getString("id"));

                                //Genre is json array
                                //JSONArray genreArry = obj.getJSONArray("category");
                                /*ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);*/

                                // adding movie to movies array
                                movieList.add(movie);

                            }} catch (JSONException e) {
                            e.printStackTrace();
                        }


                        adapter.notifyDataSetChanged();
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();

            }


        }){
            @Override
            public Map<String, String> getHeaders()  {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Oc-Merchant-Id","123");
                headers.put("X-Oc-Merchant-Language","en");
                return headers;
            }
        };
        Session.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

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
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CallViewCart() {
        Intent intent = new Intent(this, ViewCart.class);
        startActivity(intent);
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
}
