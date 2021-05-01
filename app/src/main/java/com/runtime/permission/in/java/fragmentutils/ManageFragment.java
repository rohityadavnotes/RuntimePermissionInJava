package com.runtime.permission.in.java.fragmentutils;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.List;

public class ManageFragment {

    private final FragmentManager fragmentManager;
    private final int fragmentContainerId;

    public ManageFragment(FragmentManager fragmentManager, @IdRes int fragmentContainerId) {
        this.fragmentManager        = fragmentManager;
        this.fragmentContainerId    = fragmentContainerId;
    }

    /**
     * Add fragments in frame layout.
     *
     * @param fragment fragment instance
     * @param fragmentTransactionAnimations custom animation
     * @param canAddToBackStack provide true if add in to back stack fragment
     */
    public void addFragment(IFragment fragment,
                            FragmentTransactionAnimations fragmentTransactionAnimations,
                            boolean canAddToBackStack) {

        if (fragment != null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (fragmentTransactionAnimations != null)
            {
                fragmentTransaction.setCustomAnimations(
                        fragmentTransactionAnimations.getEnter(),
                        fragmentTransactionAnimations.getExit(),
                        fragmentTransactionAnimations.getPopEnter(),
                        fragmentTransactionAnimations.getPopExit()
                );
            }

            fragmentTransaction.add(fragmentContainerId, (Fragment) fragment, fragment.getFragmentTag());

            /* By adding it to the back stack, you can return to the previous state by pressing the back button. */
            if (canAddToBackStack)
            {
                fragmentTransaction.addToBackStack(fragment.getFragmentTag());
            }

            fragmentTransaction.commit();
        }
    }

    /**
     * Replace fragments in frame layout.
     *
     * @param fragment fragment instance
     * @param fragmentTransactionAnimations custom animation
     * @param canAddToBackStack provide true if add in to back stack fragment
     */
    public void replaceFragment(IFragment fragment,
                                FragmentTransactionAnimations fragmentTransactionAnimations,
                                boolean canAddToBackStack) {
        if (fragment != null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (fragmentTransactionAnimations != null)
            {
                fragmentTransaction.setCustomAnimations(
                        fragmentTransactionAnimations.getEnter(),
                        fragmentTransactionAnimations.getExit(),
                        fragmentTransactionAnimations.getPopEnter(),
                        fragmentTransactionAnimations.getPopExit()
                );
            }

            fragmentTransaction.replace(fragmentContainerId, (Fragment) fragment, fragment.getFragmentTag());

            /* By adding it to the back stack, you can return to the previous state by pressing the back button. */
            if (canAddToBackStack)
            {
                fragmentTransaction.addToBackStack(fragment.getFragmentTag());
            }

            fragmentTransaction.commit();
        }
    }

    /**
     * Add fragments in frame layout.
     *
     * @param fragment fragment instance
     * @param fragmentTransactionAnimations custom animation
     * @param canAddToBackStack provide true if add in to back stack fragment
     * @param clearAllBackStackTillThisGivenTag provide tag
     */
    public void addFragment(IFragment fragment,
                            FragmentTransactionAnimations fragmentTransactionAnimations,
                            boolean canAddToBackStack,
                            String clearAllBackStackTillThisGivenTag) {

        if (fragment != null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (fragmentTransactionAnimations != null)
            {
                fragmentTransaction.setCustomAnimations(
                        fragmentTransactionAnimations.getEnter(),
                        fragmentTransactionAnimations.getExit(),
                        fragmentTransactionAnimations.getPopEnter(),
                        fragmentTransactionAnimations.getPopExit()
                );
            }

            fragmentTransaction.add(fragmentContainerId, (Fragment) fragment, fragment.getFragmentTag());

            /* By adding it to the back stack, you can return to the previous state by pressing the back button. */
            if (canAddToBackStack)
            {
                fragmentTransaction.addToBackStack(fragment.getFragmentTag());
            }

            popBackStack(clearAllBackStackTillThisGivenTag);

            fragmentTransaction.commit();
        }
    }

    /**
     * Replace fragments in frame layout.
     *
     * @param fragment fragment instance
     * @param fragmentTransactionAnimations custom animation
     * @param canAddToBackStack provide true if add in to back stack fragment
     * @param clearAllBackStackTillThisGivenTag provide tag
     */
    public void replaceFragment(IFragment fragment,
                                FragmentTransactionAnimations fragmentTransactionAnimations,
                                boolean canAddToBackStack,
                                String clearAllBackStackTillThisGivenTag) {
        if (fragment != null)
        {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (fragmentTransactionAnimations != null)
            {
                fragmentTransaction.setCustomAnimations(
                        fragmentTransactionAnimations.getEnter(),
                        fragmentTransactionAnimations.getExit(),
                        fragmentTransactionAnimations.getPopEnter(),
                        fragmentTransactionAnimations.getPopExit()
                );
            }

            fragmentTransaction.replace(fragmentContainerId, (Fragment) fragment, fragment.getFragmentTag());

            /* By adding it to the back stack, you can return to the previous state by pressing the back button. */
            if (canAddToBackStack)
            {
                fragmentTransaction.addToBackStack(fragment.getFragmentTag());
            }

            popBackStack(clearAllBackStackTillThisGivenTag);

            fragmentTransaction.commit();
        }
    }

    public boolean isFragmentAdded(final Fragment fragment) {
        return fragment != null && fragment.isAdded();
    }

    /**
     * Print fragment manager managed fragment in debug log.
     *
     * @param fragmentManager
     */
    public static void printActivityFragmentList(FragmentManager fragmentManager) {

        /* Get all Fragment list */
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if (fragmentList == null)
        {
            return;
        }

        System.out.println("printActivityFragmentList.size = " + fragmentList.size());

        for (Fragment fragment: fragmentList)
        {
            if (fragment != null) {
                String fragmentTag = fragment.getTag();
                System.out.println("printActivityFragmentList()=" + fragmentTag + "|fragment=" + fragment.getClass().getSimpleName());
            }
        }

        System.out.println("fragmentList.size()=" + fragmentList.size());
    }

    /**
     * Get exist Fragment by it's tag name.
     *
     * @param fragmentTag
     * @return
     */
    public Fragment getFragmentByTag(String fragmentTag) {
        return fragmentManager.findFragmentByTag(fragmentTag);
    }

    /**
     * Get exist Fragment by it's tag name.
     *
     * @param fragmentTagName
     * @return
     */
    public Fragment getFragmentByTagName(String fragmentTagName) {
        /* Get all Fragment list */
        List<Fragment> fragmentList = fragmentManager.getFragments();

        if (fragmentList == null) {
            return null;
        }

        System.out.println("getFragmentByTagName.size = " + fragmentList.size());

        for (Fragment fragment: fragmentList) {
            if (fragment != null)
            {
                String fragmentTag = fragment.getTag();

                System.out.println("getFragmentByTagName()=" + fragmentTag + "|fragment=" + fragment.getClass().getSimpleName());

                /* If Fragment tag name is equal then return it. */
                if (fragmentTag.equals(fragmentTagName)) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public Fragment getFragmentById(int id) {
        return fragmentManager.findFragmentById(id);
    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    /**
     * Remove single fragment by tag
     *
     * @param tag
     */
    public void clearFragmentByTag(String tag) {
        try
        {
            for (int i = fragmentManager.getBackStackEntryCount() - 1; i >= 0; i--)
            {
                String backEntry = fragmentManager.getBackStackEntryAt(i).getName();
                System.out.println("fragmentManager.backEntry=" + backEntry);

                if (backEntry.equals(tag))
                {
                    fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                else
                {
                    break;
                }
            }
        }
        catch (Exception exception) {
            System.out.print("Pop back stack error : " + exception);
            exception.printStackTrace();
        }
    }

    /**
     * Close the all the fragment till the given tag name
     *
     * @param tag if tag name is null then all the fragment will be close
     *            or
     *            till the given tag name ( दिए गए टैग नाम तक )
     *            Example : you add three fragment A, B, C, D then you provide fragment B tag then A & B fragment close.
     *            C & D remaining.
     *
     *             if provide tag name, then close all the fragment with the given tag name
     */
    public void popBackStack(String tag) {
        fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Remove all entries from the backStack of this fragmentManager.
     */
    private boolean clearBackStack() {
        try
        {
            List<Fragment> fragsList = fragmentManager.getFragments();
            if (fragsList.size() == 0) {
                return true;
            }
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Close the number of fragments.
     *
     * @param numBackStack,   number of fragments to pop up.
     */
    public void popBackStack(int numBackStack) {
        int fragCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < fragCount - numBackStack; i++) {
            fragmentManager.popBackStack();
        }
    }
}
