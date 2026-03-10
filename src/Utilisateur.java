public class Utilisateur {
	
    private String nom;
    
    
    Utilisateur(String nom){
    	
    	if(nom == null)
    		throw new IllegalArgumentException("Le nom est null");
    	
    	String nomSansEsp = nom.trim();
    	
    	if(nomSansEsp.equals(""))
    		throw new IllegalArgumentException("Le nom est vide");
    		
    		this.nom = nomSansEsp;
    }
    
    
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		
		if(nom == null)
    		throw new IllegalArgumentException("Le nom est null");
    	
    	String nomSansEsp = nom.trim();
    	
    	if(nomSansEsp.equals(""))
    		throw new IllegalArgumentException("Le nom est vide");
    		
		this.nom = nomSansEsp;
	}
}

