package com.example.implementingserversidekotlindevelopment.domain

import ValidationError
import arrow.core.EitherNel
import arrow.core.leftNel
import arrow.core.right

interface Description {
    val value: String

    private data class ValidatedDescription(override val value: String): Description
    private data class DescriptionWithoutValidation(override val value: String): Description

    companion object {
        private const val maximumLength = 64

        fun new(description: String): EitherNel<CreationError, Description> {
            if(description.length > maximumLength) {
                return CreationError.TooLong(maximumLength).leftNel()
            }
            return ValidatedDescription(description).right()
        }

        fun newWithoutValidation(description: String): Description = DescriptionWithoutValidation(description)
    }

    sealed interface CreationError: ValidationError {
        data class TooLong(val maximum: Int): CreationError {
            override val message: String
                get() = "description は $maximum 文字以下にしてください"
        }
    }
}