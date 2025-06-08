package com.example.implementingserversidekotlindevelopment.domain

import ValidationError
import arrow.core.EitherNel
import arrow.core.leftNel
import arrow.core.right
import java.util.UUID

interface Slug {
    val value: String

    private data class ValidatedSlug(override val value: String): Slug
    private data class SlugWithoutValidation(override val value: String): Slug

    companion object {
        private const val format: String = "^[a-z0-9]{32}$"

        fun newWithoutValidation(slug: String): Slug = SlugWithoutValidation(slug)

        fun new(slug: String): EitherNel<CreationError, Slug> {
            if(!slug.matches(Regex(format))) {
                return  CreationError.ValidFormat(slug).leftNel()
            }
            return ValidatedSlug(slug).right()
        }

        /**
         * 引数有りの場合、UUID から生成
         *
         * @return
         */
        fun new(): Slug {
            return ValidatedSlug(UUID.randomUUID().toString().split("-").joinToString(""))
        }
    }

    sealed interface CreationError: ValidationError {
        data class ValidFormat(val slug: String): CreationError {
            override val message: String
                get() = "slug は 32 文字の英小文字数字です。"
        }
    }
}