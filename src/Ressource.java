public class Ressource {

    private String nom;
    private String description;
    private Domaine domaine;

    public Ressource(String nom, Domaine domaine, String description) {
    	
        if (nom == null) {
            throw new IllegalArgumentException("Le nom de la ressource est null.");
        }
        if (domaine == null) {
            throw new IllegalArgumentException("Le domaine de la ressource est null.");
        }

        String nomSansEsp = nom.trim();
        if (nomSansEsp.equals("")) {
            throw new IllegalArgumentException("Le nom de la ressource est vide.");
        }

        String descriptionFinale;

        if (description == null) {
            descriptionFinale = "Non renseignée";
        } else {
            String descSansEsp = description.trim();
            if (descSansEsp.equals("")) {
                descriptionFinale = "Non renseignée";
            } else {
                descriptionFinale = descSansEsp;
            }
        }
        
        this.nom = nomSansEsp;
        this.domaine = domaine;
        this.description = descriptionFinale;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom == null) {
            throw new IllegalArgumentException("Le nom de la ressource est null.");
        }

        String nomSansEsp = nom.trim();
        if (nomSansEsp.equals("")) {
            throw new IllegalArgumentException("Le nom de la ressource est vide.");
        }

        this.nom = nomSansEsp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = "Non renseignée";
        } else {
            String descSansEsp = description.trim();
            if (descSansEsp.equals("")) {
                this.description = "Non renseignée";
            } else {
                this.description = descSansEsp;
            }
        }
    }

    public Domaine getDomaine() {
        return domaine;
    }

    public void setDomaine(Domaine domaine) {
        if (domaine == null) {
            throw new IllegalArgumentException("Le domaine de la ressource est null.");
        }
        this.domaine = domaine;
    }

    @Override
    public String toString() {
        return nom + " - " + description + " (" + domaine.getNom() + ")";
    }
}