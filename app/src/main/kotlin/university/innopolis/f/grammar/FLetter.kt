enum class FLetter {
    SMALL_A,
    SMALL_B,
    SMALL_C,
    SMALL_D,
    SMALL_E,
    SMALL_F,
    SMALL_G,
    SMALL_H,
    SMALL_I,
    SMALL_J,
    SMALL_K,
    SMALL_L,
    SMALL_M,
    SMALL_N,
    SMALL_O,
    SMALL_P,
    SMALL_Q,
    SMALL_R,
    SMALL_S,
    SMALL_T,
    SMALL_U,
    SMALL_V,
    SMALL_W,
    SMALL_X,
    SMALL_Y,
    SMALL_Z,
    CAPITAL_A,
    CAPITAL_B,
    CAPITAL_C,
    CAPITAL_D,
    CAPITAL_E,
    CAPITAL_F,
    CAPITAL_G,
    CAPITAL_H,
    CAPITAL_I,
    CAPITAL_J,
    CAPITAL_K,
    CAPITAL_L,
    CAPITAL_M,
    CAPITAL_N,
    CAPITAL_O,
    CAPITAL_P,
    CAPITAL_Q,
    CAPITAL_R,
    CAPITAL_S,
    CAPITAL_T,
    CAPITAL_U,
    CAPITAL_V,
    CAPITAL_W,
    CAPITAL_X,
    CAPITAL_Y,
    CAPITAL_Z;

    companion object {
        fun fromCharOrNull(c: Char): FLetter? =
                when (c) {
                    'a' -> SMALL_A
                    'b' -> SMALL_B
                    'c' -> SMALL_C
                    'd' -> SMALL_D
                    'e' -> SMALL_E
                    'f' -> SMALL_F
                    'g' -> SMALL_G
                    'h' -> SMALL_H
                    'i' -> SMALL_I
                    'j' -> SMALL_J
                    'k' -> SMALL_K
                    'l' -> SMALL_L
                    'm' -> SMALL_M
                    'n' -> SMALL_N
                    'o' -> SMALL_O
                    'p' -> SMALL_P
                    'q' -> SMALL_Q
                    'r' -> SMALL_R
                    's' -> SMALL_S
                    't' -> SMALL_T
                    'u' -> SMALL_U
                    'v' -> SMALL_V
                    'w' -> SMALL_W
                    'x' -> SMALL_X
                    'y' -> SMALL_Y
                    'z' -> SMALL_Z
                    'A' -> CAPITAL_A
                    'B' -> CAPITAL_B
                    'C' -> CAPITAL_C
                    'D' -> CAPITAL_D
                    'E' -> CAPITAL_E
                    'F' -> CAPITAL_F
                    'G' -> CAPITAL_G
                    'H' -> CAPITAL_H
                    'I' -> CAPITAL_I
                    'J' -> CAPITAL_J
                    'K' -> CAPITAL_K
                    'L' -> CAPITAL_L
                    'M' -> CAPITAL_M
                    'N' -> CAPITAL_N
                    'O' -> CAPITAL_O
                    'P' -> CAPITAL_P
                    'Q' -> CAPITAL_Q
                    'R' -> CAPITAL_R
                    'S' -> CAPITAL_S
                    'T' -> CAPITAL_T
                    'U' -> CAPITAL_U
                    'V' -> CAPITAL_V
                    'W' -> CAPITAL_W
                    'X' -> CAPITAL_X
                    'Y' -> CAPITAL_Y
                    'Z' -> CAPITAL_Z
                    else -> null
                }
    }
}
