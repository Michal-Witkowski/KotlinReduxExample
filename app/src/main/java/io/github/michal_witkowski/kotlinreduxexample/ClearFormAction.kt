package io.github.michal_witkowski.kotlinreduxexample

import io.github.michal_witkowski.redux.Action

object ClearFormAction : Action<State> {

    override fun reduce(state: State): State {
        return State()
    }

}