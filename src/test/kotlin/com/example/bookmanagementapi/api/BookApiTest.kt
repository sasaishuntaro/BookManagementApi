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
class BookApiTest @Autowired constructor(
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
            .values(3,"title_3", 3, LocalDate.of(2024, 1, 3))
            .execute()
    }

    /**
     * 書籍検索
     * パラメータなし
     */
    @Test
    fun search_noParameter() {
        insertAuthors()
        insertBooks()
        val expect = """
            [
                {"id":1,"title":"title_1","authorId":1,publishedAt:"2024-01-01"},
                {"id":2,"title":"title_2","authorId":2,publishedAt:"2024-01-02"},
                {"id":3,"title":"title_3","authorId":3,publishedAt:"2024-01-03"}
            ]
        """.trimIndent()

        mockMvc
            .get("/books")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expect)
                }
            }
    }

    /**
     * 書籍検索
     * タイトル検索
     */
    @Test
    fun search_title() {
        insertAuthors()
        insertBooks()
        val expectResult = """
            [
                {"id":1,"title":"title_1","authorId":1,publishedAt:"2024-01-01"}
            ]
        """.trimIndent()

        mockMvc
            .get("/books?title=1")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expectResult)
                }
            }
    }

    /**
     * 書籍検索
     * 著者名検索
     */
    @Test
    fun search_authorName() {
        insertAuthors()
        insertBooks()
        val expectResult = """
            [
                {"id":1,"title":"title_1","authorId":1,publishedAt:"2024-01-01"}
            ]
        """.trimIndent()

        mockMvc
            .get("/books?authorName=1")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expectResult)
                }
            }
    }

    /**
     * 書籍検索
     * 出版日検索
     */
    @Test
    fun search_publishedAt() {
        insertAuthors()
        insertBooks()
        val expectResult = """
            [
                {"id":2,"title":"title_2","authorId":2,publishedAt:"2024-01-02"}
            ]
        """.trimIndent()

        mockMvc
            .get("/books?publishedFrom=2024-01-02&publishedTo=2024-01-02")
            .andExpect { status { isOk() } }
            .andExpect {
                content {
                    json(expectResult)
                }
            }
    }

    /**
     * 書籍登録
     * 正常登録
     */
    @Test
    fun register_success() {
        insertAuthors()
        insertBooks()
        val body = """
            { "title": "title", "authorId": 1, "publishedAt": "2024-01-01" }
        """.trimIndent()

        mockMvc
            .post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isOk() } }
    }

    /**
     * 書籍登録
     * パラメータエラー title
     */
    @Test
    fun register_fail_parameter_title() {
        insertAuthors()
        insertBooks()
        val body = """
            { "title": "", "authorId": 1, "publishedAt": "2024-01-01" }
        """.trimIndent()

        mockMvc
            .post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isBadRequest() } }
    }

    /**
     * 書籍登録
     * パラメータエラー authorId
     */
    @Test
    fun register_fail_parameter_authorId() {
        insertAuthors()
        insertBooks()
        val body = """
            { "title": "title", "authorId": null, "publishedAt": "2024-01-01" }
        """.trimIndent()

        mockMvc
            .post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isBadRequest() } }
    }

    /**
     * 書籍登録
     * パラメータエラー publishedAt
     */
    @Test
    fun register_fail_parameter_publishedAt() {
        insertAuthors()
        insertBooks()
        val body = """
            { "title": "title", "authorId": 1, "publishedAt": null }
        """.trimIndent()

        mockMvc
            .post("/books") {
                contentType = MediaType.APPLICATION_JSON
                content = body
            }
            .andExpect { status { isBadRequest() } }
    }

    /**
     * 書籍更新
     * 正常更新
     */
    @Test
    fun update_success() {
        insertAuthors()
        insertBooks()
        val body = """
            { "title": "title", "publishedAt": "2024-02-01" }
        """.trimIndent()

        val expect = """
            {"id":1,"title":"title","authorId":1,publishedAt:"2024-02-01"}
        """.trimIndent()

        mockMvc
            .put("/books/1") {
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
}
