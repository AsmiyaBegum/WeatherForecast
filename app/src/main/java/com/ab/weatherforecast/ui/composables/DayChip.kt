package com.ab.weatherforecast.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ab.weatherforecast.R
import java.util.Calendar
import java.util.Locale

@Composable
fun DayChip() {
    var selectedDate by remember { mutableStateOf(0) }
    val today = Calendar.getInstance()
    val dates = generateDates(today)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        dates.forEachIndexed { index, date ->
            DateChip(
                date = date,
                onSelected = { selectedDate = index },
                selected = selectedDate == index,
                index = index
            )
        }
    }
}

@Composable
fun DateChip(
    date: Calendar,
    selected: Boolean,
    onSelected: () -> Unit,
    index: Int
) {
    val chipText = when (index) {
        0 -> stringResource(id = R.string.today)
        else -> formatDate(date)
    }

    val selectedState = rememberUpdatedState(selected)
    val backgroundColor = if (selectedState.value) Color.Blue else Color.Gray

    Chip(
        text = chipText,
        backgroundColor = backgroundColor,
        textColor = Color.White,
        onClick = onSelected
    )
}

@Composable
fun  Chip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .wrapContentSize()
                .selectable(selected = false, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = textColor)
        }
    }
}






fun generateDates(startingDate: Calendar): List<Calendar> {
    val dates = mutableListOf<Calendar>()
    var currentDate = startingDate.clone() as Calendar

    repeat(5) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, it-1)
        dates.add(calendar)
    }

    return dates
}

fun formatDate(date: Calendar): String {
    val dayOfWeek = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
    return "$dayOfWeek"
}