package com.sh.prolearn.core.utils

import com.sh.prolearn.core.utils.Consts.DEFAULT_DATE_FORMAT
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

object GeneralUtils {
    fun getIPAddress(): String {
        val useIPv4 = true
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr: String = addr.hostAddress as String
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase()
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        } // for now eat exceptions

        return ""
    }

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat(DEFAULT_DATE_FORMAT)
        return sdf.format(Date()).toString()
    }

    fun getTimeFromInt(time: Int): String {
        val hours = time  / 3600
        var remainder = time - hours * 3600
        val mins = remainder / 60
        remainder = remainder - mins * 60
        val secs = remainder

        return "$hours jam $mins menit $secs detik"
    }
}