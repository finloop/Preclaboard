package com.krawiec.piotr.preclaboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements Main_button_fragment.OnFragmentInteractionListener, Favourite_button_fragment.OnFragmentInteractionListener {
    private FragmentTabHost mTabHost;
    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    private String DEBUG_TAG = "WRONGGGGG";
    private int BUTTON_COUNT = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.tabs);
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Wszystkie"), Main_button_fragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("Ulubione"), Favourite_button_fragment.class, null);

        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton3);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.favarray), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                for(int i = 0; i < BUTTON_COUNT; i++) {
                    if(sharedPreferences.contains(Integer.toString(i))) {
                        editor.remove(Integer.toString(i));
                    }
                }
                editor.apply();
                Snackbar snackbar = Snackbar
                        .make(view, "Usunięto wszystkie dźwięki z ulubionych ;)", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override @SuppressWarnings("")
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                /*
                mTabHost.removeAllViews();
                android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                layoutParams.height = 0;
                layoutParams.width = 0;
                toolbar.setLayoutParams(layoutParams);
                */
                Intent intent = new Intent(this, UstawieniaActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
