package com.example.implementingserversidekotlindevelopment.domain

import Title
import ValidationError
import arrow.core.EitherNel
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate

class CreatedArticle private constructor(
    val slug: Slug,
    val title: Title,
    val description: Description,
    val body: Body,
) {
    /**
     * Factory メソッド
     *
     * @constructor Create empty Companion
     */
    companion object {
        /**
         * Validation 有り、作成済記事を生成
         *
         * @param title
         * @param description
         * @param body
         * @return
         */
        fun new(
            title: String,
            description: String,
            body: String,
        ): EitherNel<ValidationError, CreatedArticle> {
            return either {
                zipOrAccumulate(
                    { Title.new(title).bindNel() },
                    { Description.new(description).bindNel() },
                    { Body.new(body).bindNel() }
                ) { validatedTitle, validatedDescription, validatedBody ->
                    CreatedArticle(Slug.new(), validatedTitle, validatedDescription, validatedBody)
                }
            }
        }

        /**
         * Validation 無し、作成済記事を生成
         *
         * @param slug
         * @param title
         * @param description
         * @param body
         * @return
         */
        fun newWithoutValidation(
            slug: Slug,
            title: Title,
            description: Description,
            body: Body,
        ): CreatedArticle = CreatedArticle(
            slug,
            title,
            description,
            body
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is CreatedArticle) {
            return false
        }
        return this.slug == other.slug
    }

    override fun hashCode(): Int {
        return slug.hashCode() * 31
    }
}