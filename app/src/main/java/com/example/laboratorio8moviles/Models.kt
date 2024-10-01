package com.example.laboratorio8moviles

// Modelo para cada categoría de receta
data class Category(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)

// Modelo para la respuesta de las categorías
data class CategoriesResponse(
    val categories: List<Category>
)

// Modelo para cada comida (receta) en una categoría
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)

// Modelo para la respuesta de las comidas filtradas por categoría
data class MealsResponse(
    val meals: List<Meal>
)

// Modelo para los detalles de una comida específica
data class MealDetail(
    val idMeal: String,
    val strMeal: String,
    val strInstructions: String,
    val strMealThumb: String
)

// Modelo para la respuesta de los detalles de una comida
data class MealDetailResponse(
    val meals: List<MealDetail>
)
