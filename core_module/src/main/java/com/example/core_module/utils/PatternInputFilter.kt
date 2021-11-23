package com.example.core_module.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class PatternInputFilter(private val pattern: Pattern) : InputFilter {
    override fun filter(charSeq: CharSequence?, start: Int, end: Int, span: Spanned?, dStart: Int, dEnd: Int): CharSequence? {
        charSeq?.let { source ->
            for (inputChar in source) {
                if (!inputChar.toString().matches(pattern.toRegex())) {
                    return if (source.length == 1) {
                        ""
                    } else {
                        span?.toString()
                    }
                }
            }
        }
        return charSeq
    }
}