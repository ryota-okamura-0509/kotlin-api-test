package com.example.implementing_server_side_kotlin_development.domain

import arrow.core.leftNel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TitleTest {
    class New {
        @Test
        fun `異常系-空文字列の場合、バリデーションエラーが戻り値`() {
            /**
             * given:
             */
            val emptyString = ""

            /**
             * when:
             */
            val actual = Title.new(emptyString)

            /**
             * then:
             */
            val expected = Title.CreationError.Required.leftNel()
            assertThat(actual).isEqualTo(expected)
        }

        @Test
        fun `異常系-空白の場合、バリデーションエラーが戻り値`() {
            /**
             * given:
             */
            val blankString = "  "

            /**
             * when:
             */
            val actual = Title.new(blankString)

            /**
             * then:
             */
            val expected = Title.CreationError.Required.leftNel()
            assertThat(actual).isEqualTo(expected)
        }
    }

}