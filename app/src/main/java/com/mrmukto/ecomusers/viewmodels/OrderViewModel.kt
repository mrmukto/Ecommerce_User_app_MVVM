package com.mrmukto.ecomusers.viewmodels

import androidx.lifecycle.ViewModel
import com.mrmukto.ecomusers.models.OrderSettings
import com.mrmukto.ecomusers.repos.OrderRepository

class OrderViewModel : ViewModel() {
    private val orderRepository = OrderRepository()

    fun getOrderSettings() = orderRepository.getOrderSettings()

    fun getDiscountAmount(total: Double, settings: OrderSettings) : Double {
        return (total * settings.discount) / 100
    }

    fun getVatAmount(total: Double, settings: OrderSettings) : Double {
        val priceAfterDiscount = total - getDiscountAmount(total, settings)
        return (priceAfterDiscount * settings.vat) / 100
    }

    fun getGrandTotal(total: Double, settings: OrderSettings) : Double{
        return total - getDiscountAmount(total, settings) + getVatAmount(total, settings) + settings.deliveryCharge
    }
}