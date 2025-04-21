package com.ziss.notesappandroid.shared.utils

import com.ziss.notesappandroid.core.extensions.capitalize
import com.ziss.notesappandroid.shared.responses.ErrorDetailResponse

object AppUtils {
    fun nextPage(current: Int?): Int? {
        return if (current == null) null else current + 1
    }

    fun getErrorMessage(errors: ArrayList<ErrorDetailResponse>?, attr: String? = ""): String? {
        if (errors?.isEmpty() == true) {
            return null
        } else {
            val message = errors?.find { e -> e.attr == attr }?.detail
            return message?.capitalize()
        }
    }
}