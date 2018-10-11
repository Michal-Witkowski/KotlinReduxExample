package io.github.michal_witkowski.kotlinreduxexample.category

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

object CategoryEndpoint {

    fun getCategories(title: String): Observable<List<String>> = when {
        title.toLowerCase() == "volvo" -> successfulResponse()
        else -> errorResponse()
    }

    private fun successfulResponse() = Observable
            .just(
                    listOf(
                            "Motoryzacja -> Samochody -> Osobowe -> Volvo",
                            "Motoryzacja -> Samochody -> Dostawcze -> Volvo",
                            "Motoryzacja -> Części -> Osobowe -> Volvo",
                            "Motoryzacja -> Części -> Dostawcze -> Volvo"
                    )
            )
            .delay(5, TimeUnit.SECONDS)

    private fun errorResponse(): Observable<List<String>> = Observable
            .just("")
            .delay(3, TimeUnit.SECONDS)
            .flatMap { Observable.error<List<String>>(Exception()) }


}