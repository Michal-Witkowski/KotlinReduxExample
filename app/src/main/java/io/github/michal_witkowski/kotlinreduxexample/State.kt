package io.github.michal_witkowski.kotlinreduxexample

import io.github.michal_witkowski.redux.Action
import java.util.UUID

data class State(
        val title: String = "",
        val categories: Categories = Categories.Empty,
        val description: String = "",
        val submit: Submit = Submit.Idle,
        val error: Error? = null
) {

    sealed class Categories {
        object Empty : Categories()
        data class WaitingToLoad(val requestId: UUID = UUID.randomUUID()) : Categories()
        object Loading : Categories()
        data class Loaded(val items: List<Category>) : Categories()
    }

    data class Category(
            val name: String,
            val isChosen: Boolean = false
    )

    sealed class Submit {
        object Idle : Submit()
        data class WaitingToSubmit(val requestId: UUID = UUID.randomUUID()) : Submit()
        object Pending : Submit()
        object Done : Submit()
    }

    data class Error(val retryAction: Action<State>)

    fun canBeSubmitted(): Boolean = title.isNotBlank()
            && description.isNotBlank()
            && categories is Categories.Loaded
            && categories.items.any { it.isChosen }
            && submit is Submit.Idle
}