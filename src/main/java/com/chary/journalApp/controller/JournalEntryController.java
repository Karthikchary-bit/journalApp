package com.chary.journalApp.controller;

import com.chary.journalApp.entity.JournalEntry;
import com.chary.journalApp.entity.User;
import com.chary.journalApp.service.JournalEntryService;
import com.chary.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    /**
     * Retrieves all journal entries stored in the database.
     * <p>
     * HTTP Method: GET
     * Endpoint: /journalv2
     *
     * @return A list of all JournalEntry objects.
     */
    @GetMapping()
    public ResponseEntity<?> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);

        // 1. First check if the user actually exists to prevent NullPointerException
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<JournalEntry> journalEntries = user.getJournalEntries();

        if (journalEntries != null && !journalEntries.isEmpty()) {
            // 2. Return the journalEntries list, not the user object
            return new ResponseEntity<>(journalEntries, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /**
     * Creates a new journal entry.
     * <p>
     * HTTP Method: POST
     * Endpoint: /journalv2
     * <p>
     * This method accepts a JournalEntry object from the request body, sets its
     * creation date to the current date and time, and attempts to save it using
     * the service layer.
     *
     * @param entry The JournalEntry object to be created, provided in the request body.
     * @return A ResponseEntity containing the created JournalEntry and an HTTP 201 (CREATED)
     * status on success. If an exception occurs, it returns an HTTP 400 (BAD REQUEST) status.
     */
    @PostMapping()
    public ResponseEntity<JournalEntry> entry(@RequestBody JournalEntry entry) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userName = authentication.getName();
            entry.setDate(LocalDateTime.now());
            journalEntryService.saveEntry(entry,userName);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * Updates an existing journal entry by its unique ObjectId.
     * <p>
     * HTTP Method: PUT
     * Endpoint: /journalv2/id/{id}
     * <p>
     * This method fetches an existing entry by its ID. If found, it selectively updates
     * the title and content fields (only if the new values are not null and not empty).
     * After updating the fields, it saves the modified entry back to the database.
     *
     * @param id       The ObjectId of the journal entry to update, extracted from the URL path.
     * @param newEntry The JournalEntry object containing the updated fields, from the request body.
     * @return A ResponseEntity containing the updated JournalEntry and an HTTP 200 (OK) status
     * if successful. If the entry does not exist, it returns an HTTP 404 (NOT FOUND) status.
     */
    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJournal(@PathVariable ObjectId id,
                                                      @RequestBody JournalEntry newEntry
                                                      ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x->x.getId().equals(id)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if(journalEntry.isPresent()){
                JournalEntry old = journalEntry.get();
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());

                // 3. Move saveEntry inside the if-block so it doesn't crash if 'old' is null
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }


        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Retrieves a specific journal entry by its unique ObjectId.
     * <p>
     * HTTP Method: GET
     * Endpoint: /journalv2/id/{myId}
     *
     * @param myId The ObjectId of the journal entry to retrieve, extracted from the URL path.
     * @return A ResponseEntity containing the requested JournalEntry and an HTTP 200 (OK) status
     * if found. If the entry does not exist, it returns an HTTP 404 (NOT FOUND) status.
     */
    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x->x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            if (journalEntry.isPresent()) {


                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes a specific journal entry by its unique ObjectId.
     * <p>
     * HTTP Method: DELETE
     * Endpoint: /journalv2/id/{id}
     * <p>
     * This method first checks if the entry exists. If it does, it deletes the entry
     * using the service layer.
     *
     * @param id The ObjectId of the journal entry to delete, extracted from the URL path.
     * @return A ResponseEntity with an HTTP 204 (NO CONTENT) status indicating successful
     * deletion. If the entry is not found, it returns an HTTP 404 (NOT FOUND) status.
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity<JournalEntry> deleteJournal(@PathVariable ObjectId id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        if (journalEntryService.findById(id).isPresent()) {
            journalEntryService.deleteById(id,userName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}