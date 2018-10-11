package io.github.michal_witkowski.kotlinreduxexample

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.github.michal_witkowski.kotlinreduxexample.State.Categories
import io.github.michal_witkowski.kotlinreduxexample.category.ChooseCategoryAction
import io.github.michal_witkowski.kotlinreduxexample.submit.SubmitRequestAction
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import android.content.DialogInterface


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        RxTextView
                .afterTextChangeEvents(titleEditText)
                .debounce(1, TimeUnit.SECONDS)
                .map { textChangeEvent -> textChangeEvent.editable().toString() }
                .distinctUntilChanged()
                .subscribe { title -> viewModel.dispatch(TitleChangeAction(title)) }
                .let { disposable -> compositeDisposable.add(disposable) }

        RxTextView
                .afterTextChangeEvents(descriptionEditText)
                .map { textChangeEvent -> textChangeEvent.editable().toString() }
                .distinctUntilChanged()
                .subscribe { description -> viewModel.dispatch(DescriptionChangeAction(description)) }
                .let { disposable -> compositeDisposable.add(disposable) }

        RxView
                .clicks(submitButton)
                .debounce(250, TimeUnit.MILLISECONDS)
                .subscribe { viewModel.dispatch(SubmitRequestAction) }
                .let { disposable -> compositeDisposable.add(disposable) }

        viewModel
                .getState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { viewState -> render(viewState) }
                .let { disposable -> compositeDisposable.add(disposable) }
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    private fun render(state: State) {
        with(titleEditText) {
            if (!isFocused) setText(state.title)
            if (state.submit !is State.Submit.Idle) {
                clearFocus()
                isEnabled = false
            } else {
                isEnabled = true
            }
        }

        with(descriptionEditText) {
            if (!isFocused) setText(state.description)
            if (state.submit !is State.Submit.Idle) {
                clearFocus()
                isEnabled = false
            } else {
                isEnabled = true
            }
        }

        categoriesHint.visibility = when (state.categories) {
            is Categories.Empty -> View.VISIBLE
            else -> View.GONE
        }

        categoriesProgressBar.visibility = when (state.categories) {
            is Categories.WaitingToLoad, Categories.Loading -> View.VISIBLE
            else -> View.GONE
        }

        with(categoriesRadioGroup) {
            when (state.categories) {
                is Categories.Loaded -> {
                    addCategories(state.categories.items)
                    visibility = View.VISIBLE
                }
                else -> {
                    removeAllViews()
                    visibility = View.GONE
                }
            }

            if (state.submit !is State.Submit.Idle) {
                disableAllButtons()
            } else {
                enableAllButtons()
            }
        }

        submitButton.isEnabled = state.canBeSubmitted()

        (state.submit as? State.Submit.Done)?.let {
            AlertDialog.Builder(this@MainActivity).create().apply {
                setTitle("Sukces")
                setMessage("Aukcję wystawiono poprawnie")
                setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog, _ ->
                    viewModel.dispatch(ClearFormAction)
                    dialog.dismiss()
                }
                show()
            }
        }

        state.error?.let { error ->
            viewModel.dispatch(DismissErrorAction)
            Snackbar.make(rootLayout, "Wystąpił błąd", Snackbar.LENGTH_LONG)
                    .setAction("Spróbuj ponownie") { viewModel.dispatch(error.retryAction) }
                    .show()
        }
    }

    private fun RadioGroup.addCategories(categories: List<State.Category>) {
        removeAllViews()
        categories.forEach { category ->
            addView(
                    RadioButton(context).apply {
                        text = category.name
                        isChecked = category.isChosen
                        setOnClickListener { viewModel.dispatch(ChooseCategoryAction(category)) }
                    }
            )
        }
    }

    private fun RadioGroup.disableAllButtons() {
        for (i in 0 until childCount) {
            with(getChildAt(i)) {
                clearFocus()
                isEnabled = false
            }
        }
    }

    private fun RadioGroup.enableAllButtons() {
        for (i in 0 until childCount) {
            with(getChildAt(i)) {
                isEnabled = true
            }
        }
    }
}
