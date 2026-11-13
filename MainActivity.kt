package com.darkcontrol.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val Navy = Color(0xFF070D18)
private val Panel = Color(0xFF101B2D)
private val Panel2 = Color(0xFF14243A)
private val Neon = Color(0xFF21B7FF)
private val Muted = Color(0xFF91A6BD)
private val White = Color(0xFFF3F8FF)
private val Green = Color(0xFF35D39A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DarkControlApp() }
    }
}

private enum class Page { HOME, PAIR, DASHBOARD, CONSENT, PRANK }

@Composable
private fun DarkControlApp() {
    var page by remember { mutableStateOf(Page.HOME) }
    var mode by remember { mutableStateOf("Controller") }
    var code by remember { mutableStateOf("") }
    var paired by remember { mutableStateOf(false) }
    var consent by remember { mutableStateOf(false) }
    var prankRunning by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Neon, background = Navy, surface = Panel,
            onPrimary = Navy, onBackground = White, onSurface = White
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = Navy) {
            Column(Modifier.fillMaxSize().padding(20.dp)) {
                TopBar(onHome = { page = Page.HOME })
                Spacer(Modifier.height(22.dp))
                when (page) {
                    Page.HOME -> HomeScreen(
                        mode = mode,
                        onMode = { mode = it },
                        onPair = { page = Page.PAIR },
                        onDashboard = { page = Page.DASHBOARD },
                        onPrank = { page = Page.PRANK }
                    )
                    Page.PAIR -> PairScreen(
                        mode = mode, code = code, onCode = { code = it },
                        onGenerate = {
                            code = (100000..999999).random().toString()
                        },
                        onConnect = {
                            if (code.length == 6) {
                                paired = true
                                page = Page.DASHBOARD
                            }
                        }
                    )
                    Page.DASHBOARD -> DashboardScreen(
                        paired = paired,
                        onConsent = { page = Page.CONSENT },
                        onPair = { page = Page.PAIR }
                    )
                    Page.CONSENT -> ConsentScreen(
                        consent = consent,
                        onConsent = { consent = it },
                        onBack = { page = Page.DASHBOARD }
                    )
                    Page.PRANK -> PrankScreen(
                        running = prankRunning,
                        onToggle = { prankRunning = it }
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    "V1.0  •  CONSENT-FIRST DEVICE MANAGEMENT",
                    color = Muted, fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun TopBar(onHome: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = Panel2, shape = RoundedCornerShape(14.dp),
            modifier = Modifier.size(44.dp).border(1.dp, Neon.copy(alpha = .35f), RoundedCornerShape(14.dp))
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = Neon)
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("DARK CONTROL", color = White, fontWeight = FontWeight.Black, fontSize = 18.sp, letterSpacing = 1.4.sp)
            Text("SECURE DEVICE HUB", color = Neon, fontSize = 9.sp, letterSpacing = 2.sp)
        }
        IconButton(onClick = onHome) {
            Icon(Icons.Default.Home, contentDescription = "Home", tint = Muted)
        }
    }
}

@Composable
private fun HomeScreen(
    mode: String, onMode: (String) -> Unit,
    onPair: () -> Unit, onDashboard: () -> Unit, onPrank: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("تحكّم بذكاء.
وبموافقة واضحة.", color = White, fontSize = 29.sp, fontWeight = FontWeight.Bold, lineHeight = 36.sp)
        Text("إدارة أجهزة آمنة — كل اتصال ظاهر ويمكن إيقافه في أي وقت.", color = Muted, fontSize = 14.sp)
        CardPanel {
            Text("اختار وضع التطبيق", color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ModeChip("Controller", mode == "Controller") { onMode("Controller") }
                ModeChip("Managed Device", mode == "Managed Device") { onMode("Managed Device") }
            }
        }
        ActionCard(Icons.Default.Link, "ربط جهاز جديد", "إنشاء رمز اقتران أو إدخال رمز من جهاز آخر", onPair)
        ActionCard(Icons.Default.Dashboard, "لوحة الأجهزة", "عرض حالة الاتصال ومعلومات الجهاز التجريبية", onDashboard)
        ActionCard(Icons.Default.AutoAwesome, "Prank Mode", "محاكاة مرئية آمنة للمزاح — بدون وصول حقيقي", onPrank)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Green, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("الخصوصية أولاً • لا يوجد تشغيل خفي", color = Green, fontSize = 11.sp)
        }
    }
}

@Composable
private fun ModeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected, onClick = onClick,
        label = { Text(label, fontSize = 11.sp) },
        leadingIcon = if (selected) ({ Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp)) }) else null,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Neon.copy(alpha = .18f),
            selectedLabelColor = Neon
        )
    )
}

@Composable
private fun ActionCard(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick, colors = CardDefaults.cardColors(containerColor = Panel),
        shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = .06f), RoundedCornerShape(18.dp))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Neon, modifier = Modifier.size(27.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = White, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Muted, fontSize = 11.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Muted)
        }
    }
}

@Composable
private fun CardPanel(content: @Composable ColumnScope.() -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Panel), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), content = content)
    }
}

@Composable
private fun PairScreen(mode: String, code: String, onCode: (String) -> Unit, onGenerate: () -> Unit, onConnect: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("ربط آمن", color = White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("الوضع الحالي: $mode", color = Muted)
        CardPanel {
            Text("رمز الاقتران", color = White, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = code, onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) onCode(it) },
                label = { Text("أدخل رمز من 6 أرقام") }, singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Neon, focusedLabelColor = Neon,
                    unfocusedTextColor = White, focusedTextColor = White
                )
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = onGenerate, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text("إنشاء رمز تجريبي")
            }
            OutlinedButton(onClick = onConnect, modifier = Modifier.fillMaxWidth()) {
                Text("متابعة الربط التجريبي")
            }
        }
        Text("ملاحظة: الربط في هذا الإصدار محلي وتجريبي فقط. لا يتم الاتصال بجهاز آخر بعد.", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun DashboardScreen(paired: Boolean, onConsent: () -> Unit, onPair: () -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Text("لوحة التحكم", color = White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(if (paired) "تم إنشاء جلسة تجريبية" else "لا يوجد جهاز مربوط فعلياً", color = if (paired) Green else Muted, fontSize = 12.sp)
        }
        item { StatusTile("البطارية", "86%", "قيمة تجريبية", Icons.Default.BatteryFull) }
        item { StatusTile("مساحة التخزين", "42 GB / 128 GB", "قيمة تجريبية", Icons.Default.Storage) }
        item { StatusTile("الشبكة", "Wi‑Fi", "قيمة تجريبية", Icons.Default.Wifi) }
        item {
            CardPanel {
                Text("المشاركة والصلاحيات", color = White, fontWeight = FontWeight.Bold)
                Text("أي مشاركة مستقبلية ستبدأ من الجهاز نفسه بموافقة صاحبه.", color = Muted, fontSize = 12.sp)
                Spacer(Modifier.height(10.dp))
                Button(onClick = onConsent, modifier = Modifier.fillMaxWidth()) { Text("إعداد الموافقة") }
                OutlinedButton(onClick = onPair, modifier = Modifier.fillMaxWidth()) { Text("إدارة الاقتران") }
            }
        }
    }
}

@Composable
private fun StatusTile(title: String, value: String, note: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    CardPanel {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Neon, modifier = Modifier.size(25.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = Muted, fontSize = 11.sp)
                Text(value, color = White, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            }
            Text(note, color = Muted, fontSize = 9.sp)
        }
    }
}

@Composable
private fun ConsentScreen(consent: Boolean, onConsent: (Boolean) -> Unit, onBack: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("موافقة الجهاز", color = White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        CardPanel {
            Icon(Icons.Default.Security, null, tint = Neon, modifier = Modifier.size(34.dp))
            Spacer(Modifier.height(10.dp))
            Text("مشاركة واضحة وتحت سيطرتك", color = White, fontWeight = FontWeight.Bold)
            Text("هذه شاشة توضيحية. عند إضافة المشاركة الفعلية سيظهر طلب Android الرسمي، وسيظل الإيقاف متاحاً من الجهاز المشارك.", color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = consent, onCheckedChange = onConsent, colors = CheckboxDefaults.colors(checkedColor = Neon))
                Text("أفهم أن المشاركة تتطلب موافقة صريحة", color = White, fontSize = 12.sp)
            }
        }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("حفظ والعودة") }
    }
}

@Composable
private fun PrankScreen(running: Boolean, onToggle: (Boolean) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Prank Mode", color = White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("محاكاة مرئية فقط — لا تفحص الجهاز ولا تجمع بيانات ولا تنفذ أوامر.", color = Muted, fontSize = 13.sp)
        CardPanel {
            Icon(Icons.Default.AutoAwesome, null, tint = Neon, modifier = Modifier.size(34.dp))
            Spacer(Modifier.height(12.dp))
            Text(if (running) "SIMULATION RUNNING" else "SIMULATION READY", color = if (running) Green else White, fontWeight = FontWeight.Bold)
            Text(if (running) "جارٍ عرض مؤثرات شكلية آمنة فقط." else "ابدأ عرضاً تمثيلياً واضحاً.", color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(14.dp))
            Button(onClick = { onToggle(!running) }, modifier = Modifier.fillMaxWidth()) {
                Text(if (running) "إيقاف المحاكاة" else "بدء المحاكاة")
            }
            if (running) {
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Neon)
            }
        }
        Text("لا تستخدم المحاكاة لإيهام شخص بوجود اختراق حقيقي أو تخويفه.", color = Green, fontSize = 11.sp)
    }
}
