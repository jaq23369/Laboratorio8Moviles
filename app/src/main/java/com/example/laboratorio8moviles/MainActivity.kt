package com.example.laboratorio8moviles
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
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
                // Creamos un NavController
                val navController = rememberNavController()

                // Configuramos el NavHost con dos pantallas: Categorías y Recetas
                NavHost(navController = navController, startDestination = "categories") {
                    composable("categories") { CategoriesScreen(navController) }
                    composable("meals/{categoryName}") { backStackEntry ->
                        val categoryName =backStackEntry.arguments?.getString("categoryName")
                        MealsScreen(navController, categoryName ?: "")
                    }
                }
            }
        }
    }
}

@Composable
fun CategoriesScreen(navController: NavHostController) {
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
                }        } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
            }
        }
    }

    // Mostrar un LazyColumn con las categorías
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category, onClick = {
                // Navegar a la pantalla de recetas cuando se haga clic en una categoría
                navController.navigate("meals/${category.strCategory}")
            })
        }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    // Diseño de cada elemento de la categoría
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick), // Detectar clic
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
}@Composable
fun MealsScreen(navController: NavHostController, categoryName: String) {
    val meals = remember { mutableStateListOf<Meal>() }
    val coroutineScope = rememberCoroutineScope()

    // Hacer la solicitud a la API dentro de una coroutine
    LaunchedEffect(categoryName) {
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getMealsByCategory(categoryName)
                }
                if (response.isSuccessful) {
                    response.body()?.meals?.let { fetchedMeals ->
                        meals.addAll(fetchedMeals)
                    }
                }
            } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
            }
        }
    }

    // Mostrar un LazyColumn con las recetas
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(meals) { meal ->
            MealItem(meal = meal, onClick = {
                // Aquí podrías navegar a la pantalla de detalles de la receta
                // navController.navigate("mealDetails/${meal.idMeal}")
            })
        }
    }
}

@Composable
fun MealItem(meal: Meal, onClick: () -> Unit) {
    // Diseño de cada elemento de la receta
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // Imagen de la receta
            Image(
                painter = rememberAsyncImagePainter(model = meal.strMealThumb),
                contentDescription = "Meal Image",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            // Nombre de la receta
            Text(text = meal.strMeal, style = MaterialTheme.typography.bodyLarge)
        }
    }
}