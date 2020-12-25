package app.iot.common;

import android.content.Context;
import android.preference.PreferenceManager;

import app.iot.R;

public final class DefaultPreferences {
    private DefaultPreferences() {
    }

    public static int scanDuration(Context c) {
        return readPreference(c, R.string.key_scan_duration, 15000);
    }

    public static int scanTimes(Context c) {
        return readPreference(c, R.string.key_scan_times, 2);
    }

    private static int readPreference(Context c, int key, int defaultValue) {
        String value = PreferenceManager.getDefaultSharedPreferences(c).getString(c.getString(key), null);
        return null == value ? defaultValue : Integer.parseInt(value);
    }

//    public static void setRecurringDays(Context c, Set<String> days) {
//        PreferenceManager.getDefaultSharedPreferences(c).edit()
//                .putStringSet(c.getString(R.string.key_recurring_days), days)
//                .apply();
//    }

//    public static Set<String> getRecurringDays(Context c) {
//        return PreferenceManager.getDefaultSharedPreferences(c).getStringSet(c.getString(R.string.key_recurring_days), new HashSet<String>());
//    }
}
