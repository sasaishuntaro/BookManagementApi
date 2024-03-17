package com.example.bookmanagementapi.application.controller

import com.example.bookmanagementapi.domain.model.Author
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

/** 著者コントローラー */
@RequestMapping("/authors")
class AuthorController(
    private val authorService: AuthorService,
    private val bookService: BookService
) {

    /** 著者検索 */
    @GetMapping()
    fun search(
        @RequestParam(name = "name", defaultValue = "") name: String
    ) : ResponseEntity<List<Author>> {
        val entiry = authorService.search(name)

    }

}