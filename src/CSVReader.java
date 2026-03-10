import java.io.*;

public class CSVReader {

    public void charger(HistoriqueReservations historique, String chemin) {

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(chemin), "windows-1252"))) {

            String ligne;

            String header = br.readLine();
            System.out.println(header);

            while ((ligne = br.readLine()) != null) {

                String[] tokens = ligne.split(";");

                String nom = tokens[0];
                String domaineNom = tokens[1];
                String ressourceNom = tokens[2];
                String description = tokens[3];
                String tempsCsv = tokens[4];
                String type = tokens[5].trim();

                Utilisateur u = historique.verifierUtilisateur(new Utilisateur(nom));
                Domaine d = new Domaine(domaineNom);
                Ressource r = historique.verifierRessource(new Ressource(ressourceNom, d, description));

                TempsReservation temp = new TempsReservation(tempsCsv);

                Reservation reservation;

                if (type.equals("Cours")) {
                    reservation = new ReservationCours(u, r, temp.getDateDebut(), temp.getDureeMinutes());
                } else if (type.equals("Emprunt")) {
                    reservation = new ReservationEmprunt(u, r, temp.getDateDebut(), temp.getDureeMinutes());
                } else if (type.equals("Maintenance")) {
                    reservation = new ReservationMaintenance(u, r, temp.getDateDebut(), temp.getDureeMinutes());
                } else {
                    System.out.println("Type inconnu : " + type);
                    continue;
                }

                historique.ajouter(reservation);
            }

        } catch (IOException e) {
            System.out.println("Erreur lors du chargement.");
        }
    }

    public static void main(String[] args) {
        HistoriqueReservations historique = new HistoriqueReservations();
        CSVReader reader = new CSVReader();
        reader.charger(historique, "C:/Wissem/IUT ( SD2 )/S3/Java/Projets JAVA/extraction-2021-2022-anonym.csv");
    }
}