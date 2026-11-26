package com.patitas.data.network

import com.patitas.data.network.dto.RazasPerroDto
import retrofit2.http.GET

interface RazaApiService {

    @GET("breeds/list/all")
    suspend fun getRazasPerro(): RazasPerroDto
}
