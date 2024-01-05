package com.pengxh.kt.lib.example

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pengxh.kt.lite.extensions.launch
import com.pengxh.kt.lite.vm.BaseViewModel
import com.pengxh.kt.lite.vm.LoadState

class ViewModelExample : BaseViewModel() {

    private val gson by lazy { Gson() }
    var resultModel: MutableLiveData<ExampleModel> = MutableLiveData<ExampleModel>()

    fun getTestDataByPage(channel: String, offset: Int) = launch({
        loadState.value = LoadState.Loading
        //伪代码
        val response = "请求网络"
        loadState.value = LoadState.Success
        resultModel.value = gson.fromJson<ExampleModel>(
            response, object : TypeToken<ExampleModel>() {}.type
        )
    }, {
        loadState.value = LoadState.Fail
    })
}