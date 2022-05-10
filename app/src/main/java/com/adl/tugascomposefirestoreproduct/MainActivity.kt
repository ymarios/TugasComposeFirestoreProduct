package com.adl.tugascomposefirestoreproduct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.adl.tugascomposefirestoreproduct.data.Product
import com.adl.tugascomposefirestoreproduct.repo.OnFailure
import com.adl.tugascomposefirestoreproduct.repo.OnSuccess
import com.adl.tugascomposefirestoreproduct.repo.ProductRepo
import com.adl.tugascomposefirestoreproduct.ui.theme.TugasComposeFirestoreProductTheme
import com.adl.tugascomposefirestoreproduct.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {

    val productViewModel by viewModels<ProductViewModel>(factoryProducer = {ProductViewModelFactory(ProductRepo())})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TugasComposeFirestoreProductTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProductItem(productViewModel = productViewModel)
                }
            }
        }
    }
}

@Composable
fun ProductItem(productViewModel: ProductViewModel){

    when(val productList = productViewModel.productStateFlow.asStateFlow().collectAsState().value){
        is OnFailure ->{

        }
        is OnSuccess ->{
            val listOfProduct = productList.querySnapshot?.toObjects(Product::class.java)
            listOfProduct?.let {
                Column {
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)){
                        items(listOfProduct){
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                                shape = RoundedCornerShape(16.dp), elevation = 5.dp) {
                                ProductItem(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    var showProductFullDesciption by remember { mutableStateOf(false) }

    Column {
        Row(modifier = Modifier.padding(12.dp)) {

            AsyncImage(
                model = product.gambar,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.padding(5.dp, 0.dp, 0.dp, 0.dp)) {
                Text(
                    text = product.productName,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
                Text(
                    text = product.category,
                    style = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp)
                )
                Text(
                    text = product.description,
                    style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
                )

            }

            Column(modifier = Modifier.padding(125.dp, 0.dp, 0.dp, 0.dp)) {
                AsyncImage(
                    model = "https://cdn.imgbin.com/21/2/23/imgbin-arrow-drop-down-list-button-computer-icons-down-arrow-triangular-black-art-illustration-kw6fcQdAR0GJnU5YspsZcK7pN.jpg",
                    contentDescription = null,
                    modifier = Modifier.padding(25.dp, 0.dp, 0.dp, 0.dp)
                        .size(30.dp)
                        .clickable { showProductFullDesciption = showProductFullDesciption.not() },
                    contentScale = ContentScale.Fit
                )

                Text(modifier = Modifier.padding(0.dp, 13.dp, 0.dp, 0.dp),
                    text = product.price,
                    style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp)
                )
            }
        }

        AnimatedVisibility(visible = showProductFullDesciption) {
            Text(
                text = product.fullDescription, style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic, fontSize = 11.sp
                ),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            )
        }
    }
}
    class ProductViewModelFactory (val booksRepo: ProductRepo): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductViewModel::class.java)){
                return ProductViewModel(booksRepo) as T
            }
            throw IllegalAccessException()
        }
}

