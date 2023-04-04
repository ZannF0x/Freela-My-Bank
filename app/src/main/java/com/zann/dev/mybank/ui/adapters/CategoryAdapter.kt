package com.zann.dev.mybank.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zann.dev.mybank.databinding.BankItemBinding
import com.zann.dev.mybank.models.Account
import com.zann.dev.mybank.models.Category
import com.zann.dev.mybank.utils.CoinUtil

class CategoryAdapter(
    private val onDeleteClick: (Category) -> Unit,
    private val onMoreItemClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoryList = mutableListOf<Category>()

    fun setDataCategory(categories: MutableList<Category>) {
        categoryList = categories
        if (categoryList.size == 1) {
            this.notifyItemInserted(categoryList.size - 1)
            return
        }
        this.notifyItemRangeChanged(0, categories.size-1)
    }

    fun changeDataByPosition(oldPosition: Int, newItem: Category) {
        categoryList.removeAt(oldPosition)
        categoryList.add(oldPosition, newItem)
        this.notifyItemChanged(oldPosition)
    }

    fun removeItem(category: Category) {
        val removedPosition = categoryList.indexOf(category)
        categoryList.remove(category)
        this.notifyItemRemoved(removedPosition)
    }

    fun getItemPosition(category: Category) = categoryList.indexOf(category)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            BankItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(
            categoryList[position],
            onDeleteClick,
            onMoreItemClick
        )
    }

    inner class CategoryViewHolder(
        itemView: BankItemBinding
    ) : RecyclerView.ViewHolder(itemView.root) {

        private val title: TextView
        private val details: TextView
        private val textPrice: TextView
        private val buttonDelete: ImageView
        private val buttonList: ImageView

        init {
            title = itemView.textTitle
            details = itemView.textInfo
            textPrice = itemView.textPrice
            buttonDelete = itemView.imageDelete
            buttonList = itemView.imageList
        }

        fun bind(
            category: Category,
            onDeleteClick: (Category) -> Unit,
            onMoreItemClick: (Category) -> Unit
        ) {
            title.text = category.title
            details.text = category.totalAccount.toString()
            textPrice.text = CoinUtil.doubleToReal(getTotalPrice(category.accountList))
            buttonDelete.setOnClickListener {
                onDeleteClick(category)
            }
            buttonList.setOnClickListener {
                onMoreItemClick(category)
            }
        }

        private fun getTotalPrice(list: List<Account>): Double {
            var totalPrice = 0.0
            if (list.isEmpty()) {
                return totalPrice
            }
            list.forEach { account ->
                totalPrice += account.price
            }
            return totalPrice
        }

    }

}