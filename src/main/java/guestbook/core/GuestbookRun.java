package guestbook.core;

import guestbook.core.repository.*;
import guestbook.core.ui.ConsoleUI;

class GuestbookRun {

    public static void main(String[] args) {

        GuestbookRepository guestbookRepository = new GuestbookRepositoryCSV("guests.csv");
//        GuestbookRepository guestbookRepository = new GuestbookRepositoryJson("guests.json");
//        GuestbookRepository guestbookRepository = new GuestbookRepositoryXML("guests.xml");
//        GuestbookRepository guestbookRepository = new GuestbookRepositorySQL("guestbook", "guestbook");
//        GuestbookRepository guestbookRepository = new GuestbookRepositoryMongo("guestbook", "guest_records");
        ConsoleUI consoleUI = new ConsoleUI();
        while (true) {
            consoleUI.processRequest(consoleUI.getMode(), guestbookRepository);
        }
    }
}