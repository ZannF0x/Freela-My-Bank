package com.zann.dev.mybank.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.zann.dev.mybank.R
import com.zann.dev.mybank.constants.KeysConstants
import com.zann.dev.mybank.databinding.ActivityAccountListBinding
import com.zann.dev.mybank.models.Account
import com.zann.dev.mybank.models.Category
import com.zann.dev.mybank.ui.adapters.AccountAdapter
import com.zann.dev.mybank.viewmodels.AccountListViewModel

class AccountListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountListBinding
    private lateinit var accountAdapter: AccountAdapter
    private var oldPositionExtra: Int? = null
    private var oldPositionToChangeView: Int? = null
    private var confirmDialog: AlertDialog? = null
    private val accountListViewModel: AccountListViewModel by viewModels {
        AccountListViewModel.AccountListViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        getExtras()
        initViews()
        subscribeViews()
    }

    private fun getExtras() {
        intent.getSerializableExtra(KeysConstants.KEY_CATEGORY)?.let {
            accountListViewModel.setCategoryDataExtras(it as Category)
        }
        accountListViewModel.setOldPositionDataExtra(
            intent.getIntExtra(KeysConstants.CATEGORY_OLD_POSITION, -1)
        )
    }

    private fun initViews() = with(binding) {
        initRecyclerView()
        buttonAddAccount.setOnClickListener {
            if (validateForm()) {
                Toast.makeText(
                    this@AccountListActivity,
                    getString(R.string.account_list_activity_error_input),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val account = Account(
                title = textInputAccount.text.toString(),
                price = textInputValue.text.toString().toDouble(),
                day = textInputDay.text.toString(),
                month = textInputMonth.text.toString(),
                year = textInputYear.text.toString()
            )
            accountListViewModel.setAccountData(account)
        }
        buttonEditAccount.setOnClickListener {
            if (validateForm()) {
                Toast.makeText(
                    this@AccountListActivity,
                    getString(R.string.account_list_activity_error_input),
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val account = Account(
                title = textInputAccount.text.toString(),
                price = textInputValue.text.toString().toDouble(),
                day = textInputDay.text.toString(),
                month = textInputMonth.text.toString(),
                year = textInputYear.text.toString()
            )
            oldPositionToChangeView?.let {
                initConfirmEditDialog(account, it)
            }
        }
    }

    private fun initRecyclerView() {
        accountAdapter = AccountAdapter(
            onEditItemClick = {
                setFormToEdit(it)
                accountListViewModel.setOldPositionToChange(accountAdapter.getItemPosition(it))
            }
        )
        binding.recyclerViewAccount.adapter = accountAdapter
    }

    override fun onBackPressed() { Log.d("onBackPressed:", "blocked") }

    override fun onSupportNavigateUp(): Boolean {
        setResult()
        return true
    }

    private fun subscribeViews() = with(accountListViewModel) {
        category.observe(this@AccountListActivity) { category ->
            binding.textTitle.text = category.title
        }
        accountList.observe(this@AccountListActivity) { list ->
            list?.let { safeList ->
                Log.d("ListOfAccount:", safeList.toString())
                accountAdapter.setDataAccount(safeList)
            }
        }
        oldPosition.observe(this@AccountListActivity) { position ->
            oldPositionExtra = position
        }
        oldPositionToChange.observe(this@AccountListActivity) { position ->
            position?.let {
                oldPositionToChangeView = it
                enableEditButton()
            } ?: run {
                restoreButtons()
            }
        }
    }

    private fun validateForm(): Boolean {
        return (binding.textInputAccount.text.toString().isBlank() ||
                binding.textInputValue.text.toString().isBlank() ||
                binding.textInputDay.text.toString().isBlank() ||
                binding.textInputMonth.text.toString().isBlank() ||
                binding.textInputYear.text.toString().isBlank()
                )
    }

    private fun setFormToEdit(account: Account) = with(binding) {
        textInputAccount.setText(account.title)
        textInputDay.setText(account.day)
        textInputMonth.setText(account.month)
        textInputYear.setText(account.year)
        textInputValue.setText(account.price.toString())
    }

    private fun setResult() {
        val categoryToSend = Category(
            title = accountListViewModel.category.value?.title ?: binding.textTitle.toString(),
            accountList = accountListViewModel.accountList.value ?: accountAdapter.getData(),
        )
        val intent = Intent()
        intent.apply {
            putExtra(KeysConstants.KEY_CATEGORY, categoryToSend)
            putExtra(KeysConstants.CATEGORY_OLD_POSITION, oldPositionExtra)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun initConfirmEditDialog(account: Account, oldPosition: Int) {
        if (confirmDialog == null || confirmDialog?.isShowing == false) {
            confirmDialog = AlertDialog.Builder(this).apply {
                this.setIcon(R.drawable.ic_edit)
                this.setTitle(getString(R.string.account_list_activity_confirm_dialog_title))
                this.setMessage(
                    getString(R.string.account_list_activity_confirm_dialog_description)
                )
                this.setPositiveButton(getString(R.string.common_yes)) { _, _ ->
                    accountAdapter.changeDataByPosition(oldPosition, account)
                    accountListViewModel.setOldPositionToChange(null)
                    confirmDialog?.dismiss()
                }
                this.setNegativeButton(getString(R.string.common_no)) { _, _ ->
                    accountListViewModel.setOldPositionToChange(null)
                    confirmDialog?.dismiss()
                }
                this.setCancelable(false)
            }.create()
            confirmDialog?.show()
        }
    }

    private fun enableEditButton() {
        binding.buttonAddAccount.visibility = View.INVISIBLE
        binding.buttonAddAccount.isEnabled = false
        binding.buttonEditAccount.visibility = View.VISIBLE
    }
    private fun restoreButtons() {
        binding.buttonAddAccount.visibility = View.VISIBLE
        binding.buttonEditAccount.visibility = View.GONE
        binding.buttonAddAccount.isEnabled = true
        oldPositionToChangeView = null
    }

}