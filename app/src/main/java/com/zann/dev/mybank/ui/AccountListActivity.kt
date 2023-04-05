package com.zann.dev.mybank.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
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
            if (textInputAccount.text.toString().isBlank() ||
                textInputValue.text.toString().isBlank() ||
                textInputDay.text.toString().isBlank() ||
                textInputMonth.text.toString().isBlank() ||
                textInputYear.text.toString().isBlank()
            ) {
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
                date = "${textInputDay.text.toString()}/${textInputMonth.text.toString()}/${textInputYear.text.toString()}"
            )
            accountListViewModel.setAccountData(account)
        }
    }

    private fun initRecyclerView() {
        accountAdapter = AccountAdapter(
            onDeleteClick = {  },
            onEditItemClick = {
                setResult()
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

}