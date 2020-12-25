package app.iot.common.util

import android.os.Environment
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

   fun writeToSDCard(string:String?) {
        //如果没有SD卡，直接返回
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            return
        }
        val path = Environment.getExternalStorageDirectory().absolutePath + "/"
        val fileName = "file_"
        val fileNameSuffix = ".txt"
        val fileDir = File(path)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        val currentTime = System.currentTimeMillis()
        val time = SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(Date(currentTime))

        val exFile = File(path + File.separator + fileName + time + fileNameSuffix)
        val pw = PrintWriter(BufferedWriter(FileWriter(exFile)))

        pw.println(string)

        pw.close()
    }

}