/*
 *     Not used. Parsing "EDID" display data, used as a reference,
 *     Licensed under Creative Commons Zero v1.0 Universal
 *     from https://formats.kaitai.io/edid/java.html
 */

package org.noglitchesallowed.obslink.system.info

// This is a generated file! Please edit source .ksy file and use kaitai-struct-compiler to rebuild

import io.kaitai.struct.ByteBufferKaitaiStream
import io.kaitai.struct.KaitaiStream
import io.kaitai.struct.KaitaiStruct
import java.io.IOException
import java.nio.charset.Charset
import java.util.*


class Edid(_io: KaitaiStream?, _parent: KaitaiStruct?, _root: Edid?) : KaitaiStruct(_io) {
    constructor(_io: KaitaiStream?) : this(_io, null, null) {}
    constructor(_io: KaitaiStream?, _parent: KaitaiStruct?) : this(_io, _parent, null) {}

    private fun _read() {
        magic = this._io.readBytes(8)
        if (!Arrays.equals(magic(), byteArrayOf(0, -1, -1, -1, -1, -1, -1, 0))) {
            throw KaitaiStream.ValidationNotEqualError(
                byteArrayOf(0, -1, -1, -1, -1, -1, -1, 0),
                magic(),
                _io(),
                "/seq/0"
            )
        }
        mfgBytes = this._io.readU2be()
        productCode = this._io.readU2le()
        serial = this._io.readU4le()
        mfgWeek = this._io.readU1()
        mfgYearMod = this._io.readU1()
        edidVersionMajor = this._io.readU1()
        edidVersionMinor = this._io.readU1()
        inputFlags = this._io.readU1()
        screenSizeH = this._io.readU1()
        screenSizeV = this._io.readU1()
        gammaMod = this._io.readU1()
        featuresFlags = this._io.readU1()
        chromacity = ChromacityInfo(this._io, this, _root)
        estTimings = EstTimingsInfo(this._io, this, _root)
        _raw_stdTimings = ArrayList((8 as Number).toInt())
        stdTimings = ArrayList((8 as Number).toInt())
        for (i in 0..7) {
            _raw_stdTimings!!.add(this._io.readBytes(2))
            val _io__raw_stdTimings: KaitaiStream =
                ByteBufferKaitaiStream(_raw_stdTimings!![_raw_stdTimings!!.size - 1])
            stdTimings!!.add(StdTiming(_io__raw_stdTimings, this, _root))
        }
    }

    /**
     * Chromaticity information: colorimetry and white point
     * coordinates. All coordinates are stored as fixed precision
     * 10-bit numbers, bits are shuffled for compactness.
     */
    class ChromacityInfo(_io: KaitaiStream?, private val _parent: Edid?, private val _root: Edid?) : KaitaiStruct(_io) {
        constructor(_io: KaitaiStream?) : this(_io, null, null) {}
        constructor(_io: KaitaiStream?, _parent: Edid?) : this(_io, _parent, null) {}

        private fun _read() {
            redX10 = this._io.readBitsIntBe(2)
            redY10 = this._io.readBitsIntBe(2)
            greenX10 = this._io.readBitsIntBe(2)
            greenY10 = this._io.readBitsIntBe(2)
            blueX10 = this._io.readBitsIntBe(2)
            blueY10 = this._io.readBitsIntBe(2)
            whiteX10 = this._io.readBitsIntBe(2)
            whiteY10 = this._io.readBitsIntBe(2)
            this._io.alignToByte()
            redX92 = this._io.readU1()
            redY92 = this._io.readU1()
            greenX92 = this._io.readU1()
            greenY92 = this._io.readU1()
            blueX92 = this._io.readU1()
            blueY92 = this._io.readU1()
            whiteX92 = this._io.readU1()
            whiteY92 = this._io.readU1()
        }

        private var greenXInt: Int? = null
        fun greenXInt(): Int? {
            if (greenXInt != null) return greenXInt
            greenXInt = (greenX92() shl 2 or greenX10().toInt())
            return greenXInt
        }

        private var redY: Double? = null

        /**
         * Red Y coordinate
         */
        fun redY(): Double? {
            if (redY != null) return redY
            redY = (redYInt()!! / 1024.0)
            return redY
        }

        private var greenYInt: Int? = null
        fun greenYInt(): Int? {
            if (greenYInt != null) return greenYInt
            greenYInt = (greenY92() shl 2 or greenY10().toInt())
            return greenYInt
        }

        private var whiteY: Double? = null

        /**
         * White Y coordinate
         */
        fun whiteY(): Double? {
            if (whiteY != null) return whiteY
            whiteY = (whiteYInt()!! / 1024.0)
            return whiteY
        }

        private var redX: Double? = null

        /**
         * Red X coordinate
         */
        fun redX(): Double? {
            if (redX != null) return redX
            redX = (redXInt()!! / 1024.0)
            return redX
        }

        private var whiteX: Double? = null

        /**
         * White X coordinate
         */
        fun whiteX(): Double? {
            if (whiteX != null) return whiteX
            whiteX = (whiteXInt()!! / 1024.0)
            return whiteX
        }

        private var blueX: Double? = null

        /**
         * Blue X coordinate
         */
        fun blueX(): Double? {
            if (blueX != null) return blueX
            blueX = (blueXInt()!! / 1024.0)
            return blueX
        }

        private var whiteXInt: Int? = null
        fun whiteXInt(): Int? {
            if (whiteXInt != null) return whiteXInt
            whiteXInt = (whiteX92() shl 2 or whiteX10().toInt())
            return whiteXInt
        }

        private var whiteYInt: Int? = null
        fun whiteYInt(): Int? {
            if (whiteYInt != null) return whiteYInt
            whiteYInt = (whiteY92() shl 2 or whiteY10().toInt())
            return whiteYInt
        }

        private var greenX: Double? = null

        /**
         * Green X coordinate
         */
        fun greenX(): Double? {
            if (greenX != null) return greenX
            greenX = (greenXInt()!! / 1024.0)
            return greenX
        }

        private var redXInt: Int? = null
        fun redXInt(): Int? {
            if (redXInt != null) return redXInt
            redXInt = (redX92() shl 2 or redX10().toInt())
            return redXInt
        }

        private var redYInt: Int? = null
        fun redYInt(): Int? {
            if (redYInt != null) return redYInt
            redYInt = (redY92() shl 2 or redY10().toInt())
            return redYInt
        }

        private var blueXInt: Int? = null
        fun blueXInt(): Int? {
            if (blueXInt != null) return blueXInt
            blueXInt = (blueX92() shl 2 or blueX10().toInt())
            return blueXInt
        }

        private var blueY: Double? = null

        /**
         * Blue Y coordinate
         */
        fun blueY(): Double? {
            if (blueY != null) return blueY
            blueY = (blueYInt()!! / 1024.0)
            return blueY
        }

        private var greenY: Double? = null

        /**
         * Green Y coordinate
         */
        fun greenY(): Double? {
            if (greenY != null) return greenY
            greenY = (greenYInt()!! / 1024.0)
            return greenY
        }

        private var blueYInt: Int? = null
        fun blueYInt(): Int? {
            if (blueYInt != null) return blueYInt
            blueYInt = (blueY92() shl 2 or blueY10().toInt())
            return blueYInt
        }

        private var redX10: Long = 0
        private var redY10: Long = 0
        private var greenX10: Long = 0
        private var greenY10: Long = 0
        private var blueX10: Long = 0
        private var blueY10: Long = 0
        private var whiteX10: Long = 0
        private var whiteY10: Long = 0
        private var redX92 = 0
        private var redY92 = 0
        private var greenX92 = 0
        private var greenY92 = 0
        private var blueX92 = 0
        private var blueY92 = 0
        private var whiteX92 = 0
        private var whiteY92 = 0

        /**
         * Red X, bits 1..0
         */
        fun redX10(): Long {
            return redX10
        }

        /**
         * Red Y, bits 1..0
         */
        fun redY10(): Long {
            return redY10
        }

        /**
         * Green X, bits 1..0
         */
        fun greenX10(): Long {
            return greenX10
        }

        /**
         * Green Y, bits 1..0
         */
        fun greenY10(): Long {
            return greenY10
        }

        /**
         * Blue X, bits 1..0
         */
        fun blueX10(): Long {
            return blueX10
        }

        /**
         * Blue Y, bits 1..0
         */
        fun blueY10(): Long {
            return blueY10
        }

        /**
         * White X, bits 1..0
         */
        fun whiteX10(): Long {
            return whiteX10
        }

        /**
         * White Y, bits 1..0
         */
        fun whiteY10(): Long {
            return whiteY10
        }

        /**
         * Red X, bits 9..2
         */
        fun redX92(): Int {
            return redX92
        }

        /**
         * Red Y, bits 9..2
         */
        fun redY92(): Int {
            return redY92
        }

        /**
         * Green X, bits 9..2
         */
        fun greenX92(): Int {
            return greenX92
        }

        /**
         * Green Y, bits 9..2
         */
        fun greenY92(): Int {
            return greenY92
        }

        /**
         * Blue X, bits 9..2
         */
        fun blueX92(): Int {
            return blueX92
        }

        /**
         * Blue Y, bits 9..2
         */
        fun blueY92(): Int {
            return blueY92
        }

        /**
         * White X, bits 9..2
         */
        fun whiteX92(): Int {
            return whiteX92
        }

        /**
         * White Y, bits 9..2
         */
        fun whiteY92(): Int {
            return whiteY92
        }

        fun _root(): Edid? {
            return _root
        }

        override fun _parent(): Edid? {
            return _parent as Edid?
        }

        companion object {
            @Throws(IOException::class)
            fun fromFile(fileName: String?): ChromacityInfo {
                return ChromacityInfo(ByteBufferKaitaiStream(fileName))
            }
        }

        init {
            _read()
        }
    }

    class EstTimingsInfo(_io: KaitaiStream?, private val _parent: Edid?, private val _root: Edid?) : KaitaiStruct(_io) {
        constructor(_io: KaitaiStream?) : this(_io, null, null) {}
        constructor(_io: KaitaiStream?, _parent: Edid?) : this(_io, _parent, null) {}

        private fun _read() {
            can720x400px70hz = this._io.readBitsIntBe(1) != 0L
            can720x400px88hz = this._io.readBitsIntBe(1) != 0L
            can640x480px60hz = this._io.readBitsIntBe(1) != 0L
            can640x480px67hz = this._io.readBitsIntBe(1) != 0L
            can640x480px72hz = this._io.readBitsIntBe(1) != 0L
            can640x480px75hz = this._io.readBitsIntBe(1) != 0L
            can800x600px56hz = this._io.readBitsIntBe(1) != 0L
            can800x600px60hz = this._io.readBitsIntBe(1) != 0L
            can800x600px72hz = this._io.readBitsIntBe(1) != 0L
            can800x600px75hz = this._io.readBitsIntBe(1) != 0L
            can832x624px75hz = this._io.readBitsIntBe(1) != 0L
            can1024x768px87hzI = this._io.readBitsIntBe(1) != 0L
            can1024x768px60hz = this._io.readBitsIntBe(1) != 0L
            can1024x768px70hz = this._io.readBitsIntBe(1) != 0L
            can1024x768px75hz = this._io.readBitsIntBe(1) != 0L
            can1280x1024px75hz = this._io.readBitsIntBe(1) != 0L
            can1152x870px75hz = this._io.readBitsIntBe(1) != 0L
            reserved = this._io.readBitsIntBe(7)
        }

        private var can720x400px70hz = false
        private var can720x400px88hz = false
        private var can640x480px60hz = false
        private var can640x480px67hz = false
        private var can640x480px72hz = false
        private var can640x480px75hz = false
        private var can800x600px56hz = false
        private var can800x600px60hz = false
        private var can800x600px72hz = false
        private var can800x600px75hz = false
        private var can832x624px75hz = false
        private var can1024x768px87hzI = false
        private var can1024x768px60hz = false
        private var can1024x768px70hz = false
        private var can1024x768px75hz = false
        private var can1280x1024px75hz = false
        private var can1152x870px75hz = false
        private var reserved: Long = 0

        /**
         * Supports 720 x 400 @ 70Hz
         */
        fun can720x400px70hz(): Boolean {
            return can720x400px70hz
        }

        /**
         * Supports 720 x 400 @ 88Hz
         */
        fun can720x400px88hz(): Boolean {
            return can720x400px88hz
        }

        /**
         * Supports 640 x 480 @ 60Hz
         */
        fun can640x480px60hz(): Boolean {
            return can640x480px60hz
        }

        /**
         * Supports 640 x 480 @ 67Hz
         */
        fun can640x480px67hz(): Boolean {
            return can640x480px67hz
        }

        /**
         * Supports 640 x 480 @ 72Hz
         */
        fun can640x480px72hz(): Boolean {
            return can640x480px72hz
        }

        /**
         * Supports 640 x 480 @ 75Hz
         */
        fun can640x480px75hz(): Boolean {
            return can640x480px75hz
        }

        /**
         * Supports 800 x 600 @ 56Hz
         */
        fun can800x600px56hz(): Boolean {
            return can800x600px56hz
        }

        /**
         * Supports 800 x 600 @ 60Hz
         */
        fun can800x600px60hz(): Boolean {
            return can800x600px60hz
        }

        /**
         * Supports 800 x 600 @ 72Hz
         */
        fun can800x600px72hz(): Boolean {
            return can800x600px72hz
        }

        /**
         * Supports 800 x 600 @ 75Hz
         */
        fun can800x600px75hz(): Boolean {
            return can800x600px75hz
        }

        /**
         * Supports 832 x 624 @ 75Hz
         */
        fun can832x624px75hz(): Boolean {
            return can832x624px75hz
        }

        /**
         * Supports 1024 x 768 @ 87Hz(I)
         */
        fun can1024x768px87hzI(): Boolean {
            return can1024x768px87hzI
        }

        /**
         * Supports 1024 x 768 @ 60Hz
         */
        fun can1024x768px60hz(): Boolean {
            return can1024x768px60hz
        }

        /**
         * Supports 1024 x 768 @ 70Hz
         */
        fun can1024x768px70hz(): Boolean {
            return can1024x768px70hz
        }

        /**
         * Supports 1024 x 768 @ 75Hz
         */
        fun can1024x768px75hz(): Boolean {
            return can1024x768px75hz
        }

        /**
         * Supports 1280 x 1024 @ 75Hz
         */
        fun can1280x1024px75hz(): Boolean {
            return can1280x1024px75hz
        }

        /**
         * Supports 1152 x 870 @ 75Hz
         */
        fun can1152x870px75hz(): Boolean {
            return can1152x870px75hz
        }

        fun reserved(): Long {
            return reserved
        }

        fun _root(): Edid? {
            return _root
        }

        override fun _parent(): Edid? {
            return _parent as Edid?
        }

        companion object {
            @Throws(IOException::class)
            fun fromFile(fileName: String?): EstTimingsInfo {
                return EstTimingsInfo(ByteBufferKaitaiStream(fileName))
            }
        }

        init {
            _read()
        }
    }

    class StdTiming(_io: KaitaiStream?, private val _parent: Edid?, private val _root: Edid?) : KaitaiStruct(_io) {
        enum class AspectRatios(private val id: Long) {
            RATIO_16_10(0), RATIO_4_3(1), RATIO_5_4(2), RATIO_16_9(3);

            fun id(): Long {
                return id
            }

            companion object {
                private val byId: MutableMap<Long, AspectRatios> = HashMap(4)
                fun byId(id: Long): AspectRatios? {
                    return byId[id]
                }

                init {
                    for (e in values()) byId[e.id()] =
                        e
                }
            }
        }

        constructor(_io: KaitaiStream?) : this(_io, null, null) {}
        constructor(_io: KaitaiStream?, _parent: Edid?) : this(_io, _parent, null) {}

        private fun _read() {
            horizActivePixelsMod = this._io.readU1()
            aspectRatio = AspectRatios.byId(this._io.readBitsIntBe(2))
            refreshRateMod = this._io.readBitsIntBe(6)
        }

        private var bytesLookahead: ByteArray? = null
        fun bytesLookahead(): ByteArray? {
            if (bytesLookahead != null) return bytesLookahead
            val _pos: Long = this._io.pos().toLong()
            this._io.seek(0)
            bytesLookahead = this._io.readBytes(2)
            this._io.seek(_pos)
            return bytesLookahead
        }

        var isUsed: Boolean? = null
            get() {
                if (field != null) return field
                field = !Arrays.equals(bytesLookahead(), byteArrayOf(1, 1))
                return field
            }
            private set
        private var horizActivePixels: Int? = null

        /**
         * Range of horizontal active pixels.
         */
        fun horizActivePixels(): Int? {
            if (horizActivePixels != null) return horizActivePixels
            if (isUsed!!) {
                horizActivePixels = ((horizActivePixelsMod() + 31) * 8)
            }
            return horizActivePixels
        }

        private var refreshRate: Int? = null

        /**
         * Vertical refresh rate, Hz.
         */
        fun refreshRate(): Int? {
            if (refreshRate != null) return refreshRate
            if (isUsed!!) {
                val _tmp = (refreshRateMod() + 60).toInt()
                refreshRate = _tmp
            }
            return refreshRate
        }

        private var horizActivePixelsMod = 0
        private var aspectRatio: AspectRatios? = null
        private var refreshRateMod: Long = 0

        /**
         * Range of horizontal active pixels, written in modified form:
         * `(horiz_active_pixels / 8) - 31`. This yields an effective
         * range of 256..2288, with steps of 8 pixels.
         */
        fun horizActivePixelsMod(): Int {
            return horizActivePixelsMod
        }

        /**
         * Aspect ratio of the image. Can be used to calculate number
         * of vertical pixels.
         */
        fun aspectRatio(): AspectRatios? {
            return aspectRatio
        }

        /**
         * Refresh rate in Hz, written in modified form: `refresh_rate
         * - 60`. This yields an effective range of 60..123 Hz.
         */
        fun refreshRateMod(): Long {
            return refreshRateMod
        }

        fun _root(): Edid? {
            return _root
        }

        override fun _parent(): Edid? {
            return _parent as Edid?
        }

        companion object {
            @Throws(IOException::class)
            fun fromFile(fileName: String?): StdTiming {
                return StdTiming(ByteBufferKaitaiStream(fileName))
            }
        }

        init {
            _read()
        }
    }

    private var mfgYear: Int? = null
    fun mfgYear(): Int? {
        if (mfgYear != null) return mfgYear
        mfgYear = (mfgYearMod() + 1990)
        return mfgYear
    }

    private var mfgIdCh1: Int? = null
    fun mfgIdCh1(): Int? {
        if (mfgIdCh1 != null) return mfgIdCh1
        mfgIdCh1 = (mfgBytes() and 31744 shr 10)
        return mfgIdCh1
    }

    private var mfgIdCh3: Int? = null
    fun mfgIdCh3(): Int? {
        if (mfgIdCh3 != null) return mfgIdCh3
        mfgIdCh3 = (mfgBytes() and 31)
        return mfgIdCh3
    }

    private var gamma: Double? = null
    fun gamma(): Double? {
        if (gamma != null) return gamma
        if (gammaMod() != 255) {
            gamma = ((gammaMod() + 100) / 100.0)
        }
        return gamma
    }

    private var mfgStr: String? = null
    fun mfgStr(): String? {
        if (mfgStr != null) return mfgStr
        mfgStr = String(
            byteArrayOf(
                (mfgIdCh1()!! + 64).toByte(),
                (mfgIdCh2()!! + 64).toByte(),
                (mfgIdCh3()!! + 64).toByte()
            ), Charset.forName("ASCII")
        )
        return mfgStr
    }

    private var mfgIdCh2: Int? = null
    fun mfgIdCh2(): Int? {
        if (mfgIdCh2 != null) return mfgIdCh2
        mfgIdCh2 = (mfgBytes() and 992 shr 5)
        return mfgIdCh2
    }

    private var magic: ByteArray = ByteArray(0)
    private var mfgBytes = 0
    private var productCode = 0
    private var serial: Long = 0
    private var mfgWeek = 0
    private var mfgYearMod = 0
    private var edidVersionMajor = 0
    private var edidVersionMinor = 0
    private var inputFlags = 0
    private var screenSizeH = 0
    private var screenSizeV = 0
    private var gammaMod = 0
    private var featuresFlags = 0
    private var chromacity: ChromacityInfo? = null
    private var estTimings: EstTimingsInfo? = null
    private var stdTimings: ArrayList<StdTiming>? = null
    private val _root: Edid
    private val _parent: KaitaiStruct? = null
    private var _raw_stdTimings: ArrayList<ByteArray>? = null
    fun magic(): ByteArray {
        return magic
    }

    fun mfgBytes(): Int {
        return mfgBytes
    }

    /**
     * Manufacturer product code
     */
    fun productCode(): Int {
        return productCode
    }

    /**
     * Serial number
     */
    fun serial(): Long {
        return serial
    }

    /**
     * Week of manufacture. Week numbering is not consistent between manufacturers.
     */
    fun mfgWeek(): Int {
        return mfgWeek
    }

    /**
     * Year of manufacture, less 1990. (1990-2245). If week=255, it is the model year instead.
     */
    fun mfgYearMod(): Int {
        return mfgYearMod
    }

    /**
     * EDID version, usually 1 (for 1.3)
     */
    fun edidVersionMajor(): Int {
        return edidVersionMajor
    }

    /**
     * EDID revision, usually 3 (for 1.3)
     */
    fun edidVersionMinor(): Int {
        return edidVersionMinor
    }

    fun inputFlags(): Int {
        return inputFlags
    }

    /**
     * Maximum horizontal image size, in centimetres (max 292 cm/115 in at 16:9 aspect ratio)
     */
    fun screenSizeH(): Int {
        return screenSizeH
    }

    /**
     * Maximum vertical image size, in centimetres. If either byte is 0, undefined (e.g. projector)
     */
    fun screenSizeV(): Int {
        return screenSizeV
    }

    /**
     * Display gamma, datavalue = (gamma*100)-100 (range 1.00-3.54)
     */
    fun gammaMod(): Int {
        return gammaMod
    }

    fun featuresFlags(): Int {
        return featuresFlags
    }

    /**
     * Phosphor or filter chromaticity structure, which provides info on colorimetry and white point
     * @see "Standard, section 3.7"
     */
    fun chromacity(): ChromacityInfo? {
        return chromacity
    }

    /**
     * Block of bit flags that indicates support of so called
     * "established timings", which is a commonly used subset of VESA
     * DMT (Discrete Monitor Timings) modes.
     * @see "Standard, section 3.8"
     */
    fun estTimings(): EstTimingsInfo? {
        return estTimings
    }

    /**
     * Array of descriptions of so called "standard timings", which are
     * used to specify up to 8 additional timings not included in
     * "established timings".
     */
    fun stdTimings(): ArrayList<StdTiming>? {
        return stdTimings
    }

    fun _root(): Edid {
        return _root
    }

    override fun _parent(): KaitaiStruct? {
        return _parent
    }

    fun _raw_stdTimings(): ArrayList<ByteArray>? {
        return _raw_stdTimings
    }

    companion object {
        @Throws(IOException::class)
        fun fromFile(fileName: String?): Edid {
            return Edid(ByteBufferKaitaiStream(fileName))
        }
    }

    init {
        this._parent = _parent
        this._root = _root ?: this
        _read()
    }
}