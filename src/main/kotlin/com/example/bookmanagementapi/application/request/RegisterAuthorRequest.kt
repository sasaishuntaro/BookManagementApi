package com.example.bookmanagementapi.application.request

/** 著者登録リクエスト */
data class RegisterAuthorRequest(
    @get:jakarta.validation.constraints.Size(max = 255)
    @get:jakarta.validation.constraints.NotBlank
    val name: String
)
