package org.alia.nutrisport.shared.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.alia.nutrisport.shared.Alpha
import org.alia.nutrisport.shared.BorderError
import org.alia.nutrisport.shared.BorderIdle
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.SurfaceLighter
import org.alia.nutrisport.shared.TextPrimary

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    enabled: Boolean = true,
    error: Boolean = false,
    expanded: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
    ),
) {
    val borderColor by animateColorAsState(
        targetValue = if (error) BorderError else BorderIdle
    )

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(size = 6.dp)
            ),
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        placeholder = if (placeholder != null) {
            {
                Text(
                    modifier = Modifier.alpha(Alpha.HALF),
                    text = placeholder,
                    fontSize = FontSize.REGULAR,
                )
            }
        } else null,
        singleLine = !expanded,
        shape = RoundedCornerShape(size = 6.dp),
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            disabledTextColor = TextPrimary.copy(alpha = Alpha.DISABLED),
            focusedContainerColor = SurfaceLighter,
            disabledContainerColor = SurfaceLighter,
            errorContainerColor = SurfaceLighter,
            unfocusedTextColor = SurfaceLighter,
        )
    )
}