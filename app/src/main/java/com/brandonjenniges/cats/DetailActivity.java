package com.brandonjenniges.cats;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends Activity {

    @Bind(R.id.list) ListView mList;
    @Bind(R.id.catImage) ImageView mImageView;
    @Bind(R.id.textView) TextView mTitle;
    @Bind(R.id.cat_name_holder_ll) LinearLayout mTitleHolder;
    @Bind(R.id.btn_add) ImageButton mAddButton;
    @Bind(R.id.llEditTextHolder) LinearLayout mRevealView;
    @Bind(R.id.etTodo) EditText mEditTextTodo;

    private Palette mPalette;
    private boolean isEditTextVisible;
    private InputMethodManager mInputManager;
    private Cat mCat;
    private ArrayList<String> mTodoList = new ArrayList<>();
    private ArrayAdapter mToDoAdapter;
    int defaultColorForRipple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mCat = PlaceHolderData.placeList().get(getIntent().getIntExtra(Cat.EXTRA_KEY, 0));

        mAddButton.setImageResource(R.drawable.icn_morph_reverse);

        defaultColorForRipple = ContextCompat.getColor(this, R.color.colorPrimary);
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mRevealView.setVisibility(View.INVISIBLE);
        isEditTextVisible = false;

        mToDoAdapter = new ArrayAdapter(this, R.layout.row_todo, mTodoList);
        mList.setAdapter(mToDoAdapter);

        loadCat();
        windowTransition();
        getPhoto();

    }

    private void loadCat() {
        mTitle.setText(mCat.getName());
        mImageView.setImageResource(mCat.getImageResourceId(this));
    }

    private void windowTransition() {
        getWindow().setEnterTransition(makeEnterTransition());
        getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                mAddButton.animate().alpha(1.0f);
                getWindow().getEnterTransition().removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    public static Transition makeEnterTransition() {
        Transition fade = new Fade();
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        return fade;
    }

    private void addToDo(String todo) {
        mTodoList.add(todo);
    }

    private void getPhoto() {
        Bitmap photo = BitmapFactory.decodeResource(getResources(), mCat.getImageResourceId(this));
        colorize(photo);
    }

    private void colorize(Bitmap photo) {
        mPalette = Palette.from(photo).generate();
        applyPalette();
    }

    private void applyPalette() {
        getWindow().setBackgroundDrawable(new ColorDrawable(mPalette.getDarkMutedColor(defaultColorForRipple)));
        mTitleHolder.setBackgroundColor(mPalette.getMutedColor(defaultColorForRipple));
        applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple),
                mPalette.getDarkVibrantColor(defaultColorForRipple));
        mRevealView.setBackgroundColor(mPalette.getLightVibrantColor(defaultColorForRipple));
    }

    private void applyRippleColor(int bgColor, int tintColor) {
        colorRipple(mAddButton, bgColor, tintColor);
    }

    private void colorRipple(ImageButton id, int bgColor, int tintColor) {
        RippleDrawable ripple = (RippleDrawable) id.getBackground();
        GradientDrawable rippleBackground = (GradientDrawable) ripple.getDrawable(0);
        rippleBackground.setColor(bgColor);
        ripple.setColor(ColorStateList.valueOf(tintColor));
    }

    @OnClick (R.id.btn_add)
    protected void addHobbies(ImageButton button) {
        if (!isEditTextVisible) {
            showInput();
        } else {
            hideInput();
        }
    }

    private void showInput() {
        revealEditText(mRevealView);
        mEditTextTodo.requestFocus();
        mInputManager.showSoftInput(mEditTextTodo, InputMethodManager.SHOW_IMPLICIT);
        mAddButton.setImageResource(R.drawable.icn_morp);
        Animatable animatable = (Animatable) (mAddButton).getDrawable();
        animatable.start();
        applyRippleColor(ContextCompat.getColor(this, R.color.light_green), ContextCompat.getColor(this, R.color.dark_green));
    }

    private void hideInput() {
        addToDo(mEditTextTodo.getText().toString());
        mToDoAdapter.notifyDataSetChanged();
        mInputManager.hideSoftInputFromWindow(mEditTextTodo.getWindowToken(), 0);
        hideEditText(mRevealView);
        mAddButton.setImageResource(R.drawable.icn_morph_reverse);
        Animatable animatable = (Animatable) (mAddButton).getDrawable();
        animatable.start();
        applyRippleColor(mPalette.getVibrantColor(defaultColorForRipple),
                mPalette.getDarkVibrantColor(defaultColorForRipple));
    }

    private void revealEditText(LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        isEditTextVisible = true;
        anim.start();
    }

    private void hideEditText(final LinearLayout view) {
        int cx = view.getRight() - 30;
        int cy = view.getBottom() - 60;
        int initialRadius = view.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });
        isEditTextVisible = false;
        anim.start();
    }

    @Override
    public void onBackPressed() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(100);
        mAddButton.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mAddButton.setVisibility(View.GONE);
                finishAfterTransition();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
