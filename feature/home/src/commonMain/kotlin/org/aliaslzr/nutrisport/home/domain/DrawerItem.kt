package org.aliaslzr.nutrisport.home.domain

import androidx.compose.ui.graphics.vector.ImageVector
import org.alia.nutrisport.shared.Resources

enum class DrawerItem(
    val title: String,
    val icon: ImageVector,
) {
    Profile(
        title = "Profile",
        icon = Resources.Icon.Person,
    ),
    Blog(
        title = "Blog",
        icon = Resources.Icon.Book,
    ),
    Locations(
        title = "Locations",
        icon = Resources.Icon.MapPin,
    ),
    Contact(
        title = "Contact us",
        icon = Resources.Icon.Edit,
    ),
    SignOut(
        title = "Sign out",
        icon = Resources.Icon.SignOut,
    ),
    Admin(
        title = "Admin Panel",
        icon = Resources.Icon.Unlock,
    ),
}