package com.adl.tugascomposefirestoreproduct.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adl.tugascomposefirestoreproduct.repo.ProductRepo
import com.adl.tugascomposefirestoreproduct.repo.ProductResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(val productRepo: ProductRepo): ViewModel() {

    val productStateFlow = MutableStateFlow<ProductResponse?>(null)

    init{
        viewModelScope.launch {
            productRepo.getProductDetails().collect(){
                productStateFlow.value = it
            }
        }
    }

    fun getBookInfo() = productRepo.getProductDetails()

}