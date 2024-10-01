package com.example.laboratorio8moviles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.laboratorio8moviles.ui.theme.Laboratorio8MovilesTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Laboratorio8MovilesTheme {
                CategoriesScreen()
            }
        }
    }
}

@Composable
fun CategoriesScreen() {
    val categories = remember { mutableStateListOf<Category>() }
    val coroutineScope = rememberCoroutineScope()

    // Hacer la solicitud a la API dentro de una coroutine
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getCategories()
                }
                if (response.isSuccessful) {
                    response.body()?.categories?.let { fetchedCategories ->
                        categories.addAll(fetchedCategories)
                    }
                } else {
                    // Manejar una respuesta no exitosa
                    println("Error en la respuesta de la API: ${response.code()}")
                }
            } catch (e: Exception) {
                // Manejar excepciones de red
                println("Error en la llamada a la API: ${e.localizedMessage}")
            }
        }
    }

    // Mostrar un LazyColumn con las categorías
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category)
        }
    }
}

@Composable
fun CategoryItem(category: Category) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Imagen de la categoría
            Image(
                painter = rememberAsyncImagePainter(model = category.strCategoryThumb),
                contentDescription = "Category Image",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            // Nombre de la categoría
            Text(text = category.strCategory, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoriesScreenPreview() {
    Laboratorio8MovilesTheme {
        CategoriesScreen()
    }
}
