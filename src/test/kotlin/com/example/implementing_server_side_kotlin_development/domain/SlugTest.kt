package com.example.implementing_server_side_kotlin_development.domain

import arrow.core.Either
import arrow.core.leftNel
import com.example.implementingserversidekotlindevelopment.domain.Slug
import net.jqwik.api.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SlugTest {
    class New {
        @Property
        fun `正常圭一文字列が有効な場合、検証済の Slug が戻り値`(
            @ForAll @From(supplier = SlugValidRange::class) validString: String
        ) {
            val actual = Slug.new(validString)

            val expectedSlug = Slug.newWithoutValidation(validString)
            when(actual) {
                is Either.Left -> assert(false) { "原因: ${actual.value}" }
                is Either.Right -> assertThat(actual.value.value).isEqualTo(expectedSlug.value)
            }
        }
    }

    @Property
    fun `異常系-文字列が有効でない場合、バリデーションエラーが戻り値`(
        @ForAll @From(supplier = SlugInvalidRange::class) invalidString: String,
    ) {
        /**
         * given:
         */

        /**
         * when:
         */
        val actual = Slug.new(invalidString)

        /**
         * then:
         */
        val expected = Slug.CreationError.ValidFormat(invalidString).leftNel()
        assertThat(actual).isEqualTo(expected)
    }

    class SlugValidRange : ArbitrarySupplier<String> {
        override fun get(): Arbitrary<String> {
            return Arbitraries.strings()
                .numeric()
                .withCharRange('a','z')
                .ofLength(32)
        }
    }

    class SlugInvalidRange : ArbitrarySupplier<String> {
        override fun get(): Arbitrary<String> =
            Arbitraries.strings()
                .filter { !it.matches(Regex("^[a-z0-9]{32}$")) }
    }


}