package com.mrmukto.ecomusers.viewmodels

import androidx.lifecycle.ViewModel
import com.mrmukto.ecomusers.models.CartItem
import com.mrmukto.ecomusers.repos.UserRepository
import com.google.firebase.auth.FirebaseAuth

class UserViewModel : ViewModel() {
    val userRepository = UserRepository()
    fun getCurrentUserId() = FirebaseAuth.getInstance().currentUser?.uid
    fun getAllCartItems(userId: String) = userRepository.getAllCartItems(userId)
    fun addToCart(userId: String, cartItem: CartItem) = userRepository.addToCart(userId, cartItem)
    fun removeFromCart(userId: String, cartItem: CartItem) = userRepository.removeFromCart(userId, cartItem)
    fun updateCartQuantity(userId: String, cartItem: CartItem) = userRepository.updateCartQuantity(userId, cartItem)
    fun getTotalCartItemPrice(cartList: List<CartItem>) : Double{
        var total = 0.0
        cartList.forEach {
            total += it.quantity * it.price!!
        }
        return total
    }

    fun getUser(userId: String) = userRepository.getUser(userId)
    fun updateUserAddress(userId: String, address: String) = userRepository
        .updateUserAddress(userId, address)
}