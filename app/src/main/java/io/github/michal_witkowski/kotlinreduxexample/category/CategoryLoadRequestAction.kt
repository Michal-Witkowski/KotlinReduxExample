package io.github.michal_witkowski.kotlinreduxexample.category

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.redux.Action

object CategoryLoadRequestAction : Action<State> {

    override fun reduce(state: State): State {
        return state.copy(categories = State.Categories.WaitingToLoad())
    }

}