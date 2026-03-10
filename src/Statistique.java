import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.RectangleInsets;

public class Statistique {

	private HistoriqueReservations historique;

	public Statistique(HistoriqueReservations historique) {
		this.historique = historique;
	}

	public int getNombreTotalReservations() {
		return historique.getAll().size();
	}

	public int getNombreTotalUtilisateurs() {
		return historique.getUtilisateurs().size();
	}

	public int getDureeMoyenneGlobaleMinutes() {
		List<Reservation> reservations = historique.getAll();
		if (reservations.isEmpty()) {
			return 0;
		}

		int somme = 0;
		for (Reservation r : reservations) {
			somme += r.getDureeMinutes();
		}
		return somme / reservations.size();
	}

	public String getDomaineLePlusReserve() {
		Map<String, Integer> compteurs = new HashMap<>();

		for (Reservation r : historique.getAll()) {
			String domaine = r.getRessource().getDomaine().getNom();
			compteurs.put(domaine, compteurs.getOrDefault(domaine, 0) + 1);
		}

		String meilleurDomaine = "Aucun";
		int max = 0;

		for (Map.Entry<String, Integer> entry : compteurs.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				meilleurDomaine = entry.getKey();
			}
		}

		return meilleurDomaine;
	}

	// Top 10 utilisateurs les plus actifs
	public Map<String, Integer> top10UtilisateursPlusActifs() {

		Map<String, Integer> dataH = new HashMap<>();

		for (Reservation r : historique.getAll()) {
			String nom = r.getUtilisateur().getNom();
			dataH.put(nom, dataH.getOrDefault(nom, 0) + 1);
		}

		List<Map.Entry<String, Integer>> dataListe = new ArrayList<>(dataH.entrySet());

		Collections.sort(dataListe, (a, b) -> b.getValue() - a.getValue());

		if (dataListe.size() > 10) {
			dataListe = dataListe.subList(0, 10);
		}

		Map<String, Integer> top10 = new LinkedHashMap<>();

		for (Map.Entry<String, Integer> entry : dataListe) {
			top10.put(entry.getKey(), entry.getValue());
		}

		return top10;
	}

	public DefaultCategoryDataset datasetTop10UtilisateursPlusActifs() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		Map<String, Integer> top10 = top10UtilisateursPlusActifs();

		for (Map.Entry<String, Integer> entry : top10.entrySet()) {
			dataset.addValue(entry.getValue(), "Réservations", entry.getKey());
		}

		return dataset;
	}

	public JFreeChart graphiqueTop10UtilisateursPlusActifs() {

		DefaultCategoryDataset dataset = datasetTop10UtilisateursPlusActifs();

		JFreeChart chart = ChartFactory.createBarChart(
				"Top 10 des utilisateurs les plus actifs",
				"Utilisateurs",
				"Nombre de réservations",
				dataset,
				PlotOrientation.HORIZONTAL,
				false,
				true,
				false);

		CategoryPlot plot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();

		renderer.setSeriesPaint(0, new Color(91, 159, 180));
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);

		chart.setBackgroundPaint(Color.WHITE);
		plot.setBackgroundPaint(new Color(245, 247, 248));
		plot.setRangeGridlinePaint(Color.WHITE);

		chart.getTitle().setFont(new Font("Arial", Font.BOLD, 22));
		plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 16));
		plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 16));
		plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));
		plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));
		plot.setInsets(new RectangleInsets(10, 10, 10, 10));

		return chart;
	}

	// Camembert des types de réservation
	public Map<String, Integer> camembertpartype() {

		Map<String, Integer> dataC = new HashMap<>();

		for (Reservation r : historique.getAll()) {
			String type = r.getType().toString();
			dataC.put(type, dataC.getOrDefault(type, 0) + 1);
		}

		return dataC;
	}

	public DefaultPieDataset datasetRepartitionTypesReservation() {

		DefaultPieDataset dataset = new DefaultPieDataset();

		Map<String, Integer> types = camembertpartype();

		for (Map.Entry<String, Integer> entry : types.entrySet()) {
			dataset.setValue(entry.getKey(), entry.getValue());
		}

		return dataset;
	}

	public JFreeChart graphiqueRepartitionTypesReservation() {

		DefaultPieDataset dataset = datasetRepartitionTypesReservation();

		JFreeChart chart = ChartFactory.createPieChart(
				"Répartition des types de réservation",
				dataset,
				true,
				true,
				false);

		PiePlot plot = (PiePlot) chart.getPlot();

		plot.setSectionPaint("COURS", new Color(91, 159, 180));
		plot.setSectionPaint("EMPRUNT", new Color(124, 196, 216));
		plot.setSectionPaint("MAINTENANCE", new Color(62, 111, 126));

		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} : {1} ({2})",
				new DecimalFormat("0"),
				new DecimalFormat("0%")));

		chart.setBackgroundPaint(Color.WHITE);
		plot.setBackgroundPaint(new Color(245, 247, 248));
		plot.setOutlinePaint(Color.WHITE);
		plot.setInteriorGap(0.08);

		chart.getTitle().setFont(new Font("Arial", Font.BOLD, 22));

		return chart;
	}

	// Evolution du nombre de réservations par mois
	public Map<String, Integer> evolutionReservationsParMois() {

		Map<String, Integer> dataE = new TreeMap<>();

		for (Reservation r : historique.getAll()) {

			int annee = r.getDateDebut().getYear();
			int mois = r.getDateDebut().getMonthValue();

			String cle = annee + "-" + String.format("%02d", mois);

			dataE.put(cle, dataE.getOrDefault(cle, 0) + 1);
		}

		return dataE;
	}

	public DefaultCategoryDataset datasetEvolutionReservationsParMois() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		Map<String, Integer> evolution = evolutionReservationsParMois();

		for (Map.Entry<String, Integer> entry : evolution.entrySet()) {
			dataset.addValue(entry.getValue(), "Réservations", entry.getKey());
		}

		return dataset;
	}

	public JFreeChart graphiqueEvolutionReservationsParMois() {

		DefaultCategoryDataset dataset = datasetEvolutionReservationsParMois();

		JFreeChart chart = ChartFactory.createLineChart(
				"Evolution du nombre de réservations par mois",
				"Mois",
				"Nombre de réservations",
				dataset,
				PlotOrientation.VERTICAL,
				false,
				true,
				false);

		CategoryPlot plot = chart.getCategoryPlot();
		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

		renderer.setSeriesPaint(0, new Color(91, 159, 180));
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));

		chart.setBackgroundPaint(Color.WHITE);
		plot.setBackgroundPaint(new Color(245, 247, 248));
		plot.setRangeGridlinePaint(Color.WHITE);

		chart.getTitle().setFont(new Font("Arial", Font.BOLD, 22));
		plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 16));
		plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 16));
		plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));
		plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));

		CategoryAxis axis = plot.getDomainAxis();
		axis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0));
		axis.setMaximumCategoryLabelLines(2);

		plot.setInsets(new RectangleInsets(10, 10, 30, 10));

		return chart;
	}

	// Durée moyenne par domaine
	public Map<String, Integer> dureeMoyenneParDomaine() {

		Map<String, Integer> sommeDurees = new HashMap<>();
		Map<String, Integer> compteurs = new HashMap<>();

		for (Reservation r : historique.getAll()) {

			String domaine = r.getRessource().getDomaine().getNom();
			int duree = r.getDureeMinutes();

			sommeDurees.put(domaine, sommeDurees.getOrDefault(domaine, 0) + duree);
			compteurs.put(domaine, compteurs.getOrDefault(domaine, 0) + 1);
		}

		Map<String, Integer> moyennes = new HashMap<>();

		for (String domaine : sommeDurees.keySet()) {
			int somme = sommeDurees.get(domaine);
			int nb = compteurs.get(domaine);
			moyennes.put(domaine, somme / nb);
		}

		return moyennes;
	}

	public DefaultCategoryDataset datasetDureeMoyenneParDomaine() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		Map<String, Integer> moyennes = dureeMoyenneParDomaine();

		for (Map.Entry<String, Integer> entry : moyennes.entrySet()) {
			dataset.addValue(entry.getValue(), "Durée moyenne", entry.getKey());
		}

		return dataset;
	}

	public JFreeChart graphiqueDureeMoyenneParDomaine() {

		DefaultCategoryDataset dataset = datasetDureeMoyenneParDomaine();

		JFreeChart chart = ChartFactory.createBarChart(
				"Durée moyenne par domaine",
				"Domaine",
				"Durée moyenne (minutes)",
				dataset,
				PlotOrientation.VERTICAL,
				false,
				true,
				false);

		CategoryPlot plot = chart.getCategoryPlot();
		BarRenderer renderer = (BarRenderer) plot.getRenderer();

		renderer.setSeriesPaint(0, new Color(91, 159, 180));
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);

		chart.setBackgroundPaint(Color.WHITE);
		plot.setBackgroundPaint(new Color(245, 247, 248));
		plot.setRangeGridlinePaint(Color.WHITE);

		chart.getTitle().setFont(new Font("Arial", Font.BOLD, 22));
		plot.getDomainAxis().setLabelFont(new Font("Arial", Font.BOLD, 16));
		plot.getRangeAxis().setLabelFont(new Font("Arial", Font.BOLD, 16));
		plot.getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 13));
		plot.getRangeAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 14));

		CategoryAxis axis = plot.getDomainAxis();
		axis.setCategoryLabelPositions(
				CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0));
		axis.setMaximumCategoryLabelLines(2);

		plot.setInsets(new RectangleInsets(10, 10, 30, 10));

		return chart;
	}
}