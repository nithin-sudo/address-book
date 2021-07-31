package com.bridgelabz.address;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AddressBookServiceTest
{
    @Test
    public void givenAddressBookContactsInDB_WhenRetrieved_ShouldMatchContactsCount() throws AddressBookException {
        AddressBookJDBC addressBookJDBC = new AddressBookJDBC();
        List<Person> addressBookData = addressBookJDBC.readAddressBookData(AddressBookJDBC.IOService.DB_IO);
        Assertions.assertEquals(4, addressBookData.size());
    }
    @Test
    public void givenAddressBook_WhenUpdate_ShouldSyncWithDB() throws AddressBookException {
        AddressBookJDBC addressBookJDBC = new AddressBookJDBC();
        addressBookJDBC.updateRecord("Nithin", "GKW Lauout");
        boolean result = addressBookJDBC.checkUpdatedRecordSyncWithDatabase("Nithin");
        Assertions.assertTrue(result);
    }
}
