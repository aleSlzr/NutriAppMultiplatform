package org.alia.nutrisport.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.component.ProfileForm

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier
            .background(Surface)
            .systemBarsPadding(),
    ) {
        ProfileForm(
            modifier = Modifier,
            firstName = "Alejandro",
            onFirstNameChange = {  },
            lastName = "",
            onLastNameChange = {  },
            email = "someEmail@google.com",
            city = "",
            onCityChange = {  },
            postalCode = null,
            onPostalCodeChange = {  },
            address = "",
            onAddressChange = {  },
            phoneNumber = null,
            onPhoneNumberChange = {  }
        )
    }
}