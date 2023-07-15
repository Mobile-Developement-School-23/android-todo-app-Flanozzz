package com.example.todolist.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

const val BLOCK_WIDTH = 200
const val BLOCK_HEIGHT = 90
const val PREVIEW_WIDTH = BLOCK_WIDTH * 4
const val PREVIEW_HEIGHT = BLOCK_HEIGHT * 6

@Composable
@Preview(widthDp = PREVIEW_WIDTH, heightDp = PREVIEW_HEIGHT)
fun LightThemePreview(){
    AppTheme(useDarkTheme = false) {
        Column {
            Row {
                ThemeBlock("Primary", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
                ThemeBlock("On Primary", MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.primary)
                ThemeBlock("Primary Container", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                ThemeBlock("On Primary Container", MaterialTheme.colorScheme.onPrimaryContainer, MaterialTheme.colorScheme.primaryContainer)
            }
            Row {
                ThemeBlock("Secondary", MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
                ThemeBlock("On Secondary", MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.secondary)
                ThemeBlock("Secondary Container", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
                ThemeBlock("On Secondary Container", MaterialTheme.colorScheme.onSecondaryContainer, MaterialTheme.colorScheme.secondaryContainer)
            }
            Row {
                ThemeBlock("Tertiary", MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary)
                ThemeBlock("On Tertiary", MaterialTheme.colorScheme.onTertiary, MaterialTheme.colorScheme.tertiary)
                ThemeBlock("Tertiary Container", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
                ThemeBlock("On Tertiary Container", MaterialTheme.colorScheme.onTertiaryContainer, MaterialTheme.colorScheme.tertiaryContainer)
            }
            Row {
                ThemeBlock("Error", MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
                ThemeBlock("On Error", MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
                ThemeBlock("Error Container", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
                ThemeBlock("On Error Container", MaterialTheme.colorScheme.onErrorContainer, MaterialTheme.colorScheme.errorContainer)
            }
            Row {
                ThemeBlock("Background", MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onBackground)
                ThemeBlock("On Background", MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.background)
                ThemeBlock("Surface", MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
                ThemeBlock("On Surface", MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.surface)
            }
            Row {
                ThemeBlock("Outline", MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.surface)
                ThemeBlock("Surface-Variant", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                ThemeBlock("On Surface-Variant", MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.surfaceVariant)
            }
        }
    }
}

@Composable
@Preview(widthDp = PREVIEW_WIDTH)
fun DarkThemePreview(){
    AppTheme(useDarkTheme = true) {
        Column {
            Row {
                ThemeBlock("Primary", MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
                ThemeBlock("On Primary", MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.primary)
                ThemeBlock("Primary Container", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
                ThemeBlock("On Primary Container", MaterialTheme.colorScheme.onPrimaryContainer, MaterialTheme.colorScheme.primaryContainer)
            }
            Row {
                ThemeBlock("Secondary", MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
                ThemeBlock("On Secondary", MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.secondary)
                ThemeBlock("Secondary Container", MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.onSecondaryContainer)
                ThemeBlock("On Secondary Container", MaterialTheme.colorScheme.onSecondaryContainer, MaterialTheme.colorScheme.secondaryContainer)
            }
            Row {
                ThemeBlock("Tertiary", MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.onTertiary)
                ThemeBlock("On Tertiary", MaterialTheme.colorScheme.onTertiary, MaterialTheme.colorScheme.tertiary)
                ThemeBlock("Tertiary Container", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
                ThemeBlock("On Tertiary Container", MaterialTheme.colorScheme.onTertiaryContainer, MaterialTheme.colorScheme.tertiaryContainer)
            }
            Row {
                ThemeBlock("Error", MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.onError)
                ThemeBlock("On Error", MaterialTheme.colorScheme.onError, MaterialTheme.colorScheme.error)
                ThemeBlock("Error Container", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
                ThemeBlock("On Error Container", MaterialTheme.colorScheme.onErrorContainer, MaterialTheme.colorScheme.errorContainer)
            }
            Row {
                ThemeBlock("Background", MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.onBackground)
                ThemeBlock("On Background", MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.background)
                ThemeBlock("Surface", MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurface)
                ThemeBlock("On Surface", MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.surface)
            }
            Row {
                ThemeBlock("Outline", MaterialTheme.colorScheme.outline, MaterialTheme.colorScheme.surface)
                ThemeBlock("Surface-Variant", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                ThemeBlock("On Surface-Variant", MaterialTheme.colorScheme.onSurfaceVariant, MaterialTheme.colorScheme.surfaceVariant)
            }
        }
    }
}

@Composable
fun ThemeBlock(
    text: String = "Default Text",
    color: Color = Color.White,
    textColor: Color = Color.Black,
){
    Box(
        Modifier
            .width(BLOCK_WIDTH.dp)
            .height(BLOCK_HEIGHT.dp)
            .padding(3.dp)
            .background(color = color)
    ){
        Text(
            text = text,
            color = textColor,
            modifier = Modifier.padding(10.dp)
        )
    }
}