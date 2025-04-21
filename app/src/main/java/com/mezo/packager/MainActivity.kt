package com.mezo.packager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent?.action == "RESET_PACKAGE_ACTION") {
            clearSavedPackage()
            Toast.makeText(this, "Package reset successfully", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val savedPackage = getSavedPackageName()
        if (savedPackage.isNotEmpty()) {
            openApp(savedPackage)
        } else {
            setContent {
                MyApp()
            }
        }
    }

    private fun savePackageName(packageName: String) {
        getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).edit {
            putString("savedPackageName", packageName)
            apply()
        }
    }

    private fun getSavedPackageName(): String {
        return getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            .getString("savedPackageName", "") ?: ""
    }

    private fun clearSavedPackage() {
        getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).edit {
            remove("savedPackageName")
            apply()
        }
    }

    private fun openApp(packageName: String) {
        try {
            packageManager.getLaunchIntentForPackage(packageName)?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(it)
                finish()
            } ?: showPackageError(packageName)
        } catch (e: Exception) {
            showPackageError(packageName)
        }
    }

    private fun showPackageError(packageName: String) {
        Toast.makeText(
            this,
            "Application '$packageName' not found",
            Toast.LENGTH_LONG
        ).show()
        clearSavedPackage()
    }

    @Composable
    fun MyApp() {
        val context = LocalContext.current
        val focusRequester = remember { FocusRequester() }
        var packageText = rememberSaveable { mutableStateOf("") }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BasicText(
                text = "Packager",
                style = TextStyle(color = Color.White, fontSize = 24.sp),
                modifier = Modifier.padding(16.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0f0f0f))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                if (packageText.value.isEmpty()) {
                    BasicText(
                        text = "Enter package name...",
                        style = TextStyle(color = Color.Gray, fontSize = 16.sp)
                    )
                }

                BasicTextField(
                    value = packageText.value,
                    onValueChange = { packageText.value = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .focusable(),
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .clickable {
                        if (packageText.value.isNotEmpty()) {
                            savePackageName(packageText.value)
                            openApp(packageText.value)
                        } else {
                            Toast.makeText(context, "Please enter a package name", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                BasicText(
                    text = "Save",
                    style = TextStyle(color = Color.Black, fontSize = 16.sp)
                )
            }
        }
    }
}
