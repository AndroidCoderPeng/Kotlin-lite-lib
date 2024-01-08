package com.pengxh.kt.lite.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pengxh.kt.lite.utils.LoadState

abstract class BaseViewModel : ViewModel() {
    val loadState = MutableLiveData<LoadState>()
}