package theknife;

/**
 * Classe che rappresenta un ristorante nel sistema
 * @author [Nome Cognome]
 * @matricola [Numero Matricola]
 * @sede [VA/CO]
 */
public class Restaurant {
    private String nome;
    private String nazione;
    private String citta;
    private String indirizzo;
    private double latitudine;
    private double longitudine;
    private double fasciaPrezzo;
    private boolean delivery;
    private boolean prenotazioneOnline;
    private String tipoCucina;
    private String proprietario;  // username del ristoratore

    public Restaurant(String nome, String nazione, String citta, String indirizzo,
                     double latitudine, double longitudine, double fasciaPrezzo,
                     boolean delivery, boolean prenotazioneOnline, String tipoCucina,
                     String proprietario) {
        this.nome = nome;
        this.nazione = nazione;
        this.citta = citta;
        this.indirizzo = indirizzo;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.fasciaPrezzo = fasciaPrezzo;
        this.delivery = delivery;
        this.prenotazioneOnline = prenotazioneOnline;
        this.tipoCucina = tipoCucina;
        this.proprietario = proprietario;
    }

    // Getters
    public String getNome() { return nome; }
    public String getNazione() { return nazione; }
    public String getCitta() { return citta; }
    public String getIndirizzo() { return indirizzo; }
    public double getLatitudine() { return latitudine; }
    public double getLongitudine() { return longitudine; }
    public double getFasciaPrezzo() { return fasciaPrezzo; }
    public boolean hasDelivery() { return delivery; }
    public boolean hasPrenotazioneOnline() { return prenotazioneOnline; }
    public String getTipoCucina() { return tipoCucina; }
    public String getProprietario() { return proprietario; }

    // Setters (solo per i campi modificabili)
    public void setFasciaPrezzo(double fasciaPrezzo) { this.fasciaPrezzo = fasciaPrezzo; }
    public void setDelivery(boolean delivery) { this.delivery = delivery; }
    public void setPrenotazioneOnline(boolean prenotazioneOnline) { this.prenotazioneOnline = prenotazioneOnline; }

    @Override
    public String toString() {
        return String.format("""
            Ristorante: %s
            Indirizzo: %s, %s, %s
            Tipo cucina: %s
            Fascia prezzo: %.2f€
            Servizi: %s %s""",
            nome,
            indirizzo, citta, nazione,
            tipoCucina,
            fasciaPrezzo,
            delivery ? "Delivery" : "",
            prenotazioneOnline ? "Prenotazione Online" : "");
    }
}
