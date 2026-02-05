package com.loopers.domain.member.vo

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class LoginId(
    @Column(name = "login_id", nullable = false, unique = true)
    val value: String,
) {
    init {
        if (value.isBlank() || !value.matches(PATTERN)) {
            throw CoreException(ErrorType.INVALID_LOGIN_ID_FORMAT)
        }
    }

    companion object {
        private val PATTERN = Regex("^[a-zA-Z0-9]+$")
    }
}
