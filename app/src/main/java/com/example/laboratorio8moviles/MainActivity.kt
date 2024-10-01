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
                        val categoryName =