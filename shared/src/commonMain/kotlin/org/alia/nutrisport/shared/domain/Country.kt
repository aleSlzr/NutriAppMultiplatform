package org.alia.nutrisport.shared.domain

import org.alia.nutrisport.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class Country(
    val dialCode: Int,
    val code: String,
    val flag: DrawableResource,
) {
    Mexico(
        dialCode = 52,
        code = "MX",
        flag = Resources.Flag.Mexico,
    ),
    Argentina(
        dialCode = 123,
        code = "AR",
        flag = Resources.Flag.Argentina,
    ),
    Bhutan(
        dialCode = 34,
        code = "BU",
        flag = Resources.Flag.Bhutan,
    ),
    Brazil(
        dialCode = 76,
        code = "BR",
        flag = Resources.Flag.Brazil,
    ),
    Canada(
        dialCode = 45,
        code = "CA",
        flag = Resources.Flag.Canada,
    ),
    USA(
        dialCode = 789,
        code = "USA",
        flag = Resources.Flag.USA,
    ),
}