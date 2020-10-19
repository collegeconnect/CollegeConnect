package com.college.collegeconnect.security

import android.content.Context
import android.util.Base64
import com.college.collegeconnect.datamodels.SaveSharedPreference
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Security {

    private const val IV_SEPARATOR = "~@@~"
    private const val KEY_GENERATOR = "AES"
    private const val ALGORITHM = "AES/CTR/NoPadding"

    fun encrypt(value: String, ctx: Context): String {
        return ctx.encryption().encrypt(value)
    }

    fun decrypt(value: String, ctx: Context): String {
        val (iv, encrypted) = value.split(IV_SEPARATOR)
        return ctx.decryption(iv).decrypt(encrypted)
    }

    private fun getKey(ctx: Context): SecretKey {
        val stored = SaveSharedPreference.getEncryptionKey(ctx) ?: return generateKey().store(ctx)
        return SecretKeySpec(stored.fromBase64(), KEY_GENERATOR)
    }

    private fun generateKey(): SecretKey {
        return KeyGenerator.getInstance(KEY_GENERATOR).apply { init(256) }.generateKey()
    }

    private fun SecretKey.store(ctx: Context): SecretKey = apply {
        SaveSharedPreference.setEncryptionKey(ctx, encoded.toBase64())
    }

    private fun Cipher.encrypt(value: String): String {
        return iv.toBase64() + IV_SEPARATOR + doFinal(value.toByteArray(Charsets.UTF_8)).toBase64()
    }

    private fun Cipher.decrypt(value: String): String {
        return doFinal(value.fromBase64()).toString(Charsets.UTF_8)
    }

    private fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.NO_WRAP)
    private fun String.fromBase64(): ByteArray = Base64.decode(this, Base64.NO_WRAP)

    private fun Context.encryption(): Cipher = Cipher.getInstance(ALGORITHM)
            .also { it.init(Cipher.ENCRYPT_MODE, getKey(this)) }

    private fun Context.decryption(iv: String): Cipher = Cipher.getInstance(ALGORITHM)
            .also { it.init(Cipher.DECRYPT_MODE, getKey(this), IvParameterSpec(iv.fromBase64())) }
}