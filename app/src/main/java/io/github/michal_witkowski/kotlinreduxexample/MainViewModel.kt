package io.github.michal_witkowski.kotlinreduxexample

import android.arch.lifecycle.ViewModel
import android.util.Log
import io.github.michal_witkowski.kotlinreduxexample.category.CategoryLoaderMiddleware
import io.github.michal_witkowski.kotlinreduxexample.submit.SubmitMiddleware
import io.github.michal_witkowski.redux.Action
import io.github.michal_witkowski.redux.Store

class MainViewModel : ViewModel() {

    private val store = Store.create(State()) {
        actionLogger = { action -> Log.d("KotlinRedux", "Dispatching action: $action") }
        stateLogger = { state -> Log.d("KotlinRedux", "Store state: $state") }
        middlewares = listOf(CategoryLoaderMiddleware(), SubmitMiddleware())
    }

    fun dispatch(action: Action<State>) = store.dispatch(action)

    fun getState() = store.getState()

    override fun onCleared() {
        store.shutdown()
        super.onCleared()
    }
}