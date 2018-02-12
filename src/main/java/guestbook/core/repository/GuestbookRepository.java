package guestbook.core.repository;

import guestbook.core.model.GuestRecord;

import java.util.List;

/**
 * Intetrface for reading and writing GuestRecord objects
 */
public interface GuestbookRepository {
    /**
     * Returns List of GuestRecord objcts red from repository
     * @return List of read GuestRecord objects
     * @throws RepositoryException
     */
    List<GuestRecord> getAllGuests() throws RepositoryException;

    /**
     * Writes single GuestRecord object to repository
     * @param guestRecord GuestRecord object to save
     * @throws RepositoryException
     */
    void save(GuestRecord guestRecord) throws RepositoryException;

    /**
     * Removes given record from repository
     * @param guestRecord GuestRecord to remove
     * @throws RepositoryException
     */
    void delete(GuestRecord guestRecord) throws RepositoryException;
}
