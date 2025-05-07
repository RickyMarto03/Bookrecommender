package theknife;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // Import per formattare date/ore

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
        this.stelle = Math.max(1, Math.min(5, stelle));
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

    /**
     * Imposta il numero di stelle, assicurandosi che sia tra 1 e 5.
     * @param stelle Il nuovo numero di stelle (1-5)
     */
    
    public void setStelle(int stelle) {
        this.stelle = Math.max(1, Math.min(5, stelle)); 
    }

    /**
     * Imposta il testo della recensione.
     * @param testo Il nuovo testo della recensione
     */
    
    public void setTesto(String testo) {
        this.testo = testo;
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String dataFormattata = dataCreazione.format(formatter);

        String base = String.format("""
            Recensione di: %s (del %s)
            Ristorante: %s
            Stelle: %s
            Testo: %s""",
            usernameUtente,
            dataFormattata, 
            nomeRistorante,
            "⭐".repeat(stelle), 
            testo);

        if (risposta != null && !risposta.isEmpty()) {
            base += "\nRisposta del ristoratore: " + risposta;
        }
        return base;
    }
}