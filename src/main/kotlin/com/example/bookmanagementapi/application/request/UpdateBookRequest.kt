package com.example.bookmanagementapi.application.request

import java.time.LocalDate

/** 書籍更新リクエスト */
data class UpdateBookRequest(
    @get:jakarta.validation.constraints.Size(max = 255)
    val title: String?,
    val publishedAt: LocalDate?
)
