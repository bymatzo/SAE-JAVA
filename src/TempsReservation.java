import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.regex.*;

public class TempsReservation {

    private String debutTexte;
    private String dureeTexte;
    private LocalDateTime dateDebut;
    private int dureeMinutes;

    TempsReservation(String heureDureeCsv) {
        if (heureDureeCsv == null) {
            throw new IllegalArgumentException("La date et la durée est null");
        }

        String[] tokens = heureDureeCsv.split("\\s-\\s", 2);

        if (tokens.length == 2) {
            debutTexte = tokens[0].trim();
            dureeTexte = tokens[1].trim();
        } else {
            throw new IllegalArgumentException("La séparation a échoué");
        }

        this.dateDebut = dateNumerique(debutTexte);
        this.dureeMinutes = dureeNumerique(dureeTexte);
    }

    private LocalDateTime dateNumerique(String debutTexte) {
        try {
            
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("EEEE dd MMMM yyyy HH:mm:ss")
                    .toFormatter(Locale.FRANCE);

            return LocalDateTime.parse(debutTexte.trim(), formatter);

        } catch (DateTimeParseException e) {
            
            throw new IllegalArgumentException("Le format de date est incorrecte : " + debutTexte);
        }
    }

    private int dureeNumerique(String dureeTexte) {
        int minutesTotal = 0;

        Pattern pattern = Pattern.compile(
                "(\\d+)\\s*(année(s)?|semaine(\\(s\\))?|jour(\\(s\\))?|heure(\\(s\\))?|minute(\\(s\\))?)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(dureeTexte);

        while (matcher.find()) {
            int nbr = Integer.parseInt(matcher.group(1));
            String unite = matcher.group(2).toLowerCase();

            switch (unite) {
                case "année":
                case "années":
                    minutesTotal += nbr * 365 * 24 * 60;
                    break;
                case "semaine":
                case "semaine(s)":
                    minutesTotal += nbr * 7 * 24 * 60;
                    break;
                case "jour":
                case "jour(s)":
                    minutesTotal += nbr * 24 * 60;
                    break;
                case "heure":
                case "heure(s)":
                    minutesTotal += nbr * 60;
                    break;
                case "minute":
                case "minute(s)":
                    minutesTotal += nbr;
                    break;
            }
        }
        return minutesTotal;
    }

    public String getDebutTexte() {
        return debutTexte;
    }

    public String getDureeTexte() {
        return dureeTexte;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }
}