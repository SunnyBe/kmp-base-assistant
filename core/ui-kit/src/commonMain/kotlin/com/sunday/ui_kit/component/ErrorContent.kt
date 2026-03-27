package com.sunday.ui_kit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sunday.ui_kit.generated.resources.Res
import com.sunday.ui_kit.generated.resources.add_circle_24dp
import com.sunday.ui_kit.generated.resources.dialog_error_title_label
import com.sunday.ui_kit.generated.resources.dismiss_label
import com.sunday.ui_kit.generated.resources.error_something_unexpected_label
import com.sunday.ui_kit.generated.resources.illustration_error_label
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorContent(content: StringResource, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.dialog_error_title_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.fillMaxWidth().padding(8.dp))
                Image(
                    modifier = Modifier.size(150.dp),
                    painter = painterResource(Res.drawable.add_circle_24dp),
                    contentDescription = stringResource(Res.string.illustration_error_label)
                )

                Text(
                    text = stringResource(content),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(Res.string.dismiss_label))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorContentPreview() {
    MaterialTheme {
        ErrorContent(Res.string.error_something_unexpected_label, {})
    }
}

