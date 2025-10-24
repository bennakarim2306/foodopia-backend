package com.hop.drivesharing.hopapplication.service;

import com.hop.drivesharing.hopapplication.data.user.User;
import com.hop.drivesharing.hopapplication.data.user.UserRepository;
import com.hop.drivesharing.hopapplication.exception.AddContactToListException;
import com.hop.drivesharing.hopapplication.rest.v1.dto.AccountInformationResponse;
import com.hop.drivesharing.hopapplication.rest.v1.dto.UserLight;
import com.hop.drivesharing.hopapplication.rest.v1.dto.AddressDto;
import com.hop.drivesharing.hopapplication.data.item.Address;
import com.hop.drivesharing.hopapplication.security.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AccountService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final NotificationService notificationService;

    public AccountService(UserRepository userRepository, JwtService jwtService, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.notificationService = notificationService;
    }

    public void addContactToList(String authHeader, String contactEmail) throws AddContactToListException {
        String userEmail = extractUserEmail(authHeader);
        User user = findUserByEmail(userEmail);
        User contact = findUserByEmail(contactEmail);

        // Add contact to user's contact list
        String updatedContactsList = updateContactList(user.getContactsList(), contact.getId());
        user.setContactsList(updatedContactsList);
        userRepository.save(user);

        // Add user to contact's contact list
        String updatedContactContactsList = updateContactList(contact.getContactsList(), user.getId());
        contact.setContactsList(updatedContactContactsList);
        userRepository.save(contact);

        log.info("Added contact {} to user {} and user {} to contact {}", contact.getId(), user.getId(), user.getId(), contact.getId());
    }

    public AccountInformationResponse getContactsList(String authHeader) {
        String userEmail = extractUserEmail(authHeader);
        User user = findUserByEmail(userEmail);

        List<UserLight> contacts = Optional.ofNullable(user.getContactsIdsList())
                .orElse(List.of())
                .stream()
                .map(this::mapToUserLight)
                .collect(Collectors.toList());

        return AccountInformationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .friends(contacts)
                .build();
    }

    public AccountInformationResponse confirmContactRequest(String authHeader, String contactEmail) throws AddContactToListException {
        String userEmail = extractUserEmail(authHeader);
        User user = findUserByEmail(userEmail);
        User contact = findUserByEmail(contactEmail);

        if (!user.getContactsRequestList().contains(contact.getId())) {
            throw new AddContactToListException("Contact not found in request list");
        }

        user.setContactsRequestList(removeFromList(user.getContactsRequestList(), contact.getId()));
        user.setContactsList(updateContactList(user.getContactsList(), contact.getId()));
        userRepository.save(user);

        return buildAccountInformationResponse(user);
    }

    public AccountInformationResponse generateContactRequest(String authHeader, String contactEmail) throws AddContactToListException {
        String userEmail = extractUserEmail(authHeader);
        User user = findUserByEmail(userEmail);
        User contact = findUserByEmail(contactEmail);

        notificationService.sendContactNotification(user.getEmail(), contact.getEmail());
        user.setContactsRequestList(updateContactList(user.getContactsRequestList(), contact.getId()));
        userRepository.save(user);

        return buildAccountInformationResponse(user);
    }

    public void deleteContact(String authHeader, String contactEmail) {
        String userEmail = extractUserEmail(authHeader);
        User user = findUserByEmail(userEmail);
        User contact = findUserByEmail(contactEmail);

        user.setContactsList(removeFromList(user.getContactsList(), contact.getId()));
        userRepository.save(user);
    }

    public void setAddressByUserId(String authHeader, String userId, AddressDto addressDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.setAddress(mapToAddress(addressDto));
        userRepository.save(user);
    }

    public void setAddressByEmail(String authHeader, AddressDto addressDto) {
        String email = jwtService.extractUserEmail(authHeader.substring(7)); // "sub"-Claim extrahieren
        User user = findUserByEmail(email);
        user.setAddress(mapToAddress(addressDto));
        userRepository.save(user);
    }

    public AddressDto getAddressByUserId(String authHeader, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return mapToAddressDto(user.getAddress());
    }

    public AddressDto getAddressByEmail(String authHeader) {
        String email = jwtService.extractUserEmail(authHeader.substring(7)); // "sub"-Claim extrahieren
        User user = findUserByEmail(email);
        return mapToAddressDto(user.getAddress());
    }

    // Private helper methods

    private String extractUserEmail(String authHeader) {
        return jwtService.extractUserEmail(authHeader.substring(7));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    private String updateContactList(String currentList, String newContactId) {
        if (!StringUtils.hasLength(currentList)) {
            return newContactId;
        }
        if (currentList.contains(newContactId)) {
            return currentList;
        }
        return currentList + "|" + newContactId;
    }

    private String removeFromList(String currentList, String contactId) {
        if (!StringUtils.hasLength(currentList)) {
            return "";
        }
        return List.of(currentList.split("\\|")).stream()
                .filter(id -> !id.equals(contactId))
                .collect(Collectors.joining("|"));
    }

    private UserLight mapToUserLight(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return UserLight.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }

    private AccountInformationResponse buildAccountInformationResponse(User user) {
        return AccountInformationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    private Address mapToAddress(AddressDto dto) {
        if (dto == null) return null;
        return Address.builder()
                .city(dto.getCity())
                .street(dto.getStreet())
                .zip(dto.getZip())
                .lat(dto.getLat())
                .lng(dto.getLng())
                .build();
    }

    private AddressDto mapToAddressDto(Address address) {
        if (address == null) return null;
        AddressDto dto = new AddressDto();
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setZip(address.getZip());
        dto.setLat(address.getLat());
        dto.setLng(address.getLng());
        return dto;
    }
}