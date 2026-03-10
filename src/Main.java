import javax.swing.JFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        HistoriqueReservations historique = new HistoriqueReservations();

        Utilisateur u1 = new Utilisateur("Simon");
        Utilisateur u2 = new Utilisateur("Matéo");

        Domaine d1 = new Domaine("informatique");
        Domaine d2 = new Domaine("multimedia");
        Domaine d3 = new Domaine("reseau");

        Ressource r1 = new Ressource("Salle info", d1, "Salle informatique");
        Ressource r2 = new Ressource("Projecteur", d2, "Projecteur HD");
        Ressource r3 = new Ressource("Routeur", d3, "Routeur test");

        historique.ajouter(new ReservationCours(u1, r1, LocalDateTime.of(2026,1,5,10,0),120));
        historique.ajouter(new ReservationCours(u2, r1, LocalDateTime.of(2026,1,8,9,0),90));
        historique.ajouter(new ReservationEmprunt(u1, r1, LocalDateTime.of(2026,1,12,14,0),60));

        historique.ajouter(new ReservationCours(u1, r2, LocalDateTime.of(2026,2,3,10,0),45));
        historique.ajouter(new ReservationEmprunt(u2, r2, LocalDateTime.of(2026,2,10,13,0),60));

        historique.ajouter(new ReservationMaintenance(u1, r3, LocalDateTime.of(2026,3,4,15,0),30));
        historique.ajouter(new ReservationCours(u2, r3, LocalDateTime.of(2026,3,12,11,0),60));

        Statistique stat = new Statistique(historique);

        JFreeChart chart = stat.graphiqueDureeMoyenneParDomaine();

        ChartPanel panel = new ChartPanel(chart);

        JFrame frame = new JFrame("Durée moyenne par domaine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,600);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        frame.setVisible(true);
    }
}