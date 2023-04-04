package com.zann.dev.mybank.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import com.zann.dev.mybank.R
import com.zann.dev.mybank.constants.MenuConstants
import com.zann.dev.mybank.databinding.ActivityMainBinding
import com.zann.dev.mybank.models.Category
import com.zann.dev.mybank.ui.adapters.CategoryAdapter
import com.zann.dev.mybank.viewmodels.MainViewModel
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CategoryAdapter
    private var confirmDialog: AlertDialog? = null
    private var listSize: Int = 0
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
        initOptionsMenu()
        buttonAddCategory.setOnClickListener {
            if (textInputCategory.text.toString().isBlank()) {
                textFieldCategory.error = getString(R.string.main_activity_error_message)
                return@setOnClickListener
            }
            textFieldCategory.error = null
            val objectCategory = Category(
                title = textInputCategory.text.toString(),
                totalPrice = (1..100).random().toDouble()
            )
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
                listSize = list.size
                adapter.setDataCategory(safeList)
            }
        }
    }

    private fun initOptionsMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (listSize <= 1) {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.main_activity_few_items),
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }
                return when(menuItem.itemId) {
                    R.id.option_order_by_name -> {
                        mainViewModel.orderData(MenuConstants.ORDER_BY_NAME)
                        true
                    }
                    R.id.option_order_by_price -> {
                        mainViewModel.orderData(MenuConstants.ORDER_BY_PRICE)
                        true
                    }
                    R.id.option_order_by_count -> {
                        mainViewModel.orderData(MenuConstants.ORDER_BY_COUNT)
                        true
                    }
                    else -> false
                }
            }

        })

    }
}