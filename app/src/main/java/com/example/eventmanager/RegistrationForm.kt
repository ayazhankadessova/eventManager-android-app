import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eventmanager.KtorClient
import kotlinx.coroutines.launch

data class RegistrationData(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var contact: String = "",
    var ageGroup: String = "",
    var about: String = "",
    var terms: Boolean = false
) {
    fun isNotEmpty(): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && contact.isNotEmpty() && ageGroup.isNotEmpty() && about.isNotEmpty()
    }
}

fun checkRegData(creds: RegistrationData): Boolean {
    return creds.isNotEmpty() && creds.terms
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeGroupExposedDropdown(onAgeGroupChange: (String) -> Unit) {
    val context = LocalContext.current
    val ageGroups = arrayOf("15-18", "18-21", "21-30", "30-50", "50+")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(ageGroups[0]) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth() // Modified modifier here
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth() // Modified modifier here
            ) {
                ageGroups.forEach { group ->
                    DropdownMenuItem(
                        text = { Text(text = group) },
                        onClick = {
                            selectedText = group
                            expanded = false
                            onAgeGroupChange(group)
                            Toast.makeText(context, group, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RegistrationField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    placeholder: String = ""
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}

@Composable
fun RegistrationForm(snackbarHostState: SnackbarHostState, navController: NavHostController) {

    val coroutineScope = rememberCoroutineScope()

    Surface {
        var registrationData by remember { mutableStateOf(RegistrationData()) }
        val context = LocalContext.current

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            RegistrationField(
                value = registrationData.email,
                onChange = { data -> registrationData = registrationData.copy(email = data) },
                label = "Email",
                placeholder = "Enter your Email",
                modifier = Modifier.fillMaxWidth()
            )
            RegistrationField(
                value = registrationData.password,
                onChange = { data -> registrationData = registrationData.copy(password = data) },
                label = "Password",
                placeholder = "Enter your Password",
                modifier = Modifier.fillMaxWidth()
            )
            RegistrationField(
                value = registrationData.name,
                onChange = { data -> registrationData = registrationData.copy(name = data) },
                label = "Name",
                placeholder = "Enter your Name",
                modifier = Modifier.fillMaxWidth()
            )
            RegistrationField(
                value = registrationData.contact,
                onChange = { data -> registrationData = registrationData.copy(contact = data) },
                label = "Contact",
                placeholder = "Enter your Contact",
                modifier = Modifier.fillMaxWidth()
            )
            AgeGroupExposedDropdown(
                onAgeGroupChange = { data -> registrationData = registrationData.copy(ageGroup = data) }
            )
            RegistrationField(
                value = registrationData.about,
                onChange = { data -> registrationData = registrationData.copy(about = data) },
                label = "About me and Remark",
                placeholder = "Enter About",
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = registrationData.terms,
                    onCheckedChange = { isChecked ->
                        registrationData = registrationData.copy(terms = isChecked)
                    }
                )
                Text(
                    text = "I agree to the Terms and Conditions",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (!checkRegData(registrationData)) {

                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Wrong inputs...")
                        }
                    } else {
                        coroutineScope.launch {
                            val stringBody: String? =
                                KtorClient.register(registrationData.email, registrationData.password, registrationData.name, registrationData.contact, registrationData.ageGroup, registrationData.about, registrationData.terms)

                            if (stringBody!= null) {
                                snackbarHostState.showSnackbar("Succefully created new account: " + stringBody + "\nLogin with your new account.")
                                navController.navigate("login")
                            } else {
                                snackbarHostState.showSnackbar("Error while creating new account.")
                            }
                        }

                    }
                },
                enabled = registrationData.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
        }
    }
}