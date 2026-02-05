package com.loopers.application.auth

import com.loopers.domain.member.MemberModel
import java.time.LocalDate

class AuthInfo {

    data class SignupResult(
        val id: Long,
        val loginId: String,
        val name: String,
        val birthDate: LocalDate,
        val email: String,
    ) {
        companion object {
            fun from(member: MemberModel) = SignupResult(
                id = member.id,
                loginId = member.loginId.value,
                name = member.name.value,
                birthDate = member.birthDate.value,
                email = member.email.value,
            )
        }
    }
}
