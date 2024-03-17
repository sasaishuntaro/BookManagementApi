package com.example.bookmanagementapi.domain.service

import com.example.bookmanagementapi.domain.model.Book
import com.example.bookmanagementapi.domain.repository.AuthorRepository
import com.example.bookmanagementapi.domain.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/** 書籍サービス */
@Service
class BookService(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) {

    /**
     * 書籍検索
     * - タイトル
     * - 著者名
     * - 出版日
     */
    fun search(
        title: String?,
        authorName: String?,
        publishedFrom: LocalDate?,
        publishedTo: LocalDate?
    ): List<Book> {
        return bookRepository
            .search(
                title = title,
                authorName = authorName,
                publishedFrom = publishedFrom,
                publishedTo = publishedTo
            )
    }

    /** 書籍登録 */
    @Transactional
    fun register(
        title: String,
        authorId: Long,
        publishedAt: LocalDate
    ): Book? {
        val author = authorRepository.findById(id = authorId) ?: return null
        return bookRepository.insert(
            title = title,
            authorId = author.id,
            publishedAt = publishedAt
        )
    }

    /** 書籍更新 */
    @Transactional
    fun update(id: Long, title: String?, publishedAt: LocalDate?): Book? {
        val book = bookRepository.findById(id = id) ?: return null
        val target = Book(
            id = book.id,
            title = title ?: book.title,
            authorId = book.authorId,
            publishedAt = publishedAt ?: book.publishedAt
        )
        bookRepository.update(book = target)
        return target
    }
}