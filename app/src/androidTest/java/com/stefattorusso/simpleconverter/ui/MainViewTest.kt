package com.stefattorusso.simpleconverter.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stefattorusso.simpleconverter.R
import com.stefattorusso.simpleconverter.model.ErrorModel
import com.stefattorusso.simpleconverter.model.RateModel
import com.stefattorusso.simpleconverter.ui.main.MainAdapter
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainFragment
import com.stefattorusso.simpleconverter.ui.main.mvvm.MainViewModel
import com.stefattorusso.simpleconverter.utils.RecyclerViewUtils
import com.stefattorusso.simpleconverter.utils.RecyclerViewUtils.typeTextToChild
import com.stefattorusso.simpleconverter.utils.ViewModelUtil
import com.stefattorusso.simpleconverter.utils.swap
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class MainViewTest {


    private lateinit var mViewModel: MainViewModel
    private lateinit var mViewModelFactory: ViewModelProvider.Factory
    private lateinit var mFragmentScenario: FragmentScenario<MainFragmentTest>

    private val mItemList = MutableLiveData<List<RateModel>>()
    private val mSelectedItem = MutableLiveData<Boolean>()
    private val mError = MutableLiveData<ErrorModel>()

    private val mErrorModel = ErrorModel(throwable = Error("Generic error"))

    private var mSelectedBase = "EUR"

    private var rateList = listOf(
        RateModel(
            code = "EUR",
            name = "EUR name",
            value = "1",
            base = true
        ),
        RateModel(
            code = "AUD",
            name = "AUD name",
            value = "1.61",
            base = false
        ),
        RateModel(
            code = "BGN",
            name = "BGN name",
            value = "1.98",
            base = false
        ),
        RateModel(
            code = "BRL",
            name = "BRL name",
            value = "4.77",
            base = false
        )
    )

    @Before
    fun init() {
        mViewModel = Mockito.mock(MainViewModel::class.java)
        mViewModelFactory = ViewModelUtil.createFor(mViewModel)

        Mockito.`when`(mViewModel.error).thenReturn(mError)
        Mockito.`when`(mViewModel.rateModelList).thenReturn(mItemList)
        Mockito.`when`(mViewModel.rateSelected).thenReturn(mSelectedItem)
        Mockito.`when`(mViewModel.onItemSelected(POSITION)).then {
            mSelectedItem.postValue(true)
        }

        mFragmentScenario = launchFragmentInContainer<MainFragmentTest>(
            factory = object : FragmentFactory() {
                override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                    val fragmentInstance = super.instantiate(classLoader, className)
                    (fragmentInstance as MainFragmentTest).viewModelFactory = mViewModelFactory
                    return fragmentInstance
                }
            }
        )
    }

    @Test
    fun should_check_list_not_visible_on_start() {

        onView(withId(R.id.recycler_view))
            .check(matches(CoreMatchers.not(isDisplayed())))

    }

    @Test
    fun should_check_list_visible_after_items_loaded() {

        Mockito.`when`(mViewModel.loadData(mSelectedBase)).then {
            mItemList.postValue(rateList)
        }

        mViewModel.loadData(mSelectedBase)

        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))

    }

    @Test
    fun should_show_alert_when_error_occurs() {

        Mockito.`when`(mViewModel.loadData(mSelectedBase)).then {
            mError.postValue(mErrorModel)
        }

        mViewModel.loadData(mSelectedBase)

        onView(CoreMatchers.anyOf(withId(R.id.recycler_view)))
            .check(ViewAssertions.doesNotExist())

        onView(withText("Error!")).check(matches(isDisplayed()))
    }

    @Test
    fun should_move_item_to_top_on_click() {

        Mockito.`when`(mViewModel.loadData(mSelectedBase)).then {
            mItemList.postValue(rateList)
        }
        Mockito.`when`(mViewModel.onItemSelected(CLICKED_POSITION)).then {
            rateList = rateList.toMutableList().swap(0, CLICKED_POSITION)
            mItemList.postValue(rateList)
        }

        mViewModel.loadData(mSelectedBase)

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MainAdapter.RateViewHolder>(
                CLICKED_POSITION,
                click()
            )
        )

        onView(withId(R.id.recycler_view)).check(
            matches(
                RecyclerViewUtils.atPosition(
                    0,
                    hasDescendant(withText("BGN"))
                )
            )
        )
    }

    @Test
    fun should_check_edit_text() {

        Mockito.`when`(mViewModel.loadData(mSelectedBase)).then {
            mItemList.postValue(rateList)
        }

        mViewModel.loadData(mSelectedBase)

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.actionOnItem<MainAdapter.RateViewHolder>(
                hasDescendant(withText("1")),
                typeTextToChild(R.id.rate_edit, "10")
            )
        )

        onView(withId(R.id.recycler_view)).check(
            matches(
                RecyclerViewUtils.atPosition(
                    0,
                    hasDescendant(withText("10"))
                )
            )
        )
    }

    class MainFragmentTest : MainFragment() {
        override fun injectMembers() {

        }
    }

    companion object {
        private const val POSITION = 1
        private const val CLICKED_POSITION = 2
    }
}