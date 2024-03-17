package com.example.bookmanagementapi.domain.service

import com.example.bookmanagementapi.domain.model.Author
import com.example.bookmanagementapi.domain.model.Book
import com.example.bookmanagementapi.domain.repository.AuthorRepository
import com.example.bookmanagementapi.domain.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/** 著者サービス */
@Service
class AuthorService(
    private val authorRepository: AuthorRepository,
    private val bookRepository: BookRepository
) {
    /** 著者名による検索 */
    fun search(name: String): List<Author> {
        return authorRepository.search(name = name)
    }

    /**
     * 著者に紐づく書籍検索
     */
    fun getBooks(authorId: Long): List<Book>? {
        val targetId = authorRepository
            .findById(id = authorId)
            ?.id ?: return null
        return bookRepository.findByAuthorId(authorId = targetId)
    }

    /** 著者登録 */
    @Transactional
    fun register(name: String): Author {
        return authorRepository.insert(name = name)
    }

    /** 著者更新 */
    @Transactional
    fun update(id: Long, name: String): Author? {
        val author = authorRepository.findById(id = id) ?: return null
        val target = Author(id = author.id, name = name)
        authorRepository.update(author = target)
        return target
    }
}