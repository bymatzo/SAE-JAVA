import java.io.*;
import java.time.format.*;
import java.util.*;

public class CSVWriter {

    public void export(HistoriqueReservations historiqueReservation, String chemin) throws IOException {

        BufferedWriter csvWr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(chemin), "windows-1252"));

        String header = "Utilisateur;Domaine;Ressource;Description;Date;Durée;Type";
        csvWr.write(header);
        csvWr.newLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy HH:mm:ss", Locale.FRANCE);

        for (Reservation r : historiqueReservation.getAll()) {

            String utilisateur = r.getUtilisateur().getNom();
            String domaine = r.getRessource().getDomaine().toString();
            String ressource = r.getRessource().getNom();
            String description = r.getRessource().getDescription();
            String dateDebut = r.getDateDebut().format(formatter);
            String dureeTexte = dureeTexte(r.getDureeMinutes());
            String type = r.getType().toString();

            String csvData = utilisateur + ";" + domaine + ";" + ressource + ";" + description + ";" + dateDebut + ";" + dureeTexte + ";" + type;

            csvWr.write(csvData);
            csvWr.newLine();
        }

        csvWr.close();
    }

    private String dureeTexte(int minutes) {

        if (minutes == 0) {
            return "0 minute(s)";
        }

        int semaine = 7 * 24 * 60;
        int jour = 24 * 60;
        int heure = 60;

        int nbSemaines = minutes / semaine;
        minutes = minutes % semaine;

        int nbJours = minutes / jour;
        minutes = minutes % jour;

        int nbHeures = minutes / heure;
        minutes = minutes % heure;

        int nbMinutes = minutes;

        StringBuilder sb = new StringBuilder();

        ajouterPart(sb, nbSemaines, "semaine(s)");
        ajouterPart(sb, nbJours, "jour(s)");
        ajouterPart(sb, nbHeures, "heure(s)");
        ajouterPart(sb, nbMinutes, "minute(s)");

        return sb.toString();
    }

    private void ajouterPart(StringBuilder sb, int valeur, String unite) {
        if (valeur <= 0) {
            return;
        }
        if (sb.length() > 0) {
            sb.append(" et ");
        }
        sb.append(valeur).append(" ").append(unite);
    }
}