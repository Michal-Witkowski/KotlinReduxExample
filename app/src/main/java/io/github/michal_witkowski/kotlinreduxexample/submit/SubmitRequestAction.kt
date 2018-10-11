package io.github.michal_witkowski.kotlinreduxexample.submit

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.redux.Action

object SubmitRequestAction : Action<State> {

    override fun reduce(state: State): State {
        return if (state.canBeSubmitted()) {
            state.copy(submit = State.Submit.WaitingToSubmit())
        } else {
            state
        }
    }

}