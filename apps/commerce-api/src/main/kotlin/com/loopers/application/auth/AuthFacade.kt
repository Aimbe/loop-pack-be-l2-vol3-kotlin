package com.loopers.application.auth

import com.loopers.domain.member.Member
import com.loopers.domain.member.MemberAuthenticator
import com.loopers.domain.member.MemberRegister
import com.loopers.domain.member.vo.BirthDate
import com.loopers.domain.member.vo.Email
import com.loopers.domain.member.vo.LoginId
import com.loopers.domain.member.vo.Name
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AuthFacade(
    private val memberRegister: MemberRegister,
    private val memberAuthenticator: MemberAuthenticator,
) {

    /**
     * 회원가입을 처리합니다.
     */
    fun signup(command: SignupCommand): AuthInfo.SignupResult {
        val member = memberRegister.register(
            loginId = LoginId(command.loginId),
            rawPassword = command.rawPassword,
            name = Name(command.name),
            birthDate = BirthDate(command.birthDate),
            email = Email(command.email),
        )

        return AuthInfo.SignupResult.from(member)
    }

    /**
     * 인증 정보를 검증합니다.
     */
    fun authenticate(loginId: String, rawPassword: String): Member {
        return memberAuthenticator.authenticate(
            loginId = loginId,
            rawPassword = rawPassword,
        )
    }

    data class SignupCommand(
        val loginId: String,
        val rawPassword: String,
        val name: String,
        val birthDate: LocalDate,
        val email: String,
    )
}
