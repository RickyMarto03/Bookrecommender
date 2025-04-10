
package theknife;

/**
 * Classe che rappresenta un utente del sistema
 */

public class User {
    private String nome;
    private String cognome;
    private String username;
    private String password;
    private String dataNascita;
    private String domicilio;
    private String ruolo;  // "cliente" o "ristoratore"

    public User(String nome, String cognome, String username, String password, 
                String dataNascita, String domicilio, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
        this.dataNascita = dataNascita;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDataNascita() {
        return dataNascita;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getRuolo() {
        return ruolo;
    }

    // Setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    // Metodo per verificare la password
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", username='" + username + '\'' +
                ", dataNascita='" + dataNascita + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", ruolo='" + ruolo + '\'' +
                '}';
    }

    // Metodo per convertire l'utente in formato CSV
    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                nome, cognome, username, password,
                dataNascita != null ? dataNascita : "",
                domicilio, ruolo);
    }

    // Metodo statico per creare un utente da una riga CSV
    public static User fromCSV(String line) {
        String[] parts = line.split(",");
        return new User(
                parts[0],                    
                parts[1],                    
                parts[2],                    
                parts[3],                    
                parts[4].isEmpty() ? null : parts[4],  
                parts[5],                   
                parts[6]                     
        );
    }
}