package com.example.bookmanagementapi.api

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@Suppress("unused")
@SpringBootTest
@ActiveProfiles("integration")
@AutoConfigureMockMvc
@Testcontainers
class AuthorControllerTest {
    private val mockMvc: MockMvc,
    private val authorRepository: AuthorRepository
}