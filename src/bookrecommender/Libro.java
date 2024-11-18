package bookrecommender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Classe che rappresenta un libro con attributi come titolo, autori, anno di pubblicazione, editore e categoria.
 * Include funzionalità per gestire suggerimenti e leggere libri da un file CSV.
 */
public class Libro {
    private String titolo;
    private String autori;
    private int anno;
    private String editore;
    private String categoria;
    private ArrayList<Libro> suggerimenti;

    /**
     * Costruttore della classe Libro.
     *
     * @param titolo     Il titolo del libro.
     * @param autori     Gli autori del libro.
     * @param anno       L'anno di pubblicazione del libro.
     * @param editore    L'editore del libro.
     * @param categoria  La categoria del libro.
     */
    public Libro(String titolo, String autori, int anno, String editore, String categoria) {
        this.titolo = titolo;
        this.autori = autori;
        this.anno = anno;
        this.editore = editore;
        this.categoria = categoria;
        this.suggerimenti = new ArrayList<>();
    }

    // Metodi getter

    /**
     * Restituisce il titolo del libro.
     *
     * @return Il titolo del libro.
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Restituisce gli autori del libro.
     *
     * @return Gli autori del libro.
     */
    public String getAutori() {
        return autori;
    }

    /**
     * Restituisce l'anno di pubblicazione del libro.
     *
     * @return L'anno di pubblicazione.
     */
    public int getAnno() {
        return anno;
    }

    /**
     * Restituisce l'editore del libro.
     *
     * @return L'editore del libro.
     */
    public String getEditore() {
        return editore;
    }

    /**
     * Restituisce la categoria del libro.
     *
     * @return La categoria del libro.
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Restituisce la lista dei suggerimenti associati al libro.
     *
     * @return Una lista di libri suggeriti.
     */
    public ArrayList<Libro> getSuggerimenti() {
        return suggerimenti;
    }

    // Metodi setter

    /**
     * Imposta il titolo del libro.
     *
     * @param titolo Il nuovo titolo del libro.
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /**
     * Imposta gli autori del libro.
     *
     * @param autori I nuovi autori del libro.
     */
    public void setAutori(String autori) {
        this.autori = autori;
    }

    /**
     * Imposta l'anno di pubblicazione del libro.
     *
     * @param anno Il nuovo anno di pubblicazione.
     */
    public void setAnno(int anno) {
        this.anno = anno;
    }

    /**
     * Imposta l'editore del libro.
     *
     * @param editore Il nuovo editore del libro.
     */
    public void setEditore(String editore) {
        this.editore = editore;
    }

    /**
     * Imposta la categoria del libro.
     *
     * @param categoria La nuova categoria del libro.
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Restituisce una rappresentazione in stringa del libro.
     *
     * @return Una stringa contenente le informazioni del libro.
     */
    @Override
    public String toString() {
        return titolo + " di " + autori + " (" + anno + "), " + editore + ", " + categoria;
    }

    /**
     * Verifica l'uguaglianza tra questo libro e un altro oggetto.
     *
     * @param obj L'oggetto da confrontare.
     * @return true se gli oggetti sono uguali, altrimenti false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Libro libro = (Libro) obj;
        return titolo.equalsIgnoreCase(libro.titolo) && autori.equalsIgnoreCase(libro.autori);
    }

    /**
     * Restituisce il codice hash del libro.
     *
     * @return Il codice hash calcolato.
     */
    @Override
    public int hashCode() {
        return Objects.hash(titolo.toLowerCase(), autori.toLowerCase());
    }

    /**
     * Imposta la lista dei suggerimenti per il libro.
     *
     * @param suggerimenti La nuova lista di suggerimenti.
     */
    public void setSuggerimenti(ArrayList<Libro> suggerimenti) {
        this.suggerimenti = suggerimenti;
    }

    /**
     * Legge i libri da un file CSV e li restituisce come una lista di oggetti Libro.
     *
     * @param filename Il nome del file CSV da cui leggere i libri.
     * @return Una lista di libri letti dal file.
     */
    public static ArrayList<Libro> leggiLibriDaFile(String filename) {
        ArrayList<Libro> libri = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Salta la prima riga con i nomi delle colonne
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] campi = line.split(",");
                String titolo = campi[0];
                String autori = campi[1];
                int anno = Integer.parseInt(campi[2]);
                String editore = campi[3];
                String categoria = campi[4];
                Libro libro = new Libro(titolo, autori, anno, editore, categoria);
                libri.add(libro);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libri;
    }

    /**
     * Aggiunge un suggerimento alla lista dei suggerimenti del libro, fino a un massimo di 3.
     *
     * @param suggerimento Il libro da aggiungere come suggerimento.
     */
    public void aggiungiSuggerimento(Libro suggerimento) {
        if (suggerimenti.size() < 3) {
            suggerimenti.add(suggerimento);
        } else {
            System.out.println("Puoi aggiungere solo fino a 3 suggerimenti per questo libro.");
        }
    }

    /**
     * Metodo principale per eseguire test sulla classe Libro.
     *
     * @param args Argomenti da linea di comando.
     */
    public static void main(String[] args) {
        // Dichiarazione corretta della variabile "libri"
        ArrayList<Libro> libri = leggiLibriDaFile("Libri.dati.csv");

        // Usa la variabile "libri" per stampare ogni libro
        for (Libro libro : libri) {
            System.out.println(libro);
        }
    }
}
