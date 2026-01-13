package org.alia.nutrisport.shared.domain

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.Serializable
import org.alia.nutrisport.shared.CategoryBlue
import org.alia.nutrisport.shared.CategoryGreen
import org.alia.nutrisport.shared.CategoryPurple
import org.alia.nutrisport.shared.CategoryRed
import org.alia.nutrisport.shared.CategoryYellow

@Serializable
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: String,
    val flavors: List<String>? = null,
    val weight: Int? = null,
    val price: Double,
    val isPopular: Boolean = false,
    val isDiscounted: Boolean = false,
    val isNew: Boolean = false,
)

enum class ProductCategory(
    val title: String,
    val color: Color
) {
    Protein(
        title = "Protein",
        color = CategoryYellow,
    ),
    Creatine(
        title = "Creatine",
        color = CategoryBlue,
    ),
    PreWorkout(
        title = "PreWorkout",
        color = CategoryGreen,
    ),
    Gainers(
        title = "Gainers",
        color = CategoryPurple,
    ),
    Accessories(
        title = "Accessories",
        color = CategoryRed,
    ),
}