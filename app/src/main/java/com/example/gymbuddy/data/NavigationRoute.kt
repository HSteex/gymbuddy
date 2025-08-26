package com.example.gymbuddy.data

 object NavigationRoute {
    fun getRoute(string: String?): String{
        if (string == null) return "GymBuddy"
        if (string.contains("Scheda Esercizio")) return "Dettagli scheda"
        if (string.contains("StartAllenamento")) return "Allenati"
        if (string.contains("ListaSchede")) return "ListaSchede"
        if (string.contains("QRCode")) return "Scanneriza un QR Code"
        if (string.contains("Login")) return "Login"
        if (string.contains("Profilo")) return "Profilo"
        return "GymBuddy"
    }
 }

