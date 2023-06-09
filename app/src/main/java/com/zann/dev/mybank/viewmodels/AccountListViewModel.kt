package com.zann.dev.mybank.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zann.dev.mybank.constants.MenuConstants
import com.zann.dev.mybank.models.Account
import com.zann.dev.mybank.models.Category

class AccountListViewModel: ViewModel() {

    private var listOfAccount = mutableListOf<Account>()

    private var _accountList = MutableLiveData<MutableList<Account>>()
    val accountList: LiveData<MutableList<Account>>
        get() = _accountList

    private var _category = MutableLiveData<Category>()
    val category: LiveData<Category>
        get() = _category

    private var _oldPosition = MutableLiveData<Int>()
    val oldPosition: LiveData<Int>
        get() = _oldPosition

    private var _oldPositionToChange = MutableLiveData<Int?>()
    val oldPositionToChange: LiveData<Int?>
        get() = _oldPositionToChange

    fun setCategoryDataExtras(category: Category?) {
        category?.let {
            _category.value = it
            listOfAccount = it.accountList
            _accountList.value = listOfAccount
        }
    }

    fun setOldPositionDataExtra(oldPosition: Int) {
        if (oldPosition > -1) {
            _oldPosition.value = oldPosition
        }
    }

    fun setAccountData(account: Account) {
        listOfAccount.add(account)
        _accountList.value = listOfAccount
    }

    fun setOldPositionToChange(oldPosition: Int?) {
        _oldPositionToChange.value = oldPosition
    }

    fun orderData(orderBy: String) {
        when(orderBy) {
            MenuConstants.ORDER_BY_NAME -> {
                listOfAccount.sortBy { it.title }
            }
            MenuConstants.ORDER_BY_DATE -> {
                listOfAccount.sortBy { it.date }
            }
            MenuConstants.ORDER_BY_PRICE -> {
                listOfAccount.sortBy { it.price }
            }
        }
        _accountList.value = listOfAccount
    }

    class AccountListViewModelFactory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AccountListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AccountListViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}