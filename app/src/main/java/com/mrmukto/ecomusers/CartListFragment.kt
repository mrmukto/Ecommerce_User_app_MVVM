package com.mrmukto.ecomusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrmukto.ecomusers.adapters.CartItemAdapter
import com.mrmukto.ecomusers.databinding.FragmentCartListBinding
import com.mrmukto.ecomusers.viewmodels.UserViewModel

class CartListFragment : Fragment() {
    private lateinit var binding: FragmentCartListBinding
    private val userViewModel: UserViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartListBinding.inflate(inflater, container, false)
        val adapter = CartItemAdapter {
            userViewModel.updateCartQuantity(userViewModel.getCurrentUserId()!!, it)
        }
        binding.cartRV.layoutManager = LinearLayoutManager(requireActivity())
        binding.cartRV.adapter = adapter
        userViewModel.getAllCartItems(userViewModel.getCurrentUserId()!!)
            .observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding.checkoutBtn.isEnabled = !it.isNullOrEmpty()
                binding.cartTotalPriceTV.text = "BDT${userViewModel.getTotalCartItemPrice(it)}"
            }
        binding.checkoutBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cartListFragment_to_checkoutFragment)
        }

        return binding.root
    }

}