/*
 * Copyright (C) 2016 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.billing

import android.util.Base64
import android.util.Log
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec

object Security {
    private val TAG = "Billing/Security"
    private val KEY_FACTORY_ALGORITHM = "RSA"
    private val SIGNATURE_ALGORITHM = "SHA1withRSA"

    fun verifyPurchase(base64PublicKey: String, signedData: String, signature: String): Boolean {
        if (base64PublicKey.isEmpty() || signedData.isEmpty() || signature.isEmpty()) return false
        val key = generatePublicKey(base64PublicKey)
        return verify(key, signedData, signature)
    }

    private fun generatePublicKey(base64PublicKey: String): PublicKey {
        return try {
            val decodedKey = Base64.decode(base64PublicKey, Base64.DEFAULT)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
            keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e);
        } catch (e: InvalidKeySpecException) {
            throw IllegalArgumentException(e)
        }
    }

    private fun verify(publicKey: PublicKey, signedData: String, signature: String): Boolean {
        val signatureBytes: ByteArray
        try {
            signatureBytes = Base64.decode(signature, Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Base64 decoding failed.")
            return false
        }

        try {
            val sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(publicKey)
            sig.update(signedData.toByteArray())
            if (!sig.verify(signatureBytes)) {
                Log.e(TAG, "Signature verification failed.")
                return false
            }
            return true
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "NoSuchAlgorithmException.")
        } catch (e: InvalidKeyException) {
            Log.e(TAG, "Invalid key specification.")
        } catch (e: SignatureException) {
            Log.e(TAG, "Signature exception.")
        }

        return false
    }
}