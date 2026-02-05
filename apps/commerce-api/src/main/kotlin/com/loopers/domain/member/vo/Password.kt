package com.loopers.domain.member.vo

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDate

@Embeddable
data class Password private constructor(
    @Column(name = "password", nullable = false)
    val value: String,
) {
    fun matches(rawPassword: String): Boolean {
        return ENCODER.matches(rawPassword, value)
    }

    companion object {
        private val ENCODER = BCryptPasswordEncoder()
        private val ALLOWED_CHARS_PATTERN = Regex("^[a-zA-Z0-9!@#\$%^&*()_+\\-=\\[\\]{}|;':\",./<>?`~]+$")
        private const val MIN_LENGTH = 8
        private const val MAX_LENGTH = 16

        fun of(rawPassword: String, birthDate: LocalDate): Password {
            validateFormat(rawPassword)
            validateNotContainsBirthDate(rawPassword, birthDate)
            return Password(ENCODER.encode(rawPassword))
        }

        fun fromEncoded(encodedPassword: String): Password {
            return Password(encodedPassword)
        }

        private fun validateFormat(rawPassword: String) {
            if (rawPassword.length < MIN_LENGTH || rawPassword.length > MAX_LENGTH) {
                throw CoreException(ErrorType.INVALID_PASSWORD_FORMAT, "비밀번호는 ${MIN_LENGTH}~${MAX_LENGTH}자여야 합니다.")
            }
            if (!rawPassword.matches(ALLOWED_CHARS_PATTERN)) {
                throw CoreException(ErrorType.INVALID_PASSWORD_FORMAT, "비밀번호는 영문 대소문자, 숫자, 특수문자만 사용 가능합니다.")
            }
        }

        private fun validateNotContainsBirthDate(rawPassword: String, birthDate: LocalDate) {
            val yyyymmdd = birthDate.toString().replace("-", "")
            if (rawPassword.contains(yyyymmdd)) {
                throw CoreException(ErrorType.PASSWORD_CONTAINS_BIRTHDATE)
            }
        }
    }
}
