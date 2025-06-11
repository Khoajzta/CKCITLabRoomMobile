fun formatGio(gio: String?): String {
    return try {
        val parts = gio?.split(":")
        if (parts != null && parts.size >= 2) {
            val h = parts[0].toIntOrNull() ?: return ""
            val m = parts[1].toIntOrNull() ?: return ""
            if (m == 0) "$h giờ" else "$h giờ $m"
        } else ""
    } catch (e: Exception) {
        ""
    }
}
