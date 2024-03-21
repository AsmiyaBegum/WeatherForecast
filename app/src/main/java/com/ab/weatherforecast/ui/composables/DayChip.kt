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
import androidx.compose.material3.MaterialTheme
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
import com.ab.weatherforecast.domain.model.WeatherItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DayChip(weatherList: List<WeatherItem>,
            onChipSelected: (Triple<Double,Double,Calendar>) -> Unit) {
    var selectedDateIndex by remember { mutableStateOf(0) }
    val today = Calendar.getInstance()
    val dates = generateDates(today)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        dates.forEachIndexed { index, date ->
            DateChip(
                date = date,
                onSelected = {
                    selectedDateIndex = index
                    val selectedDate = Calendar.getInstance()
                    selectedDate.add(Calendar.DAY_OF_MONTH, index)
                    val list = weatherList.filter {
                        changeDateFormat( it.dt_txt) == formatCalendarDate(selectedDate)
                    }

                    onChipSelected(Triple(list.minOf { it.main.temp_min },list.maxOf { it.main.temp_max }, selectedDate))
                             },
                selected = selectedDateIndex == index,
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
    val backgroundColor = if (selectedState.value) MaterialTheme.colorScheme.primary else Color.Gray

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
        modifier = Modifier.height(30.dp)
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .wrapContentSize()
                .selectable(selected = false, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = textColor,
                style = MaterialTheme.typography.bodySmall)
        }
    }
}






fun generateDates(startingDate: Calendar): List<Calendar> {
    val dates = mutableListOf<Calendar>()
    var currentDate = startingDate.clone() as Calendar

    repeat(5) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, it)
        dates.add(calendar)
    }

    return dates
}

fun formatDate(date: Calendar): String {
    val dayOfWeek = date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
    return "$dayOfWeek"
}

fun formatCalendarDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun changeDateFormat(originalDateString: String): String {
    val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val parsedDate = originalFormat.parse(originalDateString)

    val newFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return newFormat.format(parsedDate)
}