package com.example.assmobile.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assmobile.data.model.SchoolInfo
import com.example.assmobile.ui.viewmodels.SchoolViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolInfoScreen(
    onBack: (() -> Unit)? = null,
    showBack: Boolean = true,
) {
    val activity = LocalContext.current as ComponentActivity
    val viewModel: SchoolViewModel = viewModel(viewModelStoreOwner = activity)
    val schoolInfo by viewModel.schoolInfo.collectAsState()

    if (showBack && onBack != null) {
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
            SchoolInfoBody(
                modifier = Modifier.padding(padding),
                schoolInfo = schoolInfo
            )
        }
    } else {
        SchoolInfoBody(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            schoolInfo = schoolInfo
        )
    }
}

@Composable
private fun SchoolInfoBody(
    modifier: Modifier = Modifier,
    schoolInfo: SchoolInfo?,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.School,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.padding(bottom = 16.dp))

        if (schoolInfo != null) {
            InfoCard("School Name", schoolInfo.schoolName, Icons.Default.Business)
            InfoCard("Address", schoolInfo.address, Icons.Default.LocationOn)
            InfoCard("Phone", schoolInfo.phone, Icons.Default.Phone)
            InfoCard("Email", schoolInfo.email, Icons.Default.Email)
            InfoCard("Principal", schoolInfo.principal, Icons.Default.Person)
        } else {
            CircularProgressIndicator()
            Text("Loading information...", modifier = Modifier.padding(top = 16.dp))
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, icon: ImageVector) {
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
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
