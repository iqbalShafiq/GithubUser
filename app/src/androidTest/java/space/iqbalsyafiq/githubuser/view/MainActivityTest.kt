package space.iqbalsyafiq.githubuser.view


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import space.iqbalsyafiq.githubuser.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        val appCompatImageView = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_button), withContentDescription("Telusuri"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_bar),
                        childAtPosition(
                            withId(R.id.searchUsername),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("iqbalShafiq"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text), withText("iqbalShafiq"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(pressImeActionButton())

        val textView = onView(
            allOf(
                withId(R.id.tvUsername), withText("iqbalShafiq"),
                withParent(
                    allOf(
                        withId(R.id.cardUserItem),
                        withParent(withId(R.id.rvUser))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("iqbalShafiq")))

        val searchAutoComplete3 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text), withText("iqbalShafiq"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete3.perform(click())

        val appCompatImageView2 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_close_btn),
                withContentDescription("Hapus kueri"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView2.perform(click())

        val searchAutoComplete4 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete4.perform(replaceText("hsjdhhduwisjfudhjd"), closeSoftKeyboard())

        val searchAutoComplete5 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text), withText("hsjdhhduwisjfudhjd"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete5.perform(pressImeActionButton())

        val textView2 = onView(
            allOf(
                withId(R.id.tvError), withText("We can't find match user"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("We can't find match user")))

        val appCompatImageView3 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_close_btn),
                withContentDescription("Hapus kueri"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageView3.perform(click())

        val searchAutoComplete6 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete6.perform(replaceText("iqbalShafiq"), closeSoftKeyboard())

        val searchAutoComplete7 = onView(
            allOf(
                withId(androidx.appcompat.R.id.search_src_text), withText("iqbalShafiq"),
                childAtPosition(
                    allOf(
                        withId(androidx.appcompat.R.id.search_plate),
                        childAtPosition(
                            withId(androidx.appcompat.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete7.perform(pressImeActionButton())

        val appCompatButton = onView(
            allOf(
                withId(R.id.btnDetails), withText("Details"),
                childAtPosition(
                    allOf(
                        withId(R.id.cardUserItem),
                        childAtPosition(
                            withId(R.id.rvUser),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())

        val textView3 = onView(
            allOf(
                withId(R.id.tvRepository), withText("25 Repositories"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("25 Repositories")))

        val textView4 = onView(
            allOf(
                withId(R.id.tvGist), withText("0 Gists"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("0 Gists")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
