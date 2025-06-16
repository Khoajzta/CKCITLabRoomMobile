package com.example.ckcitlabroom.components

import InfoRow
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.*
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.LopHocViewModel

@Composable
fun CardLopHoc(
    lopHoc: LopHoc,
    navController: NavHostController,
    lopHocViewModel: LopHocViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val (statusColor, statusText, statusIcon) = when (lopHoc.TrangThai) {
        1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
        0 -> Triple(Color(0xFFF44336), "Không hoạt động", Lucide.CircleX)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tiêu đề
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mã lớp: ${lopHoc.MaLopHoc}", color = Color(0xFF1B8DDE), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            HorizontalDivider(Modifier.padding(vertical = 8.dp), 2.dp, Color(0xFFDDDDDD))

            InfoRow(icon = Lucide.School, label = "Tên Lớp", value = lopHoc.TenLopHoc)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái:", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(Modifier.width(6.dp))
                Text(statusText, color = statusColor, fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            navController.navigate(NavRoute.EDITLOPHOC.route + "?malop=${lopHoc.MaLopHoc}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE))
                    ) {
                        Icon(Lucide.PenLine, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Chỉnh Sửa", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                    ) {
                        Icon(Lucide.Activity, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Cập Nhật Trạng Thái", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                    ) {
                        Icon(Lucide.Trash2, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Xoá Lớp", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    containerColor = Color.White,
                    title = { Text("Cập nhật trạng thái", fontWeight = FontWeight.Bold, color = Color(0xFF1B8DDE)) },
                    text = { Text("Lớp: ${lopHoc.TenLopHoc}", fontSize = 16.sp, color = Color.Black) },
                    confirmButton = {
                        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    lopHocViewModel.updateLopHoc(lopHoc.copy(TrangThai = 1))
                                    showDialog = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                            ) {
                                Text("Hoạt Động", color = Color.White)
                            }
                            Button(
                                onClick = {
                                    lopHocViewModel.updateLopHoc(lopHoc.copy(TrangThai = 0))
                                    showDialog = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFFF44336))
                            ) {
                                Text("Không Hoạt Động", color = Color.White)
                            }
                        }
                    }
                )
            }

            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    containerColor = Color.White,
                    title = { Text("Xác nhận", fontWeight = FontWeight.Bold) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Lucide.ShieldQuestion, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Bạn có chắc chắn muốn xoá lớp này không?", fontSize = 16.sp)
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                lopHocViewModel.deleteLopHoc(lopHoc.MaLopHoc)
                                showConfirmDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Xoá", color = Color.White)
                        }
                    },
                    dismissButton = {
                        OutlinedButton(onClick = { showConfirmDialog = false }) {
                            Text("Huỷ", color = Color.Black)
                        }
                    }
                )
            }
        }
    }
}
