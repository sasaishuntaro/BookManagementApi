package com.example.bookmanagementapi.api

import com.example.bookmanagementapi.infra.jooq.tables.references.AUTHOR
import com.example.bookmanagementapi.infra.jooq.tables.references.BOOK
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import java.time.LocalDate

@SpringBootTest
@AutoConfigureMockMvc
class AuthorApiTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val dsl: DSLContext
) {
    @BeforeEach
    @AfterEach
    fun clearTables() {
        dsl.deleteFrom(BOOK).execute()
        dsl.deleteFrom(AUTHOR).execute()
    }

    fun insertAuthors() {
        dsl.insertInto(
            AUTHOR,
            AUTHOR.ID,
            AUTHOR.NAME
        )
            .values(1,"author_1")
            .values(2,"author_2")
            .values(3,"author_3")
            .execute()
    }

    fun insertBooks() {
        dsl.insertInto(
            BOOK,
            BOOK.ID,
            BOOK.TITLE,
            BOOK.AUTHOR_ID,
            BOOK.PUBLISHED_AT
        )
            .values(1,"title_1", 1, LocalDate.of(2024, 1, 1))
            .values(2,"title_2", 2, LocalDate.of(2024, 1, 2))
            .execute()
    }

    /**
     * 著者検索
     * パラメータなし
     */
    @Test
    fun search_noParameter() {
        insertAuthors()
        val expect = """
            [
                {"id":1,"name":"author_1"},
                {"id":2,"name":"author_2"},
                {"id":3,"name":"author_3"}
            ]
        """.trimIndent()

        mockMvc
            .get("/authors")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expect)
                }
            }
    }

    /**
     * 著者検索
     * 著者名あり
     */
    @Test
    fun search_existParameter() {
        insertAuthors()
        val expectResult = """
            [
                {"id":1,"name":"author_1"}
            ]
        """.trimIndent()

        mockMvc
            .get("/authors?name=1")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expectResult)
                }
            }
    }

    /**
     * 著者検索
     * 該当なし
     */
    @Test
    fun search_notFound() {
        insertAuthors()
        val expectResult = """[]""".trimIndent()
        mockMvc
            .get("/authors?name=author_not_found")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expectResult)
                }
            }
    }

    /**
     * 著者に紐づく書籍検索
     * 対象の著者、書籍あり
     */
    @Test
    fun searchBooks_existBooks() {
        insertAuthors()
        insertBooks()
        val expect = """
            [
                {"id":1,"title":"title_1","authorId":1,"publishedAt":"2024-01-01"}
            ]
        """.trimIndent()

        mockMvc
            .get("/authors/1/books")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expect)
                }
            }
    }

    /**
     * 著者に紐づく書籍検索
     * 書籍なしの著者
     */
    @Test
    fun searchBooks_emptyBooks() {
        insertAuthors()
        insertBooks()
        val expect = """[]""".trimIndent()

        mockMvc
            .get("/authors/3/books")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expect)
                }
            }
    }

    /**
     * 著者に紐づく書籍検索
     * 対象の著者なし
     */
    @Test
    fun searchBooks_noAuthor() {
        insertAuthors()
        insertBooks()

        mockMvc
            .get("/authors/99999/books")
            .andExpect { status { isNotFound() } }
    }

    /**
     * 著者登録
     * 正常登録
     */
    @Test
    fun register_success() {
        val name = "test_register_author"
        val body = """
            { "name": "$name" }
        """.trimIndent()

        mockMvc
            .post("/authors") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isOk() } }
    }

    /**
     * 著者登録
     * パラメーターエラー
     */
    @Test
    fun register_fail_parameter() {
        val body = """
            { "name": "" }
        """.trimIndent()

        mockMvc
            .post("/authors") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isBadRequest() } }
    }

    /**
     * 著者更新
     * 正常更新
     */
    @Test
    fun update_success() {
        insertAuthors()
        val body = """
            { "name": "更新" }
        """.trimIndent()

        val expect = """{"id":1,"name":"更新"}""".trimIndent()

        mockMvc
            .put("/authors/1") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expect)
                }
            }
    }

    /**
     * 著者更新
     * 更新失敗_パラメータエラー
     */
    @Test
    fun update_fail_parameter() {
        insertAuthors()
        val body = """
            { "name": "" }
        """.trimIndent()

        mockMvc
            .put("/authors/1") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isBadRequest() } }
    }
}