package app.iot.widget

import android.text.Editable
import android.text.TextWatcher

/**
 * Created by danbo on 2019-11-12.
 */
abstract class SimpleTextWatcher : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}