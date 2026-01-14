package org.alia.nutrisport.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GifBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerticalSplit
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.ShoppingCart
import nutrisport.shared.generated.resources.Res
import nutrisport.shared.generated.resources.argentina
import nutrisport.shared.generated.resources.bhutan
import nutrisport.shared.generated.resources.brazil
import nutrisport.shared.generated.resources.canada
import nutrisport.shared.generated.resources.icon_cart
import nutrisport.shared.generated.resources.icon_cat
import nutrisport.shared.generated.resources.icon_success
import nutrisport.shared.generated.resources.mexico
import nutrisport.shared.generated.resources.united_states

object Resources {
    object Icon {
        val Plus = Icons.Default.Add
        val Minus = Icons.Default.Remove
        val SignIn = Icons.AutoMirrored.Default.Login
        val SignOut = Icons.AutoMirrored.Default.Logout
        val Unlock = Icons.Default.Lock
        val Search = Icons.Default.Search
        val Person = Icons.Default.Person
        val CheckMark = Icons.Default.Check
        val Edit = Icons.Default.Edit
        val Menu = Icons.Default.Menu
        val BackArrow = Icons.AutoMirrored.Default.ArrowLeft
        val RightArrow = Icons.AutoMirrored.Default.ArrowRight
        val Home = Icons.Outlined.Home
        val ShoppingCart = Icons.Outlined.ShoppingCart
        val Categories = Icons.Outlined.Category
        val Dollar = Icons.Default.Money
        val MapPin = Icons.Default.PinDrop
        val Close = Icons.Default.Close
        val Book = Icons.Default.Book
        val VerticalMenu = Icons.Default.VerticalSplit
        val Delete = Icons.Default.Delete
        val Google = Icons.Default.GifBox
        val PayPal = Icons.Default.Park
        val Weight = Icons.Outlined.Scale
    }
    object Image {
        val Cat = Res.drawable.icon_cat
        val Cart = Res.drawable.icon_cart
        val Success = Res.drawable.icon_success
    }
    object Flag {
        val Mexico = Res.drawable.mexico
        val USA = Res.drawable.united_states
        val Canada = Res.drawable.canada
        val Argentina = Res.drawable.argentina
        val Brazil = Res.drawable.brazil
        val Bhutan = Res.drawable.bhutan
    }
}
