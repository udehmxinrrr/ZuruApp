package com.udeh.zuru.ui.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import com.udeh.zuru.navigation.ROUT_HOTELS
import com.udeh.zuru.ui.theme.zurublue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


// ── Calls the Anthropic API and returns the response text ─────────────────────

suspend fun fetchDestinationFacts(destinationName: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.anthropic.com/v1/messages")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("x-api-key", "YOUR_API_KEY_HERE") // ← replace with your key
            connection.setRequestProperty("anthropic-version", "2023-06-01")
            connection.doOutput = true

            // Build the request body
            val prompt = """
                Tell me 5 fun, surprising, and rarely known facts about $destinationName in Kenya.
                Make each fact engaging and educational. Format them as a numbered list.
                Keep the tone friendly and exciting, like a knowledgeable travel guide.
            """.trimIndent()

            val body = JSONObject().apply {
                put("model", "claude-sonnet-4-20250514")
                put("max_tokens", 1024)
                put("messages", JSONArray().put(
                    JSONObject().apply {
                        put("role", "user")
                        put("content", prompt)
                    }
                ))
            }

            connection.outputStream.write(body.toString().toByteArray())

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val json = JSONObject(response)
                json.getJSONArray("content")
                    .getJSONObject(0)
                    .getString("text")
            } else {
                "Could not load facts right now. Please try again later."
            }
        } catch (e: Exception) {
            "Something went wrong: ${e.message}"
        }
    }
}


// ── The screen ────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationFactsScreen(
    navController: NavController,
    destinationName: String
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Possible states the screen can be in
    var isLoading by remember { mutableStateOf(true) }
    var factsText by remember { mutableStateOf("") }
    var hasError by remember { mutableStateOf(false) }

    // Fetch facts as soon as the screen opens
    LaunchedEffect(destinationName) {
        isLoading = true
        hasError = false
        val result = fetchDestinationFacts(destinationName)
        factsText = result
        hasError = result.startsWith("Could not") || result.startsWith("Something went wrong")
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = destinationName,
                        color = zurublue,
                        fontSize = 22.sp,
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

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Header box ────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(zurublue.copy(alpha = 0.08f))
                    .border(1.dp, zurublue.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = zurublue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Rare & fun facts about $destinationName",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = zurublue
                )
            }

            // ── Loading / Error / Facts content ───────────────────────────────
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = zurublue)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Discovering facts about $destinationName…",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    }
                }

                hasError -> {
                    Text(
                        text = factsText,
                        fontSize = 14.sp,
                        color = Color.Red.copy(alpha = 0.8f),
                        lineHeight = 22.sp
                    )
                    // Retry button
                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                hasError = false
                                val result = fetchDestinationFacts(destinationName)
                                factsText = result
                                hasError = result.startsWith("Could not") || result.startsWith("Something went wrong")
                                isLoading = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = zurublue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Try Again", color = Color.White)
                    }
                }

                else -> {
                    // ── The AI response ───────────────────────────────────────
                    Text(
                        text = factsText,
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )
                }
            }

            // ── Explore Hotels button — shown once loading is done ────────────
            if (!isLoading) {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { navController.navigate(ROUT_HOTELS) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = zurublue)
                ) {
                    Text(
                        text = "Explore Hotels Near $destinationName",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DestinationFactsScreenPreview() {
    DestinationFactsScreen(rememberNavController(), "Maasai Mara")
}