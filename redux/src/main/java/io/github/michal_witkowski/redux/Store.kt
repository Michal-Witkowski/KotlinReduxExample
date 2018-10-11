package io.github.michal_witkowski.redux

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class Store<State> private constructor(builder: Builder<State>) {

    private val actionStream = PublishSubject.create<Action<State>>()
    private val stateStream = BehaviorSubject.create<State>()
    private val compositeDisposable = CompositeDisposable()

    private val actionLogger: ((Action<State>) -> Unit)?
    private val stateLogger: ((State) -> Unit)?

    init {
        actionLogger = builder.actionLogger
        stateLogger = builder.stateLogger

        actionStream
                .observeOn(Schedulers.single())
                .doOnNext { action -> actionLogger?.invoke(action) }
                .scan(builder.initialState) { state, action -> action.reduce(state) }
                .doOnNext { state -> stateLogger?.invoke(state) }
                .distinctUntilChanged()
                .subscribe(stateStream::onNext)
                .let { disposable -> compositeDisposable.add(disposable) }

        builder.middlewares.forEach { middleware ->
            middleware
                    .mapToActions(stateStream)
                    .subscribe(this::dispatch)
                    .let { disposable -> compositeDisposable.add(disposable) }
        }
    }

    fun shutdown() = compositeDisposable.dispose()

    fun dispatch(action: Action<State>) = actionStream.onNext(action)

    fun getState(): Observable<State> = stateStream

    class Builder<State>(val initialState: State) {

        var actionLogger: ((Action<State>) -> Unit)? = null
        var stateLogger: ((State) -> Unit)? = null
        var middlewares = listOf<Middleware<State>>()

        fun build(): Store<State> = Store(this)
    }

    companion object {

        fun <State> create(initialState: State, builder: (Builder<State>.() -> Unit)? = null): Store<State> =
                Builder(initialState)
                        .apply { builder?.invoke(this) }
                        .build()
    }
}