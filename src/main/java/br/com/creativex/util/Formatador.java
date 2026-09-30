// Bruno Leal Engenheiro de Software
// CREATIVEX SISTEMAS -  https://lealcreativex.com/

// Peracio Dias
// creativex sistemas
package br.com.creativex.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.sql.Timestamp;

public class Formatador {

    private static final Locale LOCALE_BR = Locale.of("pt", "BR");

    private static final NumberFormat MOEDA =
            NumberFormat.getCurrencyInstance(LOCALE_BR);

    private static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                    .withLocale(LOCALE_BR);

    // =========================
    // MOEDA
    // =========================
    public static String moeda(BigDecimal valor) {
        if (valor == null) return "R$ 0,00";
        return MOEDA.format(valor);
    }

    // =========================
    // DATA
    // =========================
    public static String dataHora(Timestamp ts) {
        if (ts == null) return "";
        return DATA_HORA.format(ts.toLocalDateTime());
    }
}