package com.zann.dev.mybank.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zann.dev.mybank.databinding.AccountItemBinding
import com.zann.dev.mybank.models.Account
import com.zann.dev.mybank.utils.CoinUtil

class AccountAdapter(
    private val onDeleteClick: (Account) -> Unit,
    private val onEditItemClick: (Account) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private var accountList = mutableListOf<Account>()

    fun setDataAccount(account: MutableList<Account>) {
        accountList = account
        this.notifyDataSetChanged()
    }

    fun getData() = accountList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            AccountItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = accountList.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(
            accountList[position],
            onDeleteClick,
            onEditItemClick
        )
    }

    inner class AccountViewHolder(
        itemView: AccountItemBinding
    ) : RecyclerView.ViewHolder(itemView.root) {

        private val title: TextView
        private val details: TextView
        private val textPrice: TextView
        private val buttonDelete: ImageView
        private val buttonEdit: ImageView

        init {
            title = itemView.textTitle
            details = itemView.textInfo
            textPrice = itemView.textPrice
            buttonDelete = itemView.imageDelete
            buttonEdit = itemView.imageEdit
        }

        fun bind(
            category: Account,
            onDeleteClick: (Account) -> Unit,
            onMoreItemClick: (Account) -> Unit
        ) {
            title.text = category.title
            details.text = category.date
            textPrice.text = CoinUtil.doubleToReal(category.price)
            buttonDelete.setOnClickListener {
                onDeleteClick(category)
            }
            buttonEdit.setOnClickListener {
                onMoreItemClick(category)
            }
        }

    }

}