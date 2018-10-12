package io.github.michal_witkowski.kotlinreduxexample.fields

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.redux.Action

class TitleChangeAction(private val title: String) : Action<State> {
    override fun reduce(state: State): State = when {
        state.title == title -> state
        title.isBlank() -> state.copy(
                title = title,
                categories = State.Categories.Empty
        )
        else -> state.copy(
                title = title,
                categories = State.Categories.WaitingToLoad()
        )
    }
}