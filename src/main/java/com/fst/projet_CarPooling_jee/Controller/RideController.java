package com.fst.projet_CarPooling_jee.Controller;

import com.fst.projet_CarPooling_jee.Entity.Ride;
import com.fst.projet_CarPooling_jee.Entity.User;
import com.fst.projet_CarPooling_jee.Repository.UserRepository;
import com.fst.projet_CarPooling_jee.Service.impl.RideService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;

@Controller
public class RideController {

    private static final Logger logger = LoggerFactory.getLogger(RideController.class);

    @Autowired
    private RideService rideService;

    @Autowired
    private UserRepository userRepository;





    /*@GetMapping("/showNewRidesForm")
    public String showNewRidesForm(Model model, HttpSession session){
        // Vérifier si l'utilisateur est connecté
        if (session.getAttribute("loggedInUserId") == null) {
            // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
            return "redirect:/loginn";
        }
        //create model attribute to bind form data
        //creation dun new ride:
        Ride ride = new Ride();
        model.addAttribute("ride", ride);
        //Thymleaf template will access the empty ride object for binding form data
        return "addRideForm";
    }*/

    @GetMapping("/showNewRidesForm")
    public String showNewRidesForm(Model model,HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // Si l'utilisateur n'est pas connecté, sauvegardez la page initiale visée
            session.setAttribute("redirectAfterLogin", "/showNewRidesForm");
            return "redirect:/loginn";
        }

        model.addAttribute("loggedInUser", loggedInUser); // Ajouter l'utilisateur au modèle
        model.addAttribute("ride", new Ride()); // Ajoutez un objet ride pour le binding
        return "addRideForm"; // Vue de formulaire de ride
    }

    // Ajouter un nouveau ride
    @PostMapping("/saveRide")
    public String saveRide(@ModelAttribute("ride") Ride ride, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginn"; // Sécurité supplémentaire
        }
        ride.setDriver(loggedInUser); // Associez l'utilisateur connecté comme conducteur
        rideService.saveRide(ride); // Sauvegarder le ride dans la BD
        return "redirect:/myRides"; // Rediriger vers une liste des rides
    }


    //seperate method for handling pagination
    /*@GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        //fixing the pageSize:
        int pageSize = 6;

        //Récupérer les trajets paginés
        Page<Ride> page = rideService.findPaginated(pageNo,pageSize);
        List<Ride> userRides = page.getContent();
        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("userRides", userRides);
        return "myRides";
    }*/



    @GetMapping("/myRides")
    public String myRides(Model model, HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
        if (loggedInUserId == null) {
            return "redirect:/login"; // Rediriger vers la page de connexion si non connecté
        }

        // Récupérer l'utilisateur depuis la base de données ou le service
        User loggedInUser = (User) session.getAttribute("loggedInUser"); // Exemple
        model.addAttribute("loggedInUser", loggedInUser);

        // Récupérer les trajets créés par l'utilisateur
        List<Ride> userRides = rideService.findRidesByDriverId(loggedInUserId);
        model.addAttribute("userRides", userRides);

        // Appeler la méthode de pagination avec le numéro de page initial (1)
        //return findPaginated(1,model);
        return "myRides";

    }




    // Save a new ride
    /*@PostMapping("/saveRide")
    public String saveRide(@ModelAttribute("ride") Ride ride,HttpSession session) {
        // Vérifier si l'utilisateur est connecté
        Long loggedInUserId = (Long) session.getAttribute("loggedInUserId"); // Assurez-vous que l'ID de l'utilisateur est stocké dans la session
        if (loggedInUserId != null) {
            // Trouver l'utilisateur dans la base de données par ID
            User driver = userRepository.findById(loggedInUserId).orElseThrow(() -> new RuntimeException("User not found"));

            // Associer l'utilisateur comme conducteur du trajet
            ride.setDriver(driver); // L'attribut "driver" est de type User, donc on lui assigne l'objet "User"
        } else {
            // Si l'utilisateur n'est pas connecté, rediriger vers la page de connexion
            return "redirect:/login"; // Assurez-vous que vous avez une page de connexion
        }
        rideService.saveRide(ride);
        return "redirect:/searchRides"; // Redirect to search page after saving
    }*/

    /*@PostMapping("/publishRide")
    public String publishRide(@ModelAttribute Ride ride, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/loginn";
        }

        // Associez le trajet à l'utilisateur connecté
        ride.setDriver(loggedInUser);
        rideService.saveRide(ride);

        return "redirect:/myRides"; // Redirige vers une page affichant les trajets de l'utilisateur
    }*/



    @GetMapping("/searchRides")
    public String searchRides(
            @RequestParam(value = "startLocation", required = false) String startLocation,
            @RequestParam(value = "endLocation", required = false) String endLocation,
            @RequestParam(value = "departureDate", required = false) String departureDate,
            @RequestParam(value = "nbPassengers", required = false) Integer nbPassengers,
            @RequestParam(value = "sortOption", required = false) String sortOption,
            Model model, HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);

        logger.info("Search parameters received: startLocation={}, endLocation={}, departureDate={}, nbPassengers={}",
                startLocation, endLocation, departureDate, nbPassengers);

        // Paramètres de tri par défaut
        String sortField = "pricePerSeat";
        String sortDir = "asc";

        // Gérer le tri basé sur l'option sélectionnée
        if (sortOption != null) {
            switch (sortOption) {
                case "ascendingPrice":
                    sortField = "pricePerSeat";
                    sortDir = "asc";
                    break;
                case "descendingPrice":
                    sortField = "pricePerSeat";
                    sortDir = "desc";
                    break;
                case "closestDeparture":
                    sortField = "departureDate";
                    sortDir = "asc";
                    break;
                default:
                    break;
            }
        }

        boolean searchPerformed = (startLocation != null && !startLocation.isEmpty()) ||
                (endLocation != null && !endLocation.isEmpty()) ||
                (departureDate != null && !departureDate.isEmpty()) ||
                (nbPassengers != null);

        List<Ride> listRides = null;

        // Execute search only if at least one parameter is provided
        if (searchPerformed) {
            Date sqlDate = null;
            if (departureDate != null && !departureDate.isEmpty()) {
                try {
                    sqlDate = Date.valueOf(departureDate); // Convert string to SQL Date
                } catch (IllegalArgumentException e) {
                    model.addAttribute("error", "Invalid date format. Use yyyy-MM-dd.");
                    return "searchRides"; // Return to the search page with an error
                }
            }
            listRides = rideService.findRides(startLocation, endLocation, sqlDate, nbPassengers);

            // Apply sorting logic manually
            if ("ascendingPrice".equals(sortOption)) {
                listRides.sort(Comparator.comparing(Ride::getPricePerSeat));
            } else if ("descendingPrice".equals(sortOption)) {
                listRides.sort(Comparator.comparing(Ride::getPricePerSeat).reversed());
            } else if ("closestDeparture".equals(sortOption)) {
                listRides.sort(Comparator.comparing(Ride::getDepartureDate));
            }
        }

        model.addAttribute("searchPerformed", searchPerformed);
        model.addAttribute("listRides", listRides);
        model.addAttribute("sortOption", sortOption);  // Add sort option to the model
        model.addAttribute("startLocation", startLocation);
        model.addAttribute("endLocation", endLocation);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("nbPassengers", nbPassengers);

        return "searchRides";  // Return the view for displaying search results
    }


}
