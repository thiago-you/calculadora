package you.thiago.calculadora.enums

enum class ThemeState {
    DARK, LIGHT;

    fun switchTheme(): ThemeState {
        if (this == DARK) {
            return LIGHT
        }

        return DARK
    }
}
