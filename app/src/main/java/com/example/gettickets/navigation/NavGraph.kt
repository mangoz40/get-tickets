package com.example.gettickets.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gettickets.view.EventListScreen
import com.example.gettickets.view.BookingScreen
import com.example.gettickets.view.QRScannerScreen

sealed class Screen(val route: String) {
    object EventList : Screen("eventList")
    object Booking : Screen("booking/{eventId}") {
        fun createRoute(eventId: Int) = "booking/$eventId"
    }
    object QRScanner : Screen("qrScanner") // Add this
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.EventList.route
    ) {
        composable(Screen.EventList.route) {
            EventListScreen(
                onEventClick = { event ->
                    navController.navigate(Screen.Booking.createRoute(event.id))
                },
                onScanClick = {
                    navController.navigate(Screen.QRScanner.route)
                }
            )
        }

        composable(
            route = Screen.Booking.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: return@composable
            BookingScreen(
                eventId = eventId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.QRScanner.route) {
            QRScannerScreen(
                onQRCodeScanned = { qrContent ->
                    // Handle the QR code content - you might want to parse it
                    // to get an event ID and navigate to booking
                    navController.popBackStack()
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }
}

