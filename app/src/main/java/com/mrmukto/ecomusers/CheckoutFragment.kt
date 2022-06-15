package com.mrmukto.ecomusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.mrmukto.ecomusers.databinding.FragmentCheckoutBinding
import com.mrmukto.ecomusers.utils.PaymentMethod
import com.mrmukto.ecomusers.viewmodels.UserViewModel

class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private var paymentMethod = PaymentMethod.cod
    private var deliveryAddress: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        binding.paymentRG.setOnCheckedChangeListener { group, checkedId ->
            var rb: RadioButton = container!!.findViewById(checkedId)
            paymentMethod = rb.text.toString()
        }
        userViewModel.getUser(userViewModel.getCurrentUserId()!!)
            .observe(viewLifecycleOwner) {
                deliveryAddress = it.userAddress
                deliveryAddress?.let { address->
                    binding.deliveryAddressET.setText(address)
                }
            }
        binding.nextButton.setOnClickListener {
            val address = binding.deliveryAddressET.text.toString()
            // TODO: validate empty fields
            userViewModel.updateUserAddress(userViewModel.getCurrentUserId()!!, address)
            findNavController()
                .navigate(
                    R.id.action_checkoutFragment_to_orderConfirmationFragment,
                args = bundleOf("address" to address, "payment" to paymentMethod))
        }
        return binding.root
    }

}