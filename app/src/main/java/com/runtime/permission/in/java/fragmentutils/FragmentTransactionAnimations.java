package com.runtime.permission.in.java.fragmentutils;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import com.runtime.permission.in.java.R;

public class FragmentTransactionAnimations {

    private int enter;
    private int exit;
    private int popEnter;
    private int popExit;

    public FragmentTransactionAnimations() {
        this.enter = R.anim.slide_in_from_right; /* open fragment */
        this.exit = R.anim.slide_out_to_left; /* close fragment */
        this.popEnter = R.anim.slide_in_from_left; /* open fragment when back button press*/
        this.popExit = R.anim.slide_out_to_right; /* close fragment when back button press */
    }

    public FragmentTransactionAnimations(@AnimatorRes @AnimRes int enter, @AnimatorRes @AnimRes int exit, @AnimatorRes @AnimRes int popEnter, @AnimatorRes @AnimRes int popExit) {
        this.enter      = enter;
        this.exit       = exit;
        this.popEnter   = popEnter;
        this.popExit    = popExit;
    }

    public int getEnter() {
        return enter;
    }

    public void setEnter(@AnimatorRes @AnimRes int enter) {
        this.enter = enter;
    }

    public int getExit() {
        return exit;
    }

    public void setExit(@AnimatorRes @AnimRes int exit) {
        this.exit = exit;
    }

    public int getPopEnter() {
        return popEnter;
    }

    public void setPopEnter(@AnimatorRes @AnimRes int popEnter) {
        this.popEnter = popEnter;
    }

    public int getPopExit() {
        return popExit;
    }

    public void setPopExit(@AnimatorRes @AnimRes int popExit) {
        this.popExit = popExit;
    }
}
