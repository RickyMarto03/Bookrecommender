package theknife;

import java.io.*;
import java.util.ArrayList;

/**
 * Gestisce le operazioni di lettura e scrittura su file
 * @author [Nome Cognome]
 * @matricola [Numero Matricola]
 * @sede [VA/CO]
 */
public class FileManager {
	private static final String FAVORITES_FILE = "data/favorites.csv";
	private static final String REVIEWS_FILE = "data/reviews.csv";
	private static final String RESTAURANTS_FILE = "data/restaurants.csv";
    private static final String USERS_FILE = "data/users.csv";
    private static final String CSV_SEPARATOR = ",";

    /**
     * Carica la lista degli utenti dal file CSV
     * @return ArrayList<User> lista degli utenti
     */
    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
        
        // Crea la directory data se non esiste
        new File("data").mkdirs();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            // Salta l'header
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                // nome,cognome,username,password,dataNascita,luogoDomicilio,ruolo
                User user = new User(
                    data[0],             // nome
                    data[1],             // cognome
                    data[2],             // username
                    data[3],             // password
                    data[4].equals("null") ? null : data[4], // dataNascita
                    data[5],             // luogoDomicilio
                    data[6]              // ruolo
                );
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            // Se il file non esiste, lo creiamo con l'header
            createUsersFile();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file users.csv: " + e.getMessage());
        }
        
        return users;
    }

    /**
     * Salva la lista degli utenti su file CSV
     * @param users lista degli utenti da salvare
     */
    public void saveUsers(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            // Scrivi l'header
            writer.println("nome,cognome,username,password,dataNascita,domicilio,ruolo");
            
            // Scrivi i dati di ogni utente
            for (User user : users) {
                writer.println(String.format("%s,%s,%s,%s,%s,%s,%s",
                    user.getNome(),
                    user.getCognome(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getDataNascita() != null ? user.getDataNascita() : "",
                    user.getDomicilio(),  // Cambiato da getLuogoDomicilio a getDomicilio
                    user.getRuolo()));
            }
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio degli utenti: " + e.getMessage());
        }
    }

    /**
     * Crea il file users.csv con l'header
     */
    private void createUsersFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("nome,cognome,username,password,dataNascita,luogoDomicilio,ruolo");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file users.csv: " + e.getMessage());
        }
    }
    
    /**
     * Carica la lista dei ristoranti dal file CSV
     */
    public ArrayList<Restaurant> loadRestaurants() {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(RESTAURANTS_FILE))) {
            String line;
            // Salta l'header
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                Restaurant restaurant = new Restaurant(
                    data[0],                    // nome
                    data[1],                    // nazione
                    data[2],                    // citta
                    data[3],                    // indirizzo
                    Double.parseDouble(data[4]), // latitudine
                    Double.parseDouble(data[5]), // longitudine
                    Double.parseDouble(data[6]), // fasciaPrezzo
                    Boolean.parseBoolean(data[7]), // delivery
                    Boolean.parseBoolean(data[8]), // prenotazioneOnline
                    data[9],                    // tipoCucina
                    data[10]                    // proprietario
                );
                restaurants.add(restaurant);
            }
        } catch (FileNotFoundException e) {
            createRestaurantsFile();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file restaurants.csv: " + e.getMessage());
        }
        
        return restaurants;
    }

    /**
     * Salva la lista dei ristoranti su file CSV
     */
    public void saveRestaurants(ArrayList<Restaurant> restaurants) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESTAURANTS_FILE))) {
            // Scrivi header
            writer.println("Name,Address,Location,Price,Cuisine,Longitude,Latitude,PhoneNumber,Url,WebsiteUrl,Award,GreenStar,FacilitiesAndServices,Description");
            
            // Scrivi dati ristoranti
            for (Restaurant r : restaurants) {
                writer.println(
                    r.getNome() + CSV_SEPARATOR +
                    r.getNazione() + CSV_SEPARATOR +
                    r.getCitta() + CSV_SEPARATOR +
                    r.getIndirizzo() + CSV_SEPARATOR +
                    r.getLatitudine() + CSV_SEPARATOR +
                    r.getLongitudine() + CSV_SEPARATOR +
                    r.getFasciaPrezzo() + CSV_SEPARATOR +
                    r.hasDelivery() + CSV_SEPARATOR +
                    r.hasPrenotazioneOnline() + CSV_SEPARATOR +
                    r.getTipoCucina() + CSV_SEPARATOR +
                    r.getProprietario()
                );
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file restaurants.csv: " + e.getMessage());
        }
    }

    private void createRestaurantsFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RESTAURANTS_FILE))) {
            writer.println("nome,nazione,citta,indirizzo,latitudine,longitudine,fasciaPrezzo,delivery,prenotazioneOnline,tipoCucina,proprietario");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file restaurants.csv: " + e.getMessage());
        }
    }
    
    public ArrayList<Review> loadReviews() {
        ArrayList<Review> reviews = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEWS_FILE))) {
            String line;
            reader.readLine(); // Salta header
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                Review review = new Review(
                    data[0],  // usernameUtente
                    data[1],  // nomeRistorante
                    Integer.parseInt(data[2]),  // stelle
                    data[3]   // testo
                );
                if (!data[4].equals("null")) {
                    review.setRisposta(data[4]);  // risposta
                }
                reviews.add(review);
            }
        } catch (FileNotFoundException e) {
            createReviewsFile();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file reviews.csv: " + e.getMessage());
        }
        
        return reviews;
    }

    public void saveReviews(ArrayList<Review> reviews) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REVIEWS_FILE))) {
            writer.println("usernameUtente,nomeRistorante,stelle,testo,risposta");
            
            for (Review r : reviews) {
                writer.println(
                    r.getUsernameUtente() + CSV_SEPARATOR +
                    r.getNomeRistorante() + CSV_SEPARATOR +
                    r.getStelle() + CSV_SEPARATOR +
                    r.getTesto() + CSV_SEPARATOR +
                    (r.getRisposta() == null ? "null" : r.getRisposta())
                );
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file reviews.csv: " + e.getMessage());
        }
    }

    private void createReviewsFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REVIEWS_FILE))) {
            writer.println("usernameUtente,nomeRistorante,stelle,testo,risposta");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file reviews.csv: " + e.getMessage());
        }
    }
    
    public ArrayList<Favorite> loadFavorites() {
        ArrayList<Favorite> favorites = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FAVORITES_FILE))) {
            String line;
            reader.readLine(); // Salta header
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(CSV_SEPARATOR);
                favorites.add(new Favorite(
                    data[0],  // usernameUtente
                    data[1],  // nomeRistorante
                    data[2].equals("null") ? null : data[2]  // nota
                ));
            }
        } catch (FileNotFoundException e) {
            createFavoritesFile();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file favorites.csv: " + e.getMessage());
        }
        
        return favorites;
    }

    public void saveFavorites(ArrayList<Favorite> favorites) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FAVORITES_FILE))) {
            writer.println("usernameUtente,nomeRistorante,nota");
            
            for (Favorite f : favorites) {
                writer.println(
                    f.getUsernameUtente() + CSV_SEPARATOR +
                    f.getNomeRistorante() + CSV_SEPARATOR +
                    (f.getNota() == null ? "null" : f.getNota())
                );
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio del file favorites.csv: " + e.getMessage());
        }
    }

    private void createFavoritesFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FAVORITES_FILE))) {
            writer.println("usernameUtente,nomeRistorante,nota");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file favorites.csv: " + e.getMessage());
        }
    }
}

