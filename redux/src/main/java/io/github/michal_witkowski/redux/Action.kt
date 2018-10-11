package io.github.michal_witkowski.redux

interface Action<State> {

    fun reduce(state: State): State

}