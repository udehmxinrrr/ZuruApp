package com.udeh.zuru.ui.screens.tourpayment

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

data class TourPaymentMethod(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

val tourPaymentMethods = listOf(
    TourPaymentMethod("mpesa", "M-Pesa", "Pay instantly via Safaricom M-Pesa", Icons.Default.Phone, Color(0xFF4CAF50)),
    TourPaymentMethod("kcb", "KCB", "Pay with KCB", Icons.Default.AccountBox, Color(0xFF09570C)),
    TourPaymentMethod("absa", "Absa Bank", "Pay with Absa", Icons.Default.AccountBox, Color(0xFFE80E0E)),
    TourPaymentMethod("stanbic", "Stanbic Bank", "Pay with Stanbic Bank", Icons.Default.AccountBox, Color(0xFF1582D9)),
    TourPaymentMethod("cooperative", "Cooperative Bank", "Pay with Cooperative", Icons.Default.AccountBox, Color(0xFF0E7213)),
    TourPaymentMethod("equity", "Equity Bank", "Pay with Equity Bank", Icons.Default.AccountBox, Color(0xFF573506)),
    TourPaymentMethod("icealion", "ICEA Lion Bank", "Pay with ICEA Lion Bank", Icons.Default.AccountBox, Color(0xFF253EC5)),
    TourPaymentMethod("otherbank", "Pay with another Bank", "Direct transfer from your bank account", Icons.Default.AccountBox, Color(0xFF7B1FA2)),
    TourPaymentMethod("mastercard", "Mastercard Global", "Pay with Mastercard", Icons.Default.CheckCircle, Color(0xFFD9850D)),
    TourPaymentMethod("visa", "Visa", "Pay with Visa", Icons.Default.CheckCircle, Color(0xFF1565C0)),
    TourPaymentMethod("paypal", "PayPal", "Pay securely with your PayPal account", Icons.Default.AccountCircle, Color(0xFF0288D1)),
    TourPaymentMethod("cash", "Pay on Arrival", "Cash payment at the hotel / lodge", Icons.Default.Home, Color(0xFFFF8F00))
)


// ── 2. Single payment option tile ─────────────────────────────────────────────

@Composable
fun TourPaymentMethodTile(
    method: TourPaymentMethod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

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
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(method.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = method.icon, contentDescription = method.name, tint = method.color, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = method.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(text = method.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = method.color)
        )
    }
}


// ── 3. Tour Payment Screen ────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourPaymentScreen(navController: NavController) {

    val isDark = isSystemInDarkTheme()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedMethodId by remember { mutableStateOf("mpesa") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        val selectedMethod = tourPaymentMethods.first { it.id == selectedMethodId }
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = selectedMethod.color, modifier = Modifier.size(32.dp))
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
                TextButton(onClick = { showConfirmDialog = false }) { Text("Cancel") }
            }
        )
    }

    // ── Drawer wraps the Scaffold ─────────────────────────────────────────────
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                Text("Zuru", fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = zurublue, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(icon = { Icon(Icons.Default.Person, null, tint = zurublue) }, label = { Text("Profile", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_PROFILE) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Home, null, tint = zurublue) }, label = { Text("Home", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOME) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Home, null, tint = zurublue) }, label = { Text("Hotels", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOTELS) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Star, null, tint = zurublue) }, label = { Text("Tour with Zuru", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURWITHZURU) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.LocationOn, null, tint = zurublue) }, label = { Text("Maps", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_MAPS) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Person, null, tint = zurublue) }, label = { Text("Tourers", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURERS) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Search, null, tint = zurublue) }, label = { Text("Discover", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_DISCOVER) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Close, null, tint = Color.Red) }, label = { Text("Logout", color = Color.Red) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_LOGIN) })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Tour Payment", color = zurublue, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
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
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {

                // ── Header text ───────────────────────────────────────────────
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                        Text(text = "Choose Payment Method", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(4.dp))
                        Text(text = "Select how you'd like to complete your booking", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    }
                }

                // ── Payment method tiles ───────────────────────────────────────
                items(tourPaymentMethods.size) { index ->
                    val method = tourPaymentMethods[index]
                    TourPaymentMethodTile(
                        method = method,
                        isSelected = selectedMethodId == method.id,
                        onClick = { selectedMethodId = method.id }
                    )
                }

                // ── Buttons ───────────────────────────────────────────────────
                item {
                    Spacer(Modifier.height(140.dp))
                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 90.dp).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = paygreen)
                    ) {
                        Text(text = "Confirm Payment", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { navController.navigate(ROUT_HOME) },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 90.dp).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = zurublue)
                    ) {
                        Text(text = "Home", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TourPaymentScreenPreview() {
    TourPaymentScreen(rememberNavController())
}