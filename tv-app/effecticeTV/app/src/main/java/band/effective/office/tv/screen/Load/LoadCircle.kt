package band.effective.office.tv.screen.Load

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.dp
import com.example.effecticetv.ui.theme.BackgroundColor
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun LoadCircle(modifier: Modifier, inactive: Int){
    Box(modifier = modifier){
        Canvas(modifier = modifier){
            val positions: List<Pair<Float, Float>> = getCords(Pair(size.width/2,size.height/2),size.width/2 )
            val colors: List<Color> = List(positions.size){if (inactive % positions.size == it) BackgroundColor else Color.White}
            translate(left = -size.width/2) {
                for (index in 0 until positions.size){
                    translate(left = positions[index].first, top = positions[index].second) {
                        drawCircle(color = colors[index], radius = 5.dp.toPx())
                    }
                }
            }
        }
    }
}

fun getCords(center: Pair<Float, Float>, radius: Float): List<Pair<Float,Float>>{
    val angles: List<Double> = List(12){ PI + it * PI /6}
    return angles.map {
        Pair((center.first + radius * cos(it)).toFloat(),(center.second + radius * sin(it)).toFloat())
    }
}