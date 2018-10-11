package io.github.michal_witkowski.kotlinreduxexample.category

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.redux.Action

class CategoriesLoadedAction(private val categories: List<String>) : Action<State> {

    override fun reduce(state: State): State = state.copy(
            categories = State.Categories.Loaded(categories.map { State.Category(name = it) })
    )

}