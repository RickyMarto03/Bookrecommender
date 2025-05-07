package theknife;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.InputMismatchException; // Importato per gestire eccezioni

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
            try { // Aggiunto blocco try-catch per gestire input imprevisti
                if (currentUser == null) {
                    showMainMenu();
                    int choice = getUserChoice();
                    exit = handleMainMenu(choice);
                } else if (currentUser.getRuolo().equals("cliente")) {
                    showClientMenu();
                    int choice = getUserChoice();
                    handleClientMenu(choice);
                } else { // ristoratore
                    showRestaurateurMenu();
                    int choice = getUserChoice();
                    handleRestaurateurMenu(choice);
                }
            } catch (InputMismatchException e) {
                System.out.println("Errore: Inserire un numero valido.");
                scanner.next(); // Consuma l'input non valido
            } catch (Exception e) { // Catch generico per altri errori imprevisti
                System.out.println("Si è verificato un errore inatteso: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Salvataggio dati in corso...");
        // Salva tutti i dati prima di uscire
        fileManager.saveUsers(users);
        fileManager.saveRestaurants(restaurants);
        fileManager.saveReviews(reviews);
        fileManager.saveFavorites(favorites);
        System.out.println("Dati salvati. Arrivederci!");
    }

    private void showMainMenu() {
        System.out.println("\n=== TheKnife - Menu Principale ===");
        System.out.println("1. Accedi");
        System.out.println("2. Registrati");
        System.out.println("3. Cerca Ristoranti (Ospite)");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private void showClientMenu() {
        System.out.println("\n=== TheKnife - Menu Cliente ("+ currentUser.getUsername() +") ===");
        System.out.println("1. Cerca ristoranti");
        System.out.println("2. Visualizza preferiti");
        System.out.println("3. Aggiungi recensione");
        System.out.println("4. Le mie recensioni");
        System.out.println("5. Modifica recensione");
        System.out.println("6. Cancella recensione");
        System.out.println("7. Logout");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private void showRestaurateurMenu() {
        System.out.println("\n=== TheKnife - Menu Ristoratore ("+ currentUser.getUsername() +") ===");
        System.out.println("1. Aggiungi ristorante");
        System.out.println("2. I miei ristoranti");
        System.out.println("3. Gestisci recensioni (Rispondi)");
        System.out.println("4. Visualizza riepilogo recensioni");
        System.out.println("5. Logout");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private int getUserChoice() {
        int choice = -1; // Inizializza a un valore non valido
        while (choice == -1) {
           try {
               choice = scanner.nextInt();
           } catch (InputMismatchException e) {
               System.out.println("Errore: Inserire un numero valido!");
               scanner.next(); // Consuma l'input non valido
               System.out.print("Scelta: "); // Richiedi nuovamente l'input
           }
        }
        scanner.nextLine(); // Consuma il newline residuo dopo nextInt()
        return choice;
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
                // Il salvataggio avviene nel ciclo run() prima di uscire
                return true; // Indica al ciclo run() di uscire
            default:
                System.out.println("Scelta non valida!");
        }
        return false; // Indica al ciclo run() di continuare
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
                promptAndAddReview();
                break;
            case 4:
                showMyReviews();
                break;
             case 5:
                 modifyReview();
                 break;
             case 6:
                 deleteReview();
                 break;
            case 7:
                logout();
                break;
            case 0:
                 System.out.println("Per uscire dall'applicazione, scegli '0' dal menu principale dopo il logout.");
                 logout();
                break;
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
                showReviewSummary();
                break;
            case 5:
                logout();
                break;
            case 0:
                 System.out.println("Per uscire dall'applicazione, scegli '0' dal menu principale dopo il logout.");
                 logout();
                break;
            default:
                System.out.println("Scelta non valida!");
        }
    }

    private void login() {
        System.out.println("\n=== Login ===");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        boolean loggedIn = false;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.checkPassword(password)) {
                currentUser = user;
                System.out.println("Login effettuato con successo! Benvenuto/a " + currentUser.getNome());
                loggedIn = true;
                break; // Esce dal ciclo una volta trovato l'utente
            }
        }
        if (!loggedIn) {
             System.out.println("Username o password non validi!");
        }
    }

    private void register() {
        System.out.println("\n=== Registrazione ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Cognome: ");
        String cognome = scanner.nextLine();

        String username;
        boolean usernameExists;
        do {
            usernameExists = false;
            System.out.print("Username: ");
            username = scanner.nextLine();
            // Verifica username disponibile (case-insensitive)
            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(username)) {
                    System.out.println("Username già in uso! Scegline un altro.");
                    usernameExists = true;
                    break;
                }
            }
        } while (usernameExists);


        System.out.print("Password: ");
        String password = scanner.nextLine(); 

        System.out.print("Data di nascita (gg/mm/aaaa, opzionale, premi invio per saltare): ");
        String dataNascita = scanner.nextLine();

        System.out.print("Città di domicilio: ");
        String domicilio = scanner.nextLine();

        String ruolo;
        do {
            System.out.print("Ruolo (cliente/ristoratore): ");
            ruolo = scanner.nextLine().toLowerCase();
            if (!ruolo.equals("cliente") && !ruolo.equals("ristoratore")) {
                System.out.println("Ruolo non valido! Inserisci 'cliente' o 'ristoratore'.");
            }
        } while (!ruolo.equals("cliente") && !ruolo.equals("ristoratore"));


        User newUser = new User(nome, cognome, username, password, 
                                dataNascita.isEmpty() ? null : dataNascita,
                                domicilio, ruolo);
        users.add(newUser);
        // Salva subito il nuovo utente
        fileManager.saveUsers(users);
        System.out.println("Registrazione completata con successo!");
    }
    
    private void promptAndAddReview() {
        if (currentUser == null || !currentUser.getRuolo().equals("cliente")) {
            System.out.println("Errore: Solo i clienti possono aggiungere recensioni.");
            return;
        }

        System.out.println("\n=== Aggiungi Recensione ===");
        System.out.print("Inserisci il nome esatto del ristorante da recensire: ");
        String nomeRistorante = scanner.nextLine().trim();

        Restaurant restaurant = findRestaurantByName(nomeRistorante);

        if (restaurant == null) {
            System.out.println("Errore: Ristorante '" + nomeRistorante + "' non trovato nel sistema.");
            System.out.println("Verifica il nome o cerca il ristorante per trovarlo.");
            return;
        }

        addSingleReview(restaurant.getNome());
    }

    private void guestAccess() {
         System.out.println("\nAccesso come ospite...");
         searchRestaurants();
    }

    private void logout() {
        if (currentUser != null) {
            System.out.println("Logout di " + currentUser.getUsername() + " effettuato.");
            currentUser = null;
        } else {
            System.out.println("Nessun utente loggato.");
        }
    }

    // Metodo di ricerca RISTORANTI MIGLIORATO
    private void searchRestaurants() {
        System.out.println("\n=== Ricerca Ristoranti Avanzata ===");

        // 1. Locazione Geografica (Obbligatorio)
        String citta;
        do {
            System.out.print("Città (obbligatorio): ");
            citta = scanner.nextLine().trim().toLowerCase();
            if (citta.isEmpty()) {
                System.out.println("La città è un campo obbligatorio per la ricerca.");
            }
        } while (citta.isEmpty());

        // 2. Tipologia di Cucina (Opzionale)
        System.out.print("Tipo cucina (es. italiana,mediterranea, contemporante, innovativa, pizza, invio per saltare): ");
        String tipoCucina = scanner.nextLine().trim().toLowerCase();

        // 3. Fascia di Prezzo (Opzionale)
        double minPrezzo = -1, maxPrezzo = -1;
        System.out.print("Vuoi filtrare per fascia di prezzo? (si/no): ");
        if (scanner.nextLine().trim().toLowerCase().startsWith("si")) {
             while (minPrezzo < 0) {
                 System.out.print("Prezzo minimo (€, es. 10, inserisci 0 se non c'è minimo): ");
                 try {
                     minPrezzo = scanner.nextDouble();
                     if (minPrezzo < 0) System.out.println("Il prezzo minimo non può essere negativo.");
                 } catch (InputMismatchException e) {
                     System.out.println("Inserire un numero valido.");
                     scanner.next();
                 }
             }
             scanner.nextLine(); 
             while (maxPrezzo < 0 || maxPrezzo < minPrezzo) {
                 System.out.print("Prezzo massimo (€, es. 50): ");
                  try {
                     maxPrezzo = scanner.nextDouble();
                     if (maxPrezzo < 0) System.out.println("Il prezzo massimo non può essere negativo.");
                     else if (maxPrezzo < minPrezzo) System.out.println("Il prezzo massimo non può essere inferiore al minimo.");
                 } catch (InputMismatchException e) {
                     System.out.println("Inserire un numero valido.");
                     scanner.next();
                 }
             }
             scanner.nextLine();
             System.out.printf("Filtro prezzo: da %.2f€ a %.2f€\n", minPrezzo, maxPrezzo);
        }


        // 4. Servizio Delivery (Opzionale)
        Boolean requiresDelivery = null; // null = non specificato, true = sì, false = no
        System.out.print("Filtrare per servizio delivery? (si/no invio=Indifferente): ");
        String deliveryChoice = scanner.nextLine().trim().toLowerCase();
        if (deliveryChoice.startsWith("si")) {
            requiresDelivery = true;
             System.out.println("Filtro delivery: Sì");
        } else if (deliveryChoice.startsWith("no")) {
            requiresDelivery = false;
             System.out.println("Filtro delivery: No");
        } else {
             System.out.println("Filtro delivery: Indifferente");
        }


        // 5. Prenotazione Online (Opzionale)
        Boolean requiresOnlineBooking = null; // null = non specificato, true = sì, false = no
        System.out.print("Filtrare per prenotazione online? (si/no invio=Indifferente): ");
        String bookingChoice = scanner.nextLine().trim().toLowerCase();
        if (bookingChoice.startsWith("si")) {
            requiresOnlineBooking = true;
             System.out.println("Filtro prenotazione online: Sì");
        } else if (bookingChoice.startsWith("no")) {
            requiresOnlineBooking = false;
             System.out.println("Filtro prenotazione online: No");
        } else {
             System.out.println("Filtro prenotazione online: Indifferente");
        }


        // 6. Media Stelle (Opzionale)
        double minRating = -1;
        System.out.print("Vuoi filtrare per valutazione media minima? (si/no): ");
         if (scanner.nextLine().trim().toLowerCase().startsWith("si")) {
            while (minRating < 0 || minRating > 5) {
                System.out.print("Valutazione media minima (1-5, es. 3.5): ");
                 try {
                     minRating = scanner.nextDouble();
                     if (minRating < 0 || minRating > 5) System.out.println("Inserire un valore tra 0 e 5.");
                 } catch (InputMismatchException e) {
                     System.out.println("Inserire un numero valido.");
                     scanner.next(); 
                 }
            }
            scanner.nextLine(); 
             System.out.printf("Filtro valutazione: >= %.1f ⭐\n", minRating);
         }


        // Esecuzione della ricerca con i filtri combinati
        ArrayList<Restaurant> results = new ArrayList<>();
        for (Restaurant r : restaurants) {
            // Calcola la valutazione media per il ristorante corrente
            double avgRating = getAverageRating(r.getNome());

            // Applica i filtri
            boolean match = true; 

            // Filtro Città (obbligatorio, case-insensitive, corrispondenza parziale)
            if (!r.getCitta().toLowerCase().contains(citta)) {
                match = false;
            }

            // Filtro Tipo Cucina (se specificato, case-insensitive, corrispondenza parziale)
            if (match && !tipoCucina.isEmpty() && !r.getTipoCucina().toLowerCase().contains(tipoCucina)) {
                match = false;
            }

            // Filtro Fascia di Prezzo (se specificato)
            if (match && minPrezzo != -1 && maxPrezzo != -1) {
                 if (r.getFasciaPrezzo() < minPrezzo || r.getFasciaPrezzo() > maxPrezzo) {
                     match = false;
                 }
            }

             // Filtro Delivery (se specificato)
             if (match && requiresDelivery != null && r.hasDelivery() != requiresDelivery) {
                 match = false;
             }

             // Filtro Prenotazione Online (se specificato)
            if (match && requiresOnlineBooking != null && r.hasPrenotazioneOnline() != requiresOnlineBooking) {
                 match = false;
            }

            // Filtro Media Stelle (se specificato)
             if (match && minRating != -1 && avgRating < minRating) {
                 match = false;
             }


            // Se tutti i filtri sono soddisfatti, aggiungi ai risultati
            if (match) {
                results.add(r);
            }
        }

        // Visualizzazione risultati
        if (results.isEmpty()) {
            System.out.println("\nNessun ristorante trovato con i criteri specificati!");
        } else {
            System.out.println("\n--- Risultati della Ricerca (" + results.size() + ") ---");
            for (int i = 0; i < results.size(); i++) {
                Restaurant res = results.get(i);
                System.out.println("\n" + (i + 1) + ") " + res); 
                double rating = getAverageRating(res.getNome());
                if (rating > 0) {
                    System.out.printf("   Valutazione media: %.1f ⭐ (%d recensioni)\n", rating, getReviewCount(res.getNome()));
                } else {
                     System.out.println("   Nessuna recensione disponibile.");
                }
                 System.out.println("   Servizi: " + (res.hasDelivery() ? "Delivery " : "") + (res.hasPrenotazioneOnline() ? "Prenotazione Online" : ""));
            }
             System.out.println("------------------------------------");

            // Opzioni post-ricerca (solo per clienti loggati)
             if (currentUser != null && currentUser.getRuolo().equals("cliente")) {
                  System.out.println("\nOpzioni:");
                  System.out.println("1. Aggiungi un ristorante ai preferiti");
                  System.out.println("2. Aggiungi una recensione per un ristorante");
                  System.out.println("0. Torna al menu precedente");
                  System.out.print("Scelta: ");
                  int postSearchChoice = getUserChoice();
                  switch (postSearchChoice) {
                      case 1:
                          addFavoriteFromResults(results);
                          break;
                       case 2:
                           addReviewFromResults(results);
                           break;
                      case 0:
                          break; // Torna al menu
                      default:
                          System.out.println("Scelta non valida.");
                  }
             } else if (currentUser == null) {
                 // Ospite: può solo visualizzare o tornare indietro
                  System.out.println("\nPremi Invio per tornare al menu principale...");
                  scanner.nextLine();
             }
        }
    }


    // Metodo per aggiungere preferito dai risultati
    private void addFavoriteFromResults(ArrayList<Restaurant> results) {
        System.out.print("Inserisci il numero del ristorante da aggiungere ai preferiti (0 per annullare): ");
        int choice = getUserChoice();
        if (choice > 0 && choice <= results.size()) {
             Restaurant selected = results.get(choice - 1);
             addSingleFavorite(selected.getNome()); // Chiama il metodo per aggiungere un singolo preferito
        } else if (choice != 0) {
            System.out.println("Selezione non valida.");
        }
    }

     // Metodo per aggiungere recensione dai risultati
     private void addReviewFromResults(ArrayList<Restaurant> results) {
         System.out.print("Inserisci il numero del ristorante da recensire (0 per annullare): ");
         int choice = getUserChoice();
         if (choice > 0 && choice <= results.size()) {
             Restaurant selected = results.get(choice - 1);
             addSingleReview(selected.getNome()); // Chiama il metodo per aggiungere una singola recensione
         } else if (choice != 0) {
             System.out.println("Selezione non valida.");
         }
     }


    // Metodo per aggiungere UN SINGOLO preferito 
    private void addSingleFavorite(String nomeRistorante) {
         boolean alreadyFavorite = false;
         for (Favorite f : favorites) {
             if (f.getUsernameUtente().equals(currentUser.getUsername()) &&
                 f.getNomeRistorante().equalsIgnoreCase(nomeRistorante)) {
                 System.out.println("'" + nomeRistorante + "' è già nei tuoi preferiti!");
                 alreadyFavorite = true;
                 break;
             }
         }

         if (!alreadyFavorite) {
             System.out.print("Aggiungi una nota per '" + nomeRistorante + "' (opzionale, premi invio per saltare): ");
             String nota = scanner.nextLine();
             Favorite newFavorite = new Favorite(currentUser.getUsername(), nomeRistorante, nota.isEmpty() ? null : nota);
             favorites.add(newFavorite);
             fileManager.saveFavorites(favorites);
             System.out.println("'" + nomeRistorante + "' aggiunto ai preferiti!");
         }
    }

    // Metodo per aggiungere UNA SINGOLA recensione 
    private void addSingleReview(String nomeRistorante) {
        boolean alreadyReviewed = false;
        for(Review rev : reviews) {
             if(rev.getUsernameUtente().equals(currentUser.getUsername()) && rev.getNomeRistorante().equalsIgnoreCase(nomeRistorante)) {
                 System.out.println("Hai già scritto una recensione per '" + nomeRistorante + "'. Puoi modificarla o cancellarla dal menu principale.");
                 alreadyReviewed = true;
                 break;
             }
        }

        if (!alreadyReviewed) {
            int stelle = -1;
            while (stelle < 1 || stelle > 5) {
                System.out.print("Stelle per '" + nomeRistorante + "' (1-5): ");
                try {
                    stelle = scanner.nextInt();
                    if (stelle < 1 || stelle > 5) System.out.println("Inserire un numero tra 1 e 5.");
                } catch (InputMismatchException e) {
                    System.out.println("Inserire un numero valido.");
                    scanner.next();
                }
            }
            scanner.nextLine(); 

            System.out.print("Testo recensione: ");
            String testo = scanner.nextLine();

            Review newReview = new Review(currentUser.getUsername(), nomeRistorante, stelle, testo);
            reviews.add(newReview);
            fileManager.saveReviews(reviews);
            System.out.println("Recensione per '" + nomeRistorante + "' aggiunta con successo!");
        }
    }


    private void addRestaurant() {
        if (currentUser == null || !currentUser.getRuolo().equals("ristoratore")) {
            System.out.println("Errore: Solo gli utenti ristoratori possono aggiungere ristoranti!");
            return;
        }

        System.out.println("\n=== Aggiungi Nuovo Ristorante ===");

        String nome;
        boolean nameExists;
        do {
             nameExists = false;
             System.out.print("Nome Ristorante: ");
             nome = scanner.nextLine().trim();
             if (nome.isEmpty()) {
                 System.out.println("Il nome non può essere vuoto.");
                 continue;
             }
             // Verifica se esiste già un ristorante con lo stesso nome (case-insensitive)
             for(Restaurant r : restaurants) {
                 if(r.getNome().equalsIgnoreCase(nome)) {
                     System.out.println("Esiste già un ristorante con questo nome!");
                     nameExists = true;
                     break;
                 }
             }
        } while (nome.isEmpty() || nameExists);


        System.out.print("Nazione: ");
        String nazione = scanner.nextLine().trim();
        System.out.print("Città: ");
        String citta = scanner.nextLine().trim();
        System.out.print("Indirizzo: ");
        String indirizzo = scanner.nextLine().trim();

        
        double latitudine = 45.0; 
        double longitudine = 9.0;
        System.out.println("Latitudine/Longitudine impostate a valori predefiniti (da implementare).");

        double fasciaPrezzo = -1;
         while (fasciaPrezzo < 0) {
             System.out.print("Fascia prezzo medio (€, es. 25.50): ");
             try {
                fasciaPrezzo = scanner.nextDouble();
                 if (fasciaPrezzo < 0) System.out.println("Il prezzo non può essere negativo.");
             } catch (InputMismatchException e) {
                 System.out.println("Inserire un numero valido.");
                 scanner.next();
             }
         }
        scanner.nextLine();

        System.out.print("Servizio delivery disponibile? (s/n): ");
        boolean delivery = scanner.nextLine().trim().toLowerCase().startsWith("s");

        System.out.print("Prenotazione online disponibile? (s/n): ");
        boolean prenotazioneOnline = scanner.nextLine().trim().toLowerCase().startsWith("s");

        System.out.print("Tipo di cucina (es. Italiana, Pesce, Pizza): ");
        String tipoCucina = scanner.nextLine().trim();

        Restaurant newRestaurant = new Restaurant(nome, nazione, citta, indirizzo,
                latitudine, longitudine, fasciaPrezzo, delivery, prenotazioneOnline,
                tipoCucina, currentUser.getUsername());

        restaurants.add(newRestaurant);
        fileManager.saveRestaurants(restaurants); // Salva subito
        System.out.println("Ristorante '" + nome + "' aggiunto con successo!");
    }

    private void showFavorites() {
        if (currentUser == null || !currentUser.getRuolo().equals("cliente")) {
             System.out.println("Errore: Devi essere loggato come cliente per vedere i preferiti.");
             return;
        }
        System.out.println("\n=== I Miei Ristoranti Preferiti ===");
        ArrayList<Favorite> userFavorites = new ArrayList<>();
        for (Favorite f : favorites) {
            if (f.getUsernameUtente().equals(currentUser.getUsername())) {
                userFavorites.add(f);
            }
        }

        if (userFavorites.isEmpty()) {
            System.out.println("Non hai ancora ristoranti preferiti!");
             System.out.println("\nPuoi aggiungerli dopo aver effettuato una ricerca.");
             System.out.println("1. Cerca ristoranti per aggiungere preferiti");
             System.out.println("0. Torna al menu");
             System.out.print("Scelta: ");
             if(getUserChoice() == 1) {
                 searchRestaurants();
             }
        } else {
            for (int i = 0; i < userFavorites.size(); i++) {
                Favorite fav = userFavorites.get(i);
                Restaurant r = findRestaurantByName(fav.getNomeRistorante());
                System.out.println("\n" + (i + 1) + ") " + fav.getNomeRistorante());
                if (r != null) {
                    System.out.println("   Indirizzo: " + r.getIndirizzo() + ", " + r.getCitta());
                    System.out.println("   Tipo Cucina: " + r.getTipoCucina());
                } else {
                    System.out.println("   (Dettagli ristorante non trovati - potrebbe essere stato rimosso)");
                }
                if (fav.getNota() != null && !fav.getNota().isEmpty()) {
                    System.out.println("   Nota: " + fav.getNota());
                }
            }

            System.out.println("\nOpzioni Preferiti:");
            System.out.println("1. Rimuovi un preferito dalla lista");
            System.out.println("2. Modifica nota di un preferito");
            System.out.println("0. Torna al menu");
            System.out.print("Scelta: ");

            int choice = getUserChoice();
            switch (choice) {
                case 1: removeFavorite(userFavorites); break;
                case 2: modifyFavoriteNote(userFavorites); break;
                case 0: break;
                default: System.out.println("Scelta non valida!");
            }
        }
    }

     // Metodo per rimuovere un preferito DALLA LISTA VISUALIZZATA
     private void removeFavorite(ArrayList<Favorite> currentList) {
         System.out.print("Inserisci il numero del preferito da rimuovere (0 per annullare): ");
         int choice = getUserChoice();
         if (choice > 0 && choice <= currentList.size()) {
             Favorite toRemove = currentList.get(choice - 1);

             boolean removed = false;
             for (Iterator<Favorite> it = favorites.iterator(); it.hasNext();) {
                 Favorite f = it.next();
                 if (f.getUsernameUtente().equals(currentUser.getUsername()) &&
                     f.getNomeRistorante().equals(toRemove.getNomeRistorante())) {
                     it.remove();
                     removed = true;
                     break;
                 }
             }

             if(removed) {
                 fileManager.saveFavorites(favorites);
                 System.out.println("Ristorante '" + toRemove.getNomeRistorante() + "' rimosso dai preferiti!");
             } else {
                  System.out.println("Errore: Impossibile rimuovere il preferito specificato.");
             }

         } else if (choice != 0) {
             System.out.println("Selezione non valida.");
         }
     }

      // Metodo per modificare la nota di un preferito DALLA LISTA VISUALIZZATA
      private void modifyFavoriteNote(ArrayList<Favorite> currentList) {
          System.out.print("Inserisci il numero del preferito di cui modificare la nota (0 per annullare): ");
          int choice = getUserChoice();
          if (choice > 0 && choice <= currentList.size()) {
              Favorite toModify = currentList.get(choice - 1);

              System.out.println("Nota attuale: " + (toModify.getNota() != null ? toModify.getNota() : "(nessuna)"));
              System.out.print("Inserisci la nuova nota (lascia vuoto per rimuovere la nota): ");
              String newNota = scanner.nextLine();

              boolean updated = false;
              for (Favorite f : favorites) {
                   if (f.getUsernameUtente().equals(currentUser.getUsername()) &&
                       f.getNomeRistorante().equals(toModify.getNomeRistorante())) {
                       f.setNota(newNota.isEmpty() ? null : newNota);
                       updated = true;
                       break;
                   }
              }

              if(updated) {
                  fileManager.saveFavorites(favorites);
                  System.out.println("Nota per '" + toModify.getNomeRistorante() + "' aggiornata!");
              } else {
                  System.out.println("Errore: Impossibile aggiornare la nota per il preferito specificato.");
              }

          } else if (choice != 0) {
              System.out.println("Selezione non valida.");
          }
      }

    private void showMyReviews() {
         if (currentUser == null || !currentUser.getRuolo().equals("cliente")) {
             System.out.println("Errore: Devi essere loggato come cliente per vedere le tue recensioni.");
             return;
         }
        System.out.println("\n=== Le Mie Recensioni Scritte ===");
        ArrayList<Review> myReviewsList = new ArrayList<>();
        for (Review r : reviews) {
            if (r.getUsernameUtente().equals(currentUser.getUsername())) {
                myReviewsList.add(r);
            }
        }

        if (myReviewsList.isEmpty()) {
            System.out.println("Non hai ancora scritto recensioni!");
             System.out.println("\nPuoi aggiungerle dopo aver effettuato una ricerca.");
             System.out.println("1. Cerca ristoranti per aggiungere recensioni");
             System.out.println("0. Torna al menu");
             System.out.print("Scelta: ");
             if(getUserChoice() == 1) {
                 searchRestaurants();
             }
        } else {
             System.out.println("Hai scritto " + myReviewsList.size() + " recensioni:");
            for (int i = 0; i < myReviewsList.size(); i++) {
                Review rev = myReviewsList.get(i);
                System.out.println("\n" + (i + 1) + ") Ristorante: " + rev.getNomeRistorante());
                System.out.println("   Stelle: " + "⭐".repeat(rev.getStelle()));
                System.out.println("   Testo: " + rev.getTesto());
                if (rev.getRisposta() != null) {
                    System.out.println("   Risposta del ristoratore: " + rev.getRisposta());
                }
            }
             System.out.println("\nPuoi modificare o cancellare le tue recensioni usando le opzioni 5 e 6 del menu.");
        }
    }

    // FUNZIONE Modifica Recensione
    private void modifyReview() {
         if (currentUser == null || !currentUser.getRuolo().equals("cliente")) {
             System.out.println("Errore: Solo i clienti possono modificare le proprie recensioni.");
             return;
         }
         System.out.println("\n=== Modifica una Recensione ===");
         ArrayList<Review> myReviewsList = new ArrayList<>();
         for (Review r : reviews) {
             if (r.getUsernameUtente().equals(currentUser.getUsername())) {
                 myReviewsList.add(r);
             }
         }

         if (myReviewsList.isEmpty()) {
             System.out.println("Non hai recensioni da modificare.");
             return;
         }

         System.out.println("Seleziona la recensione da modificare:");
         for (int i = 0; i < myReviewsList.size(); i++) {
             Review rev = myReviewsList.get(i);
             System.out.println("\n" + (i + 1) + ") Ristorante: " + rev.getNomeRistorante() + " (" + "⭐".repeat(rev.getStelle()) + ")");
             System.out.println("   Testo: " + rev.getTesto());
         }
         System.out.println("0. Annulla");
         System.out.print("Scelta: ");
         int choice = getUserChoice();

         if (choice > 0 && choice <= myReviewsList.size()) {
             Review toModify = myReviewsList.get(choice - 1);

             // Trova la recensione originale nella lista principale per modificarla
             Review originalReview = findReviewByUserAndRestaurant(currentUser.getUsername(), toModify.getNomeRistorante());

             if (originalReview != null) {
                 int newStars = -1;
                 while (newStars < 1 || newStars > 5) {
                     System.out.print("Nuove stelle (1-5) (attuale: " + originalReview.getStelle() + "): ");
                     try {
                         newStars = scanner.nextInt();
                          if (newStars < 1 || newStars > 5) System.out.println("Inserire un numero tra 1 e 5.");
                     } catch (InputMismatchException e) {
                         System.out.println("Inserire un numero valido.");
                         scanner.next(); 
                     }
                 }
                 scanner.nextLine();

                 System.out.println("Nuovo testo (attuale: " + originalReview.getTesto() + "): ");
                 String newText = scanner.nextLine();

                 // Aggiorna l'oggetto Review originale
                 originalReview.setStelle(newStars);
                 originalReview.setTesto(newText); 

                 fileManager.saveReviews(reviews);
                 System.out.println("Recensione modificata con successo!");
             } else {
                  System.out.println("Errore: Impossibile trovare la recensione originale da modificare.");
             }
         } else if (choice != 0) {
             System.out.println("Selezione non valida.");
         }
    }

    // FUNZIONE: Cancella Recensione
    private void deleteReview() {
         if (currentUser == null || !currentUser.getRuolo().equals("cliente")) {
             System.out.println("Errore: Solo i clienti possono cancellare le proprie recensioni.");
             return;
         }
        System.out.println("\n=== Cancella una Recensione ===");
         ArrayList<Review> myReviewsList = new ArrayList<>();
         for (Review r : reviews) {
             if (r.getUsernameUtente().equals(currentUser.getUsername())) {
                 myReviewsList.add(r);
             }
         }

         if (myReviewsList.isEmpty()) {
             System.out.println("Non hai recensioni da cancellare.");
             return;
         }

         System.out.println("Seleziona la recensione da cancellare:");
          for (int i = 0; i < myReviewsList.size(); i++) {
             Review rev = myReviewsList.get(i);
             System.out.println("\n" + (i + 1) + ") Ristorante: " + rev.getNomeRistorante() + " (" + "⭐".repeat(rev.getStelle()) + ")");
              System.out.println("   Testo: " + rev.getTesto());
         }
         System.out.println("0. Annulla");
         System.out.print("Scelta: ");
         int choice = getUserChoice();

         if (choice > 0 && choice <= myReviewsList.size()) {
             Review toDelete = myReviewsList.get(choice - 1);

             System.out.print("Sei sicuro di voler cancellare la recensione per '" + toDelete.getNomeRistorante() + "'? (s/n): ");
             if (scanner.nextLine().trim().toLowerCase().startsWith("s")) {
                 boolean removed = false;
                 for (Iterator<Review> it = reviews.iterator(); it.hasNext();) {
                     Review r = it.next();
                     if (r.getUsernameUtente().equals(currentUser.getUsername()) &&
                         r.getNomeRistorante().equals(toDelete.getNomeRistorante())) {
                         it.remove();
                         removed = true;
                         break; 
                     }
                 }

                 if(removed) {
                     fileManager.saveReviews(reviews); 
                     System.out.println("Recensione cancellata con successo!");
                 } else {
                      System.out.println("Errore: Impossibile cancellare la recensione specificata.");
                 }
             } else {
                  System.out.println("Cancellazione annullata.");
             }
         } else if (choice != 0) {
             System.out.println("Selezione non valida.");
         }
    }

    // Metodo ausiliario per trovare una specifica recensione
    private Review findReviewByUserAndRestaurant(String username, String restaurantName) {
        for (Review r : reviews) {
            if (r.getUsernameUtente().equals(username) && r.getNomeRistorante().equalsIgnoreCase(restaurantName)) {
                return r;
            }
        }
        return null;
    }


    private void manageReviews() { // Rispondere alle recensioni
         if (currentUser == null || !currentUser.getRuolo().equals("ristoratore")) {
             System.out.println("Errore: Solo i ristoratori possono rispondere alle recensioni.");
             return;
         }
        System.out.println("\n=== Rispondi alle Recensioni ===");
        ArrayList<Restaurant> myRestaurants = getMyRestaurants();

        if (myRestaurants.isEmpty()) {
            System.out.println("Non hai ancora registrato ristoranti. Non puoi gestire recensioni.");
            return;
        }

        ArrayList<Review> reviewsToManage = new ArrayList<>();
        System.out.println("Recensioni per i tuoi ristoranti:");
        boolean foundReviews = false;
        for (Restaurant r : myRestaurants) {
            boolean restaurantHeaderPrinted = false;
            for (Review review : reviews) {
                if (review.getNomeRistorante().equals(r.getNome())) {
                     if (!restaurantHeaderPrinted) {
                          System.out.println("\n--- Recensioni per: " + r.getNome() + " ---");
                          restaurantHeaderPrinted = true;
                          foundReviews = true;
                     }
                    reviewsToManage.add(review);
                    System.out.println("\nRecensione " + reviewsToManage.size() + ":");
                    System.out.println("  Utente: " + review.getUsernameUtente());
                    System.out.println("  Stelle: " + "⭐".repeat(review.getStelle()));
                    System.out.println("  Testo: " + review.getTesto());
                    if (review.getRisposta() == null) {
                        System.out.println("  Stato: In attesa di risposta");
                    } else {
                        System.out.println("  La tua risposta: " + review.getRisposta());
                    }
                }
            }
        }

        if (!foundReviews) {
             System.out.println("Nessuna recensione trovata per i tuoi ristoranti.");
             return;
        }


        System.out.print("\nSeleziona il numero della recensione a cui aggiungere/modificare una risposta (0 per tornare indietro): ");
        int choice = getUserChoice();

        if (choice > 0 && choice <= reviewsToManage.size()) {
             Review selectedReview = reviewsToManage.get(choice - 1);

             // Trova la recensione originale per modificarla
             Review originalReview = findReviewById(selectedReview.getUsernameUtente(), selectedReview.getNomeRistorante(), selectedReview.getDataCreazione()); // Assumendo un ID o modo univoco

             if(originalReview != null) {
                 if (originalReview.getRisposta() != null) {
                     System.out.println("Questa recensione ha già una risposta: " + originalReview.getRisposta());
                     System.out.print("Vuoi sovrascriverla? (s/n): ");
                     if (!scanner.nextLine().trim().toLowerCase().startsWith("s")) {
                         System.out.println("Operazione annullata.");
                         return;
                     }
                 }

                 System.out.print("Inserisci la tua risposta: ");
                 String risposta = scanner.nextLine();
                 originalReview.setRisposta(risposta.isEmpty() ? null : risposta);
                 fileManager.saveReviews(reviews);
                 System.out.println("Risposta aggiunta/modificata con successo!");
             } else {
                 System.out.println("Errore: impossibile trovare la recensione originale.");
             }

        } else if (choice != 0) {
             System.out.println("Selezione non valida.");
        }
    }

     // Metodo ausiliario per trovare una recensione
     private Review findReviewById(String username, String restaurantName, java.time.LocalDateTime creationDate) {
         for (Review r : reviews) {
             if (r.getUsernameUtente().equals(username) &&
                 r.getNomeRistorante().equals(restaurantName) &&
                 r.getDataCreazione().equals(creationDate)) {
                 return r;
             }
         }
         return null;
     }


    // FUNZIONE: Visualizza Riepilogo Recensioni
    private void showReviewSummary() {
         if (currentUser == null || !currentUser.getRuolo().equals("ristoratore")) {
             System.out.println("Errore: Solo i ristoratori possono visualizzare il riepilogo.");
             return;
         }
         System.out.println("\n=== Riepilogo Recensioni dei Miei Ristoranti ===");
         ArrayList<Restaurant> myRestaurants = getMyRestaurants();

         if (myRestaurants.isEmpty()) {
             System.out.println("Non hai ancora registrato ristoranti.");
             return;
         }

         boolean foundAnyReviews = false;
         for (Restaurant r : myRestaurants) {
             int reviewCount = 0;
             double totalStars = 0;
             for (Review rev : reviews) {
                 if (rev.getNomeRistorante().equals(r.getNome())) {
                     reviewCount++;
                     totalStars += rev.getStelle();
                 }
             }

             System.out.println("\n--- " + r.getNome() + " ---");
             if (reviewCount > 0) {
                 foundAnyReviews = true;
                 double averageRating = totalStars / reviewCount;
                 System.out.printf("  Numero totale recensioni: %d\n", reviewCount);
                 System.out.printf("  Valutazione media: %.1f ⭐\n", averageRating);
             } else {
                 System.out.println("  Nessuna recensione ricevuta.");
             }
         }
         if (!foundAnyReviews) {
              System.out.println("\nNessuna recensione ricevuta per nessuno dei tuoi ristoranti.");
         }
    }


    private void showMyRestaurants() {
         if (currentUser == null || !currentUser.getRuolo().equals("ristoratore")) {
             System.out.println("Errore: Devi essere loggato come ristoratore.");
             return;
         }
        System.out.println("\n=== I Miei Ristoranti Registrati ===");
        ArrayList<Restaurant> myRestaurants = getMyRestaurants();

        if (myRestaurants.isEmpty()) {
            System.out.println("Non hai ancora registrato ristoranti!");
            System.out.println("Usa l'opzione 'Aggiungi ristorante' nel menu.");
            return;
        }

        System.out.println("Hai registrato " + myRestaurants.size() + " ristoranti:");
        for (int i = 0; i < myRestaurants.size(); i++) {
            Restaurant r = myRestaurants.get(i);
            System.out.println("\n" + (i + 1) + ") " + r); 
            double rating = getAverageRating(r.getNome());
             int count = getReviewCount(r.getNome());
            if (rating > 0) {
                System.out.printf("   Valutazione media: %.1f ⭐ (%d recensioni)\n", rating, count);
            } else {
                System.out.println("   Nessuna recensione disponibile.");
            }
             System.out.println("   Servizi: Delivery=" + (r.hasDelivery()?"Sì":"No") + ", Prenotazione Online=" + (r.hasPrenotazioneOnline()?"Sì":"No"));
        }
    }

    // Metodo ausiliario per trovare un ristorante per nome 
    private Restaurant findRestaurantByName(String name) {
        for (Restaurant r : restaurants) {
            if (r.getNome().equalsIgnoreCase(name)) { 
                return r;
            }
        }
        return null; 
    }

    // Metodo ausiliario per ottenere i ristoranti dell'utente corrente
    private ArrayList<Restaurant> getMyRestaurants() {
        ArrayList<Restaurant> myRestaurants = new ArrayList<>();
         if (currentUser == null || !currentUser.getRuolo().equals("ristoratore")) {
             return myRestaurants;
         }
        for (Restaurant r : restaurants) {
            if (r.getProprietario().equalsIgnoreCase(currentUser.getUsername())) {
                myRestaurants.add(r);
            }
        }
        return myRestaurants;
    }

    // Metodo ausiliario per calcolare la media delle stelle
    private double getAverageRating(String nomeRistorante) {
        double total = 0;
        int count = 0;
        for (Review r : reviews) {
            if (r.getNomeRistorante().equalsIgnoreCase(nomeRistorante)) {
                total += r.getStelle();
                count++;
            }
        }
        return count > 0 ? total / count : 0;
    }

     // Metodo ausiliario per contare le recensioni
     private int getReviewCount(String nomeRistorante) {
         int count = 0;
         for (Review r : reviews) {
             if (r.getNomeRistorante().equalsIgnoreCase(nomeRistorante)) {
                 count++;
             }
         }
         return count;
     }
} 