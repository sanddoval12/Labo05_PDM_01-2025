package com.falconteam.laboratorio_5.utils

import android.util.Log
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class WSSClient(serverUri: URI, private val viewModel: BlogsitoViewModel) :
    WebSocketClient(serverUri) {
    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d("WssViewModel", "Conexión WebSocket abierta")
        viewModel.websocketConnectionOpened(false)
    }

    override fun onMessage(message: String?) {
        message?.let {
            println("Mensaje recibido: $it")
            viewModel.setMessageWSS(message)
        }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d("WssViewModel","Conexión WebSocket cerrada: $reason, $code, $remote")
        viewModel.websocketConnectionClosed(webSocketErrorDescriptions[code]?: "La aplicación a presentado un fallo reportarlo a support@infoking.win", webSocketErrorCodes[code]?: "Error interno en la aplicación", remote)
    }

    override fun onError(ex: Exception?) {
        Log.d("WssViewModel","Error en la conexión WebSocket: ${ex}")
        viewModel.websocketConnectionClosed(webSocketErrorDescriptions[-1],
            webSocketErrorCodes[-1]!!, false)
    }
}


val webSocketErrorCodes = mapOf(
    -1 to "Sin conexión a Internet",
    1000 to "Cierre Normal",
    1001 to "Saliendo",
    1002 to "Error de Protocolo",
    1003 to "Datos no Soportados",
    1005 to "No se Recibió Estado",
    1006 to "Cierre Anormal",
    1007 to "Datos de Carga de Trama Inválidos",
    1008 to "Violación de Política",
    1009 to "Mensaje Demasiado Grande",
    1010 to "Extensión Ausente",
    1011 to "Error Interno",
    1015 to "Fallo en el Handshake de TLS"
)

val webSocketErrorDescriptions = mapOf(
    -1 to "No se pudo establecer la conexión. Verifica tu conexión a Internet y vuelve a intentarlo.",
    1000 to "¡El servidor cerro la conexión!",
    1001 to "¡Ey, el servidor se está despidiendo! Vuelve más tarde.",
    1002 to "¡Ouch! Algo salió mal al hablar con el servidor. Mejor intenta más tarde.",
    1003 to "¡Oops! Los datos que mandé no son compatibles. Prueba de nuevo o habla con el soporte.",
    1005 to "¡Eh! No oímos nada del servidor. ¿Revisaste tu conexión?",
    1006 to "¡Vaya! La conexión se cortó inesperadamente. Inténtalo de nuevo.",
    1007 to "¡Oh no! Los datos que recibimos están raros. Prueba otra vez o pide ayuda.",
    1008 to "¡Oh cielos! Algo en el servidor no le gustó lo que hiciste. Contacta al soporte.",
    1009 to "¡Ups! El mensaje que recibimos es enorme. Manda algo más pequeño, por favor.",
    1010 to "¡Ey! Solicitaste algo que no entendimos. Habla con el soporte.",
    1011 to "¡Vaya lío! Algo se rompió en el servidor. Intenta más tarde o habla con el soporte.",
    1015 to "¡Uh oh! Hay problemas con la conexión segura. Revisa tu conexión o habla con el soporte."
)