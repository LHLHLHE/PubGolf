package jg.com.pubgolf.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun convertDateTime(dateTimeString: String): String {
    // Создаем DateTimeFormatter для парсинга строки с датой и временем
    val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
    // Парсим строку с датой и временем в LocalDateTime
    val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)
    // Создаем DateTimeFormatter для форматирования даты в нужном нам формате
    val outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    // Форматируем LocalDateTime в строку с нужным форматом
    val formattedDateTime = dateTime.format(outputFormatter)
    // Возвращаем отформатированную строку с датой
    return formattedDateTime
}