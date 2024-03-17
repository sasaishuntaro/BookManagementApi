package com.example.bookmanagementapi.application.request

import java.time.LocalDate

/** 書籍登録リクエスト */
data class RegisterBookRequest(
    @get:jakarta.validation.constraints.Size(max = 255)
    @get:jakarta.validation.constraints.NotBlank
    val title: String,
    @get:jakarta.validation.constraints.NotNull
    @get:jakarta.validation.constraints.Min(1)
    val authorId: Long,
    @get:jakarta.validation.constraints.NotNull
    val publishedAt: LocalDate
)
