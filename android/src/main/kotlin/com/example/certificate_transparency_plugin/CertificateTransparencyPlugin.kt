package com.example.certificate_transparency_plugin
import android.os.Build
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.appmattus.certificatetransparency.CTLogger
import com.appmattus.certificatetransparency.VerificationResult
import com.appmattus.certificatetransparency.cache.AndroidDiskCache
import com.appmattus.certificatetransparency.certificateTransparencyTrustManager
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import okhttp3.*
import java.io.IOException
import java.security.KeyStore
import java.security.SecureRandom
import java.util.concurrent.CompletableFuture
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
class CertificateTransparencyPlugin : FlutterPlugin, MethodChannel.MethodCallHandler {
    private lateinit var channel: MethodChannel
    private lateinit var context: android.content.Context

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "certificate_transparency")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        if (call.method == "verifyUrl") {
            val url = call.argument<String>("url") ?: return result.error("MISSING_URL", "URL is required", null)
            val includeHosts = call.argument<List<String>>("includeHosts")?.toSet() ?: emptySet()
            val excludeHosts = call.argument<List<String>>("excludeHosts")?.toSet() ?: emptySet()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                makeNetworkRequest(url, includeHosts, excludeHosts) { response ->
                    result.success(mapOf(
                        "isValidCertificate" to response.isValidCertificate,
                        "certificateStatus" to response.certificateStatus,
                        "trustedLogs" to response.trustedLogs,
                        "networkStatus" to response.networkStatus,
                        "responseCode" to response.responseCode,
                        "responseMessage" to response.responseMessage,
                        "errorMessage" to response.errorMessage,
                        "errorType" to response.errorType  
                    ))
                }
            } else {
                result.error("UNSUPPORTED_ANDROID_VERSION", "Requires Android N or higher", null)
            }
        } else {
            result.notImplemented()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun makeNetworkRequest(
        url: String,
        includeHosts: Set<String>,
        excludeHosts: Set<String>,
        callback: (NetworkResponse) -> Unit
    ) {
        var isValidCertificate = false
        var certificateStatus = "Failed"
        var trustedLogs: List<String>? = null

        val client = createOkHttpClient(
            includeCommonNames = includeHosts,
            excludeCommonNames = excludeHosts,
            isFailOnError = true,
            defaultLogger = object : CTLogger {
                override fun log(host: String, result: VerificationResult) {
                    if (result is VerificationResult.Success.Trusted) {
                        isValidCertificate = true
                        certificateStatus = "Success"
                        trustedLogs = result.scts.map { "${it.key}: ${it.value}" }
                    } else {
                        trustedLogs = listOf(result.toString())
                    }

                    if (result is VerificationResult.Failure ||
                        result is VerificationResult.Success.DisabledForHost ||
                        result is VerificationResult.Success.InsecureConnection
                    ) {
                        callback(
                            NetworkResponse(
                                false, certificateStatus, trustedLogs, "CERTIFICATE_ERROR",
                                -1, result.toString(), result.toString(), "CERTIFICATE_ERROR"
                            )
                        )
                    }
                }
            }
        )

        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(
                    NetworkResponse(
                        isValidCertificate, certificateStatus, trustedLogs, "NETWORK_ERROR",
                        -1, "Network request failed", e.message, "NETWORK_ERROR"
                    )
                )
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful) {
                    callback(
                        NetworkResponse(
                            isValidCertificate, certificateStatus, trustedLogs, "SUCCESS",
                            response.code, responseBody, null, "SUCCESS"
                        )
                    )
                } else {
                    callback(
                        NetworkResponse(
                            isValidCertificate, certificateStatus, trustedLogs, "HTTP_ERROR",
                            response.code, response.message, null, "HTTP_ERROR"
                        )
                    )
                }
            }
        })
    }

    private fun createOkHttpClient(
        includeCommonNames: Set<String>,
        excludeCommonNames: Set<String>,
        isFailOnError: Boolean,
        defaultLogger: CTLogger
    ): OkHttpClient {
        val trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(null as KeyStore?)
        }.trustManagers.first { it is X509TrustManager } as X509TrustManager

        val wrappedTrustManager = certificateTransparencyTrustManager(trustManager) {
            excludeCommonNames.forEach { -it }
            includeCommonNames.forEach { +it }
            failOnError = isFailOnError
            logger = defaultLogger
            diskCache = AndroidDiskCache(context)
        }

        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(wrappedTrustManager), SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, wrappedTrustManager)
            .build()
    }

    data class NetworkResponse(
        val isValidCertificate: Boolean,
        val certificateStatus: String,
        val trustedLogs: List<String>?,
        val networkStatus: String,
        val responseCode: Int,
        val responseMessage: String?,
        val errorMessage: String?,
        val errorType: String?
    )
}
