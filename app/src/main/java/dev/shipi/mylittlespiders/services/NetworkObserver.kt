package dev.shipi.mylittlespiders.services

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean


class NetworkObserver constructor(
    private val context: Context,
) : DefaultLifecycleObserver {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private var connectivityManager: ConnectivityManager? = null
    private val validNetworks = HashSet<Network>()

    private lateinit var job: Job
    private lateinit var coroutineScope: CoroutineScope

    // State Holder: Indicating either the network is available or not-available
    private val _networkAvailable = MutableSharedFlow<NetworkState>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val networkAvailable = _networkAvailable.asSharedFlow()

    // ---> This variable can be accessed anytime to get the current state of the network
    private val _isConnected = AtomicBoolean(false)
    val isConnected: Boolean
        get() = _isConnected.get()


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        init()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        registerNetworkCallback(owner.lifecycle)
        checkValidNetworks()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        unregisterNetworkCallback()
    }

    private fun init() {
        // Initialize the connectivity manager instance
        connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun registerNetworkCallback(lifecycle: Lifecycle) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            // Observing the network should happen only when the life-cycle is in started state
            initCoroutine()
            initNetworkMonitoring()
        }
    }

    private fun unregisterNetworkCallback() {
        validNetworks.clear()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
        job.cancel()
    }

    /**
     * Co-Routine used to monitor the connectivity
     */
    private fun initCoroutine() {
        // Create a job instance
        job = Job()
        // Provide a co-routine scope
        coroutineScope = CoroutineScope(Dispatchers.Default + job)
    }

    private fun initNetworkMonitoring() {
        networkCallback = createNetworkCallback()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connectivityManager?.getNetworkCapabilities(network).also {
                if (it?.hasCapability(NET_CAPABILITY_INTERNET) == true) {
                    validNetworks.add(network)
                }
            }
            checkValidNetworks()
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    private fun checkValidNetworks() {
        coroutineScope.launch {
            _networkAvailable.emit(
                if (validNetworks.size > 0) {
                    _isConnected.set(true)
                    NetworkState.Available
                } else {
                    _isConnected.set(false)
                    NetworkState.Unavailable
                }
            )
        }
    }
}

sealed class NetworkState {
    abstract fun toBoolean(): Boolean

    object Unavailable : NetworkState() {
        override fun toBoolean() = false
    }

    object Available : NetworkState() {
        override fun toBoolean() = true
    }
}