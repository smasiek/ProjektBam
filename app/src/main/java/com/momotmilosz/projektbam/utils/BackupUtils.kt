package com.momotmilosz.projektbam.utils

import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File
import java.io.IOException

class BackupUtils {
    companion object {
        @Throws(IOException::class)
        fun backupFile(zipToSave: File?, fileToSave: File?, password: String) {
            val zipParameters = ZipParameters()
            zipParameters.isEncryptFiles = true
            zipParameters.compressionLevel = CompressionLevel.HIGHER
            zipParameters.encryptionMethod = EncryptionMethod.AES
            val zipFile = ZipFile(zipToSave)
            zipFile.setPassword(password.toCharArray())
            zipFile.addFile(fileToSave, zipParameters)
        }
        @Throws(IOException::class)
        fun restoreBackup(zipToRead: File?, fileToRead: File, extractPath: String?, password: String) {
            val zipFile = ZipFile(zipToRead)
            zipFile.setPassword(password.toCharArray())

            zipFile.extractFile(
                fileToRead.name,
                extractPath
            )
        }
    }
}
