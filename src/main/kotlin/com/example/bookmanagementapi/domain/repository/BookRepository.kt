package com.example.bookmanagementapi.domain.repository

import com.example.bookmanagementapi.domain.model.Book
import java.time.LocalDate

/** 書籍リポジトリ */
interface BookRepository {

    /** 書籍のID検索 */
    fun findById(id: Long): Book?

    /** 書籍のID検索 */
    fun findByAuthorId(authorId: Long): List<Book>

    /** 書籍検索 */
    fun search(
        title: String?,
        authorName: String?,
        publishedFrom: LocalDate?,
        publishedTo: LocalDate?
    ): List<Book>

    /** 書籍登録 */
    fun insert(
        title: String,
        authorId: Long,
        publishedAt: LocalDate
    ): Book

    /** 書籍更新 */
    fun update(book: Book): Int
}