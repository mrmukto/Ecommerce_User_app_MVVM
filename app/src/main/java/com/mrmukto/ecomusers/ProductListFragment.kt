package com.mrmukto.ecomusers

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.GridLayoutManager
import com.mrmukto.ecomusers.adapters.ProductAdapter
import com.mrmukto.ecomusers.databinding.FragmentProductListBinding
import com.mrmukto.ecomusers.models.CartItem
import com.mrmukto.ecomusers.models.Product
import com.mrmukto.ecomusers.utils.CartAction
import com.mrmukto.ecomusers.viewmodels.LoginViewModel
import com.mrmukto.ecomusers.viewmodels.ProductViewModel
import com.mrmukto.ecomusers.viewmodels.UserViewModel

class ProductListFragment : Fragment() {
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProductListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        val adapter = ProductAdapter {action, cartItem ->
            performCartAction(action, cartItem)
        }
        val glm = GridLayoutManager(requireActivity(), 2)
        binding.productRV.layoutManager = glm
        binding.productRV.adapter = adapter
        loginViewModel.authLD.observe(viewLifecycleOwner) {
            if (it == LoginViewModel.Authentication.UNAUTHENTICATED) {
                findNavController().navigate(R.id.action_productListFragment_to_loginFragment)
            }
        }

        userViewModel.getCurrentUserId()?.let{
            userViewModel.getAllCartItems(it)
                .observe(viewLifecycleOwner) {cartList ->
                    productViewModel.getProducts().observe(viewLifecycleOwner) {productList ->
                        val tempProductList = mutableListOf<Product>()
                        for (product in productList) {
                            for (item in cartList) {
                                if (product.id == item.productId) {
                                    product.inCart = true
                                }
                            }
                            tempProductList.add(product)
                        }
                        adapter.submitList(tempProductList)
                    }
                }

        }


        return binding.root
    }

    private fun performCartAction(action: CartAction, cartItem: CartItem) {
        when(action) {
            CartAction.ADD -> {
                userViewModel.addToCart(loginViewModel.auth.currentUser?.uid!!, cartItem)
            }
            CartAction.REMOVE -> {
                userViewModel.removeFromCart(loginViewModel.auth.currentUser?.uid!!, cartItem)
            }
        }
    }

}