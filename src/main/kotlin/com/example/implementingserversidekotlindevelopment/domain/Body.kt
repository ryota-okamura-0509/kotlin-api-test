package com.example.implementingserversidekotlindevelopment.domain

import ValidationError
import arrow.core.EitherNel
import arrow.core.leftNel
import arrow.core.right

interface Body {
    val value: String

    private data class ValidateBody(override val value: String): Body
    private data class BodyWithoutValidation(override val value: String): Body

    companion object {
        private const val maximumLength = 1024

        fun newWithoutValidation(body: String): Body = BodyWithoutValidation(body)

        fun new(body: String): EitherNel<CreationError, Body> {
            if(body.length > maximumLength) {
                return CreationError.TooLong(maximumLength).leftNel()
            }
            return  ValidateBody(body).right()
        }
    }

    sealed interface CreationError : ValidationError {
        data class TooLong(val maximum: Int): CreationError {
            override val message: String
                get() = "body は $maximum 文字以下にしてください"
        }
    }
 }