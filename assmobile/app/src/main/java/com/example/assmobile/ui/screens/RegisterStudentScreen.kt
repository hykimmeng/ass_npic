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
import com.example.assmobile.data.model.Student
import com.example.assmobile.ui.viewmodels.SchoolViewModel
import com.example.assmobile.ui.viewmodels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterStudentScreen(onBack: () -> Unit) {
    var studentId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var dob by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") }

    val viewModel: SchoolViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register Student") },
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
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("Student ID") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Simplified Gender selection
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = gender == "Male", onClick = { gender = "Male" })
                Text("Male")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(selected = gender == "Female", onClick = { gender = "Female" })
                Text("Female")
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of Birth (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = className,
                onValueChange = { className = it },
                label = { Text("Class Name") },
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
                    viewModel.registerStudent(Student(studentId, name, gender, dob, className))
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is UiState.Loading
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Register Changes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
