package info.androidhive.Mahaveer;

import info.androidhive.Mahaveer.adapter.NavDrawerListAdapter;
import info.androidhive.Mahaveer.model.NavDrawerItem;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
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
    private TypedArray navMenuIcons;
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
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        navDrawerItems = new ArrayList<NavDrawerItem>();
        // Communities, Will add a counter here
        //navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[6]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[7]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[8]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[9]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[10]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[11]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[12]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[13]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[14]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[15]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[16]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[17]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[18]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[19]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[20]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[21]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[22]));
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[23]));
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


        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

                args.putInt("key", 0);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = new HomeFragment();
                args.putInt("key", 1);
                fragment.setArguments(args);
                break;
            case 2:
                fragment = new HomeFragment();
                args.putInt("key", 2);
                fragment.setArguments(args);
                break;
            case 3:
                fragment = new HomeFragment();
                args.putInt("key", 3);
                fragment.setArguments(args);
                break;
            case 4:
                fragment = new HomeFragment();
                args.putInt("key", 4);
                fragment.setArguments(args);
                break;
            case 5:
                fragment = new HomeFragment();
                args.putInt("key", 5);
                fragment.setArguments(args);
                break;
            case 6:
                fragment = new HomeFragment();
                args.putInt("key", 6);
                fragment.setArguments(args);
                break;
            case 7:
                fragment = new HomeFragment();
                args.putInt("key", 7);
                fragment.setArguments(args);
                break;
            case 8:
                fragment = new HomeFragment();
                args.putInt("key", 8);
                fragment.setArguments(args);
                break;
            case 9:
                fragment = new HomeFragment();
                args.putInt("key", 9);
                fragment.setArguments(args);
                break;
            case 10:
                fragment = new HomeFragment();
                args.putInt("key", 10);
                fragment.setArguments(args);
                break;
            case 11:
                fragment = new HomeFragment();
                args.putInt("key", 11);
                fragment.setArguments(args);
                break;
            case 12:
                fragment = new HomeFragment();
                args.putInt("key", 12);
                fragment.setArguments(args);
                break;
            case 13:
                fragment = new HomeFragment();
                args.putInt("key", 13);
                fragment.setArguments(args);
                break;
            case 14:
                fragment = new HomeFragment();
                args.putInt("key", 14);
                fragment.setArguments(args);
                break;
            case 15:
                fragment = new HomeFragment();
                args.putInt("key", 15);
                fragment.setArguments(args);
                break;
            case 16:
                fragment = new HomeFragment();
                args.putInt("key", 16);
                fragment.setArguments(args);
                break;
            case 17:
                fragment = new HomeFragment();
                args.putInt("key", 17);
                fragment.setArguments(args);
                break;
            case 18:
                fragment = new HomeFragment();
                args.putInt("key", 18);
                fragment.setArguments(args);
                break;
            case 19:
                fragment = new HomeFragment();
                args.putInt("key", 19);
                fragment.setArguments(args);
                break;
            case 20:
                fragment = new HomeFragment();
                args.putInt("key", 20);
                fragment.setArguments(args);
                break;
            case 21:
                fragment = new HomeFragment();
                args.putInt("key", 21);
                fragment.setArguments(args);
                break;
            case 22:
                fragment = new HomeFragment();
                args.putInt("key", 22);
                fragment.setArguments(args);
                break;
            case 23:
                fragment = new HomeFragment();
                args.putInt("key", 23);
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

}
