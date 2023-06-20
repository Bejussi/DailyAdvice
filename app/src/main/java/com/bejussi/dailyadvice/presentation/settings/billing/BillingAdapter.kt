package com.bejussi.dailyadvice.presentation.settings.billing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.ProductDetails
import com.bejussi.dailyadvice.databinding.DonateItemBinding

class BillingAdapter(
    private val billingActionListener: BillingActionListener
): ListAdapter<ProductDetails, BillingAdapter.BillingViewHolder>(DiffCallback()) {

    class BillingViewHolder(
        val binding: DonateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(productDetails: ProductDetails) {
            binding.apply {
                donateText.text = productDetails.name
                donatePriceText.text = productDetails.oneTimePurchaseOfferDetails?.formattedPrice
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        return BillingViewHolder(
            DonateItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)
        holder.itemView.setOnClickListener {
            billingActionListener.startBilling(currentProduct)
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<ProductDetails>() {
        override fun areItemsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ProductDetails, newItem: ProductDetails): Boolean {
            return oldItem == newItem
        }

    }
}