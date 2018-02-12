package guestbook.core;

import guestbook.core.repository.GuestbookRepository;

public class GuestbookReader {

    /**
     * Displays all guests in repository
     * @param guestbookRepository GuestbookRepository object with guests to display
     */
    public void displayAll(GuestbookRepository guestbookRepository) {//get
        try {
            guestbookRepository.getAllGuests().forEach(System.out::println);
        } catch (Exception e) {

        }
    }
}
