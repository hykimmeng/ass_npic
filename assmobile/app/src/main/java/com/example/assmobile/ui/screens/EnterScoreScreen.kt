package com.example.assmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assmobile.data.model.Score
import com.example.assmobile.ui.viewmodels.SchoolViewModel
import com.example.assmobile.ui.viewmodels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterScoreScreen(onBack: () -> Unit) {
    var studentId by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }

    val viewModel: SchoolViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val students by viewModel.students.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enter Student Score") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add Student Grades",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("Student ID (e.g., 001)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text("Score (0 - 100)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (uiState is UiState.Error) {
                Text(text = (uiState as UiState.Error).message, color = MaterialTheme.colorScheme.error)
            } else if (uiState is UiState.Success) {
                Text(text = (uiState as UiState.Success).message, color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = {
                    val scoreVal = score.toDoubleOrNull() ?: 0.0
                    viewModel.enterScore(Score(studentId = studentId, subject = subject, score = scoreVal))
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is UiState.Loading
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
