package io.github.michal_witkowski.kotlinreduxexample.submit

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

object AuctionEndpoint {

    fun startAuction(title: String, category: String, description: String): Observable<Boolean> = Observable
            .fromCallable { listOf(title, category, description).all { it.isNotBlank() } }
            .delay(3, TimeUnit.SECONDS)

}