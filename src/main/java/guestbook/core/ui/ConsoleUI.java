package guestbook.core.ui;

import guestbook.core.model.GuestRecord;
import guestbook.core.repository.GuestbookRepository;
import guestbook.core.repository.RepositoryException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Class for interacting with user and processing requests
 */
public class ConsoleUI {

    public static final String READ_MODE = "2"; /** Input string required to start read mode */
    public static final String WRITE_MODE = "1"; /** Input string required to start write mode */
    public static final String DELETE_MODE = "3"; /** Input string required to start write mode */
    public static final String WRONG_MODE_SELECTED = "Podano niepoprawny format numeru trybu. Spróbuj ponownie"; /** Message displayed after passing wrong input while choosing mode */
    public static final String READ_MODE_SELECTED = "Wybrano tryb odczytu"; /** Message displayed after choosing read mode */
    public static final String WRITE_MODE_SELECTED = "Wybrano tryb zapisu"; /** Message displayed after choosing write mode */
    public static final String DELETE_MODE_SELECTED = "Wybrano tryb usuwania"; /** Message displayed after choosing write mode */
    public static final String HELLO_MSG = "Witaj w systemie księgi gości. \n" +
                                           "Aby dodać wpis, wpisz " + WRITE_MODE +
                                           "\nAby wyświetlić wpisy wpisz " + READ_MODE +
                                           "\nAby usunąć wpis, wpisz " + DELETE_MODE; /** Message displayed at program start  */
    public static final String DETAIL_HELLO_MSG = "Aby wyświelić szczegóły gościa wprowadź jego dane."; /** Information how to use detailed read mode  */

    /**
     * Method for dispalying messages in UI
      * @param msg Message to display
     */
    public void displayMsg(String msg) {
        System.out.println(msg);
    }

    /**
     * Method for getting selected mode from user
     * @return Selected mode as string
     */
    public String getMode() { //TODO change else-if to sth better
        displayMsg(HELLO_MSG);
        Scanner sc = new Scanner(System.in);
        String selectedMode = sc.next();
        while (true) {
            if (selectedMode.equals(WRITE_MODE)) {
                displayMsg(WRITE_MODE_SELECTED);
                return WRITE_MODE;
            } else if (selectedMode.equals(READ_MODE)) {
                displayMsg(READ_MODE_SELECTED);
                return READ_MODE;
            } else if (selectedMode.equals(DELETE_MODE)) {
                displayMsg(DELETE_MODE_SELECTED);
                return DELETE_MODE;
            } else {
                displayMsg(WRONG_MODE_SELECTED);
                displayMsg(HELLO_MSG);
                selectedMode = sc.next();
            }
        }
    }

    /**
     * Runs methods responsible for selected mode
     * @param reqMode selected mode
     * @param guestbookRepository GuestbookRepository object with data to process
     */
    public void processRequest(String reqMode, GuestbookRepository guestbookRepository) {
        switch (reqMode) {
            case (WRITE_MODE): { //strategia?
                processWriteGuest(guestbookRepository);
                break;
            }
            case (READ_MODE): {
                processReadGuest(guestbookRepository);
                break;
            }
            case (DELETE_MODE): {
                processDeleteGuest(guestbookRepository);
                break;
            }
            default: {
                break;
            }
        }
    }

    private void processReadGuest(GuestbookRepository guestbookRepository) {
        displayAll(guestbookRepository);
        displayDetails(guestbookRepository);
    }

    private void displayDetails(GuestbookRepository guestbookRepository) {
        displayMsg(DETAIL_HELLO_MSG);
        String firstName = getUserInput("Podaj imię");
        String lastName = getUserInput("Podaj nazwisko");
        try {
            List<GuestRecord> foundRecords = findRecordByName(firstName, lastName, guestbookRepository);
            foundRecords.forEach(o -> System.out.println(o.toDetailedString()));
        } catch (Exception e) {

        }
    }

    private void displayAll(GuestbookRepository guestbookRepository) {
        try {
            guestbookRepository.getAllGuests().forEach(System.out::println);
        } catch (Exception e) {

        }
    }

    private void processWriteGuest(GuestbookRepository guestbookRepository) {
        String firstName = getUserInput("Podaj imię");
        String lastName = getUserInput("Podaj nazwisko");
        try {
            guestbookRepository.save(new GuestRecord(firstName, lastName));
        } catch(RepositoryException e) {

        }
    }
    private void processDeleteGuest(GuestbookRepository guestbookRepository) {
        String firstName = getUserInput("Podaj imię");
        String lastName = getUserInput("Podaj nazwisko");
        try {
            List<GuestRecord> foundRecords = findRecordByName(firstName, lastName, guestbookRepository);
            for (GuestRecord foundRecord : foundRecords) {
                guestbookRepository.delete(foundRecord);
            }
        } catch(RepositoryException e) {

        }
    }

    private List<GuestRecord> findRecordByName(String firstName, String lastName, GuestbookRepository guestbookRepository) throws RepositoryException {

        List<GuestRecord> guestRecords = guestbookRepository.getAllGuests();
        List<GuestRecord> foundRecords = new ArrayList<>();
        Iterator<GuestRecord> it = guestRecords.iterator();
        while(it.hasNext()) {
            GuestRecord guestRecord = it.next();
            if(guestRecord.getFirstName().equals(firstName)&&guestRecord.getLastName().equals(lastName)) {
                foundRecords.add(guestRecord);
            }
        }
        return foundRecords;
    }

    private String getUserInput(String displayedMessage){
        Scanner sc = new Scanner(System.in);
        displayMsg(displayedMessage);
        return sc.next();
    }
}
