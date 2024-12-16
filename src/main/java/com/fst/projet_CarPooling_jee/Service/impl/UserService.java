package com.fst.projet_CarPooling_jee.Service.impl;

import com.fst.projet_CarPooling_jee.Entity.User;
import com.fst.projet_CarPooling_jee.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void saveUser(User user) {
        /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);*/  // Remplace le mot de passe en clair par le mot de passe haché
        this.userRepository.save(user);
    }



    // Authentifier l'utilisateur avec son email et son mot de passe
    public User authenticate(String email, String password) {
        // Chercher l'utilisateur par email
        User user = userRepository.findByEmail(email);
        // Vérifier si l'utilisateur existe et que le mot de passe correspond
        if (user != null) {
            // Vérifier que le mot de passe correspond
            if (user.getPassword().equals(password)) {
                return user; // Retourner l'utilisateur si le mot de passe est correct
            } else {
                System.out.println("Mot de passe incorrect pour l'utilisateur : " + email);
            }
        } else {
            System.out.println("Aucun utilisateur trouvé avec l'email : " + email);
        }
        return null; // Retourner null si l'authentification échoue
    }


    // Méthode pour vérifier si un email existe déjà dans la base de données
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public User findById(long id)
    {
        return userRepository.findById(id) ;
    }


}
