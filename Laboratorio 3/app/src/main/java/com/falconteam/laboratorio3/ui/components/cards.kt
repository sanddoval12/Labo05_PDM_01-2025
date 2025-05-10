
package com.falconteam.laboratorio3.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.falconteam.laboratorio3.ui.theme.Green
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cards(
    pos: Int,
    max: Int,
    title: String = "Title",
    description: String = "Description sobre lo que es esta tarea que estamos por realizar en este lugar",
    endDate: Date = Date(),
    delete: (Int) -> Unit = {},
    check: (Boolean, Int) -> Unit = { _, _ ->
    },
    checked: Boolean = true,
    changePosition: (Int, Int) -> Unit = { _, _ ->
    }
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), colors = CardDefaults.cardColors(
            containerColor = if (checked) Green else MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.headlineSmall
                )
                Row {
                    Button(
                        onClick = {
                            changePosition(pos, pos - 1)
                        },
                        enabled = pos > 0,
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                    Button(
                        onClick = {
                            changePosition(pos, pos + 1)
                        },
                        enabled = pos < max,
                        modifier = Modifier.padding(start = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Row {
                Text(
                    text = "Fecha de entrega: $endDate",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        delete(pos)
                    },

                    ) {
                    Text(
                        text = "Eliminar",
                        textAlign = TextAlign.Start,
                    )
                }
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        check(it, pos)
                    },
                )
            }
        }
    }
}


@Composable
@Preview
fun CardsPreview() {
    Cards(
        pos = 0,
        max = 5,
        title = "Title",
        description = "Description",
        delete = { },
        check = { _, _ -> },
        checked = false
    )
}