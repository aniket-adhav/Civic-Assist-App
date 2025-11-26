package com.example.spotfix.ScreensAdmin

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.spotfix.R
import com.example.spotfix.ScreensUser.Screen
import com.example.spotfix.ScreensUser.backgroundColor
import com.example.spotfix.ui.theme.GreenTint1
import com.example.spotfix.ui.theme.Inter
import com.google.accompanist.systemuicontroller.rememberSystemUiController


// A darker orange shade for the gradient
val darkOrange =  Color(0xFFFF8C33)
@Composable
fun AdminLogin(navController: NavController) {
    val context = LocalContext.current
    val activity = context.findActivity() ?: return
    val systemUiController = rememberSystemUiController()


    val primaryColor = Color(0xFFFF6A00)
    val onSurfaceVariantColor = Color.Gray
    val onSurfaceColor = Color.White
    val DarkBackground = Color(0xFF121212)
    val containerColor = Color(0xFF2B2B2B)

    SideEffect { systemUiController.setSystemBarsColor(containerColor, darkIcons = false) }

    var adminid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }



    val fieldBg = Color(0xFF2B2B2B)
    val placeholder = Color(0xFF9AA39B)
    val accentGreen = GreenTint1
    val AccentGreenLight = Color(0xFF81C784)
    val GreenTint3 = Color(0xFF99E0B2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


            SideEffect {
                // Top status bar
                systemUiController.setStatusBarColor(
                    color = backgroundColor,
                    darkIcons = false
                )

                // Bottom navigation bar (system bar)
                systemUiController.setNavigationBarColor(
                    color = backgroundColor,
                    darkIcons = false
                )
            }


        Row(modifier = Modifier.fillMaxWidth().padding(top = 25.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .size(28.dp)
            )

            Spacer(modifier = Modifier.width(77.dp))

            Text(
                text = "Admin Login",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = Inter
            )
        }

        Spacer(modifier = Modifier.height(35.dp))


        Box(
            modifier = Modifier
                .size(85.dp)
                .background(
                    brush = Brush.linearGradient(listOf(GreenTint3, GreenTint1)),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.setting),
                contentDescription = "Lock",
                tint = onSurfaceColor,
                modifier = Modifier.size(46.dp)
            )
        }

        Spacer(modifier=Modifier.height(10.dp))
        Text("Admin Portal", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold, fontFamily = Inter)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Secure login for authorized government officials.",
            fontSize = 12.sp,
            color = placeholder,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )


        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(adminid
            , { adminid = it }, "Admin Id", Icons.Default.AdminPanelSettings, fieldBg, placeholder)
        Spacer(modifier = Modifier.height(16.dp))
        CustomPasswordField(password, { password = it }, "Password", passwordVisible, { passwordVisible = !passwordVisible }, fieldBg, placeholder)
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            Text(
                "Forgot Password?", color = accentGreen, fontSize = 14.sp,
                modifier = Modifier.clickable {
//                    navController.navigate("forgetpassword") {
//                        popUpTo("login") { inclusive = true }
//                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if(adminid=="pune137" && password=="Pune@1234"){
                    navController.navigate(Screen.Dashboard.route)
                }
                else{
                    Toast.makeText(context, "Invalid Admin Id or Password", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = accentGreen),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Secure Login", fontWeight = FontWeight.Bold, fontSize = 16.sp, fontFamily = Inter, color = Color.Black)
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    fieldBg: Color,
    placeholder: Color,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = placeholder) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = placeholder) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = fieldBg,
            unfocusedContainerColor = fieldBg,
            disabledContainerColor = fieldBg,
            errorContainerColor = fieldBg,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.Gray,
            errorTextColor = Color.Red,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red,
            cursorColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onVisibilityChange: () -> Unit,
    fieldBg: Color,
    placeholder: Color,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = placeholder) },
        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = placeholder) },
        trailingIcon = {
            IconButton(onClick = onVisibilityChange) {
                Icon(
                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = placeholder
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = fieldBg,
            unfocusedContainerColor = fieldBg,
            disabledContainerColor = fieldBg,
            errorContainerColor = fieldBg,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            disabledTextColor = Color.Gray,
            errorTextColor = Color.Red,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red,
            cursorColor = Color.White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}

// Helper to get Activity from Context
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
