package com.zann.dev.mybank.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zann.dev.mybank.databinding.AccountItemBinding
import com.zann.dev.mybank.models.Account
import com.zann.dev.mybank.models.Category
import com.zann.dev.mybank.utils.CoinUtil

class AccountAdapter(
    private val onEditItemClick: (Account) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private var accountList = mutableListOf<Account>()

    fun setDataAccount(account: MutableList<Account>) {
        accountList = account
        this.notifyDataSetChanged()
    }

    fun getData() = accountList

    fun changeDataByPosition(oldPosition: Int, newItem: Account) {
        accountList.removeAt(oldPosition)
        accountList.add(oldPosition, newItem)
        this.notifyItemChanged(oldPosition)
    }

    fun getItemPosition(account: Account) = accountList.indexOf(account)

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
            onEditItemClick
        )
    }

    inner class AccountViewHolder(
        itemView: AccountItemBinding
    ) : RecyclerView.ViewHolder(itemView.root) {

        private val title: TextView
        private val details: TextView
        private val textPrice: TextView
        private val buttonEdit: ImageView

        init {
            title = itemView.textTitle
            details = itemView.textInfo
            textPrice = itemView.textPrice
            buttonEdit = itemView.imageEdit
        }

        fun bind(
            account: Account,
            onEditItemClick: (Account) -> Unit
        ) {
            title.text = account.title
            details.text = account.date
            textPrice.text = CoinUtil.doubleToReal(account.price)
            buttonEdit.setOnClickListener {
                onEditItemClick(account)
            }
        }

    }

}