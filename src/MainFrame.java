import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.jfree.chart.ChartPanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JPanel mainPanel;

	private HistoriqueReservations historique;
	private CSVReader reader;
	private CSVWriter writer;

	private String pageCourante = "utilisateurs";

	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainFrame() {
		historique = new HistoriqueReservations();
		reader = new CSVReader();
		writer = new CSVWriter();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1400, 900);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel sidebar = new JPanel();
		sidebar.setBackground(new Color(36, 68, 84));
		sidebar.setPreferredSize(new Dimension(210, 0));
		sidebar.setLayout(new BorderLayout());
		contentPane.add(sidebar, BorderLayout.WEST);

		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(36, 68, 84));
		topPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		sidebar.add(topPanel, BorderLayout.NORTH);

		JLabel lblLogo = new JLabel("IUT Lyon 2");
		lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblLogo.setForeground(Color.WHITE);
		lblLogo.setFont(new Font("Arial", Font.BOLD, 22));
		topPanel.add(lblLogo);

		topPanel.add(Box.createRigidArea(new Dimension(0, 8)));

		JLabel lblSousTitre = new JLabel("IUT Lumière - SAE Java");
		lblSousTitre.setAlignmentX(Component.LEFT_ALIGNMENT);
		lblSousTitre.setForeground(new Color(210, 220, 225));
		lblSousTitre.setFont(new Font("Arial", Font.PLAIN, 15));
		topPanel.add(lblSousTitre);

		JPanel centerPanel = new JPanel();
		centerPanel.setBackground(new Color(36, 68, 84));
		centerPanel.setBorder(new EmptyBorder(20, 15, 20, 15));
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		sidebar.add(centerPanel, BorderLayout.CENTER);

		JButton btnUtilisateurs = creerBoutonMenu("Utilisateurs");
		JButton btnRessources = creerBoutonMenu("Ressources");
		JButton btnReservations = creerBoutonMenu("Réservations");
		JButton btnStatistiques = creerBoutonMenu("Statistiques");

		centerPanel.add(btnUtilisateurs);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		centerPanel.add(btnRessources);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		centerPanel.add(btnReservations);
		centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		centerPanel.add(btnStatistiques);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(36, 68, 84));
		bottomPanel.setBorder(new EmptyBorder(20, 15, 20, 15));
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		sidebar.add(bottomPanel, BorderLayout.SOUTH);

		JButton btnImport = creerBoutonBas("Charger CSV");
		JButton btnExport = creerBoutonBas("Exporter CSV");

		bottomPanel.add(btnImport);
		bottomPanel.add(Box.createRigidArea(new Dimension(0, 12)));
		bottomPanel.add(btnExport);

		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(245, 247, 248));
		mainPanel.setLayout(new BorderLayout());
		contentPane.add(mainPanel, BorderLayout.CENTER);

		btnUtilisateurs.addActionListener(e -> afficherPageUtilisateurs());
		btnReservations.addActionListener(e -> afficherPageReservations());
		btnRessources.addActionListener(e -> afficherPageRessources());
		btnStatistiques.addActionListener(e -> afficherPageStatistiques());

		btnImport.addActionListener(e -> chargerDepuisCSV());
		btnExport.addActionListener(e -> exporterCSV());

		afficherPageUtilisateurs();
	}

	private void afficherPageUtilisateurs() {
		pageCourante = "utilisateurs";
		mainPanel.removeAll();

		JPanel pagePanel = creerPageAvecTitre("Utilisateurs");
		mainPanel.add(pagePanel, BorderLayout.CENTER);

		String[] colonnes = { "Nom", "Nombre de réservations", "Statut" };
		DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};

		for (Utilisateur u : historique.getUtilisateurs()) {
			int nbReservations = historique.rechercherParUtilisateur(u).size();
			model.addRow(new Object[] { u.getNom(), nbReservations, "Actif" });
		}

		JTable table = new JTable(model);
		configurerTableCentree(table, 35, 16, 16);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(190, 205, 215), 1));
		pagePanel.add(scrollPane, BorderLayout.CENTER);

		JPanel boutonsPanel = creerPanelBoutonsBas();
		JButton btnAjouter = creerBoutonAction("Ajouter");
		JButton btnModifier = creerBoutonAction("Modifier");
		JButton btnSupprimer = creerBoutonAction("Supprimer");

		btnAjouter.addActionListener(e -> ajouterUtilisateur());
		btnModifier.addActionListener(e -> modifierUtilisateur(table));
		btnSupprimer.addActionListener(e -> supprimerUtilisateur(table));

		boutonsPanel.add(btnAjouter);
		boutonsPanel.add(btnModifier);
		boutonsPanel.add(btnSupprimer);
		pagePanel.add(boutonsPanel, BorderLayout.SOUTH);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void afficherPageReservations() {
		pageCourante = "reservations";
		mainPanel.removeAll();

		JPanel pagePanel = creerPageAvecTitre("Réservations");
		mainPanel.add(pagePanel, BorderLayout.CENTER);

		String[] colonnes = { "ID", "Utilisateur", "Ressource", "Domaine", "Début", "Durée", "Type" };
		DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		for (Reservation r : historique.getAll()) {
			model.addRow(new Object[] {
					r.getId(),
					r.getUtilisateur().getNom(),
					r.getRessource().getNom(),
					r.getRessource().getDomaine().getNom(),
					r.getDateDebut().format(formatter),
					dureeTexte(r.getDureeMinutes()),
					r.getType().toString()
			});
		}

		JTable table = new JTable(model);
		configurerTableCentree(table, 38, 15, 15);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(190, 205, 215), 1));
		pagePanel.add(scrollPane, BorderLayout.CENTER);

		JPanel boutonsPanel = creerPanelBoutonsBas();
		JButton btnAjouter = creerBoutonAction("Ajouter");
		JButton btnModifier = creerBoutonAction("Modifier");
		JButton btnSupprimer = creerBoutonAction("Supprimer");

		btnAjouter.addActionListener(e -> ajouterReservation());
		btnModifier.addActionListener(e -> modifierReservation(table));
		btnSupprimer.addActionListener(e -> supprimerReservation(table));

		boutonsPanel.add(btnAjouter);
		boutonsPanel.add(btnModifier);
		boutonsPanel.add(btnSupprimer);
		pagePanel.add(boutonsPanel, BorderLayout.SOUTH);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void afficherPageRessources() {
		pageCourante = "ressources";
		mainPanel.removeAll();

		JPanel pagePanel = creerPageAvecTitre("Ressources");
		mainPanel.add(pagePanel, BorderLayout.CENTER);

		String[] colonnes = { "Nom", "Domaine", "Description", "Nombre de réservations" };
		DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) { return false; }
		};

		for (Ressource r : historique.getRessources()) {
			int nbReservations = historique.rechercherParRessource(r).size();
			model.addRow(new Object[] {
					r.getNom(),
					r.getDomaine().getNom(),
					r.getDescription(),
					nbReservations
			});
		}

		JTable table = new JTable(model);
		configurerTableCentree(table, 35, 15, 15);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createLineBorder(new Color(190, 205, 215), 1));
		pagePanel.add(scrollPane, BorderLayout.CENTER);

		JPanel boutonsPanel = creerPanelBoutonsBas();
		JButton btnAjouter = creerBoutonAction("Ajouter");
		JButton btnModifier = creerBoutonAction("Modifier");
		JButton btnSupprimer = creerBoutonAction("Supprimer");

		btnAjouter.addActionListener(e -> ajouterRessource());
		btnModifier.addActionListener(e -> modifierRessource(table));
		btnSupprimer.addActionListener(e -> supprimerRessource(table));

		boutonsPanel.add(btnAjouter);
		boutonsPanel.add(btnModifier);
		boutonsPanel.add(btnSupprimer);
		pagePanel.add(boutonsPanel, BorderLayout.SOUTH);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void afficherPageStatistiques() {
		pageCourante = "statistiques";
		mainPanel.removeAll();

		JPanel pagePanel = creerPageAvecTitre("Statistiques");
		mainPanel.add(pagePanel, BorderLayout.CENTER);

		Statistique statistique = new Statistique(historique);

		JPanel contenuStats = new JPanel();
		contenuStats.setBackground(new Color(245, 247, 248));
		contenuStats.setLayout(new BoxLayout(contenuStats, BoxLayout.Y_AXIS));
		contenuStats.setBorder(new EmptyBorder(4, 0, 4, 0));

		// KPI cards
		JPanel kpiPanel = new JPanel(new GridLayout(1, 4, 8, 0));
		kpiPanel.setBackground(new Color(245, 247, 248));
		kpiPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
		kpiPanel.setPreferredSize(new Dimension(100, 70));
		kpiPanel.add(creerCarteKPI("Réservations totales", String.valueOf(statistique.getNombreTotalReservations())));
		kpiPanel.add(creerCarteKPI("Utilisateurs totaux", String.valueOf(statistique.getNombreTotalUtilisateurs())));
		kpiPanel.add(creerCarteKPI("Durée moyenne", statistique.getDureeMoyenneGlobaleMinutes() + " min"));
		kpiPanel.add(creerCarteKPI("Domaine le plus réservé", statistique.getDomaineLePlusReserve()));

		// Ligne du haut : 2 graphiques côte à côte (hauteur +25% : 360 → 450)
		JPanel topRow = new JPanel(new GridLayout(1, 2, 8, 0));
		topRow.setBackground(new Color(245, 247, 248));
		topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 450));
		topRow.setPreferredSize(new Dimension(100, 450));

		ChartPanel chartTop10 = new ChartPanel(statistique.graphiqueTop10UtilisateursPlusActifs());
		ChartPanel chartTypes = new ChartPanel(statistique.graphiqueRepartitionTypesReservation());
		chartTop10.setPreferredSize(new Dimension(100, 450));
		chartTypes.setPreferredSize(new Dimension(100, 450));
		topRow.add(creerBlocGraphique(chartTop10));
		topRow.add(creerBlocGraphique(chartTypes));

		// Graphique évolution (hauteur +25% : 340 → 425)
		ChartPanel chartEvolution = new ChartPanel(statistique.graphiqueEvolutionReservationsParMois());
		chartEvolution.setPreferredSize(new Dimension(100, 425));
		JPanel blocEvolution = creerBlocGraphique(chartEvolution);
		blocEvolution.setMaximumSize(new Dimension(Integer.MAX_VALUE, 425));
		blocEvolution.setPreferredSize(new Dimension(100, 425));

		// Graphique durée moyenne par domaine (hauteur +25% : 340 → 425)
		ChartPanel chartDuree = new ChartPanel(statistique.graphiqueDureeMoyenneParDomaine());
		chartDuree.setPreferredSize(new Dimension(100, 425));
		JPanel blocDuree = creerBlocGraphique(chartDuree);
		blocDuree.setMaximumSize(new Dimension(Integer.MAX_VALUE, 425));
		blocDuree.setPreferredSize(new Dimension(100, 425));

		contenuStats.add(kpiPanel);
		contenuStats.add(Box.createRigidArea(new Dimension(0, 8)));
		contenuStats.add(topRow);
		contenuStats.add(Box.createRigidArea(new Dimension(0, 8)));
		contenuStats.add(blocEvolution);
		contenuStats.add(Box.createRigidArea(new Dimension(0, 8)));
		contenuStats.add(blocDuree);

		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setBackground(new Color(245, 247, 248));
		wrapper.add(contenuStats, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane(
				wrapper,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.getViewport().setBackground(new Color(245, 247, 248));

		pagePanel.add(scrollPane, BorderLayout.CENTER);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private JPanel creerBlocGraphique(ChartPanel chartPanel) {
		JPanel bloc = new JPanel(new BorderLayout());
		bloc.setBackground(Color.WHITE);
		bloc.setBorder(BorderFactory.createLineBorder(new Color(190, 205, 215), 1));
		bloc.add(chartPanel, BorderLayout.CENTER);
		return bloc;
	}

	private JPanel creerCarteKPI(String titre, String valeur) {
		JPanel carte = new JPanel();
		carte.setBackground(Color.WHITE);
		carte.setBorder(BorderFactory.createLineBorder(new Color(190, 205, 215), 1));
		carte.setLayout(new BorderLayout());
		carte.setPreferredSize(new Dimension(100, 70));
		carte.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

		JPanel contenu = new JPanel();
		contenu.setBackground(Color.WHITE);
		contenu.setBorder(new EmptyBorder(6, 10, 6, 10));
		contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));

		JLabel lblTitre = new JLabel(titre);
		lblTitre.setForeground(new Color(90, 110, 125));
		lblTitre.setFont(new Font("Arial", Font.PLAIN, 12));
		lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

		JLabel lblValeur = new JLabel(valeur);
		lblValeur.setForeground(new Color(35, 64, 84));
		lblValeur.setFont(new Font("Arial", Font.BOLD, 14));
		lblValeur.setAlignmentX(Component.LEFT_ALIGNMENT);

		contenu.add(lblTitre);
		contenu.add(Box.createRigidArea(new Dimension(0, 3)));
		contenu.add(lblValeur);

		carte.add(contenu, BorderLayout.CENTER);
		return carte;
	}

	private JPanel creerPageAvecTitre(String titreTexte) {
		JPanel pagePanel = new JPanel();
		pagePanel.setBackground(new Color(245, 247, 248));
		pagePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		pagePanel.setLayout(new BorderLayout(0, 16));

		JLabel titre = new JLabel(titreTexte);
		titre.setFont(new Font("Arial", Font.BOLD, 28));
		titre.setForeground(new Color(35, 64, 84));
		pagePanel.add(titre, BorderLayout.NORTH);

		return pagePanel;
	}

	private JPanel creerPanelBoutonsBas() {
		JPanel boutonsPanel = new JPanel();
		boutonsPanel.setBackground(new Color(245, 247, 248));
		boutonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
		return boutonsPanel;
	}

	private JButton creerBoutonAction(String texte) {
		JButton bouton = new JButton(texte);
		bouton.setFont(new Font("Arial", Font.BOLD, 16));
		return bouton;
	}

	private void configurerTableCentree(JTable table, int rowHeight, int fontSize, int headerFontSize) {
		table.setRowHeight(rowHeight);
		table.setFont(new Font("Arial", Font.PLAIN, fontSize));
		table.setForeground(new Color(35, 64, 84));
		table.setGridColor(new Color(210, 220, 225));
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(true);

		DefaultTableCellRenderer centreRenderer = new DefaultTableCellRenderer();
		centreRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < table.getColumnCount(); i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(centreRenderer);
		}

		JTableHeader header = table.getTableHeader();
		header.setFont(new Font("Arial", Font.BOLD, headerFontSize));
		header.setBackground(new Color(92, 150, 175));
		header.setForeground(Color.WHITE);
		header.setReorderingAllowed(false);

		DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
		headerRenderer.setHorizontalAlignment(JLabel.CENTER);
	}

	private void ajouterUtilisateur() {
		String nom = JOptionPane.showInputDialog(this, "Nom du nouvel utilisateur :");
		if (nom == null) return;
		try {
			historique.verifierUtilisateur(new Utilisateur(nom));
			rafraichirPageCourante();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void modifierUtilisateur(JTable table) {
		int ligne = table.getSelectedRow();
		if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionne un utilisateur."); return; }

		String ancienNom = table.getValueAt(ligne, 0).toString();
		Utilisateur utilisateur = historique.getUtilisateurParNom(ancienNom);
		if (utilisateur == null) { JOptionPane.showMessageDialog(this, "Utilisateur introuvable."); return; }

		String nouveauNom = JOptionPane.showInputDialog(this, "Nouveau nom :", utilisateur.getNom());
		if (nouveauNom == null) return;
		try {
			utilisateur.setNom(nouveauNom);
			rafraichirPageCourante();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void supprimerUtilisateur(JTable table) {
		int ligne = table.getSelectedRow();
		if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionne un utilisateur."); return; }
		historique.supprimerUtilisateurParNom(table.getValueAt(ligne, 0).toString());
		rafraichirPageCourante();
	}

	private void ajouterRessource() {
		String nom = JOptionPane.showInputDialog(this, "Nom de la ressource :");
		if (nom == null) return;
		String domaineNom = JOptionPane.showInputDialog(this, "Domaine :");
		if (domaineNom == null) return;
		String description = JOptionPane.showInputDialog(this, "Description :");
		if (description == null) return;
		try {
			Domaine domaine = new Domaine(domaineNom);
			Ressource ressource = new Ressource(nom, domaine, description);
			historique.verifierRessource(ressource);
			rafraichirPageCourante();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void modifierRessource(JTable table) {
		int ligne = table.getSelectedRow();
		if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionne une ressource."); return; }

		String ancienNom = table.getValueAt(ligne, 0).toString();
		Ressource ressource = historique.getRessourceParNom(ancienNom);
		if (ressource == null) { JOptionPane.showMessageDialog(this, "Ressource introuvable."); return; }

		String nouveauNom = JOptionPane.showInputDialog(this, "Nouveau nom :", ressource.getNom());
		if (nouveauNom == null) return;
		String nouveauDomaine = JOptionPane.showInputDialog(this, "Nouveau domaine :", ressource.getDomaine().getNom());
		if (nouveauDomaine == null) return;
		String nouvelleDescription = JOptionPane.showInputDialog(this, "Nouvelle description :", ressource.getDescription());
		if (nouvelleDescription == null) return;

		try {
			ressource.setNom(nouveauNom);
			ressource.setDomaine(new Domaine(nouveauDomaine));
			ressource.setDescription(nouvelleDescription);
			rafraichirPageCourante();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void supprimerRessource(JTable table) {
		int ligne = table.getSelectedRow();
		if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionne une ressource."); return; }
		historique.supprimerRessourceParNom(table.getValueAt(ligne, 0).toString());
		rafraichirPageCourante();
	}

	private void ajouterReservation() {
		List<Utilisateur> utilisateurs = historique.getUtilisateurs();
		List<Ressource> ressources = historique.getRessources();

		if (utilisateurs.isEmpty() || ressources.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Il faut au moins un utilisateur et une ressource.");
			return;
		}

		String utilisateurNom = (String) JOptionPane.showInputDialog(this, "Choisir l'utilisateur :", "Nouvelle réservation",
				JOptionPane.QUESTION_MESSAGE, null, listeNomsUtilisateurs(), listeNomsUtilisateurs()[0]);
		if (utilisateurNom == null) return;

		String ressourceNom = (String) JOptionPane.showInputDialog(this, "Choisir la ressource :", "Nouvelle réservation",
				JOptionPane.QUESTION_MESSAGE, null, listeNomsRessources(), listeNomsRessources()[0]);
		if (ressourceNom == null) return;

		String typeChoisi = (String) JOptionPane.showInputDialog(this, "Choisir le type :", "Nouvelle réservation",
				JOptionPane.QUESTION_MESSAGE, null, new String[] { "EMPRUNT", "COURS", "MAINTENANCE" }, "EMPRUNT");
		if (typeChoisi == null) return;

		String dateTexte = JOptionPane.showInputDialog(this, "Date début (yyyy-MM-dd HH:mm) :");
		if (dateTexte == null) return;
		String dureeTexte = JOptionPane.showInputDialog(this, "Durée en minutes :");
		if (dureeTexte == null) return;

		try {
			Utilisateur utilisateur = historique.getUtilisateurParNom(utilisateurNom);
			Ressource ressource = historique.getRessourceParNom(ressourceNom);
			LocalDateTime dateDebut = LocalDateTime.parse(dateTexte, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			int duree = Integer.parseInt(dureeTexte);
			historique.ajouter(creerReservationDepuisType(typeChoisi, utilisateur, ressource, dateDebut, duree));
			rafraichirPageCourante();
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "Format de date invalide. Utilise yyyy-MM-dd HH:mm");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "La durée doit être un entier.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void modifierReservation(JTable table) {
		int ligne = table.getSelectedRow();
		if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionne une réservation."); return; }

		int id = Integer.parseInt(table.getValueAt(ligne, 0).toString());
		Reservation reservation = historique.getReservationParId(id);
		if (reservation == null) { JOptionPane.showMessageDialog(this, "Réservation introuvable."); return; }

		String utilisateurNom = (String) JOptionPane.showInputDialog(this, "Choisir l'utilisateur :", "Modifier réservation",
				JOptionPane.QUESTION_MESSAGE, null, listeNomsUtilisateurs(), reservation.getUtilisateur().getNom());
		if (utilisateurNom == null) return;

		String ressourceNom = (String) JOptionPane.showInputDialog(this, "Choisir la ressource :", "Modifier réservation",
				JOptionPane.QUESTION_MESSAGE, null, listeNomsRessources(), reservation.getRessource().getNom());
		if (ressourceNom == null) return;

		String typeChoisi = (String) JOptionPane.showInputDialog(this, "Choisir le type :", "Modifier réservation",
				JOptionPane.QUESTION_MESSAGE, null, new String[] { "EMPRUNT", "COURS", "MAINTENANCE" }, reservation.getType().toString());
		if (typeChoisi == null) return;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String dateTexte = JOptionPane.showInputDialog(this, "Date début (yyyy-MM-dd HH:mm) :", reservation.getDateDebut().format(formatter));
		if (dateTexte == null) return;
		String dureeTexte = JOptionPane.showInputDialog(this, "Durée en minutes :", String.valueOf(reservation.getDureeMinutes()));
		if (dureeTexte == null) return;

		try {
			Utilisateur utilisateur = historique.getUtilisateurParNom(utilisateurNom);
			Ressource ressource = historique.getRessourceParNom(ressourceNom);
			LocalDateTime dateDebut = LocalDateTime.parse(dateTexte, formatter);
			int duree = Integer.parseInt(dureeTexte);
			historique.modifierReservation(id, creerReservationDepuisType(typeChoisi, utilisateur, ressource, dateDebut, duree));
			rafraichirPageCourante();
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "Format de date invalide. Utilise yyyy-MM-dd HH:mm");
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "La durée doit être un entier.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void supprimerReservation(JTable table) {
		int ligne = table.getSelectedRow();
		if (ligne == -1) { JOptionPane.showMessageDialog(this, "Sélectionne une réservation."); return; }
		historique.supprimerParId(Integer.parseInt(table.getValueAt(ligne, 0).toString()));
		rafraichirPageCourante();
	}

	private Reservation creerReservationDepuisType(String typeChoisi, Utilisateur utilisateur, Ressource ressource,
			LocalDateTime dateDebut, int duree) {
		if (typeChoisi.equals("COURS")) return new ReservationCours(utilisateur, ressource, dateDebut, duree);
		if (typeChoisi.equals("MAINTENANCE")) return new ReservationMaintenance(utilisateur, ressource, dateDebut, duree);
		return new ReservationEmprunt(utilisateur, ressource, dateDebut, duree);
	}

	private String[] listeNomsUtilisateurs() {
		List<Utilisateur> utilisateurs = historique.getUtilisateurs();
		String[] noms = new String[utilisateurs.size()];
		for (int i = 0; i < utilisateurs.size(); i++) noms[i] = utilisateurs.get(i).getNom();
		return noms;
	}

	private String[] listeNomsRessources() {
		List<Ressource> ressources = historique.getRessources();
		String[] noms = new String[ressources.size()];
		for (int i = 0; i < ressources.size(); i++) noms[i] = ressources.get(i).getNom();
		return noms;
	}

	private void chargerDepuisCSV() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choisir le fichier CSV à charger");
		if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
		try {
			historique = new HistoriqueReservations();
			reader.charger(historique, chooser.getSelectedFile().getAbsolutePath());
			rafraichirPageCourante();
			JOptionPane.showMessageDialog(this, "CSV chargé avec succès.");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erreur lors du chargement du CSV.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void exporterCSV() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Choisir le fichier d'export CSV");
		if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
		String chemin = chooser.getSelectedFile().getAbsolutePath();
		if (!chemin.toLowerCase(Locale.ROOT).endsWith(".csv")) chemin += ".csv";
		try {
			writer.export(historique, chemin);
			JOptionPane.showMessageDialog(this, "Export réalisé avec succès.");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erreur lors de l'export CSV.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void rafraichirPageCourante() {
		switch (pageCourante) {
			case "utilisateurs"  -> afficherPageUtilisateurs();
			case "reservations"  -> afficherPageReservations();
			case "ressources"    -> afficherPageRessources();
			case "statistiques"  -> afficherPageStatistiques();
		}
	}

	private String dureeTexte(int minutes) {
		if (minutes == 0) return "0 minute(s)";
		int nbSemaines = minutes / (7 * 24 * 60); minutes %= (7 * 24 * 60);
		int nbJours    = minutes / (24 * 60);      minutes %= (24 * 60);
		int nbHeures   = minutes / 60;             minutes %= 60;
		int nbMinutes  = minutes;
		StringBuilder sb = new StringBuilder();
		ajouterPart(sb, nbSemaines, "semaine(s)");
		ajouterPart(sb, nbJours,    "jour(s)");
		ajouterPart(sb, nbHeures,   "heure(s)");
		ajouterPart(sb, nbMinutes,  "minute(s)");
		return sb.toString();
	}

	private void ajouterPart(StringBuilder sb, int valeur, String unite) {
		if (valeur <= 0) return;
		if (sb.length() > 0) sb.append(" et ");
		sb.append(valeur).append(" ").append(unite);
	}

	private JButton creerBoutonMenu(String texte) {
		JButton bouton = new JButton(texte);
		bouton.setAlignmentX(Component.LEFT_ALIGNMENT);
		bouton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
		bouton.setPreferredSize(new Dimension(230, 50));
		bouton.setBackground(new Color(68, 96, 110));
		bouton.setForeground(Color.WHITE);
		bouton.setFont(new Font("Arial", Font.BOLD, 18));
		bouton.setFocusPainted(false);
		bouton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		return bouton;
	}

	private JButton creerBoutonBas(String texte) {
		JButton bouton = new JButton(texte);
		bouton.setAlignmentX(Component.LEFT_ALIGNMENT);
		bouton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
		bouton.setPreferredSize(new Dimension(230, 45));
		bouton.setBackground(new Color(52, 86, 102));
		bouton.setForeground(Color.WHITE);
		bouton.setFont(new Font("Arial", Font.PLAIN, 16));
		bouton.setFocusPainted(false);
		bouton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		return bouton;
	}
}