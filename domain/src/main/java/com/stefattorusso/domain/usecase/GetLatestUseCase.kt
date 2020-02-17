package com.stefattorusso.domain.usecase

import com.stefattorusso.domain.RateDomain
import com.stefattorusso.domain.repository.RatesRepositoryContract
import io.reactivex.Flowable
import javax.inject.Inject

class GetLatestUseCase @Inject constructor(
    private val repository: RatesRepositoryContract
) : GetLatestUseCaseContract {

    override fun getLatest(base: String): Flowable<List<RateDomain>> {
        return repository.startRetrievingRates(base)
    }
}