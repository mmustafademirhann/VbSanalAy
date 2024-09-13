package com.example.socialmediavbsanalay.presentation.fragments
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class FixedBackgroundBehavior(context: Context, attrs: AttributeSet) : AppBarLayout.ScrollingViewBehavior(context, attrs) {

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        // Ensure dependency is an AppBarLayout
        val appBarLayout = dependency as? AppBarLayout ?: return super.onDependentViewChanged(parent, child, dependency)
        Log.d("FixedBackgroundBehavior", "onDependentViewChanged called")
        // Get the scroll range and current scroll position
        val maxScroll = appBarLayout.totalScrollRange
        val currentScroll = -appBarLayout.y

        // Calculate the scroll percentage
        val scrollPercentage = currentScroll / maxScroll.toFloat()

        // Limit the scroll based on the percentage
        if (scrollPercentage > 0.2f) {
            appBarLayout.translationY = -maxScroll * 0.2f
        } else {
            appBarLayout.translationY = 0.2f
        }

        return super.onDependentViewChanged(parent, child, dependency)
    }
}
