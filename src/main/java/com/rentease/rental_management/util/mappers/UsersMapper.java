

package com.rentease.rental_management.util.mappers;

import com.rentease.rental_management.auth.dto.UserAsParty;
import com.rentease.rental_management.auth.dto.UsersProfile;
import com.rentease.rental_management.auth.dto.UsersRegistration;
import com.rentease.rental_management.auth.dto.UsersUpdate;
import com.rentease.rental_management.auth.entity.Users;

import java.time.LocalDate;

public class UsersMapper {

    public static Users UsersRegistrationToUsers(UsersRegistration usersRegistration)
    {
        Users users = new Users();

        users.setFirstName(usersRegistration.getFirstName());
        users.setLastName(usersRegistration.getLastName());
        users.setGender(usersRegistration.getGender());
        users.setDateOfBirth(usersRegistration.getDateOfBirth());
        users.setUsername(usersRegistration.getUsername());
        users.setPassword(usersRegistration.getPassword());
        users.setEmail(usersRegistration.getEmail());
        users.setIsEmailVerified(false);
        users.setPhoneNumber(usersRegistration.getPhoneNumber());
        users.setIsPhoneNumberVerified(true);
        users.setIsAccountLocked(true);
        users.setLastLoginDate(LocalDate.now());
        users.setPasswordExpiryDate(LocalDate.now().plusDays(90));

        return users;
    }

    public static Users UsersUpdateToUsers(Users users, UsersUpdate usersUpdate)
    {
        users.setFirstName(usersUpdate.firstName());
        users.setLastName(usersUpdate.lastName());
        users.setGender(usersUpdate.gender());
        users.setDateOfBirth(usersUpdate.dateOfBirth());

        return users;
    }

    public static UsersProfile UsersToUsersProfile(Users users)
    {
        UsersProfile usersProfile = new UsersProfile();

        usersProfile.setFirstName(users.getFirstName());
        usersProfile.setLastName(users.getLastName());
        usersProfile.setUsername(users.getUsername());
        usersProfile.setDateOfBirth(users.getDateOfBirth());
        usersProfile.setGender(users.getGender());
        usersProfile.setEmail(users.getEmail());
        usersProfile.setIsEmailVerified(users.getIsEmailVerified());
        usersProfile.setPhoneNumber(users.getPhoneNumber());
        usersProfile.setIsPhoneNumberVerified(users.getIsPhoneNumberVerified());

        return usersProfile;
    }

    public static UserAsParty UsersToUserAsSeller(Users users)
    {
        UserAsParty userAsSeller = new UserAsParty();

        userAsSeller.setFirstName(users.getFirstName());
        userAsSeller.setLastName(users.getLastName());
        userAsSeller.setGender(users.getGender());
        userAsSeller.setEmail(users.getEmail());
        userAsSeller.setIsEmailVerified(users.getIsEmailVerified());
        userAsSeller.setPhoneNumber(users.getPhoneNumber());
        userAsSeller.setIsPhoneNumberVerified(users.getIsPhoneNumberVerified());

        return userAsSeller;
    }
}
