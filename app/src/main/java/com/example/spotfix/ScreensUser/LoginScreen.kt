package com.example.spotfix.ScreensUser

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spotfix.R
import com.example.spotfix.ui.theme.Inter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as? Activity

    // ✅ Simple FirebaseAuth instance (no extra logic here)
    val auth = remember { FirebaseAuth.getInstance() }

    var isSendingOtp by remember { mutableStateOf(false) }

    // Colors
    val primaryColor = Color(0xFFFF6A00)
    val onSurfaceVariantColor = Color.Gray
    val onSurfaceColor = Color.White
    val backgroundColor = Color(0xFF121212)
    val containerColor = Color(0xFF2B2B2B)
    val darkOrange = Color(0xFFFF8C33)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .systemBarsPadding()
            .padding(top = 100.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setStatusBarColor(
                color = backgroundColor,
                darkIcons = false
            )
        }

        // 🔶 Icon
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
                painter = painterResource(R.drawable.enter),
                contentDescription = "Lock",
                tint = onSurfaceColor,
                modifier = Modifier.size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Let's Get Started",
            color = onSurfaceColor,
            fontSize = 35.sp,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your mobile number to sign in",
            fontSize = 14.sp,
            fontFamily = Inter,
            color = onSurfaceVariantColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            singleLine = true,
            leadingIcon = {
                Text(
                    text = "+91",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = onSurfaceVariantColor,
                    modifier = Modifier.padding(start = 4.dp)
                )
            },
            placeholder = {
                Text(
                    text = "Phone Number",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = onSurfaceVariantColor
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = onSurfaceColor
            ),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = containerColor,
                focusedBorderColor = primaryColor,
                cursorColor = primaryColor,
                focusedTextColor = onSurfaceColor,
                unfocusedTextColor = onSurfaceColor,
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Request OTP button
        Button(
            onClick = {
                if (phoneNumber.length != 10) {
                    Toast.makeText(
                        context,
                        "Enter valid 10 digit phone number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                if (activity == null) {
                    Toast.makeText(
                        context,
                        "Error: Activity not found",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                val fullPhone = "+91$phoneNumber"
                isSendingOtp = true

                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                        // Optional: auto sign-in if you want
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        isSendingOtp = false
                        Toast.makeText(
                            context,
                            "Verification failed: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationId, token)
                        isSendingOtp = false
                        Toast.makeText(
                            context,
                            "OTP sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // If you have Screen.OTP.createRoute():
                        // navController.navigate(Screen.OTP.createRoute(phoneNumber, verificationId))

                        navController.navigate("otp/$phoneNumber/$verificationId")
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(fullPhone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = onSurfaceColor
            ),
            enabled = !isSendingOtp
        ) {
            Text(
                text = if (isSendingOtp) "Sending..." else "Request OTP",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    fontFamily = Inter
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Are you an Admin?",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = Inter,
                color = onSurfaceVariantColor
            )
            Text(
                text = " Login here",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Inter,
                color = primaryColor,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.AdminLogin.route)
                    Toast.makeText(context, "Admin", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(230.dp))

        ClickableText(
            text = buildAnnotatedString {
                append("By continuing, you agree to our ")
                pushStringAnnotation("TERMS", "terms")
                withStyle(SpanStyle(color = primaryColor)) {
                    append("Terms of Service")
                }
                pop()
                append(" and ")
                pushStringAnnotation("PRIVACY", "privacy")
                withStyle(SpanStyle(color = primaryColor)) {
                    append("Privacy Policy")
                }
                pop()
                append(".")
            },
            style = MaterialTheme.typography.labelSmall.copy(
                color = onSurfaceVariantColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            ),
            onClick = { /* TODO */ }
        )
    }
}
