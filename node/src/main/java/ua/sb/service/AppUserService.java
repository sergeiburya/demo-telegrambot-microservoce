package ua.sb.service;

import ua.sb.model.AppUser;

public interface AppUserService {

    String registerUser(AppUser appUser);

    String setEmail(AppUser appUser, String email);
}
