package io.github.michal_witkowski.kotlinreduxexample.common

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.redux.Action

object DismissErrorAction : Action<State> {

    override fun reduce(state: State): State {
        return state.copy(error = null)
    }

}