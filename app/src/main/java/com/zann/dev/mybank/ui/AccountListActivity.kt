package com.zann.dev.mybank.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.zann.dev.mybank.R
import com.zann.dev.mybank.constants.KeysConstants
import com.zann.dev.mybank.databinding.ActivityAccountListBinding
import com.zann.dev.mybank.models.Account
import com.zann.dev.mybank.models.Category
import kotlin.properties.Delegates

class AccountListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountListBinding
    private lateinit var category: Category
    private var oldPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getExtras()
        initViews()
    }

    private fun getExtras() {
        intent.getSerializableExtra(KeysConstants.KEY_CATEGORY)?.let {
            category  = it as Category
        }
        oldPosition = intent.getIntExtra(KeysConstants.CATEGORY_OLD_POSITION, 0)
        Log.d("AccountListActivity Extras:", "$oldPosition, $category")
    }

    private fun initViews() = with(binding) {
        buttonClose.setOnClickListener {
            val categoryToSend = Category(
                title = "Texto Mudado",
                accountList = mutableListOf(
                    Account("contaq", "1111", 50.0),
                    Account("contaqaaa", "11aa11", 100.0)
                )
            )
            setResult(categoryToSend)
        }
    }

    private fun setResult(category: Category) {
        val intent = Intent()
        intent.apply {
            putExtra(KeysConstants.KEY_CATEGORY, category)
            putExtra(KeysConstants.CATEGORY_OLD_POSITION, oldPosition)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

}