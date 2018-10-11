package io.github.michal_witkowski.kotlinreduxexample.submit

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.kotlinreduxexample.State.Submit
import io.github.michal_witkowski.redux.Action
import io.github.michal_witkowski.redux.Middleware
import io.reactivex.Observable

class SubmitMiddleware : Middleware<State> {

    override fun mapToActions(stateStream: Observable<State>): Observable<Action<State>> {
        return stateStream
                .filter { state -> state.submit is Submit.WaitingToSubmit }
                .distinct { state -> (state.submit as Submit.WaitingToSubmit).requestId }
                .switchMap { state ->
                    AuctionEndpoint
                            .startAuction(state.title, state.getChosenCategory(), state.description)
                            .map<Action<State>> { SubmitDoneAction }
                            .startWith(SubmitPendingAciton)
                }
    }

    private fun State.getChosenCategory(): String =
            (categories as State.Categories.Loaded).items.first { it.isChosen }.name

}