package com.stefattorusso.simpleconverter.domain.usecase

import com.stefattorusso.simpleconverter.data.repository.RatesRepositoryContract
import com.stefattorusso.simpleconverter.domain.RateDomain
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GetLatestUseCase @Inject constructor(
    private val repository: RatesRepositoryContract
): GetLatestUseCaseContract {

    override fun getLatest(base: String): Flowable<List<RateDomain>> {
        return repository.retrieveLatest(base)
            .repeatWhen {
                Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .toFlowable(BackpressureStrategy.LATEST) as Publisher<*>?
            }
    }
}