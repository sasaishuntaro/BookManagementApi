package com.example.bookmanagementapi.application.request

/** 著者更新リクエスト */
data class UpdateAuthorRequest(
    @get:jakarta.validation.constraints.Size(max = 255)
    @get:jakarta.validation.constraints.NotBlank
    val name: String
)
