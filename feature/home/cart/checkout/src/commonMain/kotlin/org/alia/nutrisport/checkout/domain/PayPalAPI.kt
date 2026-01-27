package org.alia.nutrisport.checkout.domain

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.alia.nutrisport.shared.ConstantKeys.PAYPAL_AUTH_ENDPOINT
import org.alia.nutrisport.shared.ConstantKeys.PAYPAL_AUTH_KEY
import org.alia.nutrisport.shared.ConstantKeys.PAYPAL_CHECKOUT_ENDPOINT
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class PayPalAPI {

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken.asStateFlow()

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )
        }
    }

    suspend fun fetchAccessToken(
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val authKey = PAYPAL_AUTH_KEY.encodeBase64()
            val response = client.post(urlString = PAYPAL_AUTH_ENDPOINT) {
                headers {
                    append(HttpHeaders.Authorization, "Basic $authKey")
                    append(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                }
                setBody("grant_type=client_credentials")
            }
            if (response.status == HttpStatusCode.OK) {
                val tokenResponse = response.body<PaypalTokenResponse>()
                _accessToken.value = tokenResponse.accessToken
                onSuccess(tokenResponse.accessToken)
            } else {
                onError("Error while fetching access token ${response.status} - ${response.bodyAsText()}")
            }
        } catch (e: Exception) {
            onError("Error while fetching access token: ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun beginCheckout(
        amount: Amount,
        fullName: String,
        shippingAddress: ShippingAddress,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        if (_accessToken.value.isEmpty()) {
            onError("Error while starting the checkout: Access Token is empty.")
            return
        }
        
        val uniqueId = Uuid.random().toHexString()
        val orderRequest = OrderRequest(
            purchaseUnits = listOf(
                PurchaseUnit(
                    referenceId = uniqueId,
                    amount = amount,
                    shipping = Shipping(
                        name = Name(fullName = fullName),
                        address = shippingAddress,
                    )
                )
            ),
        )
        val response = client.post(urlString = PAYPAL_CHECKOUT_ENDPOINT) {
            headers {
                append(HttpHeaders.Authorization, "Bearer ${_accessToken.value}")
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                append("PayPal-Request-Id", uniqueId)
            }
            setBody(orderRequest)
        }
        if (response.status == HttpStatusCode.OK) {
            val orderResponse = response.body<OrderResponse>()
            val payerLink = orderResponse.links.firstOrNull { it.rel == "payer-action" }?.href ?: ""
            onSuccess()
        } else {
            onError("Error while starting the checkout: ${response.status} - ${response.bodyAsText()}")
        }
    }
}