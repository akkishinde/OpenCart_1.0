package info.androidhive.Mahaveer;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import info.androidhive.Mahaveer.adapter.CustomSubCatAdapter;
import info.androidhive.Mahaveer.model.SubCat;

public class HomeFragment extends Fragment {
    private static String url = "";
    private ProgressDialog pDialog;
    private List<SubCat> movieList = new ArrayList<SubCat>();
    private ListView listView;
    private CustomSubCatAdapter adapter;
    ViewPager mHandler;
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        int key = getArguments().getInt("key");
        //Toast.makeText(getActivity(),"Key:"+key,Toast.LENGTH_SHORT).show();

        listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new CustomSubCatAdapter(getActivity(), movieList);
        listView.setAdapter(adapter);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        ImageAdapterForGallery adapterr = new ImageAdapterForGallery(
                getActivity());
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setAdapter(adapterr);
        viewPager.setCurrentItem(0);
        adapterr.setTimer(viewPager, 3);
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();
        //Here  you can add prduct links directly instead of sub Catagory.
        url = "http://webshop.opencart-api.com/api/rest/categories/parent/" + key;
        JsonObjectRequest movieReq = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    public static final String TAG = "";

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {
                            hidePDialog();
                            //String data=response.getString("Data");
                            JSONArray jr = response.getJSONArray("data");
                            for (int i = 0; i < jr.length(); i++) {
                                JSONObject obj = jr.getJSONObject(i);
                                SubCat movie = new SubCat();
                                movie.setTitle(obj.getString("name"));
                                movie.setThumbnailUrl(obj.getString("image"));
                                //movie.setRating((obj.getString("price")));
                                movie.setYear(obj.getString("category_id"));

                                // Genre is json array
                                //JSONArray genreArry = obj.getJSONArray("category");
                                /*ArrayList<String> genre = new ArrayList<String>();
                                for (int j = 0; j < genreArry.length(); j++) {
                                    genre.add((String) genreArry.get(j));
                                }
                                movie.setGenre(genre);*/

                                // adding movie to movies array
                                movieList.add(movie);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }


        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("X-Oc-Merchant-Id", "123");
                headers.put("X-Oc-Merchant-Language", "en");
                return headers;
            }
        };
        Session.getInstance().addToRequestQueue(movieReq);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

}
