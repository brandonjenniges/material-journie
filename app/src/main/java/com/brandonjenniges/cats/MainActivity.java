package com.brandonjenniges.cats;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.list) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupList();
    }

    private void setupList() {
        StaggeredGridLayoutManager mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        MainActivityAdapter mAdapter = new MainActivityAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    MainActivityAdapter.OnItemClickListener onItemClickListener = new MainActivityAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent transitionIntent = new Intent(getApplicationContext(), DetailActivity.class);
            transitionIntent.putExtra(Cat.EXTRA_KEY, position);

            ImageView catImage = (ImageView) v.findViewById(R.id.catImage);
            LinearLayout catNameHolder = (LinearLayout) v.findViewById(R.id.cat_name_holder_ll);

            View navigationBar = findViewById(android.R.id.navigationBarBackground);
            View statusBar = findViewById(android.R.id.statusBarBackground);

            Pair<View, String> imagePair = Pair.create((View) catImage, "tImage");
            Pair<View, String> holderPair = Pair.create((View) catNameHolder, "tNameHolder");
            Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> toolbarPair = Pair.create((View)toolbar, "tActionBar");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair, holderPair, navPair, statusPair, toolbarPair);
            ActivityCompat.startActivity(MainActivity.this, transitionIntent, options.toBundle());
        }
    };
}
