package com.stefattorusso.simpleconverter.data

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.stefattorusso.data.network.entity.RatesContainerEntity
import com.stefattorusso.data.network.retrofit.AppRetrofitService
import com.stefattorusso.simpleconverter.utils.getStringFromAssets
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class ServiceTest {

    private lateinit var context: Context
    private lateinit var server: MockWebServer
    private lateinit var service: AppRetrofitService

    @Before
    fun init() {
        context = InstrumentationRegistry.getInstrumentation().context
        server = MockWebServer().apply {
            start()
        }
        service = Retrofit.Builder()
            .baseUrl(server.url(("/")))
            .addCallAdapterFactory(
                RxJava2CallAdapterFactory.create()
            )
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .create()
                )
            )
            .build()
            .create(AppRetrofitService::class.java)
    }

    @After
    fun closeGateway() {
        server.shutdown()
    }

    @Test
    fun throw_server_error_and_catch_it() {
        server.enqueue(MockResponse().apply { setResponseCode(500) })
        service.retrieveLatest("EUR").test().assertError { it is Exception }
    }

    @Test
    fun validate_mock_item_entity_list() {
        server.enqueue(
            MockResponse().apply {
                setResponseCode(200)
                setBody(context.getStringFromAssets("data.json"))
            }
        )
        service.retrieveLatest("EUR").test()
            .assertValue { entity ->
                validateItemEntity(entity)
            }
            .assertNoErrors()
    }

    private fun validateItemEntity(entity: RatesContainerEntity): Boolean {
        return entity.base != null && !entity.rates.isNullOrEmpty()
    }
}
