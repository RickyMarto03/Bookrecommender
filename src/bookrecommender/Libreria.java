package bookrecommender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.io.File;


/**
 * La classe Libreria rappresenta una collezione di libri appartenenti a un utente registrato.
 * Permette di aggiungere, rimuovere, cercare e visualizzare libri nella libreria.
 */
public class Libreria {
    private String nomeLibreria;
    private String userid;
    private ArrayList<Libro> libri;

    
    /**
     * Costruttore per creare una nuova libreria.
     *
     * @param nomeLibreria Il nome della libreria.
     * @param userid       L'ID dell'utente proprietario della libreria.
     */
    public Libreria(String nomeLibreria, String userid) {
        this.nomeLibreria = nomeLibreria;
        this.userid = userid;
        this.libri = new ArrayList<>();
    }

    
    /**
     * Aggiunge un libro alla libreria dell'utente e lo salva nel catalogo globale.
     *
     * @param libro Il libro da aggiungere alla libreria.
     */
    public void aggiungiLibro(Libro libro) {
        libri.add(libro);
        aggiungiLibroAlCatalogoGlobale(libro, "Libri.dati.csv");
    }
    
    /**
     * Aggiunge un libro al catalogo globale se non esiste già.
     *
     * @param libro        Il libro da aggiungere al catalogo globale.
     * @param filePathLibri Il percorso del file che contiene il catalogo globale dei libri.
     */
    private void aggiungiLibroAlCatalogoGlobale(Libro libro, String filePathLibri) {
        // Controlla se il libro esiste già nel catalogo globale
        boolean esisteGia = false;
        try (BufferedReader br = new BufferedReader(new FileReader(filePathLibri))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] campi = line.split(",");
                if (campi.length >= 1 && campi[0].equalsIgnoreCase(libro.getTitolo())) {
                    esisteGia = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Se il libro non esiste, lo aggiunge al catalogo globale
        if (!esisteGia) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePathLibri, true))) {
                bw.write(libro.getTitolo() + "," + libro.getAutori() + "," + libro.getAnno() + "," + libro.getEditore() + "," + libro.getCategoria());
                bw.newLine();
                System.out.println("Il libro è stato aggiunto al catalogo globale.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Il libro esiste già nel catalogo globale.");
        }
    }

    /**
     * Restituisce la lista di libri presenti nella libreria.
     *
     * @return La lista dei libri nella libreria.
     */
    public ArrayList<Libro> getLibri() {
        return libri;
    }

    
    /**
    * Visualizza tutti i libri presenti nella libreria.
    * Stampa un messaggio se la libreria è vuota.
    */
    public void visualizzaLibri() {
        System.out.println("Libreria: " + nomeLibreria);  
        if(libri.isEmpty()) {
        	System.out.println("Non ci sono libri nella tua libreria. ");
        } else {
        	for (Libro libro : libri) {
        		System.out.println(libro);
        	}
        }
    }
    
    /**
     * Cerca un libro nella libreria in base al titolo.
     *
     * @param titolo Il titolo del libro da cercare.
     * @return Il libro trovato, oppure null se non esiste.
     */
    public Libro trovaLibro(String titolo) {
        for (Libro libro : libri) {
            if (libro.getTitolo().equalsIgnoreCase(titolo)) {
                System.out.println("Libro trovato: " + libro.getTitolo());  // DEBUG
                return libro;
            }
        }
        System.out.println("Libro non trovato per titolo: " + titolo);  // DEBUG
        return null;
    }
    

    /**
     * Cerca tutti i libri scritti da un determinato autore.
     *
     * @param autore Il nome dell'autore da cercare.
     * @return La lista dei libri scritti dall'autore.
     */
    public ArrayList<Libro> trovaLibriPerAutore(String autore) {
        ArrayList<Libro> risultati = new ArrayList<>();
        for (Libro libro : libri) {
            if (libro.getAutori().equalsIgnoreCase(autore)) {
                risultati.add(libro);
            }
        }
        return risultati;
    }

    /**
     * Rimuove un libro dalla libreria in base al titolo.
     *
     * @param titolo Il titolo del libro da rimuovere.
     * @return true se il libro è stato rimosso, altrimenti false.
     */
    public boolean rimuoviLibro(String titolo) {
        for (Libro libro : libri) {
            if (libro.getTitolo().equalsIgnoreCase(titolo)) {
                libri.remove(libro);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Restituisce il nome della libreria.
     *
     * @return Il nome della libreria.
     */
    public String getNomeLibreria() {
        return nomeLibreria;
    }
   
    /**
     * Salva la libreria su un file specificato.
     *
     * @param filePath Il percorso del file in cui salvare la libreria.
     */
    public void salvaLibreria(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) { // modalità append
            bw.write("Libreria:" + nomeLibreria + ",Utente:" + userid);
            bw.newLine();
            for (Libro libro : libri) {
                bw.write(libro.getTitolo() + "," + libro.getAutori() + "," + libro.getAnno() + ","
                         + libro.getEditore() + "," + libro.getCategoria());
                bw.newLine();
            }
            bw.write("---"); // Segna la fine della libreria
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carica una libreria per un determinato utente da un file.
     *
     * @param userid              L'ID dell'utente proprietario della libreria.
     * @param filePathLibrerie    Il percorso del file delle librerie.
     * @param filePathConsigliLibri Il percorso del file dei consigli sui libri.
     * @return La libreria caricata o null se non viene trovata.
     */
    public static Libreria caricaLibreria(String userid, String filePathLibrerie, String filePathConsigliLibri) {
        Libreria libreria = null;
        HashMap<String, Libro> libriCaricati = new HashMap<>(); // Mappa per tenere traccia dei libri

        try (BufferedReader br = new BufferedReader(new FileReader(filePathLibrerie))) {
            String line;
            boolean libreriaTrovata = false;

            // Scorri il file
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Libreria:") && line.contains("Utente:" + userid)) {
                    // Se troviamo la libreria associata all'utente
                    String nomeLibreria = line.split(",")[0].split(":")[1];

                    // Se troviamo la libreria, inizializziamo una nuova libreria
                    libreria = new Libreria(nomeLibreria, userid);
                    libreriaTrovata = true;
                    libriCaricati.clear();  // Ripuliamo la mappa per i libri della nuova libreria
                } else if (libreriaTrovata && !line.equals("---")) {
                    // Carichiamo i libri della libreria
                    String[] campi = line.split(",");
                    if (campi.length == 5) {
                        String titolo = campi[0];
                        String autori = campi[1];
                        int anno = Integer.parseInt(campi[2]);
                        String editore = campi[3];
                        String categoria = campi[4];
                        
                        // Aggiungiamo il libro alla mappa
                        Libro libro = new Libro(titolo, autori, anno, editore, categoria);
                        libriCaricati.put(titolo, libro);
                    }
                } else if (libreriaTrovata && line.equals("---")) {
                    // Fine di una libreria, aggiungiamo i libri trovati alla libreria
                    for (Libro libro : libriCaricati.values()) {
                        libreria.aggiungiLibro(libro);
                    }
                    libreriaTrovata = false;
                }
            }

            // Carichiamo i suggerimenti dal file ConsigliLibri
            try (BufferedReader brConsigli = new BufferedReader(new FileReader(filePathConsigliLibri))) {
                while ((line = brConsigli.readLine()) != null) {
                    String[] campi = line.split(",");
                    if (campi.length == 6) {
                        String titoloLibroOriginale = campi[0].trim();
                        String titoloSuggerito = campi[1].trim();
                        String autoriSuggeriti = campi[2].trim();
                        int annoSuggerito = Integer.parseInt(campi[3].trim());
                        String editoreSuggerito = campi[4].trim();
                        String categoriaSuggerita = campi[5].trim();

                        // Linee di debug aggiunte qui
                        System.out.println("Libro originale: " + titoloLibroOriginale);
                        System.out.println("Suggerimento caricato: " + titoloSuggerito + " di " + autoriSuggeriti);

                        // Trova il libro originale nella libreria e aggiungi il suggerimento
                        Libro libroOriginale = libreria.trovaLibro(titoloLibroOriginale);
                        if (libroOriginale != null) {
                            Libro suggerimento = new Libro(titoloSuggerito, autoriSuggeriti, annoSuggerito, editoreSuggerito, categoriaSuggerita);
                            libroOriginale.aggiungiSuggerimento(suggerimento);
                            // Debug per confermare l'aggiunta del suggerimento
                            System.out.println("Suggerimento aggiunto correttamente per il libro: " + titoloLibroOriginale);
                        } else {
                            // Debug se non si trova il libro originale
                            System.out.println("Libro non trovato per aggiungere suggerimento: " + titoloLibroOriginale);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Errore durante il caricamento della libreria: " + e.getMessage());
        }

        return libreria;
    }

    /**
     * Carica tutte le librerie di un determinato utente da un file.
     *
     * @param userid              L'ID dell'utente proprietario delle librerie.
     * @param filePathLibrerie    Il percorso del file contenente le librerie.
     * @param filePathConsigliLibri Il percorso del file contenente i consigli sui libri.
     * @return Una lista di librerie appartenenti all'utente.
     */
    public static ArrayList<Libreria> caricaLibrerieUtente(String userid, String filePathLibrerie, String filePathConsigliLibri) {
        ArrayList<Libreria> librerie = new ArrayList<>();
        Set<String> librerieCaricate = new HashSet<>();

        // Carica i suggerimenti dal file
        HashMap<String, ArrayList<Libro>> mappaSuggerimenti = caricaSuggerimentiDaFile(filePathConsigliLibri);

        try (BufferedReader br = new BufferedReader(new FileReader(filePathLibrerie))) {
            String line;
            Libreria libreriaCorrente = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("Libreria:") && line.contains("Utente:" + userid)) {
                    String nomeLibreria = line.split(",")[0].split(":")[1];

                    if (!librerieCaricate.contains(nomeLibreria)) {
                        libreriaCorrente = new Libreria(nomeLibreria, userid);
                        librerieCaricate.add(nomeLibreria);
                        librerie.add(libreriaCorrente);
                    }
                } else if (libreriaCorrente != null && !line.equals("---")) {
                    String[] campi = line.split(",");
                    if (campi.length == 5) {
                        String titolo = campi[0];
                        String autori = campi[1];
                        int anno = Integer.parseInt(campi[2]);
                        String editore = campi[3];
                        String categoria = campi[4];
                        Libro libro = new Libro(titolo, autori, anno, editore, categoria);

                        // Aggiungi i suggerimenti al libro
                        if (mappaSuggerimenti.containsKey(titolo)) {
                            libro.setSuggerimenti(mappaSuggerimenti.get(titolo));
                        }

                        libreriaCorrente.aggiungiLibro(libro);
                    }
                } else if (line.equals("---")) {
                    libreriaCorrente = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return librerie;
    }

    
    /**
     * Carica i suggerimenti per i libri da un file e li associa ai libri corrispondenti.
     *
     * @param filePathConsigliLibri Il percorso del file contenente i consigli sui libri.
     * @return Una mappa che associa il titolo di un libro alla lista dei suggerimenti.
     */
    public static HashMap<String, ArrayList<Libro>> caricaSuggerimentiDaFile(String filePathConsigliLibri) {
        HashMap<String, ArrayList<Libro>> mappaSuggerimenti = new HashMap<>();
        
        File file = new File(filePathConsigliLibri);
        if (!file.exists()) {
            System.out.println("File " + filePathConsigliLibri + " non trovato.");
            return mappaSuggerimenti; // Restituisci una mappa vuota
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] campi = line.split(",");
                if (campi.length == 6) {
                    String titoloLibro = campi[0];
                    String titoloSuggerito = campi[1];
                    String autoriSuggeriti = campi[2];
                    int annoSuggerito = Integer.parseInt(campi[3]);
                    String editoreSuggerito = campi[4];
                    String categoriaSuggerita = campi[5];

                    Libro suggerimento = new Libro(titoloSuggerito, autoriSuggeriti, annoSuggerito, editoreSuggerito, categoriaSuggerita);

                    // Aggiungi il suggerimento alla lista associata al libro
                    mappaSuggerimenti.putIfAbsent(titoloLibro, new ArrayList<>());
                    ArrayList<Libro> listaSuggerimenti = mappaSuggerimenti.get(titoloLibro);

                    // Evita duplicati
                    if (!listaSuggerimenti.contains(suggerimento)) {
                        listaSuggerimenti.add(suggerimento);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mappaSuggerimenti;
    }

    /**
     * Permette all'utente di inserire una valutazione per un libro specificato e salva la valutazione su file.
     *
     * @param filePathValutazioni Il percorso del file in cui salvare le valutazioni.
     * @param userid              L'ID dell'utente che inserisce la valutazione.
     */
    public void inserisciValutazioneLibro(String filePathValutazioni, String userid) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il titolo del libro che vuoi valutare:");
        String titolo = scanner.nextLine();
        Libro libro = trovaLibro(titolo);

        if (libro != null) {
            System.out.println("Inserisci la valutazione per i seguenti criteri (da 1 a 5):");

            System.out.print("Stile: ");
            int stile = scanner.nextInt();

            System.out.print("Contenuto: ");
            int contenuto = scanner.nextInt();

            System.out.print("Gradevolezza: ");
            int gradevolezza = scanner.nextInt();

            System.out.print("Originalità: ");
            int originalità = scanner.nextInt();

            System.out.print("Edizione: ");
            int edizione = scanner.nextInt();

            double votoFinale = (stile + contenuto + gradevolezza + originalità + edizione) / 5.0;
            System.out.println("Voto finale: " + votoFinale);

            // Salvataggio delle valutazioni su file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathValutazioni, true))) {
                writer.write(userid + "," + titolo + "," + stile + "," + contenuto + "," + gradevolezza + "," + originalità + "," + edizione + "," + votoFinale);
                writer.newLine();
                System.out.println("Valutazione salvata con successo!");
            } catch (IOException e) {
                System.out.println("Errore durante il salvataggio della valutazione: " + e.getMessage());
            }
        } else {
            System.out.println("Libro non trovato.");
        }
    }

    /**
     * Permette all'utente di aggiungere un suggerimento a un libro e salva il suggerimento su file.
     *
     * @param filePathConsigliLibri Il percorso del file in cui salvare i suggerimenti.
     */
    public void inserisciSuggerimentoLibro(String filePathConsigliLibri) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il titolo del libro a cui vuoi aggiungere un suggerimento:");
        String titolo = scanner.nextLine();
        Libro libro = trovaLibro(titolo);

        if (libro != null) {
            if (libro.getSuggerimenti().size() >= 3) {
                System.out.println("Non puoi aggiungere più di 3 suggerimenti per il libro \"" + libro.getTitolo() + "\".");
            } else {
                System.out.println("Inserisci il titolo del libro suggerito:");
                String titoloSuggerito = scanner.nextLine();
                System.out.println("Inserisci l'autore del libro suggerito:");
                String autoriSuggeriti = scanner.nextLine();
                System.out.println("Inserisci l'anno di pubblicazione del libro suggerito:");
                int annoSuggerito = scanner.nextInt();
                scanner.nextLine(); // Consuma la newline
                System.out.println("Inserisci l'editore del libro suggerito:");
                String editoreSuggerito = scanner.nextLine();
                System.out.println("Inserisci la categoria del libro suggerito:");
                String categoriaSuggerita = scanner.nextLine();

                Libro suggerimento = new Libro(titoloSuggerito, autoriSuggeriti, annoSuggerito, editoreSuggerito, categoriaSuggerita);

                // Controlla se il suggerimento esiste già
                if (libro.getSuggerimenti().contains(suggerimento)) {
                    System.out.println("Questo suggerimento è già presente per questo libro.");
                } else {
                    libro.aggiungiSuggerimento(suggerimento);

                    // Salvataggio immediato del suggerimento nel file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePathConsigliLibri, true))) {
                        writer.write(libro.getTitolo() + "," + titoloSuggerito + "," + autoriSuggeriti + "," + annoSuggerito + "," + editoreSuggerito + "," + categoriaSuggerita);
                        writer.newLine();
                        System.out.println("Suggerimento per \"" + libro.getTitolo() + "\" aggiunto e salvato con successo!");
                    } catch (IOException e) {
                        System.out.println("Errore durante il salvataggio del suggerimento: " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("Libro non trovato.");
        }
    }
   
    /**
     * Visualizza i suggerimenti per un libro specificato dall'utente.
     * L'utente inserisce il titolo del libro e, se vengono trovati suggerimenti, questi vengono visualizzati.
     * Vengono evitati i duplicati utilizzando un HashSet.
     * Se non ci sono suggerimenti o il libro non viene trovato, viene mostrato un messaggio appropriato.
     */
    public void visualizzaSuggerimentiLibro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il titolo del libro di cui vuoi visualizzare i suggerimenti:");
        String titolo = scanner.nextLine();
        Libro libro = trovaLibro(titolo);

        if (libro != null) {
            ArrayList<Libro> suggerimenti = libro.getSuggerimenti();
            if (suggerimenti.isEmpty()) {
                System.out.println("Non ci sono suggerimenti per questo libro.");
            } else {
                System.out.println("Suggerimenti per il libro " + libro.getTitolo() + ":");
                // Evita duplicati usando un HashSet
                Set<Libro> suggerimentiUnici = new HashSet<>(suggerimenti);
                for (Libro suggerimento : suggerimentiUnici) {
                    System.out.println(suggerimento);
                }
            }
        } else {
            System.out.println("Libro non trovato.");
        }
    }
}
