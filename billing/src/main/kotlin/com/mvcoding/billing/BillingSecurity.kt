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


import android.util.Log
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.X509EncodedKeySpec

object BillingSecurity {
    private val TAG = BillingSecurity::class.simpleName

    private val KEY_FACTORY_ALGORITHM = "RSA"
    private val SIGNATURE_ALGORITHM = "SHA1withRSA"

    /**
     * Verifies that the data was signed with the given signature, and returns the verified purchase. The data is in JSON format and signed
     * with a private key. The data also contains the [BillingPurchase] and product ID of the purchase.
     *
     * @param base64PublicKey the base64-encoded public key to use for verifying.
     * @param signedData the signed JSON string (signed, not encrypted)
     * @param signature the signature for the data, signed with the private key
     */
    fun verifyPurchase(base64PublicKey: String, signedData: String, signature: String): Boolean {
        if (signedData.isEmpty() || base64PublicKey.isEmpty() || signature.isEmpty()) {
            Log.e(TAG, "Purchase verification failed: missing data.")
            return false
        }

        return verify(generatePublicKey(base64PublicKey), signedData, signature)
    }

    /**
     * Generates a PublicKey instance from a string containing the Base64-encoded public key.
     *
     * @param encodedPublicKey Base64-encoded public key
     * @throws IllegalArgumentException if encodedPublicKey is invalid
     */
    private fun generatePublicKey(encodedPublicKey: String): PublicKey {
        return try {
            val decodedKey = BillingBase64.decode(encodedPublicKey)
            val keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
            keyFactory.generatePublic(X509EncodedKeySpec(decodedKey))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidKeySpecException) {
            Log.e(TAG, "Invalid key specification.")
            throw IllegalArgumentException(e)
        } catch (e: Base64DecoderException) {
            Log.e(TAG, "Base64 decoding failed.")
            throw IllegalArgumentException(e)
        }
    }

    /**
     * Verifies that the signature from the server matches the computed signature on the data.  Returns true if the data is correctly
     * signed.
     *
     * @param publicKey public key associated with the developer account
     * @param signedData signed data from server
     * @param signature server signature
     *
     * @return true if the data and signature match
     */
    private fun verify(publicKey: PublicKey, signedData: String, signature: String): Boolean {
        try {
            val sig = Signature.getInstance(SIGNATURE_ALGORITHM)
            sig.initVerify(publicKey)
            sig.update(signedData.toByteArray())
            if (!sig.verify(BillingBase64.decode(signature))) {
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
        } catch (e: Base64DecoderException) {
            Log.e(TAG, "Base64 decoding failed.")
        }

        return false
    }
}
