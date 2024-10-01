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
import androidx.compose.ui.Alignment
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
                // Creamos un NavController para manejar la navegación
                val navController = rememberNavController()

                // Definimos el NavHost para manejar las rutas entre pantallas
                NavHost(navController = navController, startDestination = "categories") {
                    // Pantalla de categorías
                    composable("categories") { CategoriesScreen(navController) }

                    // Pantalla de recetas filtradas por categoría
                    composable("meals/{categoryName}") { backStackEntry ->
                        val categoryName = backStackEntry.arguments?.getString("categoryName")
                        MealsScreen(navController, categoryName ?: "")
                    }

                    // Pantalla de detalles de la receta
                    composable("mealDetails/{mealId}") { backStackEntry ->
                        val mealId = backStackEntry.arguments?.getString("mealId")
                        MealDetailScreen(navController, mealId ?: "")
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
                // Navegar a la pantalla de detalles de la receta
                navController.navigate("mealDetails/${meal.idMeal}")
            })
        }
    }
}

@Composable
fun MealItem(meal: Meal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = meal.strMealThumb),
                contentDescription = "Meal Image",
                modifier = Modifier
                    .size(64.dp)
                    .padding(end = 16.dp)
            )
            Text(text = meal.strMeal, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun MealDetailScreen(navController: NavHostController, mealId: String) {
    // Variable para almacenar los detalles de la receta
    val mealDetail = remember { mutableStateOf<MealDetail?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Hacer la solicitud a la API para obtener los detalles de la receta
    LaunchedEffect(mealId) {
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitClient.api.getMealDetails(mealId)
                }
                if (response.isSuccessful) {
                    response.body()?.meals?.firstOrNull()?.let {
                        mealDetail.value = it
                    }
                }
            } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
            }
        }
    }

    // Mostrar los detalles de la receta
    mealDetail.value?.let { meal ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = meal.strMeal,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(model = meal.strMealThumb),
                contentDescription = "Meal Image",
                modifier = Modifier.fillMaxWidth().height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = meal.strInstructions,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
