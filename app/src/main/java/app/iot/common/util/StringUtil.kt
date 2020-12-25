package app.iot.common.util

import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtil {

    fun replaceBlank(str: String?): String? {
        var dest = ""
        if (str != null) {
            val p: Pattern = Pattern.compile("\\s*|\t|\r|\n")
            val m: Matcher = p.matcher(str)
            dest = m.replaceAll("")
        }
        return dest
    }
}