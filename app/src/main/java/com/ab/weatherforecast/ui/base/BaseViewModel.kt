package com.ab.weatherforecast.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.weatherforecast.data.model.RequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

data class Event<out T>(val content : T, val hasBeenHandled : Boolean = false)

open class BaseViewModel : ViewModel() {

    val loading: LiveData<Boolean>
        get() = _loading

    private val _loading: MutableLiveData<Boolean> = MutableLiveData()

    val showSnackBar: LiveData<Event<String>>
        get() = _showSnackBar


    private val _showSnackBar: MutableLiveData<Event<String>> = MutableLiveData()

    private val _error : MutableLiveData<Event<String>> = MutableLiveData()
    val error : LiveData<Event<String>>
        get() = _error

    val unauthorized: LiveData<Boolean> get() = _unauthorized
    private val _unauthorized: MutableLiveData<Boolean> = MutableLiveData()

    protected fun <T> call1(
        apiOrDBCall: suspend () -> Result<T>,
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        handleLoading: Boolean = true,
        handleError: Boolean = true,
    ) = viewModelScope.launch(Dispatchers.IO) {
        // Show loading
        if (handleLoading) {
            _loading.postValue(true)
        }

        // Execute call
        val result = apiOrDBCall.invoke()

        // hide loading
        if (handleLoading) {
            _loading.postValue(false)
        }

        // Check for result
        result.getOrNull()?.let { value ->
            onSuccess?.invoke(value)
        }

        result.exceptionOrNull()?.let { error ->
            onError?.invoke(error)
            if (handleError) {
                onCallError(error)
            }
        }
    }

    protected fun  call(
        apiCalls: List<suspend () -> Result<Any>>,
        onSuccess: ((List<Any>) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        handleLoading: Boolean = true,
        handleError: Boolean = true
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // Show loading
            if (handleLoading) {
                _loading.postValue(true)
            }

            try {
                val results = mutableListOf<Any>()
                for (apiCall in apiCalls) {
                    val result = apiCall.invoke()
                    result.getOrNull()?.let { value ->
                        results.add(value)
                    } ?: throw NullPointerException("Result is null")
                }

                // Hide loading
                if (handleLoading) {
                    _loading.postValue(false)
                }

                // Invoke onSuccess callback with the list of results
                onSuccess?.invoke(results)
            } catch (e: Exception) {
                // Hide loading in case of error
                if (handleLoading) {
                    _loading.postValue(false)
                }

                // Invoke onError callback with the error
                onError?.invoke(e)
                if (handleError) {
                    onCallError(e)
                }
            }
        }
    }



    protected fun onCallError(error: Throwable) {
        checkIsUnauthorized(error)
        setError(error.message.orEmpty())
    }

    protected fun setLoading(isLoading: Boolean) = _loading.postValue(isLoading)

    protected fun setError(errorMessage: String) = _error.postValue(Event(errorMessage))

    fun showSnackBar(snackBarMsg: String) = _showSnackBar.postValue(Event(snackBarMsg))

    private fun checkIsUnauthorized(error: Throwable) {
        if (error is RequestException && error.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            _unauthorized.postValue(true)
        }
    }

}