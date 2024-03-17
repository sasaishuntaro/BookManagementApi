package com.example.bookmanagementapi.infrastructure.repositoryImpl

import com.example.bookmanagementapi.domain.model.Book
import com.example.bookmanagementapi.domain.repository.BookRepository
import com.example.bookmanagementapi.infra.jooq.tables.references.AUTHOR
import com.example.bookmanagementapi.infra.jooq.tables.references.BOOK
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class BookRepositoryImpl(
    private val dslContext: DSLContext
) : BookRepository {

    override fun findById(id: Long): Book? {
        return dslContext
            .select()
            .from(BOOK)
            .where(
                BOOK.ID.eq(id)
            )
            .fetchOne()
            ?.map {
                Book(
                    id = it.getValue(BOOK.ID)!!,
                    title = it.getValue(BOOK.TITLE)!!,
                    authorId = it.getValue(BOOK.AUTHOR_ID)!!,
                    publishedAt = it.getValue(BOOK.PUBLISHED_AT)!!
                )
            }
    }

    override fun findByAuthorId(authorId: Long): List<Book> {
        return dslContext
            .select()
            .from(BOOK)
            .where(
                BOOK.AUTHOR_ID.eq(authorId)
            )
            .fetch()
            .map {
                Book(
                    id = it.getValue(BOOK.ID)!!,
                    title = it.getValue(BOOK.TITLE)!!,
                    authorId = it.getValue(BOOK.AUTHOR_ID)!!,
                    publishedAt = it.getValue(BOOK.PUBLISHED_AT)!!
                )
            }
    }

    override fun search(
        title: String?,
        authorName: String?,
        publishedFrom: LocalDate?,
        publishedTo: LocalDate?,
    ): List<Book> {
        return dslContext
            .select()
            .from(BOOK)
            .join(AUTHOR).on(AUTHOR.ID.eq(BOOK.AUTHOR_ID))
            .where(
                if (title.isNullOrBlank()) {
                    DSL.trueCondition()
                } else {
                    BOOK.TITLE.like("%${title}%")
                }
            )
            .and(
                if (authorName.isNullOrBlank()) {
                    DSL.trueCondition()
                } else {
                    AUTHOR.NAME.like("%${authorName}%")
                }
            )
            .and(
                if (publishedFrom === null) {
                    DSL.trueCondition()
                } else {
                    BOOK.PUBLISHED_AT.ge(publishedFrom)
                }
            )
            .and(
                if (publishedTo === null) {
                    DSL.trueCondition()
                } else {
                    BOOK.PUBLISHED_AT.le(publishedTo)
                }
            )
            .fetch()
            .map {
                Book(
                    id = it.getValue(BOOK.ID)!!,
                    title = it.getValue(BOOK.TITLE)!!,
                    authorId = it.getValue(BOOK.AUTHOR_ID)!!,
                    publishedAt = it.getValue(BOOK.PUBLISHED_AT)!!
                )
            }
    }

    override fun insert(
        title: String,
        authorId: Long,
        publishedAt: LocalDate
    ): Book {
        return dslContext
            .insertInto(BOOK, BOOK.TITLE, BOOK.AUTHOR_ID, BOOK.PUBLISHED_AT)
            .values(title, authorId, publishedAt)
            .returningResult(BOOK.ID, BOOK.TITLE, BOOK.AUTHOR_ID, BOOK.PUBLISHED_AT)
            .fetchOne()
            ?.map {
                Book(
                    id = it.getValue(BOOK.ID)!!,
                    title = it.getValue(BOOK.TITLE)!!,
                    authorId = it.getValue(BOOK.AUTHOR_ID)!!,
                    publishedAt = it.getValue(BOOK.PUBLISHED_AT)!!
                )
            }!!
    }

    override fun update(book: Book): Int {
        return dslContext
            .update(BOOK)
            .set(BOOK.TITLE, book.title)
            .set(BOOK.AUTHOR_ID, book.authorId)
            .set(BOOK.PUBLISHED_AT, book.publishedAt)
            .where(BOOK.ID.eq(book.id))
            .execute()
    }
}