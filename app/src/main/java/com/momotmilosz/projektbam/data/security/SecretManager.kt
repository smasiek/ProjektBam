package com.momotmilosz.projektbam.data.security

import android.content.ContentValues.TAG
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream


class SecretManager() {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")

    init {
        this.keyStore.load(null)
    }

    private fun refreshKeys() {
        val keyAliases = ArrayList<String>()
        try {
            val aliases: Enumeration<String> = keyStore.aliases()
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement())
            }
        } catch (_: Exception) {
        }
    }

    fun createNewKeys(usernameAlias: String, context: Context) {
        try {
            // Create new key if needed
            if (!keyStore.containsAlias(usernameAlias)) {
                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 1)
                val kpg = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore"
                )
                kpg.initialize(
                    KeyGenParameterSpec.Builder(
                        usernameAlias,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setKeyValidityStart(start.time)
                        .setKeyValidityEnd(end.time)
                        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()
                )
                val keyPair: KeyPair = kpg.generateKeyPair()
            }
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
        }
        refreshKeys()
    }

    fun encryptString(usernameAlias: String?, stringToEncrypt: String, context: Context): String {
        try {
            val privateKeyEntry = keyStore.getEntry(usernameAlias, null) as KeyStore.PrivateKeyEntry
            val publicKey = privateKeyEntry.certificate.publicKey
            // Encrypt the text
            if (stringToEncrypt.isEmpty()) {
                Toast.makeText(
                    context,
                    "Enter text in the 'Initial Text' widget",
                    Toast.LENGTH_LONG
                )
                    .show()
                throw IllegalArgumentException()
            }
            val input: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            input.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(
                outputStream, input
            )
            cipherOutputStream.write(stringToEncrypt.toByteArray(charset("UTF-8")))
            cipherOutputStream.close()
            val vals: ByteArray = outputStream.toByteArray()
            return Base64.getEncoder().encodeToString(vals)
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
            return ""
        }
    }

    fun decryptString(usernameAlias: String?, stringToDecode: String, context: Context) :String{
        try {
            val privateKeyEntry = keyStore.getEntry(usernameAlias, null) as KeyStore.PrivateKeyEntry

            val output = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.privateKey)

            val cipherInputStream = CipherInputStream(
                ByteArrayInputStream(Base64.getDecoder().decode(stringToDecode)), output
            )
            val values: ArrayList<Byte> = ArrayList()
            var nextByte: Int
            while (cipherInputStream.read().also { nextByte = it } != -1) {
                values.add(nextByte.toByte())
            }
            val bytes = ByteArray(values.size)
            for (i in bytes.indices) {
                bytes[i] = values[i]
            }
            return String(bytes, 0, bytes.size, Charset.forName("UTF-8"))
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Exception " + e.message + " occured", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
            return ""
        }
    }
}