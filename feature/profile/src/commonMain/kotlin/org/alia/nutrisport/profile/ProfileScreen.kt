package org.alia.nutrisport.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.component.ProfileForm
import org.alia.nutrisport.shared.domain.Country

@Composable
fun ProfileScreen() {
    var country by remember { mutableStateOf(Country.Mexico) }
    Box(
        modifier = Modifier
            .background(Surface)
            .systemBarsPadding(),
    ) {
        ProfileForm(
            modifier = Modifier,
            firstName = "Alejandro",
            onFirstNameChange = { },
            lastName = "",
            onLastNameChange = { },
            email = "someEmail@google.com",
            country = country,
            onCountrySelect = { selectedCountry ->
                country = selectedCountry
            },
            city = "",
            onCityChange = { },
            postalCode = null,
            onPostalCodeChange = { },
            address = "",
            onAddressChange = { },
            phoneNumber = null,
            onPhoneNumberChange = { },
        )
    }
}