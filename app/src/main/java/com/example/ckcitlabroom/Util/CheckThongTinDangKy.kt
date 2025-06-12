fun isValidEmail(email: String): Boolean {
    return email.matches(Regex("^[A-Za-z0-9._%+-]+@caothang\\.edu\\.vn$"))
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8 &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { !it.isLetterOrDigit() }
}
