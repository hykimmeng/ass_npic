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
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.assmobile.data.model.Score
import com.example.assmobile.data.model.Student
import com.example.assmobile.ui.viewmodels.SchoolViewModel
import com.example.assmobile.ui.viewmodels.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentManagementScreen() {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Add student", "Enter scores")

    val activity = LocalContext.current as ComponentActivity
    val viewModel: SchoolViewModel = viewModel(viewModelStoreOwner = activity)
    val uiState by viewModel.uiState.collectAsState()
    val students by viewModel.students.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStudents()
    }

    LaunchedEffect(tabIndex) {
        viewModel.clearMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (tabIndex) {
            0 -> AddStudentTab(viewModel, uiState)
            else -> EnterScoresTab(viewModel, uiState, students)
        }
    }
}

@Composable
private fun AddStudentTab(viewModel: SchoolViewModel, uiState: UiState) {
    var studentId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var genderMale by remember { mutableStateOf(true) }
    var dob by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Student fields match the `students` table (ID, name, gender, DOB, class, phone, address).",
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
                RadioButton(
                    selected = genderMale,
                    onClick = { genderMale = true }
                )
                Text("Male")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !genderMale,
                    onClick = { genderMale = false }
                )
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

        MessageBlock(uiState)

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
    }
}

@Composable
private fun EnterScoresTab(
    viewModel: SchoolViewModel,
    uiState: UiState,
    students: List<Student>
) {
    var studentId by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var score by remember { mutableStateOf("") }
    var semester by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Scores match the `scores` table (student_id, subject, score, semester, year).",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (students.isNotEmpty()) {
            Text(
                "Known IDs: ${students.joinToString { it.studentId }}",
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

        MessageBlock(uiState)

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
    }
}

@Composable
private fun MessageBlock(uiState: UiState) {
    when (uiState) {
        is UiState.Error -> Text(uiState.message, color = MaterialTheme.colorScheme.error)
        is UiState.Success -> Text(uiState.message, color = MaterialTheme.colorScheme.primary)
        else -> {}
    }
}
