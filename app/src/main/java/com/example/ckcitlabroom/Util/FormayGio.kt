fun formatGio(gio: String?): String {
    return try {
        val parts = gio?.split(":")
        if (parts != null && parts.size >= 2) {
            val h = parts[0].toIntOrNull() ?: return ""
            val m = parts[1].toIntOrNull() ?: return ""
            String.format("%02d:%02d", h, m)
        } else ""
    } catch (e: Exception) {
        ""
    }
}

