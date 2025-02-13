package com.example.gettickets.view

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gettickets.model.Event
import com.example.gettickets.viewmodel.BookingViewModel
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    eventId: Int,
    onNavigateBack: () -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTickets by remember { mutableStateOf(1) }

    LaunchedEffect(eventId) {
        viewModel.loadEvent(eventId)
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
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "Date: ${event.date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Divider()

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

                        Divider()

                        // Price Summary
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Price per ticket")
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
                            onClick = { /* Handle booking */ },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Book Now")
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
