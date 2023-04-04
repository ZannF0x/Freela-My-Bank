package com.zann.dev.mybank.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zann.dev.mybank.models.Category

class MainViewModel: ViewModel() {

    private val listOfCategory = mutableListOf<Category>()

    private var _categoryList = MutableLiveData<MutableList<Category>>()
    val categoryList: LiveData<MutableList<Category>>
        get() = _categoryList

    fun setDataItem(category: Category) {
        listOfCategory.add(category)
        _categoryList.value = listOfCategory
    }

    class MainViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}