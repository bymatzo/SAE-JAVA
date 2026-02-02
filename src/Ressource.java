public class Ressource {

    private String nom;
    private Domaine domaine;

    public Ressource(String nom, Domaine domaine) {
        
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

        this.nom = nomSansEsp;
        this.domaine = domaine;
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
        return nom + " (" + domaine.getNom() + ")";
    }
}
