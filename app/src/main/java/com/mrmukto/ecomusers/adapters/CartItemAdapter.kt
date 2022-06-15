package com.mrmukto.ecomusers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrmukto.ecomusers.databinding.CartItemRowBinding
import com.mrmukto.ecomusers.models.CartItem

class CartItemAdapter(val cartActionCallback: (CartItem) -> Unit) : ListAdapter<CartItem, CartItemAdapter.CartItemViewHolder>(CartItemDiffUtil()){

    class CartItemViewHolder(val binding: CartItemRowBinding) :
            RecyclerView.ViewHolder(binding.root) {
                fun bind(cartItem: CartItem) {
                    binding.cartItem = cartItem
                }
            }

    class CartItemDiffUtil : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding = CartItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = getItem(position)
        holder.bind(cartItem)
        updateQuantityAndPrice(cartItem, holder.binding)
        holder.binding.cartItemAddBtn.setOnClickListener {
            cartItem.quantity += 1
            updateQuantityAndPrice(cartItem, holder.binding)
            cartActionCallback(cartItem)
        }
        holder.binding.cartItemMinusBtn.setOnClickListener {
            if (cartItem.quantity > 1) {
                cartItem.quantity -= 1
                updateQuantityAndPrice(cartItem, holder.binding)
                cartActionCallback(cartItem)
            }
        }
        holder.binding.cartItemDeleteBtn.setOnClickListener {

        }
    }

    private fun updateQuantityAndPrice(cartItem: CartItem, binding: CartItemRowBinding) {
        binding.price = cartItem.quantity * cartItem.price!!
        binding.quantity = cartItem.quantity
    }
}