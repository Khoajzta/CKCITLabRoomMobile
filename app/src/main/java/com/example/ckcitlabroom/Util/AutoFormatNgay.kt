fun formatInputDate(input: String): String {
    // Loại bỏ ký tự không phải số
    val digits = input.filter { it.isDigit() }

    return when (digits.length) {
        in 1..2 -> digits
        in 3..4 -> "${digits.substring(0, 2)}/${digits.substring(2)}"
        in 5..8 -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4)}"
        else -> digits.take(8).let {
            "${it.substring(0, 2)}/${it.substring(2, 4)}/${it.substring(4)}"
        }
    }
}
