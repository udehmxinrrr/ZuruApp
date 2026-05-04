package com.udeh.zuru.ui.screens.tourers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.udeh.zuru.navigation.*
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.launch
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.udeh.zuru.R


// ── 1. Data model ─────────────────────────────────────────────────────────────

data class TourCompany(
    val name: String,
    val imageRes: Int,      // ← add this
    val rating: Float,
    val reviewCount: Int,
    val reviews: List<String>,
    val specialty: String
)

val tourCompanies = listOf(
    TourCompany("Abercrombie & Kent Kenya", R.drawable.abercrombie, 4.9f, 3200, listOf("Absolutely world-class service", "Our guide knew every animal by name"), "Luxury safaris"),
    TourCompany("Gamewatchers Safaris", R.drawable.gamewatchers, 4.8f, 2100, listOf("Best value luxury safari", "Superb camp locations in the Mara"), "Eco-friendly safaris"),
    TourCompany("Micato Safaris", R.drawable.micato, 4.9f, 1800, listOf("Once-in-a-lifetime experience", "Flawless organisation from start to finish"), "Ultra-luxury safaris"),
    TourCompany("Basecamp Explorer", R.drawable.basecamp, 4.8f, 1100, listOf("Deeply committed to conservation", "Authentic Maasai cultural experiences"), "Community safaris"),
    TourCompany("Axis Africa Safari", R.drawable.axisafrica, 4.6f, 980, listOf("Thrilling white-water rafting add-ons", "Adventure and wildlife perfectly combined"), "Adventure safaris"),
    TourCompany("Kichwa Tembo Safaris", R.drawable.kichwatembo, 4.7f, 870, listOf("Prime location in the Mara Triangle", "Incredible big cat sightings daily"), "Wildlife safaris"),
    TourCompany("Offbeat Safaris", R.drawable.offbeat, 4.8f, 760, listOf("Walking and horseback safaris are superb", "Small groups make it very personal"), "Walking safaris"),
    TourCompany("Cheli & Peacock", R.drawable.cheliandpeacock, 4.9f, 690, listOf("The most knowledgeable guides we've had", "Remote camps with zero crowds"), "Exclusive safaris"),
    TourCompany("African Horizons", R.drawable.africanhorizons, 4.6f, 640, listOf("Great mid-range option", "Very professional and punctual team"), "Mid-range safaris"),
    TourCompany("Serian Safaris", R.drawable.seriansafaris, 4.9f, 580, listOf("Intimate camp experience", "Night game drives were magical"), "Boutique safaris"),
    TourCompany("Let's Go Travel Kenya", R.drawable.letsgotravel, 4.5f, 1900, listOf("Excellent budget packages", "Good for solo travellers"), "Budget safaris"),
    TourCompany("Origins Safaris", R.drawable.originsafaris, 4.7f, 420, listOf("Hidden gem — highly underrated", "Private conservancy access is unbeatable"), "Conservancy safaris"),
    TourCompany("Porini Safari Camps", R.drawable.porinisafaris, 4.8f, 510, listOf("Fantastic community-run camps", "Very sustainable approach to tourism"), "Community safaris"),
    TourCompany("Expedition Maasai Safaris", R.drawable.expeditionmaasai, 4.8f, 1300, listOf("Legendary name in the Mara", "Classic safari atmosphere, perfectly executed"), "Classic safaris"),
    TourCompany("Aanika Karibu Safaris", R.drawable.aanikakaribu, 4.9f, 390, listOf("Most exclusive safari we've done", "Three camps across the Mara ecosystem"), "Luxury mobile safaris"),
    TourCompany("Natural World Kenya Safaris", R.drawable.naturalworld, 4.6f, 720, listOf("Great for families", "Guides are very patient with children"), "Family safaris"),
    TourCompany("Aardvark Safaris", R.drawable.aardvarksafaris, 4.7f, 450, listOf("Mountain and safari combo was brilliant", "Mount Kenya trek was superbly guided"), "Mountain & safari combos"),
    TourCompany("Grayton Safaris", R.drawable.graytonsafaris, 4.9f, 330, listOf("The most remote and wild camps in Kenya", "Truly off the beaten path"), "Remote wilderness safaris"),
    TourCompany("Spirit of Kenya", R.drawable.spiritofkenya, 4.6f, 280, listOf("Excellent photography safaris", "Guide understood exactly what shots we needed"), "Photography safaris")
)


// ── 2. Star rating row ────────────────────────────────────────────────────────

@Composable
fun TourStarRow(rating: Float, reviewCount: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating.toInt()) Color(0xFFFFC107) else Color(0xFFE0E0E0),
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(Modifier.width(6.dp))
        Text(text = "$rating", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = "  ($reviewCount reviews)", fontSize = 12.sp, color = zurublue)
    }
}


// ── 3. Tour company card ──────────────────────────────────────────────────────

@Composable
fun TourCompanyCard(company: TourCompany, onWhereToClick: () -> Unit) {
    val isDark = isSystemInDarkTheme()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, zurublue, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color.Black else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

            // ── Company name ──────────────────────────────────────────────────
            Text(
                text = company.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(4.dp))

            // ── Specialty badge ───────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(zurublue.copy(alpha = 0.1f))
                    .border(1.dp, zurublue.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 3.dp)
            ) {
                Text(text = company.specialty, fontSize = 11.sp, color = zurublue, fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(12.dp))

            // ── Vehicle image placeholder ─────────────────────────────────────
            // Replace Box with an Image composable once you have vehicle drawables
            // ── Vehicle image ─────────────────────────────────────────────────────
            Image(
                painter = painterResource(id = company.imageRes),
                contentDescription = "${company.name} vehicle",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(14.dp))
            )

            Spacer(Modifier.height(12.dp))

            // ── Star rating ───────────────────────────────────────────────────
            TourStarRow(rating = company.rating, reviewCount = company.reviewCount)

            Spacer(Modifier.height(12.dp))

            // ── Reviews ───────────────────────────────────────────────────────
            company.reviews.forEach { review ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isDark) Color(0xFF1A1A1A) else Color(0xFFF5F7FF))
                        .border(1.dp, zurublue.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = zurublue, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(text = "\"$review\"", fontSize = 12.sp, lineHeight = 18.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
                }
            }

            Spacer(Modifier.height(14.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

            Spacer(Modifier.height(12.dp))

            // ── Where to button ───────────────────────────────────────────────
            Button(
                onClick = onWhereToClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = zurublue)
            ) {
                Text("Where to", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }
    }
}


// ── 4. Tourers Screen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourersScreen(navController: NavController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                NavigationDrawerItem(icon = { Icon(Icons.Default.Search, null, tint = zurublue) }, label = { Text("Discover", color = zurublue) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_DISCOVER) })
                NavigationDrawerItem(icon = { Icon(Icons.Default.Close, null, tint = Color.Red) }, label = { Text("Logout", color = Color.Red) }, selected = false, onClick = { scope.launch { drawerState.close() }; navController.navigate(ROUT_LOGIN) })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tourers", color = zurublue, fontSize = 30.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp)) },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = zurublue)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                item {
                    Text(
                        text = "Kenya's top safari operators, handpicked for you",
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                items(tourCompanies) { company ->
                    TourCompanyCard(
                        company = company,
                        onWhereToClick = { navController.navigate(ROUT_TOURSPAYMENT) }
                    )
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TourersScreenPreview() {
    TourersScreen(rememberNavController())
}