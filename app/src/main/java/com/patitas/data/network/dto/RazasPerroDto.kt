package com.patitas.data.network.dto

import kotlinx.serialization.Serializable


@Serializable
data class RazasPerroDto(
    val message: Map<String, List<String>> = emptyMap(),
    val status: String = ""
)
