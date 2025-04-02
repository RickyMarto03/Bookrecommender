package theknife;

import java.time.LocalDateTime;

/**
 * Classe che rappresenta una recensione nel sistema
 */
public class Review {
    private String usernameUtente;
    private String nomeRistorante;
    private int stelle;  // 1-5
    private String testo;
    private String risposta;
    private LocalDateTime dataCreazione;

    public Review(String usernameUtente, String nomeRistorante, int stelle, String testo) {
        this.usernameUtente = usernameUtente;
        this.nomeRistorante = nomeRistorante;
        this.stelle = (stelle >= 1 && stelle <= 5) ? stelle : 1;
        this.testo = testo;
        this.risposta = null;
        this.dataCreazione = LocalDateTime.now();
    }

    // Getters
    public String getUsernameUtente() { return usernameUtente; }
    public String getNomeRistorante() { return nomeRistorante; }
    public int getStelle() { return stelle; }
    public String getTesto() { return testo; }
    public String getRisposta() { return risposta; }
    public LocalDateTime getDataCreazione() { return dataCreazione; }

    // Setters
    public void setRisposta(String risposta) { this.risposta = risposta; }

    @Override
    public String toString() {
        String base = String.format("""
            Recensione di: %s
            Ristorante: %s
            Stelle: %s
            Testo: %s""",
            usernameUtente,
            nomeRistorante,
            "⭐".repeat(stelle),
            testo);
        
        if (risposta != null) {
            base += "\nRisposta del ristoratore: " + risposta;
        }
        return base;
    }
}
