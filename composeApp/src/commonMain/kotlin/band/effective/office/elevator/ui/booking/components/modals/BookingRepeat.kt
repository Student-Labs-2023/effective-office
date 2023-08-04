package band.effective.office.elevator.ui.booking.components.modals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import band.effective.office.elevator.ExtendedTheme
import band.effective.office.elevator.MainRes
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BookingRepeat() {
    val elevation = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = 0.dp,
        disabledElevation = 0.dp,
        hoveredElevation = 0.dp,
        focusedElevation = 0.dp
    )

    val weekNames = listOf(
        MainRes.strings.Monday,
        MainRes.strings.Tuesday,
        MainRes.strings.Wednesday,
        MainRes.strings.Thursday,
        MainRes.strings.Friday,
        MainRes.strings.Saturday,
        MainRes.strings.Sunday,
    )

    Box {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 0.dp
                    )
                )
                .padding(bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(height = 8.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth(fraction = .3f)
                    .height(height = 4.dp)
                    .background(
                        color = ExtendedTheme.colors.dividerColor,
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .padding(
                        bottom = 8.dp,
                        top = 8.dp
                    )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                IconButton(
                    onClick = {

                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "back button"
                    )
                }
                Text(
                    text = stringResource(resource = MainRes.strings.booking_repeat),
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight(500)),
                    textAlign = TextAlign.Center
                )
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth(fraction = 1.0f)
                    .height(height = 1.dp)
                    .background(
                        color = ExtendedTheme.colors._66x
                    )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 16.dp, top = 16.dp)
            ) {
                Text(
                    text = stringResource(resource = MainRes.strings.booking_repeat_in),
                    style = MaterialTheme.typography.button.copy(fontWeight = FontWeight(400))
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(start = 54.dp, end = 16.dp)
            ) {
                OutlinedTextField(
                    value = "1",
                    onValueChange = {

                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .weight(.3f)
                )
                OutlinedTextField(
                    enabled = false,
                    readOnly = true,
                    value = "Неделя",
                    onValueChange = {

                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 12.dp,
                            end = 12.dp,
                            bottom = 12.dp
                        )
                        .weight(.7f),
                    trailingIcon = {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Image(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "drop down",
                                contentScale = ContentScale.None
                            )
                        }
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            ) {
                Text(
                    text = stringResource(MainRes.strings.when_repeat),
                    style = MaterialTheme.typography.button.copy(fontWeight = FontWeight(weight = 400))
                )
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(count = 3),
                verticalItemSpacing = 12.dp,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
                content = {
                    items(weekNames.size) { index ->
                        WorkingZones(weekNames[index])
                    }
                }
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth(fraction = 1.0f)
                    .height(height = 1.dp)
                    .background(
                        color = ExtendedTheme.colors._66x
                    )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 16.dp, top = 16.dp, bottom = 7.dp)
            ) {
                Text(
                    text = stringResource(MainRes.strings.book_finish),
                    style = MaterialTheme.typography.button.copy(fontWeight = FontWeight(weight = 400)),
                    textAlign = TextAlign.Start
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 16.dp, top = 16.dp)
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        focusedElevation = 0.dp
                    ),
                    modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                    onClick = {

                    },
                    contentPadding = PaddingValues(all = 0.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(0.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            enabled = false,
                            selected = true,
                            onClick = { },
                            colors = RadioButtonDefaults.colors(
                                disabledSelectedColor = MaterialTheme.colors.primary
                            )
                        )
                        Text(
                            text = stringResource(MainRes.strings.never),
                            style = MaterialTheme.typography.button.copy(
                                color = ExtendedTheme.colors.radioTextColor,
                                fontWeight = FontWeight(400)
                            ),
                            modifier = Modifier.fillMaxWidth().wrapContentHeight()
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 16.dp, top = 16.dp)
            ) {
                RadioButton(selected = false, onClick = {

                })
                OutlinedTextField(
                    value = "01.01.2023",
                    onValueChange = {

                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 12.dp)
                        .weight(0.4f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 54.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
            ) {

            }
        }
    }
}