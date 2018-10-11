package io.github.michal_witkowski.redux

import io.reactivex.Observable

interface Middleware<State> {

    fun mapToActions(stateStream: Observable<State>): Observable<Action<State>>

}