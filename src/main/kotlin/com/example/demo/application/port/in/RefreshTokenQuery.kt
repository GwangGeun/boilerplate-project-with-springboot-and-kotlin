package com.example.demo.application.port.`in`

import com.example.demo.adapter.`in`.web.dto.ValidateRefreshTokenResource

class RefreshTokenQuery private constructor(val refreshToken: String) {
    companion object {
        fun from(validateRefreshTokenResource: ValidateRefreshTokenResource): RefreshTokenQuery {
            return RefreshTokenQuery(validateRefreshTokenResource.refreshToken)
        }
    }
}