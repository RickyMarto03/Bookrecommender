package theknife;

/**
 * Classe che rappresenta un ristorante preferito di un utente
 */
public class Favorite {
    private String usernameUtente;
    private String nomeRistorante;
    private String nota;  // nota opzionale dell'utente

    public Favorite(String usernameUtente, String nomeRistorante, String nota) {
        this.usernameUtente = usernameUtente;
        this.nomeRistorante = nomeRistorante;
        this.nota = nota;
    }

    // Getters
    public String getUsernameUtente() { return usernameUtente; }
    public String getNomeRistorante() { return nomeRistorante; }
    public String getNota() { return nota; }

    // Setters
    public void setNota(String nota) { this.nota = nota; }
}
