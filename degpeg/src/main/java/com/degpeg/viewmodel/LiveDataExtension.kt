package com.degpeg.viewmodel

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<ArrayList<T>>.addItem(item: T) {
    val list = arrayListOf<T>()
    if (value != null) list.addAll(value!!)
    list.add(item)
    postValue(list)
}

fun <T> MutableLiveData<ArrayList<T>>.addNewItemAt(index: Int, item: T) {
    val oldValue = value ?: arrayListOf()
    oldValue.add(index, item)
    postValue(oldValue)
}

fun <T> MutableLiveData<ArrayList<T>>.removeItemAt(index: Int) {
    if (!value.isNullOrEmpty()) {
        val oldValue = value
        oldValue?.removeAt(index)
        postValue(oldValue)
    } else {
        postValue(arrayListOf())
    }
}
