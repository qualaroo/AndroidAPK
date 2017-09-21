package com.qualaroo.internal.network

import com.nhaarman.mockito_kotlin.*
import com.qualaroo.internal.model.QuestionType
import com.qualaroo.internal.model.Survey
import com.qualaroo.internal.model.TestModels.answer
import com.qualaroo.internal.model.TestModels.question
import com.qualaroo.internal.model.TestModels.survey
import okhttp3.HttpUrl
import org.junit.Assert.assertEquals
import org.junit.Test

@Suppress("IllegalIdentifier", "MemberVisibilityCanPrivate")
class ReportClientTest {

    val survey: Survey = survey(id = 123)

    val restClient = mock<RestClient> {
        on { get(any()) } doReturn Result.of("")
    }

    val apiConfig = mock<ApiConfig> {
        on { reportApi() } doReturn HttpUrl.parse("https://turbo.qualaroo.com")
    }

    val client = ReportClient(restClient, apiConfig)

    @Test
    fun `builds proper url for impressions`() {
        client.recordImpression(survey)

        val captor = argumentCaptor<HttpUrl>()
        verify(restClient).get(captor.capture())

        val url = captor.lastValue
        assertEquals("https", url.scheme())
        assertEquals("turbo.qualaroo.com", url.host())
        assertEquals("/c.js", url.encodedPath())
        assertEquals("123", url.queryParameter("id"))
    }

    @Test
    fun `builds proper url for nps answer`() {
        val question = question(id = 123456, type = QuestionType.NPS)
        val answer = answer(id = 10)

        client.recordAnswer(survey, question, listOf(answer))

        val captor = argumentCaptor<HttpUrl>()
        verify(restClient).get(captor.capture())
        val url = captor.lastValue

        assertEquals("https", url.scheme())
        assertEquals("turbo.qualaroo.com", url.host())
        assertEquals("/r.js", url.encodedPath())
        assertEquals("123", url.queryParameter("id"))
        assertEquals("10", url.queryParameter("r[123456]"))
    }

    @Test
    fun `builds proper url for radio answer`() {
        val question = question(id = 123456, type = QuestionType.RADIO)
        val answer = answer(id = 10)

        client.recordAnswer(survey, question, listOf(answer))

        val captor = argumentCaptor<HttpUrl>()
        verify(restClient).get(captor.capture())
        val url = captor.lastValue

        assertEquals("https", url.scheme())
        assertEquals("turbo.qualaroo.com", url.host())
        assertEquals("/r.js", url.encodedPath())
        assertEquals("123", url.queryParameter("id"))
        assertEquals(answer.id().toString(), url.queryParameter("r[123456]"))
    }

    @Test
    fun `builds proper url for checkbox answer`() {
        val question = question(
                id = 123456,
                type = QuestionType.CHECKBOX
        )

        val answers = listOf(
                answer(id = 10),
                answer(id = 20),
                answer(id = 30)
        )

        client.recordAnswer(survey, question, answers)

        val captor = argumentCaptor<HttpUrl>()
        verify(restClient).get(captor.capture())
        val url = captor.lastValue

        assertEquals("https", url.scheme())
        assertEquals("turbo.qualaroo.com", url.host())
        assertEquals("/r.js", url.encodedPath())
        assertEquals("123", url.queryParameter("id"))
        assertEquals(3, url.queryParameterValues("r[123456]").size)

        assertEquals("10", url.queryParameterValues("r[123456]")[0])
        assertEquals("20", url.queryParameterValues("r[123456]")[1])
        assertEquals("30", url.queryParameterValues("r[123456]")[2])
    }

    @Test
    fun `builds proper url for text answer`() {
        val question = question(
                id = 123456,
                type = QuestionType.TEXT
        )

        client.recordTextAnswer(survey, question, "long answer with spaces")

        val captor = argumentCaptor<HttpUrl>()
        verify(restClient).get(captor.capture())
        val url = captor.lastValue

        assertEquals("https", url.scheme())
        assertEquals("turbo.qualaroo.com", url.host())
        assertEquals("/r.js", url.encodedPath())
        assertEquals("123", url.queryParameter("id"))
        assertEquals("long answer with spaces", url.queryParameter("r[123456][text]"))
    }

}