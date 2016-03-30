package com.brandonjenniges.journie;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;

import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.list) RecyclerView mRecyclerView;

    private MainActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setupList();
    }

    private void setupList() {
        StaggeredGridLayoutManager mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MainActivityAdapter(this, CatData.catList());
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setupSearchView(menu);
        return true;
    }

    public void setupSearchView(Menu menu) {
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        RxSearchView.queryTextChanges(searchView)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(charSequence -> {
                    filterCats(charSequence.toString());
                });
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    }

    private void filterCats(String filter) {
        ArrayList<Cat> temp = new ArrayList<>();
        Observable.from(CatData.catList())
                .filter(cat -> Pattern.compile(Pattern.quote(filter), Pattern.CASE_INSENSITIVE).matcher(cat.getName()).find())
                .subscribe(new Subscriber<Cat>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.setCats(temp);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Cat cat) {
                        temp.add(cat);
                    }
                });
    }
    MainActivityAdapter.OnItemClickListener onItemClickListener = new MainActivityAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, int position) {
            Intent transitionIntent = new Intent(getApplicationContext(), DetailActivity.class);
            transitionIntent.putExtra(Cat.EXTRA_KEY, position);

            ImageView catImage = (ImageView) v.findViewById(R.id.catImage);
            LinearLayout catNameHolder = (LinearLayout) v.findViewById(R.id.cat_name_holder_ll);

            View navigationBar = findViewById(android.R.id.navigationBarBackground);

            Pair<View, String> imagePair = Pair.create((View) catImage, "tImage");
            Pair<View, String> holderPair = Pair.create((View) catNameHolder, "tNameHolder");
            Pair<View, String> navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME);
            Pair<View, String> toolbarPair = Pair.create((View)toolbar, "tActionBar");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, imagePair, holderPair, navPair, toolbarPair);
            ActivityCompat.startActivity(MainActivity.this, transitionIntent, options.toBundle());
        }
    };
}
