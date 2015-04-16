package info.androidhive.Mahaveer;

import info.androidhive.Mahaveer.adapter.NavDrawerListAdapter;
import info.androidhive.Mahaveer.model.NavDrawerItem;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    Bundle args = new Bundle();
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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
        mTitle = mDrawerTitle = getTitle();

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        TypedArray navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        // Communities, Will add a counter here
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10], navMenuIcons.getResourceId(10, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[11], navMenuIcons.getResourceId(11, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[12], navMenuIcons.getResourceId(12, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[13], navMenuIcons.getResourceId(13, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[14], navMenuIcons.getResourceId(14, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[15], navMenuIcons.getResourceId(15, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[16], navMenuIcons.getResourceId(16, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[17], navMenuIcons.getResourceId(17, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[18], navMenuIcons.getResourceId(18, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[19], navMenuIcons.getResourceId(19, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[20], navMenuIcons.getResourceId(20, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[21], navMenuIcons.getResourceId(21, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[22], navMenuIcons.getResourceId(22, -1)));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[23], navMenuIcons.getResourceId(23, -1)));
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[24]));


        // Recycle the typed array
        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        // enabling action bar app icon and behaving it as toggle button
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            displayView(0);
        }
    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
     /*   SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));*/

        return super.onCreateOptionsMenu(menu);
        // getMenuInflater().inflate(R.menu.main, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                CallViewCart();
                return true;
         /*   case R.id.action_search:
                 CallSearchCart();
                return true;*/
            case R.id.action_order_hist:
                CallViewHistory();
                return true;
            case R.id.action_help:
                CallHelp();
                return true;
            case R.id.action_wishlist:
                CallWishList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void CallWishList() {
        Intent intent = new Intent(this, ViewWish.class);
        startActivity(intent);
    }

    private void CallHelp() {
        View messageView = getLayoutInflater().inflate(R.layout.help, null, false);

        // When linking text, force to always use default color. This works
        // around a pressed color state bug.
        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
       /* int defaultColor = textView.getTextColors().getDefaultColor();
        textView.setTextColor(defaultColor);
*/


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    private void CallSearchCart() {
        Intent intent = new Intent(this, SearchResultsActivity.class);

        //intent.putExtra("query",);

    }

    private void CallViewHistory() {
        Intent intent = new Intent(this, OrderHistory.class);
        startActivity(intent);
    }

    private void CallViewCart() {
        Intent intent = new Intent(this, ViewCart.class);
        startActivity(intent);
    }

    /* *
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();

                args.putInt("key", 76);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = new HomeFragment();
                args.putInt("key", 73);
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new HomeFragment();
                args.putInt("key", 68);
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new HomeFragment();
                args.putInt("key", 77);
                fragment.setArguments(args);
                break;
            case 4:
                fragment = new HomeFragment();
                args.putInt("key", 79);
                fragment.setArguments(args);
                break;
            case 5:
                fragment = new HomeFragment();
                args.putInt("key", 63);
                fragment.setArguments(args);
                break;
            case 6:
                fragment = new HomeFragment();
                args.putInt("key", 70);
                fragment.setArguments(args);
                break;
            case 7:
                fragment = new HomeFragment();
                args.putInt("key", 69);
                fragment.setArguments(args);
                break;
            case 8:
                fragment = new HomeFragment();
                args.putInt("key", 78);
                fragment.setArguments(args);
                break;
            case 9:
                fragment = new HomeFragment();
                args.putInt("key", 142);
                fragment.setArguments(args);
                break;
            case 10:
                fragment = new HomeFragment();
                args.putInt("key", 71);
                fragment.setArguments(args);
                break;
            case 11:
                fragment = new HomeFragment();
                args.putInt("key", 62);
                fragment.setArguments(args);
                break;
            case 12:
                fragment = new HomeFragment();
                args.putInt("key", 65);
                fragment.setArguments(args);
                break;
            case 13:
                fragment = new HomeFragment();
                args.putInt("key", 66);
                fragment.setArguments(args);
                break;
            case 14:
                fragment = new HomeFragment();
                args.putInt("key", 59);
                fragment.setArguments(args);
                break;
            case 15:
                fragment = new HomeFragment();
                args.putInt("key", 20);
                fragment.setArguments(args);
                break;
            case 16:
                fragment = new HomeFragment();
                args.putInt("key", 72);
                fragment.setArguments(args);
                break;
            case 17:
                fragment = new HomeFragment();
                args.putInt("key", 75);
                fragment.setArguments(args);
                break;
            case 18:
                fragment = new HomeFragment();
                args.putInt("key", 74);
                fragment.setArguments(args);
                break;
            case 19:
                fragment = new HomeFragment();
                args.putInt("key", 80);
                fragment.setArguments(args);
                break;
            case 20:
                fragment = new HomeFragment();
                args.putInt("key", 82);
                fragment.setArguments(args);
                break;
            case 21:
                fragment = new HomeFragment();
                args.putInt("key", 64);
                fragment.setArguments(args);
                break;
            case 22:
                fragment = new HomeFragment();
                args.putInt("key", 89);
                fragment.setArguments(args);
                break;
            case 23:
                fragment = new HomeFragment();
                args.putInt("key", 81);
                fragment.setArguments(args);
                break;

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        final Session session = (Session) getApplicationContext();
        final String sessID = session.getSession_id();
        if (sessID.isEmpty()) {
            session.setSession_id("");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_delete).setTitle("Exit")
                    .setMessage("Are you sure?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoginActivity.client.addHeader("X-Oc-Merchant-Id", "123");
                                    LoginActivity.client.addHeader("X-Oc-Merchant-Language", "en");
                                    LoginActivity.client.addHeader("X-Oc-Session", sessID);
                                    LoginActivity.client.post("http://mahaveersupermarket.com/api/rest/logout", new AsyncHttpResponseHandler() {
                                    });
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

}
