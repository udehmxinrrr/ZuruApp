package com.udeh.zuru.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.theme.paygreen
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.launch


// ── 1. Payment method data — easy to add/remove options here ─────────────────

data class PaymentMethod(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

val paymentMethods = listOf(
    PaymentMethod(
        id = "M-Pesa",
        name = "M-Pesa",
        description = "Pay instantly via Safaricom M-Pesa",
        icon = Icons.Default.Phone,
        color = Color(0xFF4CAF50)
    ),
    PaymentMethod(
        id = "KCB",
        name = "KCB",
        description = "Pay with KCB",
        icon = Icons.Default.AccountBox,
        color = Color(0xFF09570C)
    ),
    PaymentMethod(
        id = "absa",
        name = "Absa",
        description = "Pay with Absa",
        icon = Icons.Default.AccountBox,
        color = Color(0xFFE80E0E)
    ),
    PaymentMethod(
        id = "stanbic",
        name = "Stanbic Bank",
        description = "Pay with Stanbic Bank",
        icon = Icons.Default.AccountBox,
        color = Color(0xFF1582D9)
    ),
    PaymentMethod(
        id = "cooperative",
        name = "Cooperative",
        description = "Pay with Cooperative Bank",
        icon = Icons.Default.AccountBox,
        color = Color(0xFF0E7213)
    ),
    PaymentMethod(
        id = "equity",
        name = "Equity Bank",
        description = "Pay with Equity Bank",
        icon = Icons.Default.AccountBox,
        color = Color(0xFF573506)
    ),
    PaymentMethod(
        id = "icealionbank",
        name = "ICEA Lion Bank",
        description = "Pay with ICEA Lion Bank",
        icon = Icons.Default.AccountBox,
        color = Color(0xFF253EC5)
    ),
    PaymentMethod(
        id = "otherbanks",
        name = "Other Banks",
        description = "Direct transfer from your bank account",
        icon = Icons.Default.AccountBox,
        color = Color(0xFF7B1FA2)
    ),
    PaymentMethod(
        id = "mastercard",
        name = "Masterard",
        description = "Pay with Mastercard",
        icon = Icons.Default.CheckCircle,
        color = Color(0xFFD9850D)
    ),
    PaymentMethod(
        id = "visa",
        name = "Visa",
        description = "Pay with Visa",
        icon = Icons.Default.CheckCircle,
        color = Color(0xFF1565C0)
    ),
    PaymentMethod(
        id = "paypal",
        name = "PayPal",
        description = "Pay securely with your PayPal account",
        icon = Icons.Default.AccountCircle,
        color = Color(0xFF0288D1)
    ),
    PaymentMethod(
        id = "cash",
        name = "Pay on Arrival",
        description = "Cash payment at the hotel / lodge",
        icon = Icons.Default.Home,
        color = Color(0xFFFF8F00)
    )
)


// ── 2. Single payment option tile ─────────────────────────────────────────────

@Composable
fun PaymentMethodTile(
    method: PaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    // Background and border animate based on selection
    val bgColor = if (isSelected) method.color.copy(alpha = 0.10f)
    else if (isDark) Color(0xFF1A1A1A) else Color(0xFFF5F7FF)
    val borderColor = if (isSelected) method.color else method.color.copy(alpha = 0.25f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Icon box
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(method.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = method.icon,
                contentDescription = method.name,
                tint = method.color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        // Name and description
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = method.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = method.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        // Selection indicator
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = method.color)
        )
    }
}


// ── 3. Payment Screen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController) {

    val isDark = isSystemInDarkTheme()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Tracks which payment method the user has selected
    var selectedMethodId by remember { mutableStateOf("mpesa") }

    // Controls the confirmation dialog
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Confirmation dialog
    if (showConfirmDialog) {
        val selectedMethod = paymentMethods.first { it.id == selectedMethodId }
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = selectedMethod.color,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { Text("Payment Confirmed!", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Your payment via ${selectedMethod.name} has been processed successfully.\n\n" +
                            "A receipt will be sent to your registered contact.",
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showConfirmDialog = false; navController.navigate(ROUT_HOME) },
                    colors = ButtonDefaults.buttonColors(containerColor = zurublue),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("Back to Home", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }


        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Hotel Payment",
                            color = zurublue,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = zurublue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            }
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {

                // ── Header text ───────────────────────────────────────────────
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                        Text(
                            text = "Choose Payment Method",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Select how you'd like to complete your booking",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }

                // ── Payment method tiles — one per option ─────────────────────
                items(paymentMethods.size) { index ->
                    val method = paymentMethods[index]
                    PaymentMethodTile(
                        method = method,
                        isSelected = selectedMethodId == method.id,
                        onClick = { selectedMethodId = method.id }
                    )
                }

                // ── Confirm Payment + Back to Home buttons ────────────────────────────────
                item {
                    Spacer(Modifier.height(140.dp))
                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = paygreen)
                    ) {
                        Text(
                            text = "Confirm Payment",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { navController.navigate(ROUT_HOME) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 90.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = zurublue)
                    ) {
                        Text(
                            text = "Home",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }



@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(rememberNavController())
}