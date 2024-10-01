package com.example.laboratorio8moviles

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Definir la interfaz para las solicitudes de la API
interface TheMealDBApi {

    // Obtener todas las categorías de recetas
    @GET("json/v1/1/categories.php")
    suspend fun getCategories(): Response<CategoriesResponse>

    // Obtener las recetas filtradas por categoría
    @GET("json/v1/1/filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String): Response<MealsResponse>

    // Obtener los detalles de una receta por su ID
    @GET("json/v1/1/lookup.php")
    suspend fun getMealDetails(@Query("i") id: String): Response<MealDetailResponse>
}