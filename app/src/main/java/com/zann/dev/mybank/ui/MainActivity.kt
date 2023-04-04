package com.zann.dev.mybank.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.zann.dev.mybank.R
import com.zann.dev.mybank.databinding.ActivityMainBinding
import com.zann.dev.mybank.models.Category
import com.zann.dev.mybank.ui.adapters.CategoryAdapter
import com.zann.dev.mybank.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CategoryAdapter
    private var confirmDialog: AlertDialog? = null
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        subscribeViews()
    }

    private fun initViews() = with(binding) {
        initAdapter()
        buttonAddCategory.setOnClickListener {
            if (textInputCategory.text.toString().isBlank()) {
                textFieldCategory.error = getString(R.string.main_activity_error_message)
                return@setOnClickListener
            }
            textFieldCategory.error = null
            val objectCategory = Category(title = textInputCategory.text.toString())
            mainViewModel.setDataItem(objectCategory)
        }
    }

    private fun initAdapter() {
        adapter = CategoryAdapter(
            onDeleteClick = { initConfirmDialog(it) },
            onMoreItemClick = {
                val itemPosition = adapter.getItemPosition(it)
                val category = Category("Item mudado!")
                adapter.changeDataByPosition(itemPosition, category)
            }
        )
        binding.recyclerViewCategory.adapter = adapter
    }

    private fun initConfirmDialog(category: Category) {
        if (confirmDialog == null || confirmDialog?.isShowing == false) {
            confirmDialog = AlertDialog.Builder(this).apply {
                this.setIcon(R.drawable.ic_delete)
                this.setTitle(getString(R.string.main_activity_confirm_dialog_title))
                this.setMessage(
                    getString(R.string.main_activity_confirm_dialog_description, category.title)
                )
                this.setPositiveButton(getString(R.string.common_yes)) { _, _ ->
                    adapter.removeItem(category)
                    confirmDialog?.dismiss()
                }
                this.setNegativeButton(getString(R.string.common_no)) { _, _ ->
                    confirmDialog?.dismiss()
                }
            }.create()
            confirmDialog?.show()
        }
    }

    private fun subscribeViews() {
        mainViewModel.categoryList.observe(this@MainActivity) { list ->
            list?.let { safeList ->
                Log.d("Categories:", "$safeList")
                adapter.setDataCategory(safeList)
            }
        }
    }
}