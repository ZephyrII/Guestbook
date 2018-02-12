package guestbook.core;

import guestbook.core.repository.GuestbookRepository;
import guestbook.core.repository.GuestbookRepositoryJson;
import guestbook.core.repository.GuestbookRepositorySQL;
import guestbook.core.repository.GuestbookRepositoryXML;
import guestbook.core.ui.ConsoleUI;

class GuestbookRun {

    public static void main(String[] args) {

        GuestbookRepository guestbookRepository = new GuestbookRepositoryJson("guests.json");
//        GuestbookRepository guestbookRepository = new GuestbookRepositoryXML("guests.xml");
//        GuestbookRepository guestbookRepository = new GuestbookRepositorySQL("guestbook", "guestbook");
        ConsoleUI consoleUI = new ConsoleUI();
        while (true) {
            consoleUI.processRequest(consoleUI.getMode(), guestbookRepository);
        }
    }
}