package io.github.michal_witkowski.kotlinreduxexample.submit

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.redux.Action

object SubmitPendingAciton : Action<State> {

    override fun reduce(state: State): State {
        return state.copy(submit = State.Submit.Pending)
    }

}