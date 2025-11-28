package com.foodopia.backend.rest.v1;

import com.foodopia.backend.exception.AddContactToListException;
import com.foodopia.backend.rest.v1.dto.AccountInformationResponse;
import com.foodopia.backend.rest.v1.dto.AddressDto;
import com.foodopia.backend.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

@PostMapping("/addContact")
public ResponseEntity<String> addContactByEmail(@RequestHeader("Authorization") String authHeader, @RequestParam String email) {
    try {
        accountService.addContactToList(authHeader, email);
        return ResponseEntity.ok("Contact request sent successfully.");
    } catch (AddContactToListException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + email);
    }
}
    @GetMapping("/contactsList")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AccountInformationResponse> getContactsList(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok().body(accountService.getContactsList(authHeader));
    }

    // erstelle eine Api, die es ermöglicht, die Kontakte eines Benutzers zu löschen// erstelle eine Api, die es ermöglicht, die Kontakte eines Benutzers zu löschen
    @DeleteMapping("/deleteContact/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteContact(@RequestHeader("Authorization") String authHeader, @PathVariable String email) {
        accountService.deleteContact(authHeader, email);
    }

    // src/main/java/com/hop/drivesharing/hopapplication/rest/v1/AccountController.java

    @PostMapping("/setAddressByUserId/{userId}")
    public ResponseEntity<Void> setAddressByUserId(@RequestHeader("Authorization") String authHeader,
                                                   @PathVariable String userId,
                                                   @RequestBody AddressDto addressDto) {
        accountService.setAddressByUserId(authHeader, userId, addressDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/setAddressByEmail")
    public ResponseEntity<Void> setAddressByEmail(@RequestHeader("Authorization") String authHeader,
                                                  @RequestBody AddressDto addressDto) {
        accountService.setAddressByEmail(authHeader, addressDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAddressByUserId/{userId}")
    public ResponseEntity<AddressDto> getAddressByUserId(@RequestHeader("Authorization") String authHeader,
                                                         @PathVariable String userId) {
        AddressDto address = accountService.getAddressByUserId(authHeader, userId);
        return ResponseEntity.ok(address);
    }

    @GetMapping("/getAddressByEmail")
    public ResponseEntity<AddressDto> getAddressByEmail(@RequestHeader("Authorization") String authHeader) {
        AddressDto address = accountService.getAddressByEmail(authHeader);
        if (address == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(address);
    }
}
