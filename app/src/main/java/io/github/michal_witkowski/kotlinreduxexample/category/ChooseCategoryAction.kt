package io.github.michal_witkowski.kotlinreduxexample.category

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.kotlinreduxexample.State.Categories
import io.github.michal_witkowski.kotlinreduxexample.State.Categories.Loaded
import io.github.michal_witkowski.redux.Action

class ChooseCategoryAction(private val categoryToChoose: State.Category) : Action<State> {

    override fun reduce(state: State): State = when {
        state.categories is Loaded -> {
            state.copy(categories = state.categories.chooseCategory(categoryToChoose))
        }
        else -> {
            state
        }
    }

    private fun Categories.Loaded.chooseCategory(category: State.Category) = Categories.Loaded(
            items = items.map {
                if (it == category) {
                    it.copy(isChosen = true)
                } else {
                    it.copy(isChosen = false)
                }
            }
    )

}