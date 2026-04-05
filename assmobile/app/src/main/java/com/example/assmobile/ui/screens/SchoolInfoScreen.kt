package com.example.assmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assmobile.ui.viewmodels.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolInfoScreen(onBack: () -> Unit) {
    val viewModel: SchoolViewModel = viewModel()
    val schoolInfo by viewModel.schoolInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("School Information") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (schoolInfo != null) {
                InfoCard("School Name", schoolInfo!!.schoolName, Icons.Default.Business)
                InfoCard("Address", schoolInfo!!.address, Icons.Default.LocationOn)
                InfoCard("Phone", schoolInfo!!.phone, Icons.Default.Phone)
                InfoCard("Email", schoolInfo!!.email, Icons.Default.Email)
                InfoCard("Website", schoolInfo!!.website, Icons.Default.Language)
            } else {
                CircularProgressIndicator()
                Text("Loading information...", modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
