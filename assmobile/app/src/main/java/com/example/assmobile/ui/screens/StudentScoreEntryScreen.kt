package com.example.assmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assmobile.data.model.Score
import com.example.assmobile.ui.components.BrandedTopBar
import com.example.assmobile.ui.viewmodels.SchoolViewModel
import com.example.assmobile.ui.viewmodels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScoreEntryScreen(onBack: () -> Unit) {
    val activity = LocalContext.current as ComponentActivity
    val viewModel: SchoolViewModel = viewModel(viewModelStoreOwner = activity)
    val uiState by viewModel.uiState.collectAsState()
    val students by viewModel.students.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStudents()
    }

    var studentId by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BrandedTopBar(title = "Student score entry", onBack = onBack)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Enter scores for a registered student ID.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (students.isNotEmpty()) {
                Text(
                    "Registered IDs: ${students.joinToString { it.studentId }}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("Student ID *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("Subject *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text("Score *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = semester,
                onValueChange = { semester = it },
                label = { Text("Semester") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Year") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            when (uiState) {
                is UiState.Error -> Text(uiState.message, color = MaterialTheme.colorScheme.error)
                is UiState.Success -> Text(uiState.message, color = MaterialTheme.colorScheme.primary)
                else -> {}
            }

            Button(
                onClick = {
                    val value = score.toDoubleOrNull() ?: 0.0
                    viewModel.enterScore(
                        Score(
                            studentId = studentId.trim(),
                            subject = subject.trim(),
                            score = value,
                            semester = semester.takeIf { it.isNotBlank() },
                            year = year.takeIf { it.isNotBlank() }
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is UiState.Loading && studentId.isNotBlank() && subject.isNotBlank() && score.isNotBlank()
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(24.dp)
                    )
                } else {
                    Text("Save score", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
