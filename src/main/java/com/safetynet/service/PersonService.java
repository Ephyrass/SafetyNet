package com.safetynet.service;

import com.safetynet.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final DataLoaderService dataLoaderService;

    public List<Person> getAllPersons() {
        return dataLoaderService.getDataStore().getPersons();
    }

    public List<Person> getPersonsByAddress(String address) {
        log.debug("Get person by adresses: {}", address);
        return getAllPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toList());
    }

    public List<Person> getPersonsByLastName(String lastName) {
        log.debug("Get person by last name : {}", lastName);
        return getAllPersons().stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }


    public Person getPersonByFullName(String firstName, String lastName) {
        log.debug("Get person by full name: {} {}", firstName, lastName);
        return getAllPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    public Person addPerson(Person person) {
        log.info("add new person: {} {}", person.getFirstName(), person.getLastName());
        dataLoaderService.getDataStore().getPersons().add(person);
        return person;
    }

    public Person updatePerson(Person person) {
        log.info("Update person: {} {}", person.getFirstName(), person.getLastName());

        List<Person> persons = getAllPersons();
        for (int i = 0; i < persons.size(); i++) {
            Person p = persons.get(i);
            if (p.getFirstName().equals(person.getFirstName()) && p.getLastName().equals(person.getLastName())) {
                persons.set(i, person);
                return person;
            }
        }

        log.error("person not found: {} {}", person.getFirstName(), person.getLastName());
        return null;
    }

    public boolean deletePerson(String firstName, String lastName) {
        log.info("Person deleted: {} {}", firstName, lastName);

        List<Person> persons = getAllPersons();
        return persons.removeIf(p -> p.getFirstName().equals(firstName) && p.getLastName().equals(lastName));
    }






}
