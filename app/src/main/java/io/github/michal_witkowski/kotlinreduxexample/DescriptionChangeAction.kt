package io.github.michal_witkowski.kotlinreduxexample

import io.github.michal_witkowski.redux.Action

class DescriptionChangeAction(private val description: String) : Action<State> {

    override fun reduce(state: State): State = state.copy(description = description)

}