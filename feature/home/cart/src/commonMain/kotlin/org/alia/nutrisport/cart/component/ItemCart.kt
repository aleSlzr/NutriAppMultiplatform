package org.alia.nutrisport.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.alia.nutrisport.shared.BorderIdle
import org.alia.nutrisport.shared.FontSize
import org.alia.nutrisport.shared.IconPrimary
import org.alia.nutrisport.shared.Resources
import org.alia.nutrisport.shared.RobotoCondensedFont
import org.alia.nutrisport.shared.Surface
import org.alia.nutrisport.shared.SurfaceLighter
import org.alia.nutrisport.shared.TextPrimary
import org.alia.nutrisport.shared.TextSecondary
import org.alia.nutrisport.shared.component.QuantityCounter
import org.alia.nutrisport.shared.domain.CartItem
import org.alia.nutrisport.shared.domain.Product
import org.alia.nutrisport.shared.domain.QuantityCounterSize

@Composable
fun ItemCart(
    modifier: Modifier = Modifier,
    product: Product,
    cartItem: CartItem,
    onMinusClick: (Int) -> Unit,
    onPlusClick: (Int) -> Unit,
    onDeleteClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(size = 12.dp))
            .background(SurfaceLighter)
    ) {
        AsyncImage(
            modifier = Modifier
                .width(120.dp)
                .height(120.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = BorderIdle,
                    shape = RoundedCornerShape(12.dp)
                ),
            model = ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(product.thumbnail)
                .crossfade(enable = true)
                .build(),
            contentDescription = "Product thumbnail image",
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = product.title,
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.MEDIUM,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = 12.dp))
                        .background(Surface)
                        .border(
                            width = 1.dp,
                            color = BorderIdle,
                            shape = RoundedCornerShape(size = 12.dp),
                        )
                        .clickable { onDeleteClick() }
                        .padding(all = 8.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Resources.Icon.Delete,
                        contentDescription = "Delete icon",
                        tint = IconPrimary,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "$${product.price}",
                    fontFamily = RobotoCondensedFont(),
                    fontSize = FontSize.EXTRA_REGULAR,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                )
                QuantityCounter(
                    size = QuantityCounterSize.Small,
                    value = cartItem.quantity,
                    onMinusClick = onMinusClick,
                    onPlusClick = onPlusClick,
                )
            }
        }
    }
}