package com.example.bookmanagementapi.domain.repository

import com.example.bookmanagementapi.domain.model.Author

/** 著者リポジトリ */
interface AuthorRepository {

    /** 著者のID検索 */
    fun findById(id: Long): Author?

    /** 著者名のLike検索 */
    fun search(name: String): List<Author>

    /** 著者の登録 */
    fun insert(name: String): Author

    /** 著者の更新 */
    fun update(author: Author): Int
}