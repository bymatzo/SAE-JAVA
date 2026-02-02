public class Utilisateur {
	
    private String nomAnonyme;
    
    
    Utilisateur(String nomAnonyme){
    	
    	if(nomAnonyme == null)
    		throw new IllegalArgumentException("Le nom est null");
    	
    	String nomSansEsp = nomAnonyme.trim();
    	
    	if(nomSansEsp.equals(""))
    		throw new IllegalArgumentException("Le nom est vide");
    		
    		this.nomAnonyme = nomSansEsp;
    }
    
    
	public String getNomAnonyme() {
		return nomAnonyme;
	}

	public void setNomAnonyme(String nomAnonyme) {
		
		if(nomAnonyme == null)
    		throw new IllegalArgumentException("Le nom est null");
    	
    	String nomSansEsp = nomAnonyme.trim();
    	
    	if(nomSansEsp.equals(""))
    		throw new IllegalArgumentException("Le nom est vide");
    		
		this.nomAnonyme = nomSansEsp;
	}
}

