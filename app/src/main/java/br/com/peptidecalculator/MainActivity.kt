package br.com.peptidecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val Navy = Color(0xFF174A72)
private val Blue = Color(0xFF2878B8)
private val Pale = Color(0xFFF2F6FA)
private val Ink = Color(0xFF183247)
private val Muted = Color(0xFF617789)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { PeptideApp() }
    }
}

@Composable
fun PeptideApp() {
    var product by remember { mutableStateOf("") }
    var vialMg by remember { mutableStateOf("10") }
    var diluentMl by remember { mutableStateOf("2") }
    var portionMg by remember { mutableStateOf("0.5") }

    val total = vialMg.toDecimal()
    val diluent = diluentMl.toDecimal()
    val portion = portionMg.toDecimal()
    val concentration = if (total != null && diluent != null && diluent > 0.0) total / diluent else null
    val portions = if (total != null && portion != null && total > 0.0 && portion > 0.0) total / portion else null
    val whole = portions?.toInt()
    val remainder = if (whole != null && total != null && portion != null) total - whole * portion else null

    MaterialTheme(colorScheme = lightColorScheme(
        primary = Navy, secondary = Blue, background = Pale,
        surface = Color.White, onSurface = Ink
    )) {
        Scaffold(containerColor = Pale) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding)
                    .verticalScroll(rememberScrollState()).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("PEPTIDE", color = Navy, fontSize = 13.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                    Text("Calculator", color = Ink, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    Text("Calculadora de concentração e rendimento teórico", color = Muted, fontSize = 14.sp)
                }

                Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionTitle("01", "Identificação")
                        OutlinedTextField(
                            value = product, onValueChange = { product = it },
                            label = { Text("Nome / identificação do produto") },
                            placeholder = { Text("Ex.: Produto A") },
                            modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        SectionTitle("02", "Dados informados do frasco")
                        NumberField("Conteúdo total do frasco", vialMg, "mg") { vialMg = it }
                        NumberField("Volume total de diluente", diluentMl, "mL") { diluentMl = it }
                        SectionTitle("03", "Simulação de rendimento")
                        NumberField("Quantidade por porção informada", portionMg, "mg") { portionMg = it }
                        Text(
                            "Informe apenas uma quantidade definida por documentação/profissional habilitado. O app não sugere nem valida doses.",
                            color = Muted, fontSize = 12.sp
                        )
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = Navy), shape = RoundedCornerShape(20.dp)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("RESULTADOS MATEMÁTICOS", color = Color.White.copy(alpha = .8f), fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        ResultLine("Concentração calculada", concentration?.let { "${fmt(it)} mg/mL" } ?: "—")
                        HorizontalDivider(color = Color.White.copy(alpha = .2f))
                        ResultLine("Porções teóricas completas", whole?.toString() ?: "—")
                        ResultLine("Quantidade restante", remainder?.let { "${fmt(it)} mg" } ?: "—")
                        Text(
                            "Rendimento teórico: não considera perdas, volume residual, estabilidade, homogeneidade ou limitações do recipiente.",
                            color = Color.White.copy(alpha = .75f), fontSize = 11.sp
                        )
                    }
                }

                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F1F8)), shape = RoundedCornerShape(16.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Importante — uso responsável", color = Navy, fontWeight = FontWeight.Bold)
                        Text(
                            "Ferramenta de aritmética informativa. Não calcula volume de aplicação, unidades de seringa, frequência ou esquema de administração. Não substitui prescrição, bula, avaliação farmacêutica ou orientação de profissional habilitado. Peptídeos experimentais podem não ser aprovados para uso humano.",
                            color = Ink, fontSize = 12.sp
                        )
                    }
                }
                Text("Peptide Calculator • versão 1.0.0", color = Muted, fontSize = 11.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
private fun SectionTitle(number: String, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
        Box(Modifier.size(28.dp).background(Color(0xFFE8F1F8), RoundedCornerShape(9.dp)), contentAlignment = Alignment.Center) {
            Text(number, color = Blue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Text(title, color = Ink, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
    }
}

@Composable
private fun NumberField(label: String, value: String, unit: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> if (input.matches(Regex("[0-9]*([.,][0-9]*)?"))) onChange(input) },
        label = { Text(label) },
        trailingIcon = { Text(unit, color = Muted, modifier = Modifier.padding(end = 10.dp)) },
        modifier = Modifier.fillMaxWidth(), singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun ResultLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = Color.White.copy(alpha = .82f), fontSize = 13.sp)
        Text(value, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
    }
}

private fun String.toDecimal(): Double? =
    trim().replace(",", ".").toDoubleOrNull()?.takeIf { it.isFinite() && it >= 0.0 }

private fun fmt(value: Double): String =
    String.format(Locale("pt", "BR"), "%.4f", value).trimEnd('0').trimEnd(',')
