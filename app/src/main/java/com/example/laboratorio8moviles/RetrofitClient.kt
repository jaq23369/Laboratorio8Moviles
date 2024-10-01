package com.example.laboratorio8moviles

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base de la API
    private const val BASE_URL = "https://www.themealdb.com/api/"

    // Configuración de Retrofit con el patrón Singleton
    val api: TheMealDBApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)  // Establece la URL base
            .addConverterFactory(GsonConverterFactory.create())  // Convertidor de Gson para manejar JSON
            .build()
            .create(TheMealDBApi::class.java)  // Crea la implementación de la interfaz
    }
}