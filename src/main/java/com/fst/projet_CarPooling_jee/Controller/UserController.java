package com.fst.projet_CarPooling_jee.Controller;

import com.fst.projet_CarPooling_jee.Entity.User;
import com.fst.projet_CarPooling_jee.Service.impl.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    //inject service class
    @Autowired
    private UserService userService;

    @ModelAttribute("loggedInUser")
    public User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }


    //display list of users
    @GetMapping("/")
    public String viewHomePage(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser); // Ajoute l'utilisateur connecté au modèle
        return "index"; // Retourne la page d'accueil
    }





    // Page de connexion (loginn)
    @GetMapping("/loginn")
    public String loginn() {
        return "loginn"; // Affiche la page de connexion
    }



    // Authentification de l'utilisateur
    @PostMapping("/loginn")
    public String authenticate(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        System.out.println("Email: " + email);  // Afficher l'email envoyé
        System.out.println("Password: " + password);  // Afficher le mot de passe envoyé

        User user = userService.authenticate(email, password); // Authentifier l'utilisateur

        if (user != null) {
            System.out.println("User authenticated: " + user.getFirstName());
            session.setAttribute("loggedInUser", user); // Enregistrer l'utilisateur dans la session
            session.setAttribute("loggedInUserId", user.getId());

            // Vérifiez si une redirection est définie
            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            if (redirectAfterLogin != null) {
                session.removeAttribute("redirectAfterLogin");
                return "redirect:" + redirectAfterLogin; // Redirige l'utilisateur vers sa destination initiale
            }
            return "redirect:/"; // Sinon, retour à la page d'accueil

        } else {
            model.addAttribute("error", "Email or password incorrect.");
            return "loginn";
        }

    }

    @GetMapping("/profile")
    public String getProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            throw new RuntimeException("Aucun utilisateur connecté !");
        }
        User user = userService.getUserWithReviews(userId);
        model.addAttribute("loggedInUser", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam("firstName") String firstName,
                                @RequestParam("lastName") String lastName,
                                @RequestParam("email") String email,
                                @RequestParam("phoneNumber") String phoneNumber,
                                HttpSession session, Model model) {
        // Récupérer l'utilisateur connecté depuis la session
        Long userId = (Long) session.getAttribute("loggedInUserId");
        if (userId == null) {
            throw new RuntimeException("Utilisateur non connecté !");
        }

        // Mettre à jour les informations de l'utilisateur
        User user = userService.findById(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        // Sauvegarder dans la base de données
        userService.updateUser(user);

        // Ajouter les données mises à jour dans le modèle
        model.addAttribute("loggedInUser", user);

        // Retourner à la page de profil
        return "redirect:/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalide la session et supprime tous les attributs
        return "redirect:/";  // Redirige vers la page d'accueil après la déconnexion
    }




    // Afficher le formulaire d'inscription
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";  // Retourne la vue register.html
    }

    // Traitement de l'enregistrement
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        System.out.println(user.getFirstName());
        System.out.println(user.getEmail());

        // Vérification si l'email existe déjà
        if (userService.emailExists(user.getEmail())) {
        model.addAttribute("error", "email already exists.");
        return "register";  // Retourne au formulaire d'inscription
        }

        // Optionnel : Hachage du mot de passe
    /*
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String hashedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(hashedPassword);
    */

    // Sauvegarde du nouvel utilisateur
        userService.saveUser(user);
        System.out.println("User saved successfully.");



    // Redirection vers la page de connexion ou accueil
        return "redirect:/loginn";  // Redirige vers la page de connexion après l'enregistrement
}



    @GetMapping("/AllUsers")
    public String viewDrivers(Model model) {
        List<User> drivers = userService.getAllUsers();
        model.addAttribute("drivers", drivers);
        return "AllUsers";
    }

}