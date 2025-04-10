package theknife;

import java.io.*;
import java.util.ArrayList;

/**
 * Gestisce le operazioni di lettura e scrittura su file CSV per TheKnife.
 * Assicura la coerenza tra caricamento e salvataggio dei dati.
 */

public class FileManager {
    private static final String DATA_DIR = "data"; 
    private static final String FAVORITES_FILE = DATA_DIR + "/favorites.csv";
    private static final String REVIEWS_FILE = DATA_DIR + "/reviews.csv";
    private static final String RESTAURANTS_FILE = DATA_DIR + "/restaurants.csv";
    private static final String USERS_FILE = DATA_DIR + "/users.csv";
    private static final String CSV_SEPARATOR = ",";

    // Costruttore: crea la directory 'data' se non esiste all'avvio
    public FileManager() {
        createDataDirectoryIfNotExists();
    }

    private void createDataDirectoryIfNotExists() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            System.out.println("Creazione directory: " + DATA_DIR);
            boolean created = dataDir.mkdirs();
             if(!created) {
                 System.err.println("ATTENZIONE: Impossibile creare la directory " + DATA_DIR);
             }
        }
    }

    // --- Gestione Utenti (User) ---

    /**
     * Carica la lista degli utenti dal file CSV (users.csv).
     * Gestisce correttamente il valore "null" per dataNascita.
     * @return ArrayList<User> lista degli utenti caricati.
     */
    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);

        if (!file.exists()) {
            System.out.println("File " + USERS_FILE + " non trovato, verrà creato.");
            createUsersFile(); 
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); 
            if (line == null || !line.startsWith("nome,cognome,username")) {
                 System.err.println("Errore: Header del file " + USERS_FILE + " non valido o file vuoto.");
                 return users; 
            }

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR, -1); // 
                if (data.length == 7) { 
                    try {
                         // Gestione di dataNascita "null"
                        String dataNascita = data[4].trim().equalsIgnoreCase("null") || data[4].trim().isEmpty() ? null : data[4].trim();

                        User user = new User(
                                data[0].trim(),
                                data[1].trim(), 
                                data[2].trim(), 
                                data[3].trim(), 
                                dataNascita,
                                data[5].trim(),
                                data[6].trim()  
                        );
                        users.add(user);
                    } catch (ArrayIndexOutOfBoundsException e) {
                         System.err.println("Errore lettura riga (campi mancanti?) in " + USERS_FILE + ": " + line);
                    } catch (Exception e) { 
                         System.err.println("Errore parsing riga in " + USERS_FILE + ": '" + line + "' - " + e.getMessage());
                    }
                } else {
                     System.err.println("Riga scartata in " + USERS_FILE + " (numero campi errato): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file " + USERS_FILE + ": " + e.getMessage());
        }
        return users;
    }

    /**
     * Salva la lista degli utenti su file CSV (users.csv).
     * Scrive "null" per dataNascita se il valore è null.
     * @param users lista degli utenti da salvare.
     */
    public void saveUsers(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("nome,cognome,username,password,dataNascita,domicilio,ruolo");

            // Scrivi i dati di ogni utente
            for (User user : users) {
                String dataNascitaStr = (user.getDataNascita() == null || user.getDataNascita().isEmpty()) ? "null" : user.getDataNascita();

                writer.println(String.join(CSV_SEPARATOR,
                        user.getNome(),
                        user.getCognome(),
                        user.getUsername(),
                        user.getPassword(), 
                        dataNascitaStr,
                        user.getDomicilio(),
                        user.getRuolo()
                ));
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio degli utenti su " + USERS_FILE + ": " + e.getMessage());
        }
    }

    /**
     * Crea il file users.csv con l'header corretto se non esiste.
     */
    private void createUsersFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("nome,cognome,username,password,dataNascita,domicilio,ruolo");
            System.out.println("File " + USERS_FILE + " creato con header.");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file " + USERS_FILE + ": " + e.getMessage());
        }
    }


    // --- Gestione Ristoranti (Restaurant) ---

    /**
     * Carica la lista dei ristoranti dal file CSV (restaurants.csv).
     * @return ArrayList<Restaurant> lista dei ristoranti caricati.
     */
    public ArrayList<Restaurant> loadRestaurants() {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
         File file = new File(RESTAURANTS_FILE);

         if (!file.exists()) {
             System.out.println("File " + RESTAURANTS_FILE + " non trovato, verrà creato.");
             createRestaurantsFile(); 
             return restaurants; 
         }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
             String line = reader.readLine(); 
             if (line == null || !line.startsWith("nome,nazione,citta")) {
                 System.err.println("Errore: Header del file " + RESTAURANTS_FILE + " non valido o file vuoto.");
                 return restaurants; 
             }

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR, -1);
                if (data.length == 11) { 
                    try {
                        Restaurant restaurant = new Restaurant(
                                data[0].trim(), 
                                data[1].trim(), 
                                data[2].trim(), 
                                data[3].trim(), 
                                Double.parseDouble(data[4].trim()), 
                                Double.parseDouble(data[5].trim()), 
                                Double.parseDouble(data[6].trim()), 
                                Boolean.parseBoolean(data[7].trim()), 
                                Boolean.parseBoolean(data[8].trim()), 
                                data[9].trim(), 
                                data[10].trim() 
                        );
                        restaurants.add(restaurant);
                    } catch (NumberFormatException e) {
                         System.err.println("Errore formato numero in " + RESTAURANTS_FILE + ": '" + line + "' - " + e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                         System.err.println("Errore lettura riga (campi mancanti?) in " + RESTAURANTS_FILE + ": " + line);
                    } catch (Exception e) {
                         System.err.println("Errore parsing riga in " + RESTAURANTS_FILE + ": '" + line + "' - " + e.getMessage());
                    }
                } else {
                     System.err.println("Riga scartata in " + RESTAURANTS_FILE + " (numero campi errato): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file " + RESTAURANTS_FILE + ": " + e.getMessage());
        }
        return restaurants;
    }

    /**
     * Salva la lista dei ristoranti su file CSV (restaurants.csv).
     * Usa l'header e il formato dati coerenti con loadRestaurants.
     * @param restaurants lista dei ristoranti da salvare.
     */
    public void saveRestaurants(ArrayList<Restaurant> restaurants) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESTAURANTS_FILE))) {
            writer.println("nome,nazione,citta,indirizzo,latitudine,longitudine,fasciaPrezzo,delivery,prenotazioneOnline,tipoCucina,proprietario");

            // Scrivi i dati per ogni ristorante
            for (Restaurant r : restaurants) {
                writer.println(String.join(CSV_SEPARATOR,
                        r.getNome(),
                        r.getNazione(),
                        r.getCitta(),
                        r.getIndirizzo(),
                        String.valueOf(r.getLatitudine()), 
                        String.valueOf(r.getLongitudine()),
                        String.valueOf(r.getFasciaPrezzo()),
                        String.valueOf(r.hasDelivery()),
                        String.valueOf(r.hasPrenotazioneOnline()),
                        r.getTipoCucina(),
                        r.getProprietario()
                ));
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file " + RESTAURANTS_FILE + ": " + e.getMessage());
        }
    }

    /**
     * Crea il file restaurants.csv con l'header corretto se non esiste.
     */
    private void createRestaurantsFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESTAURANTS_FILE))) {
            writer.println("nome,nazione,citta,indirizzo,latitudine,longitudine,fasciaPrezzo,delivery,prenotazioneOnline,tipoCucina,proprietario");
             System.out.println("File " + RESTAURANTS_FILE + " creato con header.");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file " + RESTAURANTS_FILE + ": " + e.getMessage());
        }
    }


    // --- Gestione Recensioni (Review) ---

    /**
     * Carica la lista delle recensioni dal file CSV (reviews.csv).
     * Gestisce il valore "null" per la risposta.
     * @return ArrayList<Review> lista delle recensioni caricate.
     */
    public ArrayList<Review> loadReviews() {
        ArrayList<Review> reviews = new ArrayList<>();
        File file = new File(REVIEWS_FILE);

        if (!file.exists()) {
             System.out.println("File " + REVIEWS_FILE + " non trovato, verrà creato.");
            createReviewsFile();
            return reviews;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
             if (line == null || !line.startsWith("usernameUtente,nomeRistorante,stelle")) {
                 System.err.println("Errore: Header del file " + REVIEWS_FILE + " non valido o file vuoto.");
                 return reviews;
             }

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR, -1); 
                if (data.length == 5) { 
                    try {
                        String risposta = data[4].trim().equalsIgnoreCase("null") || data[4].trim().isEmpty() ? null : data[4].trim();

                        Review review = new Review(
                                data[0].trim(), 
                                data[1].trim(), 
                                Integer.parseInt(data[2].trim()), 
                                data[3].trim()  
                        );
                        if (risposta != null) {
                            review.setRisposta(risposta);
                        }
                        reviews.add(review);
                    } catch (NumberFormatException e) {
                         System.err.println("Errore formato numero (stelle) in " + REVIEWS_FILE + ": '" + line + "' - " + e.getMessage());
                    } catch (ArrayIndexOutOfBoundsException e) {
                         System.err.println("Errore lettura riga (campi mancanti?) in " + REVIEWS_FILE + ": " + line);
                    } catch (Exception e) {
                         System.err.println("Errore parsing riga in " + REVIEWS_FILE + ": '" + line + "' - " + e.getMessage());
                    }
                } else {
                     System.err.println("Riga scartata in " + REVIEWS_FILE + " (numero campi errato): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file " + REVIEWS_FILE + ": " + e.getMessage());
        }
        return reviews;
    }

    /**
     * Salva la lista delle recensioni su file CSV (reviews.csv).
     * Scrive "null" se la risposta è null.
     * @param reviews lista delle recensioni da salvare.
     */
    public void saveReviews(ArrayList<Review> reviews) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REVIEWS_FILE))) {
            // Header corretto
            writer.println("usernameUtente,nomeRistorante,stelle,testo,risposta");

            for (Review r : reviews) {
                String rispostaStr = (r.getRisposta() == null || r.getRisposta().isEmpty()) ? "null" : r.getRisposta();

                writer.println(String.join(CSV_SEPARATOR,
                        r.getUsernameUtente(),
                        r.getNomeRistorante(),
                        String.valueOf(r.getStelle()),
                        r.getTesto(),
                        rispostaStr
                ));
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file " + REVIEWS_FILE + ": " + e.getMessage());
        }
    }

    /**
     * Crea il file reviews.csv con l'header corretto se non esiste.
     */
    private void createReviewsFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REVIEWS_FILE))) {
            writer.println("usernameUtente,nomeRistorante,stelle,testo,risposta");
             System.out.println("File " + REVIEWS_FILE + " creato con header.");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file " + REVIEWS_FILE + ": " + e.getMessage());
        }
    }


    // --- Gestione Preferiti (Favorite) ---

    /**
     * Carica la lista dei preferiti dal file CSV (favorites.csv).
     * Gestisce il valore "null" per la nota.
     * @return ArrayList<Favorite> lista dei preferiti caricati.
     */
    public ArrayList<Favorite> loadFavorites() {
        ArrayList<Favorite> favorites = new ArrayList<>();
        File file = new File(FAVORITES_FILE);

         if (!file.exists()) {
             System.out.println("File " + FAVORITES_FILE + " non trovato, verrà creato.");
             createFavoritesFile();
             return favorites;
         }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); 
            if (line == null || !line.startsWith("usernameUtente,nomeRistorante,nota")) {
                 System.err.println("Errore: Header del file " + FAVORITES_FILE + " non valido o file vuoto.");
                 return favorites;
             }

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR, -1);
                if (data.length == 3) {
                     try {
                         String nota = data[2].trim().equalsIgnoreCase("null") || data[2].trim().isEmpty() ? null : data[2].trim();

                         Favorite favorite = new Favorite(
                                 data[0].trim(), 
                                 data[1].trim(), 
                                 nota           
                         );
                         favorites.add(favorite);
                     } catch (Exception e) {
                         System.err.println("Errore parsing riga in " + FAVORITES_FILE + ": '" + line + "' - " + e.getMessage());
                    }
                } else {
                    System.err.println("Riga scartata in " + FAVORITES_FILE + " (numero campi errato): " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file " + FAVORITES_FILE + ": " + e.getMessage());
        }
        return favorites;
    }

    /**
     * Salva la lista dei preferiti su file CSV (favorites.csv).
     * Scrive "null" se la nota è null.
     * @param favorites lista dei preferiti da salvare.
     */
    public void saveFavorites(ArrayList<Favorite> favorites) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FAVORITES_FILE))) {
            // Header
            writer.println("usernameUtente,nomeRistorante,nota");

            for (Favorite f : favorites) {
                String notaStr = (f.getNota() == null || f.getNota().isEmpty()) ? "null" : f.getNota();

                writer.println(String.join(CSV_SEPARATOR,
                        f.getUsernameUtente(),
                        f.getNomeRistorante(),
                        notaStr
                ));
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file " + FAVORITES_FILE + ": " + e.getMessage());
        }
    }

    /**
     * Crea il file favorites.csv con l'header corretto se non esiste.
     */
    private void createFavoritesFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FAVORITES_FILE))) {
            writer.println("usernameUtente,nomeRistorante,nota");
            System.out.println("File " + FAVORITES_FILE + " creato con header.");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file " + FAVORITES_FILE + ": " + e.getMessage());
        }
    }
}