package io.github.michal_witkowski.kotlinreduxexample.category

import io.github.michal_witkowski.kotlinreduxexample.State
import io.github.michal_witkowski.kotlinreduxexample.State.Categories.WaitingToLoad
import io.github.michal_witkowski.redux.Action
import io.github.michal_witkowski.redux.Middleware
import io.reactivex.Observable

class CategoryLoaderMiddleware : Middleware<State> {

    override fun mapToActions(stateStream: Observable<State>): Observable<Action<State>> {
        return stateStream
                .filter { state -> state.categories is WaitingToLoad}
                .distinct { state -> (state.categories as WaitingToLoad).requestId}
                .switchMap<Action<State>> { state ->
                    //TODO zerowanie przy pustym tytule
                    CategoryEndpoint
                            .getCategories(state.title)
                            .map<Action<State>> { categories -> CategoriesLoadedAction(categories) }
                            .onErrorReturn { CategoriesLoadingErrorAction }
                            .startWith(CategoriesLoadingAction)
                }
    }

}