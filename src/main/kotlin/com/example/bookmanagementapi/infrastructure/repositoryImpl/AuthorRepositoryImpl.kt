package com.example.bookmanagementapi.infrastructure.repositoryImpl

import com.example.bookmanagementapi.domain.model.Author
import com.example.bookmanagementapi.domain.repository.AuthorRepository
import com.example.bookmanagementapi.infra.jooq.tables.references.AUTHOR
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class AuthorRepositoryImpl(
    private val dslContext: DSLContext
) : AuthorRepository {

    override fun findById(id: Long): Author? {
        return dslContext
            .select()
            .from(AUTHOR)
            .where(
                AUTHOR.ID.eq(id)
            )
            .fetchOne()
            ?.map {
                Author(
                    id = it.getValue(AUTHOR.ID)!!,
                    name = it.getValue(AUTHOR.NAME)!!
                )
            }
    }

    override fun search(name: String): List<Author> {
        return dslContext
            .select()
            .from(AUTHOR)
            .where(
                if (name.isBlank()) {
                    DSL.trueCondition()
                } else {
                    AUTHOR.NAME.like("%${name}%")
                }
            )
            .fetch()
            .map {
                Author(
                    id = it.getValue(AUTHOR.ID)!!,
                    name = it.getValue(AUTHOR.NAME)!!
                )
            }
    }

    override fun insert(name: String): Author {
        return dslContext
            .insertInto(AUTHOR, AUTHOR.NAME)
            .values(name)
            .returningResult(AUTHOR.ID, AUTHOR.NAME)
            .fetchOne()
            ?.map {
                Author(
                    id = it.get(AUTHOR.ID)!!,
                    name = it.get(AUTHOR.NAME)!!
                )
            }!!
    }

    override fun update(author: Author): Int {
        return dslContext
            .update(AUTHOR)
            .set(AUTHOR.NAME, author.name)
            .where(AUTHOR.ID.eq(author.id))
            .execute()
    }
}