package com.pengxh.kt.lite.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    val loadState = MutableLiveData<LoadState>()
}