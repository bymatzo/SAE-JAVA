public class Domaine {
	
    private String nom;

    Domaine(String nom){
    	
    	if(nom == null)
    		throw new IllegalArgumentException("Le nom est null");
    	
    	String nomSansEsp = nom.trim();
    	
    	if(nomSansEsp.equals(""))
    		throw new IllegalArgumentException("Le nom est vide");
    
    	
    		this.nom = nomSansEsp.toLowerCase();   
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
    	
		this.nom = nomSansEsp.toLowerCase();
	}
	
	@Override
	public String toString() {
	    return nom;
	}
}
