import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun RequestPermissionsOnFirstLaunch() {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    val notificationPermission = Manifest.permission.POST_NOTIFICATIONS

    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val isFirstLaunch = remember {
        mutableStateOf(sharedPreferences.getBoolean("first_launch", true))
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.d("Permission", "Camera permission granted: $granted")
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        Log.d("Permission", "Notification permission granted: $granted")
    }

    var showNotificationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isFirstLaunch.value) {
            sharedPreferences.edit().putBoolean("first_launch", false).apply()

            // Xin quyền CAMERA
            cameraPermissionLauncher.launch(cameraPermission)

            // Android 13+ mới xin POST_NOTIFICATIONS
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(notificationPermission)
            }

            // Nếu thông báo đang tắt, hỏi người dùng
            val notificationManager = NotificationManagerCompat.from(context)
            if (!notificationManager.areNotificationsEnabled()) {
                showNotificationDialog = true
            }
        }
    }

    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            tonalElevation = 8.dp,
            title = {
                Text(
                    text = "Bật thông báo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF1B8DDE)
                )
            },
            text = {
                Text(
                    text = "Ứng dụng cần quyền gửi thông báo. Bạn có muốn bật không?",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showNotificationDialog = false
                        try {
                            val intent = Intent().apply {
                                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            }
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                Log.w("Permission", "Không mở được cài đặt thông báo")
                            }
                        } catch (e: Exception) {
                            Log.e("Permission", "Lỗi khi mở cài đặt thông báo", e)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Đồng ý", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showNotificationDialog = false }
                ) {
                    Text("Hủy", color = Color.Gray)
                }
            }
        )

    }
}



