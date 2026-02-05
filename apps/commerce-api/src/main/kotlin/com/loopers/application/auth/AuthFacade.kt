package com.loopers.application.auth

import com.loopers.domain.member.MemberModel
import com.loopers.domain.member.MemberService
import com.loopers.domain.member.vo.BirthDate
import com.loopers.domain.member.vo.Email
import com.loopers.domain.member.vo.LoginId
import com.loopers.domain.member.vo.Name
import com.loopers.domain.member.vo.Password
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class AuthFacade(
    private val memberService: MemberService,
) {

    /**
     * 회원가입을 처리합니다.
     */
    fun signup(command: SignupCommand): AuthInfo.SignupResult {
        val birthDate = command.birthDate

        val member = memberService.register(
            loginId = LoginId(command.loginId),
            password = Password.of(command.rawPassword, birthDate),
            name = Name(command.name),
            birthDate = BirthDate(birthDate),
            email = Email(command.email),
        )

        return AuthInfo.SignupResult.from(member)
    }

    /**
     * 인증 정보를 검증합니다.
     */
    fun authenticate(loginId: String, rawPassword: String): MemberModel {
        return memberService.authenticate(
            loginId = LoginId(loginId),
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
