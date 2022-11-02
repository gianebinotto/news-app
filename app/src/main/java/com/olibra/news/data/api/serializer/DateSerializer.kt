package com.olibra.news.data.api.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateSerializer : KSerializer<Date> {
    private const val SERIALIZER_NAME = "ArticleDateSerializer"
    private const val DATE_FORMAT = "dd MMM yyyy,E"

    private val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT, Locale.US)

    override val descriptor = PrimitiveSerialDescriptor(
        SERIALIZER_NAME, PrimitiveKind.STRING
    )
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(
        dateFormat.format(value)
    )
    override fun deserialize(decoder: Decoder): Date = checkNotNull(
        dateFormat.parse(decoder.decodeString())
    )
}