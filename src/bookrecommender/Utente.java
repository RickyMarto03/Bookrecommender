package bookrecommender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Classe che rappresenta un utente del sistema, con funzionalità per la registrazione,
 * il login e la gestione delle librerie personali.
 */

public class Utente {
    private String nome;
    private String cognome;
    private String email;
    private String userid;
    private String password;
    private ArrayList<Libreria> librerie;

    /**
     * Costruttore della classe Utente.
     *
     * @param nome      Il nome dell'utente.
     * @param cognome   Il cognome dell'utente.
     * @param email     L'email dell'utente.
     * @param userid    L'ID utente per il login.
     * @param password  La password dell'utente.
     */
    public Utente(String nome, String cognome, String email, String userid, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.userid = userid;
        this.password = password;
        this.librerie = new ArrayList<>();
    }

    // Metodi getter

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Il cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce l'email dell'utente.
     *
     * @return L'email dell'utente.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Restituisce l'ID utente.
     *
     * @return L'ID utente.
     */
    public String getUserid() {
        return userid;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return La password dell'utente.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Restituisce la lista delle librerie associate all'utente.
     *
     * @return Una lista di librerie.
     */
    public ArrayList<Libreria> getLibrerie() {
        return librerie;
    }

    /**
     * Aggiunge una libreria alla lista delle librerie dell'utente.
     *
     * @param libreria La libreria da aggiungere.
     */
    public void aggiungiLibreria(Libreria libreria) {
        librerie.add(libreria);
    }

    /**
     * Restituisce una rappresentazione in stringa dell'utente.
     *
     * @return Una stringa contenente le informazioni dell'utente.
     */
    @Override
    public String toString() {
        return nome + " " + cognome + " (" + email + ")";
    }

    /**
     * Registra un nuovo utente salvando le sue informazioni su file.
     *
     * @param utente   L'utente da registrare.
     * @param filePath Il percorso del file dove salvare i dati dell'utente.
     */
    public static void registraUtente(Utente utente, String filePath) {
        if (utenteEsiste(utente.getUserid(), filePath)) {
            System.out.println("Errore: l'utente con userid " + utente.getUserid() + " esiste già.");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(utente.getNome() + "," + utente.getCognome() + "," + utente.getEmail() + "," + utente.getUserid() + "," + utente.getPassword());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica se un utente esiste già nel sistema.
     *
     * @param userid   L'ID utente da verificare.
     * @param filePath Il percorso del file contenente gli utenti registrati.
     * @return true se l'utente esiste, altrimenti false.
     */
    public static boolean utenteEsiste(String userid, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] campi = line.split(",");
                if (campi[3].equals(userid)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Metodo per registrare un nuovo utente tramite input da console.
     *
     * @param filePath Il percorso del file dove salvare i dati dell'utente.
     */
    public static void registraNuovoUtente(String filePath) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il tuo nome: ");
        String nome = scanner.nextLine();

        System.out.println("Inserisci il tuo cognome: ");
        String cognome = scanner.nextLine();

        System.out.println("Inserisci la tua email: ");
        String email = scanner.nextLine();

        System.out.println("Inserisci il tuo userid: ");
        String userid = scanner.nextLine();

        System.out.println("Inserisci la tua password: ");
        String password = scanner.nextLine();

        Utente nuovoUtente = new Utente(nome, cognome, email, userid, password);
        registraUtente(nuovoUtente, filePath);
    }

    /**
     * Chiede all'utente di effettuare il login tramite input da console.
     *
     * @param filePath Il percorso del file contenente gli utenti registrati.
     */
    public static void login(String filePath) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Inserisci il tuo userid:");
        String userid = scanner.nextLine();
        System.out.println("Inserisci la tua password:");
        String password = scanner.nextLine();

        if (loginUtente(userid, password, filePath)) {
            System.out.println("Login effettuato con successo!");
        } else {
            System.out.println("Login fallito: userid o password errati.");
        }
    }

    /**
     * Verifica le credenziali di login di un utente.
     *
     * @param userid   L'ID utente.
     * @param password La password dell'utente.
     * @param filePath Il percorso del file contenente gli utenti registrati.
     * @return true se le credenziali sono corrette, altrimenti false.
     */
    public static boolean loginUtente(String userid, String password, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] campi = line.split(",");
                if (campi[3].equals(userid) && campi[4].equals(password)) {
                    return true; // Login riuscito
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Login fallito
    }
   
    // Gestione delle librerie dell'utente

    /**
     * Permette la gestione di una libreria attraverso un menu interattivo.
     *
     * @param libreria              La libreria da gestire
     * @param filePathLibrerie      Il percorso del file delle librerie
     * @param filePathValutazioni   Il percorso del file delle valutazioni
     * @param filePathConsigliLibri Il percorso del file dei consigli
     */    
	public static void gestioneLibreria(Libreria libreria, String filePathLibrerie, String filePathValutazioni, String filePathConsigliLibri) {
	    Scanner scanner = new Scanner(System.in);

	    // Menu di gestione della libreria
	    while (true) {
	        System.out.println("1 - Aggiungi un libro alla libreria");
	        System.out.println("2 - Visualizza i libri nella libreria");
	        System.out.println("3 - Modifica un libro");
	        System.out.println("4 - Rimuovi un libro");
	        System.out.println("5 - Inserisci una valutazione a un libro");
	        System.out.println("6 - Aggiungi un suggerimento a un libro");
	        System.out.println("7 - Visualizza i suggerimenti di un libro");
	        System.out.println("8 - Salva e esci");

	        int scelta = scanner.nextInt();
	        scanner.nextLine(); // Consuma la newline

	        if (scelta == 1) {
	            // Aggiungi un libro
	            System.out.println("Titolo del libro:");
	            String titolo = scanner.nextLine();
	            System.out.println("Autore del libro:");
	            String autori = scanner.nextLine();
	            System.out.println("Anno di pubblicazione:");
	            int anno = scanner.nextInt();
	            scanner.nextLine(); // Consuma la newline
	            System.out.println("Editore:");
	            String editore = scanner.nextLine();
	            System.out.println("Categoria:");
	            String categoria = scanner.nextLine();

	            Libro nuovoLibro = new Libro(titolo, autori, anno, editore, categoria);
	            libreria.aggiungiLibro(nuovoLibro);
	        } else if (scelta == 2) {
	            // Visualizza i libri nella libreria
	            libreria.visualizzaLibri();
	        } else if (scelta == 3) {
	            // Modifica un libro
	            System.out.println("Inserisci il titolo del libro da modificare:");
	            String titolo = scanner.nextLine();
	            Libro libroDaModificare = libreria.trovaLibro(titolo);
	            if (libroDaModificare != null) {
	                System.out.println("Inserisci i nuovi dati del libro (lascia vuoto per mantenere i valori attuali):");
	                System.out.println("Nuovo titolo:");
	                String nuovoTitolo = scanner.nextLine();
	                if (!nuovoTitolo.isEmpty()) {
	                    libroDaModificare.setTitolo(nuovoTitolo);
	                }
	                System.out.println("Nuovo autore:");
	                String nuovoAutore = scanner.nextLine();
	                if (!nuovoAutore.isEmpty()) {
	                    libroDaModificare.setAutori(nuovoAutore);
	                }
	                System.out.println("Nuovo anno:");
	                String nuovoAnno = scanner.nextLine();
	                if (!nuovoAnno.isEmpty()) {
	                    libroDaModificare.setAnno(Integer.parseInt(nuovoAnno));
	                }
	                System.out.println("Nuovo editore:");
	                String nuovoEditore = scanner.nextLine();
	                if (!nuovoEditore.isEmpty()) {
	                    libroDaModificare.setEditore(nuovoEditore);
	                }
	                System.out.println("Nuova categoria:");
	                String nuovaCategoria = scanner.nextLine();
	                if (!nuovaCategoria.isEmpty()) {
	                    libroDaModificare.setCategoria(nuovaCategoria);
	                }
	            } else {
	                System.out.println("Libro non trovato.");
	            }
	        } else if (scelta == 4) {
	            // Rimuovi un libro
	            System.out.println("Inserisci il titolo del libro da rimuovere:");
	            String titolo = scanner.nextLine();
	            boolean successo = libreria.rimuoviLibro(titolo);
	            if (successo) {
	                System.out.println("Libro rimosso con successo.");
	            } else {
	                System.out.println("Libro non trovato.");
	            }
	        } else if (scelta == 5) {
	            // Inserisci una valutazione per un libro
	            libreria.inserisciValutazioneLibro(filePathValutazioni, libreria.getNomeLibreria());
	        } else if (scelta == 6) {
	            // Aggiungi un suggerimento a un libro
	            libreria.inserisciSuggerimentoLibro(filePathConsigliLibri);
	        } else if (scelta == 7) {
	            // Visualizza i suggerimenti di un libro
	            libreria.visualizzaSuggerimentiLibro();
	        } else if (scelta == 8) {
	            // Salva la libreria e esci
	            libreria.salvaLibreria(filePathLibrerie);
	            System.out.println("Libreria salvata con successo.");
	            break;
	        } else {
	            System.out.println("Scelta non valida.");
	        }
	    }
	}
	
	/**
	 * Gestisce la selezione e la gestione di tutte le librerie di un utente.
	 *
	 * @param userid                L'ID utente che possiede le librerie.
	 * @param filePathLibrerie      Il percorso del file che contiene le librerie dell'utente.
	 * @param filePathValutazioni   Il percorso del file che contiene le valutazioni dei libri.
	 * @param filePathConsigliLibri Il percorso del file che contiene i consigli sui libri.
	 */	
	public static void gestioneLibrerie(String userid, String filePathLibrerie, String filePathValutazioni, String filePathConsigliLibri) {
	    Scanner scanner = new Scanner(System.in);

	    // Carica tutte le librerie dell'utente
	    ArrayList<Libreria> librerie = Libreria.caricaLibrerieUtente(userid, filePathLibrerie, filePathConsigliLibri);

	    if (librerie.isEmpty()) {
	        // Se l'utente non ha librerie, chiedi di crearne una
	        System.out.println("Nessuna libreria trovata. Vuoi crearne una nuova? (si/no)");
	        String risposta = scanner.nextLine();
	        if (risposta.equalsIgnoreCase("si")) {
	            System.out.println("Inserisci il nome della nuova libreria:");
	            String nomeLibreria = scanner.nextLine();
	            Libreria nuovaLibreria = new Libreria(nomeLibreria, userid);
	            librerie.add(nuovaLibreria);
	            gestioneLibreria(nuovaLibreria, filePathLibrerie, filePathValutazioni, filePathConsigliLibri);
	        } else {
	            System.out.println("Operazione annullata.");
	            return;
	        }
	    } else {
	        // Se ci sono già librerie, chiedi quale gestire o crearne una nuova
	        System.out.println("Librerie disponibili:");
	        for (int i = 0; i < librerie.size(); i++) {
	            System.out.println((i + 1) + " - " + librerie.get(i).getNomeLibreria());
	        }
	        System.out.println((librerie.size() + 1) + " - Crea una nuova libreria");

	        int scelta = scanner.nextInt();
	        scanner.nextLine(); // Consuma la newline

	        if (scelta > 0 && scelta <= librerie.size()) {
	            // Gestisci una libreria esistente
	            Libreria libreriaScelta = librerie.get(scelta - 1);
	            gestioneLibreria(libreriaScelta, filePathLibrerie, filePathValutazioni, filePathConsigliLibri);
	        } else if (scelta == librerie.size() + 1) {
	            // Crea una nuova libreria
	            System.out.println("Inserisci il nome della nuova libreria:");
	            String nomeLibreria = scanner.nextLine();
	            Libreria nuovaLibreria = new Libreria(nomeLibreria, userid);
	            librerie.add(nuovaLibreria);
	            gestioneLibreria(nuovaLibreria, filePathLibrerie, filePathValutazioni, filePathConsigliLibri);
	        } else {
	            System.out.println("Scelta non valida.");
	        }
	    }
	}
	
	/**
	 * Menu per utenti non registrati che permette di cercare libri, visualizzare valutazioni e consigli, o registrarsi.
	 *
	 * @param filePathLibri         Il percorso del file che contiene i libri.
	 * @param filePathValutazioni   Il percorso del file che contiene le valutazioni dei libri.
	 * @param filePathConsigliLibri Il percorso del file che contiene i consigli sui libri.
	 * @param filePathUtenti        Il percorso del file che contiene gli utenti registrati.
	 */
	public static void menuNonRegistrato(String filePathLibri, String filePathValutazioni, String filePathConsigliLibri, String filePathUtenti) {
	    Scanner scanner = new Scanner(System.in);

	    // Carica l'elenco dei libri disponibili
	    ArrayList<Libro> elencoLibri = Libro.leggiLibriDaFile(filePathLibri);

	    while (true) {
	        System.out.println("Scegli un'opzione: ");
	        System.out.println("1 - Cerca libri");
	        System.out.println("2 - Visualizza valutazioni e consigli di un libro");
	        System.out.println("3 - Registrazione");
	        System.out.println("4 - Esci");

	        int scelta = scanner.nextInt();
	        scanner.nextLine(); // Consuma la newline

	        if (scelta == 1) {
	            cercaLibri(elencoLibri, scanner);
	        } else if (scelta == 2) {
	            visualizzaValutazioniConsigliLibro(elencoLibri, filePathValutazioni, filePathConsigliLibri, scanner);
	        } else if (scelta == 3) {
	            Utente.registraNuovoUtente(filePathUtenti);
	        } else if (scelta == 4) {
	            System.out.println("Uscita dal programma.");
	            break;
	        } else {
	            System.out.println("Scelta non valida.");
	        }
	    }
	}

	/**
	 * Permette di cercare libri in base al titolo, autore o anno di pubblicazione.
	 *
	 * @param elencoLibri Elenco dei libri da cercare.
	 * @param scanner     Scanner per l'input da console.
	 */
	public static void cercaLibri(ArrayList<Libro> elencoLibri, Scanner scanner) {
	    System.out.println("Cerca un libro per: ");
	    System.out.println("1 - Titolo");
	    System.out.println("2 - Autore");
	    System.out.println("3 - Anno");

	    int scelta = scanner.nextInt();
	    scanner.nextLine(); // Consuma la newline

	    System.out.println("Inserisci il termine di ricerca:");
	    String ricerca = scanner.nextLine();

	    ArrayList<Libro> risultati = new ArrayList<>();

	    for (Libro libro : elencoLibri) {
	        if (scelta == 1 && libro.getTitolo().equalsIgnoreCase(ricerca)) {
	            risultati.add(libro);
	        } else if (scelta == 2 && libro.getAutori().equalsIgnoreCase(ricerca)) {
	            risultati.add(libro);
	        } else if (scelta == 3 && String.valueOf(libro.getAnno()).equals(ricerca)) {
	            risultati.add(libro);
	        }
	    }

	    if (risultati.isEmpty()) {
	        System.out.println("Nessun libro trovato.");
	    } else {
	        System.out.println("Libri trovati:");
	        for (Libro libro : risultati) {
	            System.out.println(libro);
	        }
	    }
	}

	/**
	 * Visualizza le valutazioni e i consigli per un libro specificato dall'utente.
	 *
	 * @param elencoLibri             Elenco dei libri disponibili.
	 * @param filePathValutazioni     Il percorso del file che contiene le valutazioni dei libri.
	 * @param filePathConsigliLibri   Il percorso del file che contiene i consigli sui libri.
	 * @param scanner                 Scanner per l'input da console.
	 */
	public static void visualizzaValutazioniConsigliLibro(ArrayList<Libro> elencoLibri, String filePathValutazioni, String filePathConsigliLibri, Scanner scanner) {
	    System.out.println("Inserisci il titolo del libro di cui vuoi visualizzare le valutazioni e i consigli:");
	    String titolo = scanner.nextLine();

	    Libro libroSelezionato = null;

	    for (Libro libro : elencoLibri) {
	        if (libro.getTitolo().equalsIgnoreCase(titolo)) {
	            libroSelezionato = libro;
	            break;
	        }
	    }

	    if (libroSelezionato != null) {
	        // Visualizza valutazioni
	        System.out.println("Valutazioni per " + libroSelezionato.getTitolo() + ":");
	        visualizzaValutazioni(filePathValutazioni, libroSelezionato.getTitolo());

	        // Visualizza consigli
	        System.out.println("Suggerimenti per " + libroSelezionato.getTitolo() + ":");
	        visualizzaConsigliLibro(libroSelezionato.getTitolo(), filePathConsigliLibri);
	    } else {
	        System.out.println("Libro non trovato.");
	    }
	}

	/**
	 * Visualizza le valutazioni per un libro specificato.
	 *
	 * @param filePathValutazioni Il percorso del file che contiene le valutazioni dei libri.
	 * @param titolo              Il titolo del libro per cui visualizzare le valutazioni.
	 */
	public static void visualizzaValutazioni(String filePathValutazioni, String titolo) {
	    try (BufferedReader br = new BufferedReader(new FileReader(filePathValutazioni))) {
	        String line;
	        int count = 0;
	        double sommaValutazioni = 0.0;

	        while ((line = br.readLine()) != null) {
	            String[] campi = line.split(",");
	            if (campi[1].equalsIgnoreCase(titolo)) {
	                double voto = Double.parseDouble(campi[7]);
	                sommaValutazioni += voto;
	                count++;
	            }
	        }

	        if (count > 0) {
	            double media = sommaValutazioni / count;
	            System.out.printf("Valutazione media: %.2f su 5 (%d valutazioni)%n", media, count);
	        } else {
	            System.out.println("Nessuna valutazione trovata per questo libro.");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Visualizza i consigli di un libro specificato.
	 * Legge i consigli per un libro dal file dei consigli e li stampa a schermo.
	 *
	 * @param titoloLibro        Il titolo del libro di cui visualizzare i consigli.
	 * @param filePathConsigliLibri Il percorso del file che contiene i consigli sui libri.
	 */
	public static void visualizzaConsigliLibro(String titoloLibro, String filePathConsigliLibri) {
	    ArrayList<Libro> consigli = new ArrayList<>();

	    try (BufferedReader br = new BufferedReader(new FileReader(filePathConsigliLibri))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] campi = line.split(",");
	            if (campi.length == 6 && campi[0].equalsIgnoreCase(titoloLibro)) {
	                String titoloSuggerito = campi[1];
	                String autoriSuggeriti = campi[2];
	                int annoSuggerito = Integer.parseInt(campi[3]);
	                String editoreSuggerito = campi[4];
	                String categoriaSuggerita = campi[5];

	                Libro suggerimento = new Libro(titoloSuggerito, autoriSuggeriti, annoSuggerito, editoreSuggerito, categoriaSuggerita);
	                consigli.add(suggerimento);
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    if (consigli.isEmpty()) {
	        System.out.println("Nessun consiglio disponibile per questo libro.");
	    } else {
	        for (Libro suggerimento : consigli) {
	            System.out.println(suggerimento);
	        }
	    }
	}

	/**
	 * Metodo principale che gestisce l'interazione con l'utente tramite un menu.
	 * L'utente può accedere come utente registrato o non registrato e gestire le librerie.
	 *
	 * @param args Argomenti della riga di comando (non utilizzati).
	 */	
	public static void main(String[] args) {
	    String filePathUtenti = "UtentiRegistrati.dati";
	    String filePathLibrerie = "Librerie.dati";
	    String filePathValutazioni = "ValutazioniLibri.dati";
	    String filePathConsigliLibri = "ConsigliLibri.dati";
	    String filePathLibri = "Libri.dati.csv"; // Aggiunto percorso del file dei libri
	    Scanner scanner = new Scanner(System.in);

	    while (true) {
	        System.out.println("Scegli un'opzione: ");
	        System.out.println("1 - Utente non registrato");
	        System.out.println("2 - Login");
	        System.out.println("3 - Esci");

	        int scelta = scanner.nextInt();
	        scanner.nextLine();

	        if (scelta == 1) {
	            // Gestisci utente non registrato
	            menuNonRegistrato(filePathLibri, filePathValutazioni, filePathConsigliLibri, filePathUtenti);
	        } else if (scelta == 2) {
	            // Login utente
	            System.out.println("Inserisci il tuo userid:");
	            String userid = scanner.nextLine();
	            System.out.println("Inserisci la tua password:");
	            String password = scanner.nextLine();

	            if (Utente.loginUtente(userid, password, filePathUtenti)) {
	                System.out.println("Login effettuato con successo!");
	                gestioneLibrerie(userid, filePathLibrerie, filePathValutazioni, filePathConsigliLibri);
	            } else {
	                System.out.println("Login fallito: userid o password errati.");
	            }
	        } else if (scelta == 3) {
	            System.out.println("Uscita dal programma.");
	            break;
	        } else {
	            System.out.println("Scelta non valida.");
	        }
	    }
	}
}

