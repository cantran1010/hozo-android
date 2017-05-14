package vn.tonish.hozo.utils;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;

import java.io.Serializable;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 5/14/2017.
 */

public enum TransitionScreen implements Serializable {
    LEFT_TO_RIGHT, RIGHT_TO_LEFT, UP_TO_DOWN, DOWN_TO_UP, FADE_IN;

    public static void overridePendingTransition(Activity activity, TransitionScreen transitionScreen) {
        switch (transitionScreen) {
            case LEFT_TO_RIGHT:
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case RIGHT_TO_LEFT:
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case UP_TO_DOWN:
                activity.overridePendingTransition(R.anim.slide_in_down, R.anim.non_amimation);
                break;

            case DOWN_TO_UP:
                activity.overridePendingTransition(R.anim.from_down_to_up, R.anim.non_amimation);
                break;

            case FADE_IN:
                activity.overridePendingTransition(R.anim.fadein, R.anim.non_amimation);
                break;

            default:
                activity.overridePendingTransition(R.anim.fadein, R.anim.non_amimation);
                break;
        }
    }

    public static void overridePendingTransitionOut(Activity activity, TransitionScreen transitionScreen) {
        switch (transitionScreen) {
            case LEFT_TO_RIGHT:
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case RIGHT_TO_LEFT:
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case UP_TO_DOWN:
                activity.overridePendingTransition(R.anim.non_amimation, R.anim.slide_out_down);
                break;

            case DOWN_TO_UP:
                activity.overridePendingTransition(R.anim.non_amimation, R.anim.from_up_to_down);
                break;

            case FADE_IN:
                activity.overridePendingTransition(R.anim.non_amimation, R.anim.fadeout);
                break;

            default:
                activity.overridePendingTransition(R.anim.non_amimation, R.anim.fadeout);
                break;
        }
    }

    public static void setCustomAnimationsFragment(FragmentTransaction transaction, TransitionScreen transitionScreen) {

        switch (transitionScreen) {
            case LEFT_TO_RIGHT:
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case RIGHT_TO_LEFT:
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case UP_TO_DOWN:
                transaction.setCustomAnimations(R.anim.slide_in_down, R.anim.from_up_to_down, R.anim.slide_in_down, R.anim.from_up_to_down);
                break;

            case DOWN_TO_UP:
                transaction.setCustomAnimations(R.anim.from_down_to_up, R.anim.slide_out_down, R.anim.from_down_to_up, R.anim.slide_out_down);
                break;

            case FADE_IN:
                transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                break;

            default:
                transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                break;

        }

    }
}
