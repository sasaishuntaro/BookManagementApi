package com.example.bookmanagementapi.application.controller

import com.example.bookmanagementapi.application.request.RegisterBookRequest
import com.example.bookmanagementapi.application.request.UpdateBookRequest
import com.example.bookmanagementapi.domain.model.Book
import com.example.bookmanagementapi.domain.service.BookService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

/** 書籍コントローラー */
@RestController
@RequestMapping("/books")
class BookController(
    private val bookService: BookService
) {
    /** 書籍検索 */
    @GetMapping
    fun search(
        @RequestParam(name = "title") title: String?,
        @RequestParam(name = "authorName") authorName: String?,
        @RequestParam(name = "publishedFrom") @DateTimeFormat(pattern = "yyyy-MM-dd") publishedFrom: LocalDate?,
        @RequestParam(name = "publishedTo") @DateTimeFormat(pattern = "yyyy-MM-dd") publishedTo: LocalDate?
    ): ResponseEntity<List<Book>> {
        val result = bookService.search(
            title = title,
            authorName = authorName,
            publishedFrom = publishedFrom,
            publishedTo = publishedTo
        )
        return ResponseEntity.ok(result)
    }

    /** 書籍登録 */
    @PostMapping
    fun register(
        @RequestBody @Validated request: RegisterBookRequest
    ): ResponseEntity<Book> {
        return try {
            val result = bookService.register(
                title = request.title,
                authorId = request.authorId,
                publishedAt = request.publishedAt
            )
            if (result === null) {
                ResponseEntity.notFound().build()
            } else {
                ResponseEntity.ok(result)
            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody @Validated request: UpdateBookRequest
    ): ResponseEntity<Book> {
        return try {
            val result = bookService.update(
                id = id,
                title = request.title,
                publishedAt = request.publishedAt
            )
            if (result === null) {
                ResponseEntity.notFound().build()
            } else {
                ResponseEntity.ok(result)
            }
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }
}