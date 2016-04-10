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

package com.mvcoding.billing.odl

// This code was converted from code at http://iharder.sourceforge.net/base64/
// Lots of extraneous features were removed.
/* The original code said:
 * <p>
 * I am placing this code in the Public Domain. Do with it as you will.
 * This software comes with no guarantees or warranties but with
 * plenty of well-wishing instead!
 * Please visit
 * <a href="http://iharder.net/xmlizable">http://iharder.net/xmlizable</a>
 * periodically to check for updates or to contribute improvements.
 * </p>
 *
 * @author Robert Harder
 * @author rharder@usa.net
 * @version 1.3
 */

private object BillingBase64 {
    val ENCODE = true
    val DECODE = false

    private val EQUALS_SIGN = '='.toByte()
    private val NEW_LINE = '\n'.toByte()
    private val ALPHABET = byteArrayOf(
            'A'.toByte(),
            'B'.toByte(),
            'C'.toByte(),
            'D'.toByte(),
            'E'.toByte(),
            'F'.toByte(),
            'G'.toByte(),
            'H'.toByte(),
            'I'.toByte(),
            'J'.toByte(),
            'K'.toByte(),
            'L'.toByte(),
            'M'.toByte(),
            'N'.toByte(),
            'O'.toByte(),
            'P'.toByte(),
            'Q'.toByte(),
            'R'.toByte(),
            'S'.toByte(),
            'T'.toByte(),
            'U'.toByte(),
            'V'.toByte(),
            'W'.toByte(),
            'X'.toByte(),
            'Y'.toByte(),
            'Z'.toByte(),
            'a'.toByte(),
            'b'.toByte(),
            'c'.toByte(),
            'd'.toByte(),
            'e'.toByte(),
            'f'.toByte(),
            'g'.toByte(),
            'h'.toByte(),
            'i'.toByte(),
            'j'.toByte(),
            'k'.toByte(),
            'l'.toByte(),
            'm'.toByte(),
            'n'.toByte(),
            'o'.toByte(),
            'p'.toByte(),
            'q'.toByte(),
            'r'.toByte(),
            's'.toByte(),
            't'.toByte(),
            'u'.toByte(),
            'v'.toByte(),
            'w'.toByte(),
            'x'.toByte(),
            'y'.toByte(),
            'z'.toByte(),
            '0'.toByte(),
            '1'.toByte(),
            '2'.toByte(),
            '3'.toByte(),
            '4'.toByte(),
            '5'.toByte(),
            '6'.toByte(),
            '7'.toByte(),
            '8'.toByte(),
            '9'.toByte(),
            '+'.toByte(),
            '/'.toByte())

    private val WEBSAFE_ALPHABET = byteArrayOf(
            'A'.toByte(),
            'B'.toByte(),
            'C'.toByte(),
            'D'.toByte(),
            'E'.toByte(),
            'F'.toByte(),
            'G'.toByte(),
            'H'.toByte(),
            'I'.toByte(),
            'J'.toByte(),
            'K'.toByte(),
            'L'.toByte(),
            'M'.toByte(),
            'N'.toByte(),
            'O'.toByte(),
            'P'.toByte(),
            'Q'.toByte(),
            'R'.toByte(),
            'S'.toByte(),
            'T'.toByte(),
            'U'.toByte(),
            'V'.toByte(),
            'W'.toByte(),
            'X'.toByte(),
            'Y'.toByte(),
            'Z'.toByte(),
            'a'.toByte(),
            'b'.toByte(),
            'c'.toByte(),
            'd'.toByte(),
            'e'.toByte(),
            'f'.toByte(),
            'g'.toByte(),
            'h'.toByte(),
            'i'.toByte(),
            'j'.toByte(),
            'k'.toByte(),
            'l'.toByte(),
            'm'.toByte(),
            'n'.toByte(),
            'o'.toByte(),
            'p'.toByte(),
            'q'.toByte(),
            'r'.toByte(),
            's'.toByte(),
            't'.toByte(),
            'u'.toByte(),
            'v'.toByte(),
            'w'.toByte(),
            'x'.toByte(),
            'y'.toByte(),
            'z'.toByte(),
            '0'.toByte(),
            '1'.toByte(),
            '2'.toByte(),
            '3'.toByte(),
            '4'.toByte(),
            '5'.toByte(),
            '6'.toByte(),
            '7'.toByte(),
            '8'.toByte(),
            '9'.toByte(),
            '-'.toByte(),
            '_'.toByte())

    /**
     * Translates a Base64 value to either its 6-bit reconstruction value
     * or a negative number indicating some other meaning.
     */
    private val DECODABET = byteArrayOf(
            -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal  0 -  8
            -5, -5, // Whitespace: Tab and Linefeed
            -9, -9, // Decimal 11 - 12
            -5, // Whitespace: Carriage Return
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 - 26
            -9, -9, -9, -9, -9, // Decimal 27 - 31
            -5, // Whitespace: Space
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 42
            62, // Plus sign at decimal 43
            -9, -9, -9, // Decimal 44 - 46
            63, // Slash at decimal 47
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
            -9, -9, -9, // Decimal 58 - 60
            -1, // Equals sign at decimal 61
            -9, -9, -9, // Decimal 62 - 64
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through 'N'
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O' through 'Z'
            -9, -9, -9, -9, -9, -9, // Decimal 91 - 96
            26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a' through 'm'
            39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n' through 'z'
            -9, -9, -9, -9, -9 // Decimal 123 - 127
    )

    /**
     * The web safe decodabet
     */
    private val WEBSAFE_DECODABET = byteArrayOf(
            -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal  0 -  8
            -5, -5, // Whitespace: Tab and Linefeed
            -9, -9, // Decimal 11 - 12
            -5, // Whitespace: Carriage Return
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 14 - 26
            -9, -9, -9, -9, -9, // Decimal 27 - 31
            -5, // Whitespace: Space
            -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, // Decimal 33 - 44
            62, // Dash '-' sign at decimal 45
            -9, -9, // Decimal 46-47
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
            -9, -9, -9, // Decimal 58 - 60
            -1, // Equals sign at decimal 61
            -9, -9, -9, // Decimal 62 - 64
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through 'N'
            14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O' through 'Z'
            -9, -9, -9, -9, // Decimal 91-94
            63, // Underscore '_' at decimal 95
            -9, // Decimal 96
            26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a' through 'm'
            39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n' through 'z'
            -9, -9, -9, -9, -9 // Decimal 123 - 127
    )

    private val WHITE_SPACE_ENC: Byte = -5
    private val EQUALS_SIGN_ENC: Byte = -1

    /**
     * Encodes up to three bytes of the array source and writes the resulting four Base64 bytes to destination. The source and destination
     * arrays can be manipulated anywhere along their length by specifying srcOffset and destOffset. This method does not check to make sure
     * your arrays are large enough to accommodate srcOffset + 3 for the source array or destOffset + 4 for the destination array. The
     * actual number of significant bytes in your array is given by numSigBytes.
     *
     * @param source      the array to convert
     * @param srcOffset   the index where conversion begins
     * @param numSigBytes the number of significant bytes in your array
     * @param destination the array to hold the conversion
     * @param destOffset  the index where output will be put
     * @param alphabet    is the encoding alphabet
     *
     * @return the destination array
     */
    private fun encode3to4(
            source: ByteArray,
            srcOffset: Int,
            numSigBytes: Int,
            destination: ByteArray,
            destOffset: Int,
            alphabet: ByteArray): ByteArray {
        //           1         2         3
        // 01234567890123456789012345678901 Bit position
        // --------000000001111111122222222 Array position from threeBytes
        // --------|    ||    ||    ||    | Six bit groups to index alphabet
        //          >>18  >>12  >> 6  >> 0  Right shift necessary
        //                0x3f  0x3f  0x3f  Additional AND

        // Create buffer with zero-padding if there are only one or two
        // significant bytes passed in the array.
        // We have to shift left 24 in order to flush out the 1's that appear
        // when Java treats a value as negative that is cast from a byte to an int.
        val inBuff = if (numSigBytes > 0) (source[srcOffset] shl 24).ushr(8) else 0 shl
        or (if (numSigBytes > 1) (source[srcOffset + 1] shl 24).ushr(16) else 0)
        or (if (numSigBytes > 2) (source[srcOffset + 2] shl 24).ushr(24) else 0)

        when (numSigBytes) {
            3 -> {
                destination[destOffset] = alphabet[inBuff.ushr(18)]
                destination[destOffset + 1] = alphabet[inBuff.ushr(12) and 0x3f]
                destination[destOffset + 2] = alphabet[inBuff.ushr(6) and 0x3f]
                destination[destOffset + 3] = alphabet[inBuff and 0x3f]
                return destination
            }
            2 -> {
                destination[destOffset] = alphabet[inBuff.ushr(18)]
                destination[destOffset + 1] = alphabet[inBuff.ushr(12) and 0x3f]
                destination[destOffset + 2] = alphabet[inBuff.ushr(6) and 0x3f]
                destination[destOffset + 3] = EQUALS_SIGN
                return destination
            }
            1 -> {
                destination[destOffset] = alphabet[inBuff.ushr(18)]
                destination[destOffset + 1] = alphabet[inBuff.ushr(12) and 0x3f]
                destination[destOffset + 2] = EQUALS_SIGN
                destination[destOffset + 3] = EQUALS_SIGN
                return destination
            }
            else -> return destination
        } // end switch
    } // end encode3to4

    /**
     * Encodes a byte array into web safe Base64 notation.

     * @param source    The data to convert
     * *
     * @param doPadding is `true` to pad result with '=' chars
     * *                  if it does not fall on 3 byte boundaries
     */
    fun encodeWebSafe(source: ByteArray, doPadding: Boolean): String {
        return encode(source, 0, source.size, WEBSAFE_ALPHABET, doPadding)
    }

    /**
     * Encodes a byte array into Base64 notation.

     * @param source    the data to convert
     * *
     * @param off       offset in array where conversion should begin
     * *
     * @param len       length of data to convert
     * *
     * @param alphabet  the encoding alphabet
     * *
     * @param doPadding is `true` to pad result with '=' chars
     * *                  if it does not fall on 3 byte boundaries
     * *
     * @since 1.4
     */
    @JvmOverloads fun encode(source: ByteArray, off: Int = 0, len: Int = source.size, alphabet: ByteArray = ALPHABET,
            doPadding: Boolean = true): String {
        val outBuff = encode(source, off, len, alphabet, Integer.MAX_VALUE)
        var outLen = outBuff.size

        // If doPadding is false, set length to truncate '='
        // padding characters
        while (doPadding == false && outLen > 0) {
            if (outBuff[outLen - 1] != '=') {
                break
            }
            outLen -= 1
        }

        return String(outBuff, 0, outLen)
    }

    /**
     * Encodes a byte array into Base64 notation.

     * @param source        the data to convert
     * *
     * @param off           offset in array where conversion should begin
     * *
     * @param len           length of data to convert
     * *
     * @param alphabet      is the encoding alphabet
     * *
     * @param maxLineLength maximum length of one line.
     * *
     * @return the BASE64-encoded byte array
     */
    fun encode(source: ByteArray, off: Int, len: Int, alphabet: ByteArray,
            maxLineLength: Int): ByteArray {
        val lenDiv3 = (len + 2) / 3 // ceil(len / 3)
        val len43 = lenDiv3 * 4
        val outBuff = ByteArray(len43 // Main 4:3
                                + len43 / maxLineLength) // New lines

        var d = 0
        var e = 0
        val len2 = len - 2
        var lineLength = 0
        while (d < len2) {

            // The following block of code is the same as
            // encode3to4( source, d + off, 3, outBuff, e, alphabet );
            // but inlined for faster encoding (~20% improvement)
            val inBuff = (source[d + off] shl 24).ushr(8)
            or ((source[d + 1 + off] shl 24).ushr(16))
            or ((source[d + 2 + off] shl 24).ushr(24))
            outBuff[e] = alphabet[inBuff.ushr(18)]
            outBuff[e + 1] = alphabet[inBuff.ushr(12) and 0x3f]
            outBuff[e + 2] = alphabet[inBuff.ushr(6) and 0x3f]
            outBuff[e + 3] = alphabet[inBuff and 0x3f]

            lineLength += 4
            if (lineLength == maxLineLength) {
                outBuff[e + 4] = NEW_LINE
                e++
                lineLength = 0
            } // end if: end of line
            d += 3
            e += 4
        } // end for: each piece of array

        if (d < len) {
            encode3to4(source, d + off, len - d, outBuff, e, alphabet)

            lineLength += 4
            if (lineLength == maxLineLength) {
                // Add a last newline
                outBuff[e + 4] = NEW_LINE
                e++
            }
            e += 4
        }

        assert(e == outBuff.size)
        return outBuff
    }


    /* ********  D E C O D I N G   M E T H O D S  ******** */


    /**
     * Decodes four bytes from array source
     * and writes the resulting bytes (up to three of them)
     * to destination.
     * The source and destination arrays can be manipulated
     * anywhere along their length by specifying
     * srcOffset and destOffset.
     * This method does not check to make sure your arrays
     * are large enough to accommodate srcOffset + 4 for
     * the source array or destOffset + 3 for
     * the destination array.
     * This method returns the actual number of bytes that
     * were converted from the Base64 encoding.

     * @param source      the array to convert
     * *
     * @param srcOffset   the index where conversion begins
     * *
     * @param destination the array to hold the conversion
     * *
     * @param destOffset  the index where output will be put
     * *
     * @param decodabet   the decodabet for decoding Base64 content
     * *
     * @return the number of decoded bytes converted
     * *
     * @since 1.3
     */
    private fun decode4to3(source: ByteArray, srcOffset: Int,
            destination: ByteArray, destOffset: Int, decodabet: ByteArray): Int {
        // Example: Dk==
        if (source[srcOffset + 2] == EQUALS_SIGN) {
            val outBuff = (decodabet[source[srcOffset]] shl 24).ushr(6) or (decodabet[source[srcOffset + 1]] shl 24).ushr(12)

            destination[destOffset] = outBuff.ushr(16).toByte()
            return 1
        } else if (source[srcOffset + 3] == EQUALS_SIGN) {
            // Example: DkL=
            val outBuff = (decodabet[source[srcOffset]] shl 24).ushr(6)
            or ((decodabet[source[srcOffset + 1]] shl 24).ushr(12))
            or ((decodabet[source[srcOffset + 2]] shl 24).ushr(18))

            destination[destOffset] = outBuff.ushr(16).toByte()
            destination[destOffset + 1] = outBuff.ushr(8).toByte()
            return 2
        } else {
            // Example: DkLE
            val outBuff = (decodabet[source[srcOffset]] shl 24).ushr(6)
            or ((decodabet[source[srcOffset + 1]] shl 24).ushr(12))
            or ((decodabet[source[srcOffset + 2]] shl 24).ushr(18))
            or ((decodabet[source[srcOffset + 3]] shl 24).ushr(24))

            destination[destOffset] = (outBuff shr 16).toByte()
            destination[destOffset + 1] = (outBuff shr 8).toByte()
            destination[destOffset + 2] = outBuff.toByte()
            return 3
        }
    } // end decodeToBytes


    /**
     * Decodes data from Base64 notation.

     * @param s the string to decode (decoded in default encoding)
     * *
     * @return the decoded data
     * *
     * @since 1.4
     */
    @Throws(Base64DecoderException::class)
    fun decode(s: String): ByteArray {
        val bytes = s.toByteArray()
        return decode(bytes, 0, bytes.size)
    }

    /**
     * Decodes data from web safe Base64 notation.
     * Web safe encoding uses '-' instead of '+', '_' instead of '/'

     * @param s the string to decode (decoded in default encoding)
     * *
     * @return the decoded data
     */
    @Throws(Base64DecoderException::class)
    fun decodeWebSafe(s: String): ByteArray {
        val bytes = s.toByteArray()
        return decodeWebSafe(bytes, 0, bytes.size)
    }

    /**
     * Decodes web safe Base64 content in byte array format and returns
     * the decoded byte array.
     * Web safe encoding uses '-' instead of '+', '_' instead of '/'

     * @param source the Base64 encoded data
     * *
     * @param off    the offset of where to begin decoding
     * *
     * @param len    the length of characters to decode
     * *
     * @return decoded data
     */
    @Throws(Base64DecoderException::class)
    @JvmOverloads fun decodeWebSafe(source: ByteArray, off: Int = 0, len: Int = source.size): ByteArray {
        return decode(source, off, len, WEBSAFE_DECODABET)
    }

    /**
     * Decodes Base64 content using the supplied decodabet and returns
     * the decoded byte array.

     * @param source    the Base64 encoded data
     * *
     * @param off       the offset of where to begin decoding
     * *
     * @param len       the length of characters to decode
     * *
     * @param decodabet the decodabet for decoding Base64 content
     * *
     * @return decoded data
     */
    @Throws(Base64DecoderException::class)
    @JvmOverloads fun decode(source: ByteArray, off: Int = 0, len: Int = source.size, decodabet: ByteArray = DECODABET): ByteArray {
        val len34 = len * 3 / 4
        val outBuff = ByteArray(2 + len34) // Upper limit on size of output
        var outBuffPosn = 0

        val b4 = ByteArray(4)
        var b4Posn = 0
        var i = 0
        var sbiCrop: Byte = 0
        var sbiDecode: Byte = 0
        i = 0
        while (i < len) {
            sbiCrop = (source[i + off] and 0x7f).toByte() // Only the low seven bits
            sbiDecode = decodabet[sbiCrop]

            if (sbiDecode >= WHITE_SPACE_ENC) {
                // White space Equals sign or better
                if (sbiDecode >= EQUALS_SIGN_ENC) {
                    // An equals sign (for padding) must not occur at position 0 or 1
                    // and must be the last byte[s] in the encoded value
                    if (sbiCrop == EQUALS_SIGN) {
                        val bytesLeft = len - i
                        val lastByte = (source[len - 1 + off] and 0x7f).toByte()
                        if (b4Posn == 0 || b4Posn == 1) {
                            throw Base64DecoderException(
                                    "invalid padding byte '=' at byte offset " + i)
                        } else if (b4Posn == 3 && bytesLeft > 2 || b4Posn == 4 && bytesLeft > 1) {
                            throw Base64DecoderException(
                                    "padding byte '=' falsely signals end of encoded value "
                                    + "at offset " + i)
                        } else if (lastByte != EQUALS_SIGN && lastByte != NEW_LINE) {
                            throw Base64DecoderException(
                                    "encoded value has invalid trailing byte")
                        }
                        break
                    }

                    b4[b4Posn++] = sbiCrop
                    if (b4Posn == 4) {
                        outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet)
                        b4Posn = 0
                    }
                }
            } else {
                throw Base64DecoderException("Bad Base64 input character at " + i
                                             + ": " + source[i + off] + "(decimal)")
            }
            i++
        }

        // Because web safe encoding allows non padding base64 encodes, we
        // need to pad the rest of the b4 buffer with equal signs when
        // b4Posn != 0.  There can be at most 2 equal signs at the end of
        // four characters, so the b4 buffer must have two or three
        // characters.  This also catches the case where the input is
        // padded with EQUALS_SIGN
        if (b4Posn != 0) {
            if (b4Posn == 1) {
                throw Base64DecoderException("single trailing character at offset " + (len - 1))
            }
            b4[b4Posn++] = EQUALS_SIGN
            outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn, decodabet)
        }

        val out = ByteArray(outBuffPosn)
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn)
        return out
    }
}
/**
 * Defeats instantiation.
 */
/**
 * Encodes a byte array into Base64 notation.
 * Equivalent to calling
 * `encodeBytes(source, 0, source.length)`

 * @param source The data to convert
 * *
 * @since 1.4
 */
/**
 * Decodes Base64 content in byte array format and returns
 * the decoded byte array.

 * @param source The Base64 encoded data
 * *
 * @return decoded data
 * *
 * @throws Base64DecoderException
 * *
 * @since 1.3
 */
/**
 * Decodes web safe Base64 content in byte array format and returns
 * the decoded data.
 * Web safe encoding uses '-' instead of '+', '_' instead of '/'

 * @param source the string to decode (decoded in default encoding)
 * *
 * @return the decoded data
 */
/**
 * Decodes Base64 content in byte array format and returns
 * the decoded byte array.

 * @param source the Base64 encoded data
 * *
 * @param off    the offset of where to begin decoding
 * *
 * @param len    the length of characters to decode
 * *
 * @return decoded data
 * *
 * @throws Base64DecoderException
 * *
 * @since 1.3
 */
