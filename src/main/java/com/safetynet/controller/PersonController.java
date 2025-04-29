package com.safetynet.controller;

import com.safetynet.model.Person;
import com.safetynet.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PersonController {

    private final PersonService personService;

    /**
     * Add a new person.
     *
     * @param person The person to add
     * @return The added person
     */
    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        try {
            log.debug("Adding new person: {} {}", person.getFirstName(), person.getLastName());

            // Check if a person already exists
            Person existingPerson = personService.getPersonByFullName(person.getFirstName(), person.getLastName());
            if (existingPerson != null) {
                log.error("Person {} {} already exists", person.getFirstName(), person.getLastName());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            Person addedPerson = personService.addPerson(person);
            log.info("New person added: {} {}", person.getFirstName(), person.getLastName());
            return ResponseEntity.status(HttpStatus.CREATED).body(addedPerson);
        } catch (Exception e) {
            log.error("Error adding person: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Update an existing person.
     *
     * @param person The person to update
     * @return The updated person or an error if not found
     */
    @PutMapping("/person")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        try {
            log.debug("Updating person: {} {}", person.getFirstName(), person.getLastName());

            Person updatedPerson = personService.updatePerson(person);
            if (updatedPerson == null) {
                log.error("Person not found: {} {}", person.getFirstName(), person.getLastName());
                return ResponseEntity.notFound().build();
            }

            log.info("Person updated: {} {}", person.getFirstName(), person.getLastName());
            return ResponseEntity.ok(updatedPerson);
        } catch (Exception e) {
            log.error("Error updating person: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete a person.
     *
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @return An empty response with HTTP status
     */
    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        try {
            log.debug("Deleting person: {} {}", firstName, lastName);

            boolean deleted = personService.deletePerson(firstName, lastName);
            if (!deleted) {
                log.error("Person not found: {} {}", firstName, lastName);
                return ResponseEntity.notFound().build();
            }

            log.info("Person deleted: {} {}", firstName, lastName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting person: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
