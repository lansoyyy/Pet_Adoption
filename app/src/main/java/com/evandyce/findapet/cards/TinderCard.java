package com.evandyce.findapet.cards;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.evandyce.findapet.User;
import com.evandyce.findapet.main.fragments.FavoritesFragment;
import com.evandyce.findapet.CardsActivity;
import com.evandyce.findapet.Utils;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import com.evandyce.findapet.R;

/**
 * Created by janisharali on 19/08/16.
 */
@NonReusable
@Layout(R.layout.tinder_card_view)
public class TinderCard {

    // decorators are for the swipecards DO NOT CHANGE

    @View(R.id.profileImageView)
    ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    TextView locationNameTxt;

    @SwipeView
    android.view.View cardView;

    private Animal mAnimal;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    private User user;
    private static int index = 0;

    public TinderCard(Context context, Animal animal, SwipePlaceHolderView swipeView) {
        mContext = context;
        mAnimal = animal;
        mSwipeView = swipeView;
    }

    // called when the next card is up on display
    // starts a new thread and loads image, sets text, etc.
    @Resolve
    public void onResolved(){
        Thread thread = new Thread();
        thread.run();
        MultiTransformation multi = new MultiTransformation(
                new BlurTransformation(mContext, 30),
                new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP));
        Glide.with(mContext).load(mAnimal.getImageUrl())
                .bitmapTransform(multi)
                .into(profileImageView);
        nameAgeTxt.setText(mAnimal.getName() + ", " + mAnimal.getAge());
        locationNameTxt.setText(mAnimal.getLocation());
    }

    @SwipeHead
    public void onSwipeHeadCard() {
        Glide.with(mContext).load(mAnimal.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(profileImageView);
        cardView.invalidate();
    }

    @Click(R.id.profileImageView)
    public void onClick(){
        Log.d("EVENT", "profileImageView click");
    }

    // when a card is swiped left
    @SwipeOut
    public void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        index++;
        User.incrementCounter();
    }

    @SwipeCancelState
    public void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    // when a card is swiped right
    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        FavoritesFragment.animalList.add(CardsActivity.getAnimalFromIndex(index));
        Log.d("TinderCard", FavoritesFragment.animalList.toString());
        index++;
        User.incrementCounter();
        User.incrementLikedCounter();
    }

    @SwipeInState
    public void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    public void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }

    public static void setIndex(int i) {
        index = i;
    }
    public static int getIndex() {
        return index;
    }
}
