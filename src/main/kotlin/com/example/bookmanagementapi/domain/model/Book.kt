package com.example.bookmanagementapi.domain.model

import java.time.LocalDate

/** 書籍 */
data class Book(
    /** ID */
    val id: Long,
    /** タイトル */
    val title: String,
    /** 著者ID */
    val authorId: Long,
    /** 出版日 */
    val publishedAt: LocalDate
)
