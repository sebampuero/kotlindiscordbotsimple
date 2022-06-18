package chistosito.util

import java.io.File

class FIleUtils {

    companion object {

        fun fileExists(fileName: String): Boolean {
            return File(fileName).exists()
        }

    }

}