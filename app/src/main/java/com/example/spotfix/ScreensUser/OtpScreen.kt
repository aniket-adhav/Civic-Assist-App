package com.example.spotfix.ScreensUser

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spotfix.ui.theme.Inter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    navController: NavController,
    phone: String,
    verificationId: String,
    onBack: () -> Unit = { navController.popBackStack() }
) {
    val context = LocalContext.current
    var otp by remember { mutableStateOf(List(6) { "" }) }
    var timeLeft by remember { mutableStateOf(60) } // 1 min timer
    var isVerifying by remember { mutableStateOf(false) }

    // Firebase auth
    val auth = remember { FirebaseAuth.getInstance() }

    // Focus requesters for OTP boxes
    val focusRequesters = List(6) { FocusRequester() }

    // Countdown timer
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    val primaryColor = Color(0xFFFF6A00) // Lighter orange
    val onSurfaceVariantColor = Color.Gray
    val onSurfaceColor = Color.White
    val backgroundColor = Color(0xFF121212)
    val containerColor = Color(0xFF2B2B2B)
    val darkOrange = Color(0xFFFF8C33) // Darker orange for gradient

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.size(45.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = onSurfaceColor
                )
            }
        }

        Spacer(modifier = Modifier.height(45.dp))

        Box(
            modifier = Modifier
                .size(85.dp)
                .background(
                    brush = Brush.linearGradient(listOf(darkOrange, primaryColor)),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock",
                tint = onSurfaceColor,
                modifier = Modifier.size(43.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Enter verification code",
            color = onSurfaceColor,
            fontSize = 26.sp,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We sent a 6-digit code to\n+91 $phone",
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceVariantColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            otp.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { input ->
                        if (input.length <= 1 && input.all { it.isDigit() }) {
                            otp = otp.toMutableList().also { it[index] = input }

                            if (input.isNotEmpty()) {
                                if (index < 5) {
                                    focusRequesters[index + 1].requestFocus()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .width(48.dp)
                        .height(56.dp)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyUp && event.key == Key.Backspace) {
                                if (value.isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                    return@onKeyEvent true
                                } else if (value.isNotEmpty()) {
                                    otp = otp.toMutableList().also { it[index] = "" }
                                    if (index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }
                                    return@onKeyEvent true
                                }
                            }
                            false
                        },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = containerColor,
                        cursorColor = primaryColor,
                        focusedTextColor = onSurfaceColor,
                        unfocusedTextColor = onSurfaceColor,
                        unfocusedContainerColor = containerColor,
                        focusedContainerColor = containerColor
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Code expires in: $formattedTime",
            style = MaterialTheme.typography.bodyMedium,
            color = onSurfaceColor,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val enteredOtp = otp.joinToString("")
                if (enteredOtp.length != 6) {
                    Toast.makeText(context, "Enter 6-digit OTP", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (verificationId.isBlank()) {
                    Toast.makeText(context, "Error: Verification ID missing", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }

                isVerifying = true

                val credential =
                    PhoneAuthProvider.getCredential(verificationId, enteredOtp)

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        isVerifying = false
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Phone verified successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        } else {
                            val e = task.exception
                            if (e is FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(
                                    context,
                                    "Invalid OTP",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Verification failed: ${e?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = onSurfaceColor
            ),
            enabled = !isVerifying
        ) {
            Text(
                text = if (isVerifying) "Verifying..." else "Verify",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Didn't receive the code? ",
                color = onSurfaceVariantColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                fontFamily = Inter
            )
            Text(
                text = "Resend",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = primaryColor,
                modifier = Modifier.clickable {
                    // 🔁 You can implement full resend here using PhoneAuthProvider again
                    timeLeft = 60
                    Toast.makeText(context, "OTP Sent", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
