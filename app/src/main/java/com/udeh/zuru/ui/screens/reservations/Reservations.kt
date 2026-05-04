package com.udeh.zuru.ui.screens.reservations

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.screens.hotels.Hotel
import com.udeh.zuru.ui.screens.hotels.hotelList
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.launch

// ── Top-level helpers ─────────────────────────────────────────────────────────

private fun Int.formatKes() = "KES ${"%,d".format(this)}"

// ── Data model ────────────────────────────────────────────────────────────────

data class PaymentOption(
    val id: String,
    val label: String,
    val subtitle: String,
    val iconVector: androidx.compose.ui.graphics.vector.ImageVector,
    val accentColor: Color
)

// ── Section header ────────────────────────────────────────────────────────────

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    )
}

// ── Price row ─────────────────────────────────────────────────────────────────

@Composable
private fun PriceRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (isBold) 15.sp else 13.sp,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Normal,
            color = if (isBold) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = if (isBold) 15.sp else 13.sp,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Medium,
            color = valueColor
        )
    }
}

// ── Star rating row ───────────────────────────────────────────────────────────

@Composable
private fun StarRatingRow(rating: Float, reviewCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val fullStars = rating.toInt()
        val hasHalf = (rating - fullStars) >= 0.5f
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = when {
                    index < fullStars -> Color(0xFFFFC107)
                    index == fullStars && hasHalf -> Color(0xFFFFE082)
                    else -> Color(0xFFE0E0E0)
                },
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(text = "$rating", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = "  ($reviewCount reviews)", fontSize = 12.sp, color = zurublue)
    }
}

// ── Guest counter ─────────────────────────────────────────────────────────────

@Composable
private fun GuestCounter(
    count: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Adults", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(
                text = "Ages 13+",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onDecrement,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (count > 1) zurublue.copy(alpha = 0.12f) else Color.Transparent)
            ) {
                Text(text= "-")
            }
            Text(
                text = "$count",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(36.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = onIncrement,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(zurublue.copy(alpha = 0.12f))
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = zurublue,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// ── Price breakdown card ──────────────────────────────────────────────────────

@Composable
private fun PriceBreakdownCard(pricePerNight: Int, nights: Int, guests: Int) {
    val isDark = isSystemInDarkTheme()
    val stayTotal = pricePerNight * nights * guests
    val taxes = (stayTotal * 0.16).toInt()
    val serviceFee = 2500 * guests
    val total = stayTotal + taxes + serviceFee

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDark) Color(0xFF1A1A1A) else Color(0xFFF5F7FF))
            .border(1.dp, zurublue.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        PriceRow("Base Rate (per person/night)", pricePerNight.formatKes())
        PriceRow("Nightly total ($guests guests)", (pricePerNight * guests).formatKes())
        PriceRow("Stay subtotal ($nights nights)", stayTotal.formatKes())
        PriceRow("VAT (16%)", taxes.formatKes())
        PriceRow("Service fee (KES 2,500/guest)", serviceFee.formatKes())
        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = zurublue.copy(alpha = 0.2f))
        PriceRow("Final Total", total.formatKes(), isBold = true, valueColor = zurublue)
    }
}

// ── Payment tile ──────────────────────────────────────────────────────────────

@Composable
private fun PaymentTile(option: PaymentOption, selected: Boolean, onClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()
    val bgColor by animateColorAsState(
        targetValue = if (selected) option.accentColor.copy(alpha = 0.1f)
        else if (isDark) Color(0xFF1A1A1A) else Color(0xFFF5F7FF),
        label = "paymentBg"
    )
    val borderColor by animateColorAsState(
        targetValue = if (selected) option.accentColor else option.accentColor.copy(alpha = 0.25f),
        label = "paymentBorder"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(option.accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(option.iconVector, contentDescription = null, tint = option.accentColor, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = option.label, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(text = option.subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
        }
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = option.accentColor)
        )
    }
}

// ── Main Screen ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(navController: NavController, hotel: Hotel = hotelList[0]) {

    val isDark = isSystemInDarkTheme()

    val pricePerNight = hotel.pricePerNight
        .replace("KES ", "")
        .replace(",", "")
        .trim()
        .toIntOrNull() ?: 0

    var guests by remember { mutableStateOf(1) }
    var nights by remember { mutableStateOf(3) }
    var selectedPayment by remember { mutableStateOf("mpesa") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val paymentOptions = listOf(
        PaymentOption("mpesa", "M-Pesa", "Pay via Safaricom M-Pesa", Icons.Default.Phone, Color(0xFF4CAF50)),
        PaymentOption("visa", "Visa / Mastercard", "Debit or credit card", Icons.Default.PlayArrow, Color(0xFF1565C0)),
        PaymentOption("paypal", "PayPal", "Pay via your PayPal account", Icons.Default.AccountCircle, Color(0xFF0288D1)),
        PaymentOption("cash", "Pay at Hotel", "Cash on arrival", Icons.Default.Home, Color(0xFFFF8F00))
    )

    val currentSubtotal = pricePerNight * nights * guests
    val currentTaxes = (currentSubtotal * 0.16).toInt()
    val currentServiceFee = 2500 * guests
    val currentTotal = currentSubtotal + currentTaxes + currentServiceFee

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = zurublue, modifier = Modifier.size(32.dp)) },
            title = { Text("Reservation Confirmed!", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Your stay at ${hotel.name} for $nights nights " +
                            "($guests guest${if (guests != 1) "s" else ""}) has been reserved.\n\n" +
                            "Total Amount Paid: ${currentTotal.formatKes()}\n\n" +
                            "A confirmation will be sent to your registered contact.",
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
                TextButton(onClick = { showConfirmDialog = false }) { Text("Stay Here") }
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Zuru",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = zurublue,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = zurublue) },
                    label = { Text("Profile", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_PROFILE) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = zurublue) },
                    label = { Text("Home", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_HOME) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = null, tint = zurublue) },
                    label = { Text("Tour with Zuru", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURWITHZURU) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = zurublue) },
                    label = { Text("Maps", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_MAPS) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null, tint = zurublue) },
                    label = { Text("Tourers", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_TOURERS) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null, tint = zurublue) },
                    label = { Text("Discover", color = zurublue) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_DISCOVER) }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red) },
                    label = { Text("Logout", color = Color.Red) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_LOGIN) }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Reservations",
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
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = zurublue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            },
        ) { paddingValues ->

            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                // ── 1. Hotel name ─────────────────────────────────────────────
                item {
                    Text(
                        text = hotel.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }

                // ── 2. Hotel image ────────────────────────────────────────────
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                    ) {
                        Image(
                            painter = painterResource(id = hotel.imageRes),
                            contentDescription = hotel.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                                    )
                                )
                        )

                    }
                }

                // ── 3. Star rating ────────────────────────────────────────────
                item {
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                        StarRatingRow(rating = hotel.rating, reviewCount = hotel.reviewCount)
                    }
                }

                // ── 4. Dynamic Pricing Summary ────────────────────────────────────────
                item {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(zurublue.copy(alpha = 0.08f))
                            .border(1.dp, zurublue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Nightly ($guests guests)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            Text(text = (pricePerNight * guests).formatKes(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = zurublue)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Per Guest ($nights nights)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            Text(text = (pricePerNight * nights).formatKes(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = zurublue)
                        }
                    }
                }

                // ── 5. Reviews ────────────────────────────────────────────────
                item {
                    Spacer(Modifier.height(20.dp))
                    SectionHeader("Guest Reviews")
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDark) Color(0xFF1A1A1A) else Color(0xFFF5F7FF))
                            .border(1.dp, zurublue.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "${hotel.rating}", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = zurublue)
                            Spacer(Modifier.width(16.dp))
                            Column {
                                StarRatingRow(rating = hotel.rating, reviewCount = hotel.reviewCount)
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "Based on ${hotel.reviewCount} verified stays",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                        Spacer(Modifier.height(14.dp))
                        HorizontalDivider(color = zurublue.copy(alpha = 0.1f))
                        Spacer(Modifier.height(14.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = zurublue, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = hotel.standout,
                                fontSize = 13.sp,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // ── 6. Nights selector ────────────────────────────────────────
                item {
                    Spacer(Modifier.height(20.dp))
                    SectionHeader("Number of Nights")
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = { if (nights > 1) nights-- }, enabled = nights > 1) {
                            Text("− Night", color = if (nights > 1) zurublue else Color.Gray)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text("$nights nights", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Spacer(Modifier.width(8.dp))
                        TextButton(onClick = { nights++ }) {
                            Text("+ Night", color = zurublue)
                        }
                    }
                }

                // ── 7. Guests ─────────────────────────────────────────────────
                item {
                    Spacer(Modifier.height(4.dp))
                    SectionHeader("Guests")
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isDark) Color(0xFF1A1A1A) else Color(0xFFF5F7FF))
                            .border(1.dp, zurublue.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                            .padding(vertical = 8.dp)
                    ) {
                        GuestCounter(
                            count = guests,
                            onDecrement = { if (guests > 1) guests-- },
                            onIncrement = { if (guests < 10) guests++ }
                        )
                    }
                }

                // ── 8. Price breakdown + Make Reservation button ──────────────────────
                item {
                    Spacer(Modifier.height(20.dp))
                    SectionHeader("Price Breakdown")
                    PriceBreakdownCard(pricePerNight = pricePerNight, nights = nights, guests = guests)
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { navController.navigate(ROUT_PAYMENT) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = zurublue)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text("Make Reservation", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(Modifier.height(16.dp))
                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReservationsScreenPreview() {
    ReservationsScreen(rememberNavController())
}