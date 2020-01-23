package com.stefattorusso.simpleconverter.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher

object RecyclerViewUtils {

    fun atPosition(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                return if (viewHolder == null) false
                else itemMatcher.matches(viewHolder.itemView)
            }
        }
    }

    fun typeTextToChild(childId: Int, text: String): ViewAction{
        return object : ViewAction{
            override fun getDescription(): String {
                return "Perform action on a specified view."
            }

            override fun getConstraints(): Matcher<View> {
                return allOf(
                    isAssignableFrom(EditText::class.java),
                    isAssignableFrom(TextView::class.java)
                )
            }

            override fun perform(uiController: UiController?, view: View?) {
                val v = view?.findViewById<EditText>(childId)
                v?.setText(text)
            }

        }
    }
}