package theknife;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Classe principale che gestisce le funzionalità dell'applicazione TheKnife
 */
public class TheKnife {
    private Scanner scanner;
    private FileManager fileManager;
    private ArrayList<User> users;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Review> reviews;
    private ArrayList<Favorite> favorites;
    private User currentUser;

    public TheKnife() {
        scanner = new Scanner(System.in);
        fileManager = new FileManager();
        users = fileManager.loadUsers();
        restaurants = fileManager.loadRestaurants();
        reviews = fileManager.loadReviews();
        favorites = fileManager.loadFavorites();
        currentUser = null;
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            if (currentUser == null) {
                showMainMenu();
                int choice = getUserChoice();
                exit = handleMainMenu(choice);
            } else if (currentUser.getRuolo().equals("cliente")) {
                showClientMenu();
                int choice = getUserChoice();
                handleClientMenu(choice);
            } else {  // ristoratore
                showRestaurateurMenu();
                int choice = getUserChoice();
                handleRestaurateurMenu(choice);
            }
        }
        scanner.close();
    }

    private void showMainMenu() {
        System.out.println("\n=== TheKnife - Menu Principale ===");
        System.out.println("1. Accedi");
        System.out.println("2. Registrati");
        System.out.println("3. Continua come ospite");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private void showClientMenu() {
        System.out.println("\n=== TheKnife - Menu Cliente ===");
        System.out.println("1. Cerca ristoranti");
        System.out.println("2. Visualizza preferiti");
        System.out.println("3. Aggiungi recensione");
        System.out.println("4. Le mie recensioni");
        System.out.println("5. Logout");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private void showRestaurateurMenu() {
        System.out.println("\n=== TheKnife - Menu Ristoratore ===");
        System.out.println("1. Aggiungi ristorante");
        System.out.println("2. I miei ristoranti");
        System.out.println("3. Gestisci recensioni");
        System.out.println("4. Logout");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Inserire un numero valido!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private boolean handleMainMenu(int choice) {
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            case 3:
                guestAccess();
                break;
            case 0:
                System.out.println("Arrivederci!");
                return true;
            default:
                System.out.println("Scelta non valida!");
        }
        return false;
    }

    private void handleClientMenu(int choice) {
        switch (choice) {
            case 1:
                searchRestaurants();
                break;
            case 2:
                showFavorites();
                break;
            case 3:
                addReview();
                break;
            case 4:
                showMyReviews();
                break;
            case 5:
                logout();
                break;
            case 0:
                System.out.println("Arrivederci!");
                System.exit(0);
            default:
                System.out.println("Scelta non valida!");
        }
    }

    private void handleRestaurateurMenu(int choice) {
        switch (choice) {
            case 1:
                addRestaurant();
                break;
            case 2:
                showMyRestaurants();
                break;
            case 3:
                manageReviews();
                break;
            case 4:
                logout();
                break;
            case 0:
                System.out.println("Arrivederci!");
                System.exit(0);
            default:
                System.out.println("Scelta non valida!");
        }
    }

    private void login() {
        scanner.nextLine(); // Pulisce il buffer
        System.out.println("\n=== Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.checkPassword(password)) {
                currentUser = user;
                System.out.println("Login effettuato con successo!");
                return;
            }
        }
        System.out.println("Username o password non validi!");
    }

    private void register() {
        scanner.nextLine(); // Pulisce il buffer
        System.out.println("\n=== Registrazione ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();

        // Verifica username disponibile
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                System.out.println("Username già in uso!");
                return;
            }
        }

        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Data di nascita (gg/mm/aaaa, invio per saltare): ");
        String dataNascita = scanner.nextLine();
        System.out.print("Città di domicilio: ");
        String domicilio = scanner.nextLine();
        System.out.print("Ruolo (cliente/ristoratore): ");
        String ruolo = scanner.nextLine().toLowerCase();

        if (!ruolo.equals("cliente") && !ruolo.equals("ristoratore")) {
            System.out.println("Ruolo non valido!");
            return;
        }

        User newUser = new User(nome, cognome, username, password, 
                              dataNascita.isEmpty() ? null : dataNascita, 
                              domicilio, ruolo);
        users.add(newUser);
        fileManager.saveUsers(users);
        System.out.println("Registrazione completata con successo!");
    }

    private void guestAccess() {
        scanner.nextLine(); // Pulisce il buffer
        System.out.print("\nInserisci la città per la ricerca: ");
        String citta = scanner.nextLine();
        searchRestaurants(citta);
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logout effettuato!");
    }

    private void searchRestaurants() {
        System.out.println("\n=== Ricerca Ristoranti ===");
        scanner.nextLine(); // Pulisce il buffer

        System.out.print("Città: ");
        String citta = scanner.nextLine().toLowerCase();
        searchRestaurants(citta);
    }

    private void searchRestaurants(String citta) {
        System.out.println("\nFiltri (opzionali):");
        System.out.print("Tipo cucina (invio per saltare): ");
        String tipoCucina = scanner.nextLine().toLowerCase();
        
        System.out.print("Prezzo massimo (0 per saltare): ");
        double maxPrezzo = scanner.nextDouble();

        ArrayList<Restaurant> results = new ArrayList<>();
        for (Restaurant r : restaurants) {
            if (r.getCitta().toLowerCase().contains(citta) &&
                (tipoCucina.isEmpty() || r.getTipoCucina().toLowerCase().contains(tipoCucina)) &&
                (maxPrezzo == 0 || r.getFasciaPrezzo() <= maxPrezzo)) {
                results.add(r);
            }
        }

        if (results.isEmpty()) {
            System.out.println("Nessun ristorante trovato!");
            return;
        }

        System.out.println("\nRisultati della ricerca:");
        for (int i = 0; i < results.size(); i++) {
            System.out.println("\n" + (i+1) + ") " + results.get(i));
            double mediaStelle = getAverageRating(results.get(i).getNome());
            if (mediaStelle > 0) {
                System.out.printf("Valutazione media: %.1f ⭐\n", mediaStelle);
            }
        }

        // Se l'utente è loggato, offri la possibilità di aggiungere ai preferiti
        if (currentUser != null && currentUser.getRuolo().equals("cliente")) {
            System.out.print("\nVuoi aggiungere un ristorante ai preferiti? (s/n): ");
            scanner.nextLine(); // Pulisce il buffer
            if (scanner.nextLine().toLowerCase().startsWith("s")) {
                addFavorite();
            }
        }
    }

    private void addRestaurant() {
        if (currentUser == null || !currentUser.getRuolo().equals("ristoratore")) {
            System.out.println("Non hai i permessi per aggiungere ristoranti!");
            return;
        }

        System.out.println("\n=== Aggiungi Nuovo Ristorante ===");
        scanner.nextLine(); // Pulisce il buffer

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nazione: ");
        String nazione = scanner.nextLine();
        System.out.print("Città: ");
        String citta = scanner.nextLine();
        System.out.print("Indirizzo: ");
        String indirizzo = scanner.nextLine();
        
        // Per semplicità usiamo coordinate dummy (da implementare con API geocoding)
        double latitudine = 45.0;
        double longitudine = 9.0;

        System.out.print("Fascia prezzo medio (€): ");
        double fasciaPrezzo = scanner.nextDouble();
        scanner.nextLine(); // Pulisce il buffer

        System.out.print("Servizio delivery disponibile? (s/n): ");
        boolean delivery = scanner.nextLine().toLowerCase().startsWith("s");

        System.out.print("Prenotazione online disponibile? (s/n): ");
        boolean prenotazioneOnline = scanner.nextLine().toLowerCase().startsWith("s");

        System.out.print("Tipo di cucina: ");
        String tipoCucina = scanner.nextLine();

        Restaurant newRestaurant = new Restaurant(nome, nazione, citta, indirizzo,
            latitudine, longitudine, fasciaPrezzo, delivery, prenotazioneOnline,
            tipoCucina, currentUser.getUsername());

        restaurants.add(newRestaurant);
        fileManager.saveRestaurants(restaurants);
        System.out.println("Ristorante aggiunto con successo!");
    }

    private void showFavorites() {
        System.out.println("\n=== I Miei Preferiti ===");
        boolean found = false;
        
        for (Favorite f : favorites) {
            if (f.getUsernameUtente().equals(currentUser.getUsername())) {
                found = true;
                Restaurant r = findRestaurantByName(f.getNomeRistorante());
                if (r != null) {
                    System.out.println("\n" + r);
                    if (f.getNota() != null) {
                        System.out.println("Nota: " + f.getNota());
                    }
                }
            }
        }

        if (!found) {
            System.out.println("Non hai ancora ristoranti preferiti!");
            return;
        }

        System.out.println("\n1. Aggiungi nuovo preferito");
        System.out.println("2. Rimuovi preferito");
        System.out.println("0. Torna al menu");
        
        int choice = getUserChoice();
        switch (choice) {
            case 1:
                addFavorite();
                break;
            case 2:
                removeFavorite();
                break;
            case 0:
                break;
            default:
                System.out.println("Scelta non valida!");
        }
    }

    private void addReview() {
        if (currentUser == null || currentUser.getRuolo().equals("ristoratore")) {
            System.out.println("Solo i clienti possono lasciare recensioni!");
            return;
        }

        System.out.println("\n=== Aggiungi Recensione ===");
        scanner.nextLine(); // Pulisce il buffer

        System.out.print("Nome del ristorante: ");
        String nomeRistorante = scanner.nextLine();

        // Verifica esistenza ristorante
        boolean found = false;
        for (Restaurant r : restaurants) {
            if (r.getNome().equalsIgnoreCase(nomeRistorante)) {
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Ristorante non trovato!");
            return;
        }

        System.out.print("Stelle (1-5): ");
        int stelle = scanner.nextInt();
        scanner.nextLine(); // Pulisce il buffer

        System.out.print("Testo recensione: ");
        String testo = scanner.nextLine();

        Review newReview = new Review(currentUser.getUsername(), nomeRistorante, stelle, testo);
        reviews.add(newReview);
        fileManager.saveReviews(reviews);
        System.out.println("Recensione aggiunta con successo!");
    }

    private void showMyReviews() {
        System.out.println("\n=== Le Mie Recensioni ===");
        boolean found = false;
        
        for (Review r : reviews) {
            if (r.getUsernameUtente().equals(currentUser.getUsername())) {
                found = true;
                System.out.println("\n" + r);
            }
        }

        if (!found) {
            System.out.println("Non hai ancora scritto recensioni!");
        }
    }

    private void manageReviews() {
        System.out.println("\n=== Gestione Recensioni ===");
        ArrayList<Restaurant> myRestaurants = getMyRestaurants();
        
        if (myRestaurants.isEmpty()) {
            System.out.println("Non hai ancora registrato ristoranti!");
            return;
        }

        ArrayList<Review> pendingReviews = new ArrayList<>();
        for (Restaurant r : myRestaurants) {
            for (Review review : reviews) {
                if (review.getNomeRistorante().equals(r.getNome()) && 
                    review.getRisposta() == null) {
                    pendingReviews.add(review);
                }
            }
        }

        if (pendingReviews.isEmpty()) {
            System.out.println("Non ci sono nuove recensioni da gestire!");
            return;
        }

        System.out.println("\nRecensioni in attesa di risposta:");
        for (int i = 0; i < pendingReviews.size(); i++) {
            System.out.println("\n" + (i+1) + ") " + pendingReviews.get(i));
        }

        System.out.print("\nSeleziona il numero della recensione a cui rispondere (0 per tornare indietro): ");
        int choice = getUserChoice() - 1;
        
        if (choice >= 0 && choice < pendingReviews.size()) {
            scanner.nextLine(); // Pulisce il buffer
            System.out.print("Inserisci la tua risposta: ");
            String risposta = scanner.nextLine();
            pendingReviews.get(choice).setRisposta(risposta);
            fileManager.saveReviews(reviews);
            System.out.println("Risposta aggiunta con successo!");
        }
    }

    private void showMyRestaurants() {
        System.out.println("\n=== I Miei Ristoranti ===");
        ArrayList<Restaurant> myRestaurants = getMyRestaurants();
        
        if (myRestaurants.isEmpty()) {
            System.out.println("Non hai ancora registrato ristoranti!");
            return;
        }

        for (Restaurant r : myRestaurants) {
            System.out.println("\n" + r);
            double rating = getAverageRating(r.getNome());
            if (rating > 0) {
                System.out.printf("Valutazione media: %.1f ⭐\n", rating);
            }
        }
    }

    private Restaurant findRestaurantByName(String name) {
        for (Restaurant r : restaurants) {
            if (r.getNome().equals(name)) {
                return r;
            }
        }
        return null;
    }

    private ArrayList<Restaurant> getMyRestaurants() {
        ArrayList<Restaurant> myRestaurants = new ArrayList<>();
        for (Restaurant r : restaurants) {
            if (r.getProprietario().equals(currentUser.getUsername())) {
                myRestaurants.add(r);
            }
        }
        return myRestaurants;
    }

    private double getAverageRating(String nomeRistorante) {
        int total = 0;
        int count = 0;
        
        for (Review r : reviews) {
            if (r.getNomeRistorante().equals(nomeRistorante)) {
                total += r.getStelle();
                count++;
            }
        }
        
        return count > 0 ? (double) total / count : 0;
    }

    private void addFavorite() {
        scanner.nextLine(); // Pulisce il buffer
        System.out.print("Nome del ristorante da aggiungere ai preferiti: ");
        String nome = scanner.nextLine();
        
        Restaurant r = findRestaurantByName(nome);
        if (r == null) {
            System.out.println("Ristorante non trovato!");
            return;
        }

        // Verifica se già nei preferiti
        for (Favorite f : favorites) {
            if (f.getUsernameUtente().equals(currentUser.getUsername()) && 
                f.getNomeRistorante().equals(nome)) {
                System.out.println("Ristorante già nei preferiti!");
                return;
            }
        }

        System.out.print("Aggiungi una nota (opzionale, premi invio per saltare): ");
        String nota = scanner.nextLine();
        if (nota.isEmpty()) nota = null;

        favorites.add(new Favorite(currentUser.getUsername(), nome, nota));
        fileManager.saveFavorites(favorites);
        System.out.println("Ristorante aggiunto ai preferiti!");
    }

    private void removeFavorite() {
        scanner.nextLine(); // Pulisce il buffer
        System.out.print("Nome del ristorante da rimuovere dai preferiti: ");
        String nome = scanner.nextLine();

        for (Iterator<Favorite> it = favorites.iterator(); it.hasNext();) {
            Favorite f = it.next();
            if (f.getUsernameUtente().equals(currentUser.getUsername()) && 
                f.getNomeRistorante().equals(nome)) {
                it.remove();
                fileManager.saveFavorites(favorites);
                System.out.println("Ristorante rimosso dai preferiti!");
                return;
            }
        }
        System.out.println("Ristorante non trovato nei preferiti!");
    }
}
