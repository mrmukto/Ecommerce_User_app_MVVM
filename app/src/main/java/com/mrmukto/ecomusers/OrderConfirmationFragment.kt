package com.mrmukto.ecomusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mrmukto.ecomusers.databinding.CartItemSimpleRowBinding
import com.mrmukto.ecomusers.databinding.FragmentOrderConfirmationBinding
import com.mrmukto.ecomusers.models.CartItem
import com.mrmukto.ecomusers.viewmodels.OrderViewModel
import com.mrmukto.ecomusers.viewmodels.UserViewModel

class OrderConfirmationFragment : Fragment() {
    private lateinit var binding: FragmentOrderConfirmationBinding
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)
        arguments?.let {
            binding.deliveryAddressTV.text = it.getString("address")
            binding.paymentMethodTV.text = it.getString("payment")
        }
        orderViewModel.getOrderSettings().observe(viewLifecycleOwner) {settings->
            binding.discountLabelTV.text = "Discount(${settings.discount}%)"
            binding.vatLabelTV.text = "VAT(${settings.vat}%)"
            binding.deliveryChargeTV.text = "BDT${settings.deliveryCharge}"
            userViewModel.getAllCartItems(userViewModel.getCurrentUserId()!!)
                .observe(viewLifecycleOwner) {cartList ->
                    binding.totalTV.text = "BDT${userViewModel.getTotalCartItemPrice(cartList)}"
                    populateCartItemLayout(cartList, inflater)
                    binding.discountAmountTV.text =
                        "BDT${orderViewModel.getDiscountAmount(userViewModel.getTotalCartItemPrice(cartList), settings)}"

                    binding.vatAmountTV.text =
                        "BDT${orderViewModel.getVatAmount(userViewModel.getTotalCartItemPrice(cartList), settings)}"

                    binding.grandTotalTV.text =
                        "BDT${orderViewModel.getGrandTotal(userViewModel.getTotalCartItemPrice(cartList), settings)}"
                }
        }


        return binding.root
    }

    private fun populateCartItemLayout(it: List<CartItem>?, inflater: LayoutInflater) {
        binding.cartItemsLinearLayout.removeAllViews()
        it?.let { cartList ->
            cartList.forEach {
                val cartRowBinding = CartItemSimpleRowBinding.inflate(inflater)
                cartRowBinding.item = it
                binding.cartItemsLinearLayout.addView(cartRowBinding.root)
            }
        }
    }

}