package es.elb4t.i2c

import android.app.Activity
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.I2cDevice
import nz.geek.android.things.drivers.adc.I2cAdc


/**
 * Skeleton of an Android Things activity.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * val service = PeripheralManagerService()
 * val mLedGpio = service.openGpio("BCM6")
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW)
 * mLedGpio.value = true
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 *
 */
class MainActivity : Activity() {
    private val ACTIVA_SALIDA: Byte = 0x40 // 0100 00 00
    private val AUTOINCREMENTO: Byte = 0x04 // 0000 01 00
    private val ENTRADA_0: Byte = 0x00 // 0000 00 00
    private val ENTRADA_1: Byte = 0x01 // 0000 00 01
    private val ENTRADA_2: Byte = 0x02 // 0000 00 10
    private val ENTRADA_3: Byte = 0x03 // 0000 00 11
    private val IN_I2C_NOMBRE = "I2C1" // Puerto de entrada
    private val IN_I2C_DIRECCION = 0x48 // Dirección de entrada
    private var i2c: I2cDevice? = null

    private val runnable: Runnable = UpdateRunner()
    companion object {
        private var adc: I2cAdc? = null
        private val handler: Handler = Handler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = I2cAdc.builder()
        adc = builder.address(0).fourSingleEnded().withConversionRate(100).build()
        adc?.startConversions()
        handler.post(runnable)

        /*val manager = PeripheralManager.getInstance()
        val listaDispositivos = manager.i2cBusList


        try {
            i2c = manager.openI2cDevice(IN_I2C_NOMBRE, IN_I2C_DIRECCION)

            val config = ByteArray(2)
            config[0] = (ACTIVA_SALIDA + ENTRADA_0).toByte() // byte de control
            config[1] = 0x80.toByte() // valor de salida (128/255)
            i2c?.write(config, config.size)  // escribimos 2 bytes

            val buffer = ByteArray(5)
            i2c?.read(buffer, buffer.size) // leemos 5 bytes
            var s = ""
            for (i in 0 until buffer.size) {
                s += " byte " + i + ": " + (buffer[i] and 0xFF.toByte())
            }
            Log.d(TAG, s)  // mostramos salida

            i2c?.close()  // cerramos i2c
            i2c = null  // liberamos memoria
        } catch (e: IOException) {
            Log.e(TAG, "Error en al acceder a dispositivo I2C", e)
        }*/

    }

    class UpdateRunner: Runnable{

        override fun run() {
            var s = ""
            for (i in 0..3) {
                s += " canal " + i + ": " + adc?.readChannel(i)
            }
            Log.d(TAG, s)
            handler.postDelayed(this, 1000)
        }
    }
}
