package dev.shipi.mylittlespiders.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import java.time.LocalDate

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DateInputWidget(state: LocalDate?, setState: (LocalDate) -> Unit) {
    var isDialogShown: Boolean by rememberSaveable {
        mutableStateOf(false)
    }
    if (isDialogShown) {
        DatePickerDialog(
            onDismissRequest = { isDialogShown = false },
            onDateChange = {
                setState(it)
                isDialogShown = false
            },
            // Optional but recommended parameter to provide the title for the dialog
            title = { Text(text = "Select date") }
        )
    }

    val color = if (state == null) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.outline
    }

    val textColor = if (state == null) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clickable { isDialogShown = true }
            .border(
                width = 1.dp,
                color = color,
                shape = MaterialTheme.shapes.extraSmall
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            color = textColor,
            modifier = Modifier.padding(start = 16.dp),
            text = (state ?: "Date").toString()
        )
        Icon(
            modifier = Modifier.padding(end = 16.dp),
            imageVector = Icons.Outlined.DateRange,
            contentDescription = "Edit date",
            tint = color
        )
    }
}

@Composable
@Preview
fun DateInputWidgetPreview() {
    DateInputWidget(state = LocalDate.now(), setState = {})
}

@Composable
@Preview
fun DateInputWidgetErrorPreview() {
    DateInputWidget(state = null, setState = {})
}