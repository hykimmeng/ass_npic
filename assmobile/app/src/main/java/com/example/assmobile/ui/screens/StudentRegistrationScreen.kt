package com.example.assmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assmobile.data.model.Student
import com.example.assmobile.ui.components.BrandedTopBar
import com.example.assmobile.ui.viewmodels.SchoolViewModel
import com.example.assmobile.ui.viewmodels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentRegistrationScreen(onBack: () -> Unit) {
    val activity = LocalContext.current as ComponentActivity
    val viewModel: SchoolViewModel = viewModel(viewModelStoreOwner = activity)
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStudents()
    }

    var studentId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var genderMale by remember { mutableStateOf(true) }
    var dob by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BrandedTopBar(title = "Student registration", onBack = onBack)
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
                "Enter student details. Student ID must be unique.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text("Student ID *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full name *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Gender *", style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = genderMale, onClick = { genderMale = true })
                    Text("Male")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = !genderMale, onClick = { genderMale = false })
                    Text("Female")
                }
            }

            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("Date of birth (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = className,
                onValueChange = { className = it },
                label = { Text("Class") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )

            FormMessage(uiState)

            Button(
                onClick = {
                    val g = if (genderMale) "male" else "female"
                    viewModel.registerStudent(
                        Student(
                            studentId = studentId.trim(),
                            name = name.trim(),
                            gender = g,
                            dateOfBirth = dob.takeIf { it.isNotBlank() },
                            className = className.takeIf { it.isNotBlank() },
                            phone = phone.takeIf { it.isNotBlank() },
                            address = address.takeIf { it.isNotBlank() }
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is UiState.Loading && studentId.isNotBlank() && name.isNotBlank()
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(24.dp)
                    )
                } else {
                    Text("Save student", fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FormMessage(uiState: UiState) {
    when (uiState) {
        is UiState.Error -> Text(uiState.message, color = MaterialTheme.colorScheme.error)
        is UiState.Success -> Text(uiState.message, color = MaterialTheme.colorScheme.primary)
        else -> {}
    }
}
