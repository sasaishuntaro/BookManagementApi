package com.example.bookmanagementapi.application.controller

import com.example.bookmanagementapi.application.request.RegisterAuthorRequest
import com.example.bookmanagementapi.application.request.UpdateAuthorRequest
import com.example.bookmanagementapi.domain.model.Author
import com.example.bookmanagementapi.domain.model.Book
import com.example.bookmanagementapi.domain.service.AuthorService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/** 著者コントローラー */
@RestController
@Validated
@RequestMapping("/authors")
class AuthorController(
    private val authorService: AuthorService,
) {
    /** 著者検索 */
    @GetMapping
    fun search(
        @RequestParam(name = "name") name: String = ""
    ): ResponseEntity<List<Author>> {
        val result = authorService.search(name)
        return ResponseEntity.ok(result)
    }

    /** 著者に紐づく書籍一覧 */
    @GetMapping("/{id}/books")
    fun searchBooks(
        @PathVariable("id") id: Long,
    ): ResponseEntity<List<Book>> {
        val result = authorService.getBooks(authorId = id)
        return if (result === null) {
            ResponseEntity.notFound().build()
        } else {
            ResponseEntity.ok(result)
        }
    }

    /** 著者登録 */
    @PostMapping
    fun register(
        @RequestBody @Validated request: RegisterAuthorRequest
    ): ResponseEntity<Author> {
        return try {
            val result = authorService.register(request.name)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message)
        }
    }

    /** 著者更新 */
    @PutMapping("/{id}")
    fun update(
        @PathVariable("id") id: Long,
        @RequestBody @Validated request: UpdateAuthorRequest
    ): ResponseEntity<Any> {
        return try {
            val result = authorService.update(id, request.name)
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