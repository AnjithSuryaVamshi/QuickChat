package com.example.quickchat

import androidx.compose.animation.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipableItemActions(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit,
    onCollapsed: () -> Unit,
    content: @Composable () -> Unit

) {
    var cardWidth by remember {
        mutableStateOf(0f)
    }
    val offset = remember {
        androidx.compose.animation.core.Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = isRevealed,cardWidth){
        if (isRevealed){
            offset.animateTo(cardWidth)
        }else{
            offset.animateTo(0f)
        }

    }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .onSizeChanged {
                    cardWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(true) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(0f, cardWidth)
                                offset.snapTo(newOffset)

                            }
                        },
                        onDragEnd = {
                            when {
                                offset.value > cardWidth / 2 -> {
                                    scope.launch {
                                        offset.animateTo(cardWidth)
                                        onExpanded()
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        offset.animateTo(0f)
                                        onCollapsed()
                                    }
                                }
                            }


                        }
                    )
                }
        ) {
            content()
        }

    }
}