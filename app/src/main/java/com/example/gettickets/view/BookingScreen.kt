package com.example.gettickets.view

import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.gettickets.ui.state.BookingUiState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gettickets.model.BookingResponse
import com.example.gettickets.model.Event
import com.example.gettickets.qrcode.generateQRCode
import com.example.gettickets.qrcode.saveQRCodeToStorage
import com.example.gettickets.ui.state.BookApiState
import com.example.gettickets.viewmodel.BookingViewModel
import com.example.gettickets.viewmodel.BookViewModel

import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalContext
import java.security.AccessController.getContext

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    eventId: Int,
    onNavigateBack: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel(),
    viewModelBook: BookViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val bookState by viewModelBook.bookState.collectAsState()
    var selectedTickets by remember { mutableStateOf(1) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(bookState) {
        when (bookState) {
            is BookApiState.Success, is BookApiState.Error -> {
                showDialog = true
                qrBitmap = generateQRCode(((bookState as BookApiState.Success).response).toString())
            }
            else -> {}
        }
    }

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                if (bookState is BookApiState.Success) {
                    onNavigateBack()
                }
            },
            title = {
                Text(
                    when (bookState) {
                        is BookApiState.Success -> "Booking Successful"
                        is BookApiState.Error -> "Booking Failed"
                        else -> "Loading..."
                    }
                )
            },
            text = {
                Column {
                    Text(
                        when (bookState) {
                            is BookApiState.Success -> "Your booking has been confirmed! Scan the QR code below."
                            is BookApiState.Error -> (bookState as BookApiState.Error).message
                            else -> ""
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (bookState is BookApiState.Success) {
                        val qrcode = (bookState as BookApiState.Success).response
                        val qrBitmap = remember { generateQRCode(qrcode.toString()) }

                        // Display the QR Code
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.size(200.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Button to Download QR Code
                        Button(onClick = {
                            val saved = saveQRCodeToStorage(context, qrBitmap, "QRCode_1.png")

                            if (saved) {
                                Toast.makeText(context, "QR Code saved successfully!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to save QR Code!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text("Download QR Code")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    if (bookState is BookApiState.Success) {
                        onNavigateBack()
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Tickets") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is BookingUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is BookingUiState.Success -> {
                    val event = (uiState as BookingUiState.Success).event
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Event Details
                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = event.description ?: "Maluzi",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Location: ${event.location}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "Date: ${event.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        // Contact Information
                        Text(
                            text = "Contact Information",
                            style = MaterialTheme.typography.titleLarge
                        )

                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        // Ticket Selection
                        Text(
                            text = "Select Tickets",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Number of Tickets",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { if (selectedTickets > 1) selectedTickets-- },
                                    enabled = selectedTickets > 1
                                ) {
                                    Text("-")
                                }

                                Text(
                                    text = selectedTickets.toString(),
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                IconButton(
                                    onClick = { if (selectedTickets < 10) selectedTickets++ },
                                    enabled = selectedTickets < 10
                                ) {
                                    Text("+")
                                }
                            }
                        }

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        // Price Summary
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Price per ticket",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text("$50.00")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total", fontWeight = FontWeight.Bold)
                                Text("$${50 * selectedTickets}.00", fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Book Button
                        Button(
                            onClick = {
                                viewModelBook.createBooking(
                                    eventId = eventId,
                                    fullName = fullName,
                                    email = email,
                                    numberOfTickets = selectedTickets,
                                    totalAmount = 50.0 * selectedTickets
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = fullName.isNotBlank() && email.isNotBlank() &&
                                    bookState !is BookApiState.Loading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            if (bookState is BookApiState.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(
                                    "Book Now Now",
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

                is BookingUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (uiState as BookingUiState.Error).message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadEvent(eventId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }


}